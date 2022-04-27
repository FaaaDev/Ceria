package com.faadev.ceria.screen.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.faadev.ceria.databinding.ActivityPaymentConfirmationBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.http.response.PurchaseIdResponse;
import com.faadev.ceria.utils.DismissListener;
import com.faadev.ceria.utils.GlideApp;
import com.faadev.ceria.utils.ShowDialog;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentConfirmationActivity extends AppCompatActivity implements DismissListener {

    private ActivityPaymentConfirmationBinding binding;
    private ApiService apiService;
    private String bankNumber = "";
    private String cameraFilePath;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View main = binding.container;
            int flags = main.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                this.getWindow().setNavigationBarColor(Color.WHITE);
            }
            main.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.argb(255, 0, 0, 23));
        }

        apiService = new ApiService(getApplicationContext());

        getDetail(getIntent().getIntExtra("purchaseId", 0));
        implement();
    }

    private void implement() {
        binding.close.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.paymentMethod.setOnClickListener(view -> {
            copyToClipBoard("Nomor Rekening", bankNumber);
        });

        binding.btnAction.setOnClickListener(view -> {
            if (uri != null) {
                confirmPurchase();
            } else {
                Toast.makeText(getApplicationContext(), "Silahkan masukkan bukti pembayaran", Toast.LENGTH_LONG).show();
            }
        });

        binding.btnCancel.setOnClickListener(view -> {
            ShowDialog.showConfirmCancel(getSupportFragmentManager());
        });

        binding.pickImage.setOnClickListener(view -> {
            ShowDialog.showPicker(getSupportFragmentManager());
        });
    }

    private void setLoading(boolean loading) {
        if (loading) {
            binding.btnLoading.setVisibility(View.VISIBLE);
            binding.btnAction.setEnabled(false);
        } else {
            binding.btnLoading.setVisibility(View.GONE);
            binding.btnAction.setEnabled(true);
        }
    }

    private void setLoadingCancel(boolean loading) {
        if (loading) {
            binding.cancelLoading.setVisibility(View.VISIBLE);
            binding.btnCancel.setEnabled(false);
        } else {
            binding.cancelLoading.setVisibility(View.GONE);
            binding.btnCancel.setEnabled(true);
        }
    }

    private void getDetail(int id) {
        apiService.purchaseId(id, new Callback<PurchaseIdResponse>() {
            @Override
            public void onResponse(Call<PurchaseIdResponse> call, Response<PurchaseIdResponse> response) {
                if (response.body().getCode() == 200) {
                    binding.bankNumber.setText(response.body().getData().getBank().getNoRek());
                    binding.bankAlias.setText("1. Pastikan kamu melakukan transfer ke rekening tertera atas nama " + response.body().getData().getBank().getAlias() + ".");
                    GlideApp.with(getApplicationContext())
                            .load(response.body().getData().getBank().getImage())
                            .into(binding.imageBank);
                    binding.total.setText(intToIdr(response.body().getData().getPurchase().getTotalPurchase()));
                    binding.coinGained.setText(response.body().getData().getPurchase().getCoinGained() + " Coin");
                    bankNumber = response.body().getData().getBank().getNoRek();
                }
            }

            @Override
            public void onFailure(Call<PurchaseIdResponse> call, Throwable t) {

            }
        });
    }

    private void confirmPurchase() {
        setLoading(true);
        apiService.confirmPayment(
                getIntent().getIntExtra("purchaseId", 0),
                uri,
                new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.body().getCode() == 200) {
                            Toast.makeText(getApplicationContext(), "Pembayaranmu sudah kami terima, tunggu konfirmasi selanjutnya ya, Terimakasih!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal melakukan konfirmasi");
                        }
                        setLoading(false);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        setLoading(false);
                        ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
                    }
                });
    }

    private void cancelPurchase() {
        setLoadingCancel(true);
        apiService.cancelPurchase(getIntent().getIntExtra("purchaseId", 0), new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getCode() == 200) {
                    Toast.makeText(getApplicationContext(), "Pesananmu berhasil dibatalkan", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal melakukan pembatalan");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    private void copyToClipBoard(String label, String text) {
        ClipboardManager clipboard = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData clip = ClipData.newPlainText(label, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(getApplicationContext(), "Disalin!", Toast.LENGTH_SHORT).show();
    }

    private String intToIdr(int value) {
        DecimalFormat purchase = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols total = new DecimalFormatSymbols();

        total.setCurrencySymbol("IDR ");
        total.setMonetaryDecimalSeparator(',');
        total.setGroupingSeparator('.');

        purchase.setDecimalFormatSymbols(total);

        return purchase.format(value).replace(",00", "");
    }

    private void pickFromCamera() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    1);

        } else {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.faadev.ceria", createImageFile()));
                startActivityForResult(intent, 1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }

    private void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Kamu bisa melanjutkan pembayaran melalui riwayat transaksi", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == 1) {
                uri = Uri.parse(cameraFilePath);
                binding.noImage.setVisibility(View.GONE);
                binding.illustration.setVisibility(View.VISIBLE);
                binding.illustration.setImageURI(uri);
            } else if (requestCode == 2) {
                if (data != null) {
                    System.out.println(data.getData().toString());
                    uri = Uri.parse(data.getData().toString());
                    binding.noImage.setVisibility(View.GONE);
                    binding.illustration.setVisibility(View.VISIBLE);
                    binding.illustration.setImageURI(uri);
                }
            }
    }

    @Override
    public void onDismissSheet(String from) {
        if (from.equals("gallery")) {
            pickFromGallery();
        } else if (from.equals("camera")) {
            pickFromCamera();
        } else if (from.equals("confirmcancel")) {
            cancelPurchase();
        }
    }

}
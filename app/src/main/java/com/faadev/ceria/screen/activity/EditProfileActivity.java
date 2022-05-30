package com.faadev.ceria.screen.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityEditProfileBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.http.response.ProfileIdResponse;
import com.faadev.ceria.http.response.UserData;
import com.faadev.ceria.model.User;
import com.faadev.ceria.utils.DismissListener;
import com.faadev.ceria.utils.GlideApp;
import com.faadev.ceria.utils.Preferences;
import com.faadev.ceria.utils.ShowDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements DismissListener {

    private ActivityEditProfileBinding binding;
    private String cameraFilePath;
    private Uri uri;
    private ApiService apiService;
    private User user;
    private Preferences preferences = new Preferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

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

        user = (User) getIntent().getSerializableExtra("data");

        apiService = new ApiService(getApplicationContext());

        implementation();
    }

    private void implementation() {
        binding.monetize.setOnClickListener(v -> {
            user.setMonetized(!user.isMonetized());
            setMonetize(user.isMonetized());
        });
        binding.close.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.pickImage.setOnClickListener(v -> ShowDialog.showPicker(getSupportFragmentManager()));
        binding.username.setText(user.getName());
        binding.btnAction.setOnClickListener(v -> {
            editProfile();
        });

        setMonetize(user.isMonetized());

        if (user.getImage() != null) {
            GlideApp.with(getApplicationContext())
                    .load(user.getImage())
                    .into(binding.profileImage);
        }
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

    private void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 2);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == 1) {
                uri = Uri.parse(cameraFilePath);
                binding.profileImage.setImageURI(uri);
            } else if (requestCode == 2) {
                if (data != null) {
                    System.out.println(data.getData().toString());
                    uri = Uri.parse(data.getData().toString());
                    binding.profileImage.setImageURI(uri);
                }
            }
    }

    private void setMonetize(boolean monetize) {
        Handler hh = new Handler();
        hh.postDelayed(() -> {
            binding.isMonetize.setImageResource(monetize ? R.drawable.ic_toggle_on_green : R.drawable.ic_toggle_off_black);
        }, 500);

    }

    private void setLoading(boolean loading) {
        if (loading) {
            binding.btnLoading.setVisibility(View.VISIBLE);
        } else {
            binding.btnLoading.setVisibility(View.GONE);
        }
    }

    private void editProfile() {
        binding.btnAction.setEnabled(false);
        setLoading(true);
        apiService.editProfile(
                user.getId(),
                binding.username.getText().toString(),
                binding.oldPass.getText().toString(),
                binding.newPass.getText().toString(),
                uri,
                user.isMonetized(),
                new Callback<ProfileIdResponse>() {
                    @Override
                    public void onResponse(Call<ProfileIdResponse> call, Response<ProfileIdResponse> response) {
                        System.out.println(response.isSuccessful());
                        System.out.println(response.code());
                        if (response.body().getCode() == 200) {
                            preferences.saveUser(new UserData(response.body().getData(), Preferences.getToken(getApplicationContext())), getApplicationContext());
                            Toast.makeText(getApplicationContext(), "Profil berhasil diperbarui!", Toast.LENGTH_LONG).show();
                            finish();
                        } else if (response.body().getCode() == 403) {
                            binding.oldPass.setText("");
                            binding.oldPass.setError("Password harus benar");
                            ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error - Passwordmu salah...");
                        } else {
                            ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal memperbarui profile");
                        }
                        binding.btnAction.setEnabled(true);
                        setLoading(false);
                    }

                    @Override
                    public void onFailure(Call<ProfileIdResponse> call, Throwable t) {
                        System.out.println(t.toString());
                        ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
                        binding.btnAction.setEnabled(true);
                        setLoading(false);
                    }
                }
        );
    }

    @Override
    public void onDismissSheet(String from) {
        if (from != null) {
            if (from.equals("gallery")) {
                pickFromGallery();
            } else if (from.equals("camera")) {
                pickFromCamera();
            }
        }
    }
}
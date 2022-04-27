package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityDetailTransactionBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.PurchaseIdResponse;
import com.faadev.ceria.model.TransactionModel;
import com.faadev.ceria.utils.GlideApp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransactionActivity extends AppCompatActivity {

    private ActivityDetailTransactionBinding binding;
    private TransactionModel transactionModel;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTransactionBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View main = findViewById(R.id.container);
            int flags = main.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                this.getWindow().setNavigationBarColor(Color.argb(255, 58, 173, 177));
            }
            main.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.WHITE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        transactionModel = (TransactionModel) getIntent().getSerializableExtra("data");
        apiService = new ApiService(getApplicationContext());
        implement();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(transactionModel != null) {
            getDetail();
        }
    }

    private void implement() {
        if(transactionModel != null ){
            binding.coinAffected.setText(transactionModel.getCoinAffected()+" Coin");
            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                input.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = input.parse(transactionModel.getDate()+"Z");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                binding.date.setText(outputFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        binding.btnAction.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PaymentConfirmationActivity.class);
            intent.putExtra("purchaseId", transactionModel.getPurchase().getId());
            startActivity(intent);
        });
    }

    private void getDetail() {
        apiService.purchaseId(transactionModel.getPurchase().getId(), new Callback<PurchaseIdResponse>() {
            @Override
            public void onResponse(Call<PurchaseIdResponse> call, Response<PurchaseIdResponse> response) {
                if (response.body().getCode() == 200) {
                    GlideApp.with(getApplicationContext())
                            .load(response.body().getData().getBank().getImage())
                            .into(binding.imageBank);
                    binding.bankNumber.setText(response.body().getData().getBank().getNoRek());
                    binding.invoice.setText(response.body().getData().getPurchase().getInvoice());

                    binding.total.setText(intToIdr(response.body().getData().getPurchase().getTotalPurchase()));
                    binding.totalDetail.setText(intToIdr(response.body().getData().getPurchase().getTotalPurchase()));
                    binding.totalFinal.setText(intToIdr(response.body().getData().getPurchase().getTotalPurchase()));
                    generateStatus(response.body().getData().getPurchase().getStatus());
                    if (response.body().getData().getPurchase().getStatus() == 0) {
                        binding.btnAction.setVisibility(View.VISIBLE);
                    } else {
                        binding.btnAction.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PurchaseIdResponse> call, Throwable t) {

            }
        });
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

    private void generateStatus(int status) {
        if (status == 0) {
            binding.status.setBackgroundColor(Color.parseColor("#30FFB302"));
            binding.statusText.setTextColor(Color.parseColor("#FFB302"));
            binding.statusText.setText("Menunggu Pembayaran");
        } else if (status == 1) {
            binding.status.setBackgroundColor(Color.parseColor("#3004BD00"));
            binding.statusText.setTextColor(Color.parseColor("#04BD00"));
            binding.statusText.setText("Menunggu Konfirmasi");
        } else if (status == 2) {
            binding.status.setBackgroundColor(Color.parseColor("#302DCCFF"));
            binding.statusText.setTextColor(Color.parseColor("#2DCCFF"));
            binding.statusText.setText("Berhasil");
        } else if (status == 3) {
            binding.status.setBackgroundColor(Color.parseColor("#30FF0076"));
            binding.statusText.setTextColor(Color.parseColor("#FF0076"));
            binding.statusText.setText("Ditolak");
        } else {
            binding.status.setBackgroundColor(Color.parseColor("#309EA7AD"));
            binding.statusText.setTextColor(Color.parseColor("#9EA7AD"));
            binding.statusText.setText("Dibatalkan");
        }
    }
}
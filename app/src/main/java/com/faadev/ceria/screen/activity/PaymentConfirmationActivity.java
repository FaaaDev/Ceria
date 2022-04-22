package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.faadev.ceria.databinding.ActivityPaymentConfirmationBinding;
import com.faadev.ceria.utils.DismissListener;
import com.faadev.ceria.utils.ShowDialog;

public class PaymentConfirmationActivity extends AppCompatActivity implements DismissListener {

    private ActivityPaymentConfirmationBinding binding;

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

        implement();
    }

    private void implement() {
        binding.close.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.paymentMethod.setOnClickListener(view -> {

        });

        binding.btnAction.setOnClickListener(view -> {});

        binding.btnCancel.setOnClickListener(view -> {});

        binding.pickImage.setOnClickListener(view -> {
            ShowDialog.showPicker(getSupportFragmentManager());
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Kamu bisa melanjutkan pembayaran melalui riwayat transaksi", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDismissSheet(String from) {

    }

}
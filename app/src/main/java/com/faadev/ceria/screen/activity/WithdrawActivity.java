package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityWithdrawBinding;
import com.faadev.ceria.screen.fragment.CreatePaymentProfileFragment;
import com.faadev.ceria.screen.fragment.PaymentProfileFragment;
import com.faadev.ceria.utils.DismissListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class WithdrawActivity extends AppCompatActivity implements DismissListener {

    private ActivityWithdrawBinding binding;
    private int sellRate = 0;
    private int coin = 0;
    private int totalCoin = 0;
    private int totalIdr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View main = findViewById(R.id.container);
            int flags = main.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                this.getWindow().setNavigationBarColor(Color.WHITE);
            }
            main.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.argb(255, 0, 0, 23));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        sellRate = getIntent().getIntExtra("rate", 0);
        coin = getIntent().getIntExtra("coin", 0);

        implement();
    }

    private void implement() {
        binding.coin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.coin.hasFocus()) {
                    if (!TextUtils.isEmpty(editable)) {
                        totalCoin = Integer.parseInt(editable.toString());
                        totalIdr = totalCoin * sellRate;
                        binding.idr.setText(intToIdr(totalIdr));
                        binding.coin.setSelection(String.valueOf(totalCoin).length());
                    } else {
                        binding.idr.setText("0");
                        totalIdr = 0;
                        totalCoin = 0;
                    }
                    if (totalIdr < 30000) {
                        binding.errorMsg.setText("Minimal penarikan adalah IDR 30.000 (600 Coin)");
                        binding.error.setVisibility(View.VISIBLE);
                    } else if (totalCoin > coin) {
                        binding.errorMsg.setText("Coin tidak cukup");
                        binding.error.setVisibility(View.VISIBLE);
                    } else {
                        binding.error.setVisibility(View.GONE);
                    }
                }
            }
        });

        binding.idr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    String cleanString = editable.toString().replace(".", "");
                    totalIdr = Integer.parseInt(cleanString);
                    String formatted = intToIdr(totalIdr);
                    binding.idr.removeTextChangedListener(this);
                    binding.idr.setText(formatted);
                    binding.idr.setSelection(formatted.length());
                    totalCoin = totalIdr / sellRate;
                    binding.coin.setText(String.valueOf(totalCoin));
                    binding.idr.addTextChangedListener(this);
                }
                if (totalIdr < 30000) {
                    binding.errorMsg.setText("Minimal penarikan adalah IDR 30.000 (600 Coin)");
                    binding.error.setVisibility(View.VISIBLE);
                } else if (totalCoin > coin) {
                    binding.errorMsg.setText("Coin tidak cukup");
                    binding.error.setVisibility(View.VISIBLE);
                } else {
                    binding.error.setVisibility(View.GONE);
                }
            }
        });

        binding.close.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.paymentProfile.setOnClickListener(v -> {
            PaymentProfileFragment profile = new PaymentProfileFragment();
            profile.show(getSupportFragmentManager(), profile.getTag());
        });
    }

    private String intToIdr(int value) {
        DecimalFormat purchase = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols total = new DecimalFormatSymbols();

        total.setCurrencySymbol("");
        total.setMonetaryDecimalSeparator(',');
        total.setGroupingSeparator('.');

        purchase.setDecimalFormatSymbols(total);

        return purchase.format(value).replace(",00", "");
    }

    @Override
    public void onDismissSheet(String from) {
        if (from.equals("tocreate")) {
            CreatePaymentProfileFragment create = new CreatePaymentProfileFragment();
            create.show(getSupportFragmentManager(), create.getTag());
        }
    }
}
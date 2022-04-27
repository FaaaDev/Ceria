package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.AmmountAdapter;
import com.faadev.ceria.adapter.BankAdapter;
import com.faadev.ceria.adapter.ItemCLickListener;
import com.faadev.ceria.databinding.ActivityDepositBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.BankResponse;
import com.faadev.ceria.http.response.PurchaseResponse;
import com.faadev.ceria.model.BankModel;
import com.faadev.ceria.model.NominalModel;
import com.faadev.ceria.utils.ShowDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepositActivity extends AppCompatActivity {

    private ActivityDepositBinding binding;
    private List<NominalModel> nominalModels;
    private List<BankModel> bankModels;
    private AmmountAdapter adapter;
    private BankAdapter bankAdapter;
    private ItemCLickListener itemCLickListener;
    private ItemCLickListener bankCLickListener;
    private int current = -1;
    private int totalAmmount = 0;
    private int selectedBank = -1;
    private boolean isMinimum = false;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDepositBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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

        apiService = new ApiService(getApplicationContext());
        implement();
    }

    private void implement() {
        nominalModels = new ArrayList<>();
        bankModels = new ArrayList<>();
        binding.close.setOnClickListener(v -> onBackPressed());
        nominalModels.add(new NominalModel(20000));
        nominalModels.add(new NominalModel(50000));
        nominalModels.add(new NominalModel(70000));
        nominalModels.add(new NominalModel(100000));
        nominalModels.add(new NominalModel(150000));
        nominalModels.add(new NominalModel(200000));

        itemCLickListener = position -> {
            if (position > -1) {
                binding.nominal.setText(String.valueOf(nominalModels.get(position).getNominal()));
                isMinimum = true;
            } else {
                binding.nominal.setText("");
            }
            current = position;

        };

        bankCLickListener = position -> {
            selectedBank = position;
        };


        binding.nominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable editable) {
                if (current > -1) {
                    nominalModels.get(current).setSelected(false);
                    adapter.notifyDataSetChanged();
                }
                current = -1;

                if (!TextUtils.isEmpty(editable)) {
                    System.out.println(editable);
                    if (!(Integer.parseInt(editable.toString()) >= 10000)) {
                        binding.error.setVisibility(View.VISIBLE);
                        isMinimum = false;
                    } else {
                        binding.error.setVisibility(View.GONE);
                        isMinimum = true;
                    }
                }
            }
        });

        binding.nominal.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == 66) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
            return false;
        });


        adapter = new AmmountAdapter(getApplicationContext(), nominalModels, itemCLickListener);

        binding.rvNominal.setAdapter(adapter);

        binding.btnAction.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.nominal.getText()) || selectedBank == -1) {
                if (TextUtils.isEmpty(binding.nominal.getText())) {
                    binding.nominal.setError("Silahkan isi nominal isi ulang");
                }
                if (selectedBank == -1) {
                    Toast.makeText(getApplicationContext(), "Pilih Metode Pembayaran", Toast.LENGTH_LONG).show();
                }
            } else {
                System.out.println(isMinimum);
                if (isMinimum) {
                    addPurchase();
                }
            }
        });

        getBank();
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

    private void getBank() {
        apiService.getBankList(new Callback<BankResponse>() {
            @Override
            public void onResponse(Call<BankResponse> call, Response<BankResponse> response) {
                if (response.body().getCode() == 200) {
                    bankModels = response.body().getData();
                    bankAdapter = new BankAdapter(getApplicationContext(), bankModels, bankCLickListener);

                    binding.rvBank.setAdapter(bankAdapter);
                }
            }

            @Override
            public void onFailure(Call<BankResponse> call, Throwable t) {

            }
        });
    }

    private void addPurchase() {
        setLoading(true);
        apiService.addPurchase(
                Integer.parseInt(binding.nominal.getText().toString()),
                bankModels.get(selectedBank).getId(),
                new Callback<PurchaseResponse>() {
                    @Override
                    public void onResponse(Call<PurchaseResponse> call, Response<PurchaseResponse> response) {
                        setLoading(false);
                        if(response.body().getCode() == 200) {
                            Intent intent = new Intent(getApplicationContext(), PaymentConfirmationActivity.class);
                            intent.putExtra("purchaseId", response.body().getData().getId());
                            startActivity(intent);
                            finish();
                        } else {
                            ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal melakukan pembelian");
                        }
                    }

                    @Override
                    public void onFailure(Call<PurchaseResponse> call, Throwable t) {
                        setLoading(false);
                        ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
                    }
                });
    }
}
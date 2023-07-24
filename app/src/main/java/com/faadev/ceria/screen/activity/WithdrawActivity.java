package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityWithdrawBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.BankListResponse;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.http.response.PaymentProfileResponse;
import com.faadev.ceria.model.BankListModel;
import com.faadev.ceria.model.CategoryModel;
import com.faadev.ceria.model.PaymentProfileModel;
import com.faadev.ceria.screen.fragment.CategoryFragment;
import com.faadev.ceria.screen.fragment.CreatePaymentProfileFragment;
import com.faadev.ceria.screen.fragment.PaymentProfileFragment;
import com.faadev.ceria.utils.DismissListener;
import com.faadev.ceria.utils.ShowDialog;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActivity extends AppCompatActivity implements DismissListener {

    private ActivityWithdrawBinding binding;
    private int sellRate = 0;
    private int coin = 0;
    private int totalCoin = 0;
    private int totalIdr = 0;
    private ApiService apiService;
    private List<BankListModel> bankList;
    private List<CategoryModel> categoryModelList;
    private List<PaymentProfileModel> profileModels;
    private int selectedProfile = -1;

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

        apiService = new ApiService(getApplicationContext());

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
                        binding.errorMsg.setText("Minimal penarikan adalah IDR 30.000 ("+30000/sellRate+" Coin)");
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
                    String cleanString = editable.toString().replace(",", "").replace(".", "");
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
                    binding.errorMsg.setText("Minimal penarikan adalah IDR 30.000 ("+30000/sellRate+" Coin)");
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
            getProfile();
        });

        binding.btnAction.setOnClickListener(v -> {
            if (totalIdr < 3000 || selectedProfile == -1) {
                if (totalIdr < 30000) {
                    binding.errorMsg.setText("Minimal penarikan adalah IDR 30.000 ("+30000/sellRate+" Coin)");
                    binding.error.setVisibility(View.VISIBLE);
                }

                if (selectedProfile == -1) {
                    Toast.makeText(getApplicationContext(), "Silahkan Pilih Profil Pembayaran", Toast.LENGTH_LONG).show();
                }
            } else {
                requestWithdraw();
            }
        });
    }

    private String intToIdr(int value) {
        return String.format("%,d", value).replaceAll(",", ".");
    }

    @Override
    public void onDismissSheet(String from) {
        if (from != null) {
            if (from.equals("tocreate")) {
                CreatePaymentProfileFragment create = new CreatePaymentProfileFragment();
                create.show(getSupportFragmentManager(), create.getTag());
            } else if (from.equals("tobankoptions")) {
                if (categoryModelList.size() > 0) {
                    CategoryFragment categoryFragment = new CategoryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("tittle", "Pilih Bank");
                    bundle.putSerializable("data", (Serializable) categoryModelList);
                    categoryFragment.setArguments(bundle);
                    categoryFragment.show(getSupportFragmentManager(), categoryFragment.getTag());
                }
            } else if (from.contains("options:")) {
                int position = Integer.parseInt(from.replace("options:", ""));
                System.out.println(position);
                for (int i = 0; i < categoryModelList.size(); i++){
                    if (categoryModelList.get(i).getId() == position) {
                        for (int j = 0; j < bankList.size(); j++) {
                            if (categoryModelList.get(i).getCategory().equals(bankList.get(j).getBankName())) {
                                BankListModel bank = bankList.get(j);
                                CreatePaymentProfileFragment create = new CreatePaymentProfileFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", bank);
                                create.setArguments(bundle);
                                create.show(getSupportFragmentManager(), create.getTag());
                            }
                        }
                    }
                }

            } else if (from.equals("create")) {
                getProfile();
            } else if (from.contains("profile:")) {
                selectedProfile = Integer.parseInt(from.replace("profile:", ""));
                for (int i = 0; i < profileModels.size(); i++) {
                    if (profileModels.get(i).getId() == selectedProfile) {
                        binding.paymentProfile.setText(profileModels.get(i).getBank().getBankName());
                        binding.profilBankNumber.setText(profileModels.get(i).getBank_number());
                        binding.profilBankAlias.setText(profileModels.get(i).getBank_alias());
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBank();
    }

    private void getBank() {
        bankList = new ArrayList<>();
        categoryModelList = new ArrayList<>();
        apiService.getBank(new Callback<BankListResponse>() {
            @Override
            public void onResponse(Call<BankListResponse> call, Response<BankListResponse> response) {
                if (response.body().getCode() == 200) {
                    bankList = response.body().getData();
                    for (int i = 0; i < bankList.size(); i++) {
                        categoryModelList.add(new CategoryModel(i, bankList.get(i).getBankName(), false));
                    }
                }
            }

            @Override
            public void onFailure(Call<BankListResponse> call, Throwable t) {

            }
        });
    }

    private void getProfile() {
        profileModels = new ArrayList<>();
        apiService.getPayment(new Callback<PaymentProfileResponse>() {
            @Override
            public void onResponse(Call<PaymentProfileResponse> call, Response<PaymentProfileResponse> response) {
                if (response.body().getCode() == 200) {
                    profileModels = response.body().getData();
                    for (int i = 0; i < profileModels.size(); i++) {
                        if (profileModels.get(i).getId() == selectedProfile) {
                            profileModels.get(i).setSelected(true);
                        }
                    }
                    PaymentProfileFragment profile = new PaymentProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) profileModels);
                    profile.setArguments(bundle);
                    profile.show(getSupportFragmentManager(), profile.getTag());
                }
            }

            @Override
            public void onFailure(Call<PaymentProfileResponse> call, Throwable t) {

            }
        });
    }

    private void requestWithdraw() {
        setLoading(true);
        apiService.withdrawRequest(selectedProfile, totalCoin, new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                setLoading(false);
                if(response.body().getCode() == 200) {
                    finish();
                } else {
                    ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal memproses permintaan");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                setLoading(false);
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
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
}
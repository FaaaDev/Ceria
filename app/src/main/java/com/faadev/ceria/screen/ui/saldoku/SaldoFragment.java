package com.faadev.ceria.screen.ui.saldoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.TransactionAdapter;
import com.faadev.ceria.databinding.FragmentSaldoBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.MyCoinResponse;
import com.faadev.ceria.http.response.MyPurchaseResponse;
import com.faadev.ceria.http.response.PurchaseData;
import com.faadev.ceria.http.response.RateResponse;
import com.faadev.ceria.http.response.TransactionResponse;
import com.faadev.ceria.model.TransactionModel;
import com.faadev.ceria.screen.activity.DepositActivity;
import com.faadev.ceria.screen.activity.WithdrawActivity;
import com.faadev.ceria.utils.ShowDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaldoFragment extends Fragment {

    private FragmentSaldoBinding binding;
    private TransactionAdapter adapter;
    private ApiService apiService;
    private List<TransactionModel> purchaseDataList;
    private int sellRate = 0;
    private int coin = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSaldoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = new ApiService(getContext());
        implement();
    }

    private void implement() {
        binding.wd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), WithdrawActivity.class);
            intent.putExtra("rate", sellRate);
            intent.putExtra("coin", coin);
            startActivity(intent);
        });
        binding.topup.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DepositActivity.class));
        });

        binding.refresh.setOnRefreshListener(this::getRate);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRate();
    }

    private void hasData(boolean data) {
        if (data) {
            binding.rvTransaction.setVisibility(View.VISIBLE);
            binding.emptyData.setVisibility(View.GONE);
        } else {
            binding.rvTransaction.setVisibility(View.GONE);
            binding.emptyData.setVisibility(View.VISIBLE);
        }
    }

    private void getHistory(){
        apiService = new ApiService(getContext());
        purchaseDataList = new ArrayList<>();
        apiService.getTransaction(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                if (response.body().getCode() == 200) {
                    purchaseDataList = response.body().getData();
                    adapter = new TransactionAdapter(getContext(), purchaseDataList);
                    binding.rvTransaction.setAdapter(adapter);
                    hasData(purchaseDataList.size() > 0);
                    binding.refresh.setRefreshing(false);
                } else {
                    ShowDialog.showError(getChildFragmentManager(), response.body().getCode(), "Error " + response.code() + "-Gagal medapatkan data");
                    hasData(false);
                    binding.refresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                hasData(false);
                System.out.println(t.getMessage());
                ShowDialog.showError(getChildFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    private void getCoin() {
        apiService = new ApiService(getContext());
        apiService.getMyCoin(new Callback<MyCoinResponse>() {
            @Override
            public void onResponse(Call<MyCoinResponse> call, Response<MyCoinResponse> response) {
                if (response.body().getCode() == 200) {
                    coin = response.body().getData().getTotal();
                    binding.myCoin.setText(response.body().getData().getTotal()+ " Coin");
                    binding.estimate.setText(intToIdr(response.body().getData().getTotal()*sellRate));
                    getHistory();
                } else {
                    binding.refresh.setRefreshing(false);
                    ShowDialog.showError(getChildFragmentManager(), response.body().getCode(), "Error " + response.code() + "-Gagal medapatkan data");
                }
            }

            @Override
            public void onFailure(Call<MyCoinResponse> call, Throwable t) {

            }
        });
    }

    private void getRate() {
        apiService = new ApiService(getContext());
        apiService.getRate(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                if (response.body().getCode() == 200) {
                    sellRate = response.body().getData().getSellPrice();
                    getCoin();
                } else {
                    ShowDialog.showError(getChildFragmentManager(), response.body().getCode(), "Error " + response.code() + "-Gagal medapatkan data");
                    binding.refresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {

            }
        });
    }

    private String intToIdr(int value) {
        return "IDR "+String.format("%,d", value).replaceAll(",", ".");
    }
}
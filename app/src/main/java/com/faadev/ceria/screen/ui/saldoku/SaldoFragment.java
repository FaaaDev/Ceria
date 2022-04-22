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
import com.faadev.ceria.http.response.MyPurchaseResponse;
import com.faadev.ceria.http.response.PurchaseData;
import com.faadev.ceria.http.response.TransactionResponse;
import com.faadev.ceria.model.TransactionModel;
import com.faadev.ceria.screen.activity.DepositActivity;
import com.faadev.ceria.utils.ShowDialog;

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
        binding.wd.setOnClickListener(v -> {});
        binding.topup.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DepositActivity.class));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getHistory();
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
                } else {
                    ShowDialog.showError(getChildFragmentManager(), response.body().getCode(), "Error " + response.code() + "-Gagal medapatkan data");
                    hasData(false);
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                hasData(false);
            }
        });
    }
}
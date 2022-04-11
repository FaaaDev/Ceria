package com.faadev.ceria.screen.ui.saldoku;

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

public class SaldoFragment extends Fragment {

    private FragmentSaldoBinding binding;
    private TransactionAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSaldoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        implement();
    }

    private void implement() {
        binding.wd.setOnClickListener(v -> {});
        binding.topup.setOnClickListener(v -> {
            System.out.println("presssss");
        });

        adapter = new TransactionAdapter(getContext());
        binding.rvTransaction.setAdapter(adapter);
    }
}
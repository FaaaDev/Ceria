package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.AmmountAdapter;
import com.faadev.ceria.adapter.BankAdapter;
import com.faadev.ceria.adapter.ItemCLickListener;
import com.faadev.ceria.databinding.ActivityDepositBinding;
import com.faadev.ceria.model.NominalModel;

import java.util.ArrayList;
import java.util.List;

public class DepositActivity extends AppCompatActivity {

    private ActivityDepositBinding binding;
    private List<NominalModel> nominalModels;
    private AmmountAdapter adapter;
    private BankAdapter bankAdapter;
    private ItemCLickListener itemCLickListener;
    private int current = -1;

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

        implement();
    }

    private void implement() {
        nominalModels = new ArrayList<>();
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
            } else {
                binding.nominal.setText("");
            }
            current = position;

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
            }
        });

        adapter = new AmmountAdapter(getApplicationContext(), nominalModels, itemCLickListener);

        binding.rvNominal.setAdapter(adapter);

        bankAdapter = new BankAdapter(getApplicationContext());

        binding.rvBank.setAdapter(bankAdapter);
    }
}
package com.faadev.ceria.screen.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.screen.activity.AuthActivity;
import com.faadev.ceria.R;
import com.faadev.ceria.databinding.FragmentErrorBinding;
import com.faadev.ceria.utils.Preferences;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ErrorFragment extends BottomSheetDialogFragment {

    private FragmentErrorBinding binding;
    private String message = "";
    private String code = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentErrorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString("message");
            code = getArguments().getString("code");
        }

        if (code.equals("401")){
            binding.messages.setText("Sesi kamu telah habis, login ulang yuk..");
        } else {
            binding.messages.setText(message);
        }

        binding.btnAction.setOnClickListener(v -> {
            dismiss();
        });
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!code.isEmpty()) {
            if (code.equals("401")){
                Preferences.clearLoggedInUser(getContext());
                startActivity(new Intent(getContext(), AuthActivity.class));
            }
        }
    }
}
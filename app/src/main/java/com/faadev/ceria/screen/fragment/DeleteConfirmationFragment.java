package com.faadev.ceria.screen.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.FragmentDeleteConfirmationBinding;
import com.faadev.ceria.utils.DismissListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DeleteConfirmationFragment extends BottomSheetDialogFragment {

    private FragmentDeleteConfirmationBinding binding;
    private DismissListener listener;
    private String from = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDeleteConfirmationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnAction.setOnClickListener(v -> {
            from = "confirmdelete";
            dismiss();
        });

        binding.btnCancel.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DismissListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement this");
        }

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onDismissSheet(from);
    }
}
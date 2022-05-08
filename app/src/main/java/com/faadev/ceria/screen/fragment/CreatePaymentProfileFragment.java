package com.faadev.ceria.screen.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.FragmentCreatePaymentProfileBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.model.BankListModel;
import com.faadev.ceria.utils.DismissListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePaymentProfileFragment extends BottomSheetDialogFragment {

    private FragmentCreatePaymentProfileBinding binding;
    private DismissListener listener;
    private String from = "";
    private BankListModel bank;
    private ApiService apiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(true);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreatePaymentProfileBinding.inflate(inflater, container, false);

        apiService = new ApiService(getContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            bank = (BankListModel) getArguments().getSerializable("data");
            binding.bank.setText(bank.getBankName());
        }

        binding.bank.setOnClickListener(v -> {
            from = "tobankoptions";
            dismiss();
        });

        binding.btnAction.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.bankAlias.getText()) || TextUtils.isEmpty(binding.bankNumber.getText()) || bank == null) {
                if (TextUtils.isEmpty(binding.bankAlias.getText())) {
                    binding.bankAlias.setError("Nama pemilik harus diisi");
                }

                if (TextUtils.isEmpty(binding.bankNumber.getText())) {
                    binding.bankNumber.setError("Nomor rekening harus diisi");
                }

                if (bank == null) {
                    Toast.makeText(getContext(), "Pilih Bank", Toast.LENGTH_LONG).show();
                }
            } else {
                addPaymentProfile();
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

    private void addPaymentProfile() {
        setLoading(true);
        apiService.addPaymentProfile(
                bank.getCode(),
                binding.bankAlias.getText().toString(),
                binding.bankNumber.getText().toString(),
                new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.body().getCode() == 200) {
                            from = "create";
                            dismiss();
                        }
                        setLoading(false);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        setLoading(false);
                    }
                }
        );
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
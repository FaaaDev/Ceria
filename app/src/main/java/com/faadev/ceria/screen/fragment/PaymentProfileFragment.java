package com.faadev.ceria.screen.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.ItemCLickListener;
import com.faadev.ceria.adapter.PaymentProfileAdapter;
import com.faadev.ceria.databinding.FragmentPaymentProfileBinding;
import com.faadev.ceria.model.PaymentProfileModel;
import com.faadev.ceria.utils.DismissListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentProfileFragment extends BottomSheetDialogFragment {

    private FragmentPaymentProfileBinding binding;
    private DismissListener listener;
    private String from;
    private ItemCLickListener itemCLickListener;
    private List<PaymentProfileModel> data;
    protected List<PaymentProfileModel> search;

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

        binding = FragmentPaymentProfileBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            data = (List<PaymentProfileModel>) getArguments().getSerializable("data");
        } else {
            data = new ArrayList<>();
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAction.setOnClickListener(v -> {
            from = "tocreate";
            dismiss();
        });

        itemCLickListener = param -> {
            from = "profile:"+param;
            dismiss();
        };

        if (data.size() > 0) {
            binding.rvProfile.setAdapter(new PaymentProfileAdapter(getContext(), data, itemCLickListener));
            binding.rvProfile.setVisibility(View.VISIBLE);
            binding.emptyData.setVisibility(View.GONE);
        } else {
            binding.rvProfile.setVisibility(View.GONE);
            binding.emptyData.setVisibility(View.VISIBLE);
        }

        binding.search.setVisibility(View.VISIBLE);

        binding.searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    search(s.toString());
                } else {
                    if (data.size() > 0) {
                        binding.rvProfile.setAdapter(new PaymentProfileAdapter(getContext(), data, itemCLickListener));
                        binding.rvProfile.setVisibility(View.VISIBLE);
                        binding.emptyData.setVisibility(View.GONE);
                    } else {
                        binding.rvProfile.setVisibility(View.GONE);
                        binding.emptyData.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        binding.searchField.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                System.out.println("KODE : "+keyCode);
                if (keyCode == 66){
                    View vi = Objects.requireNonNull(getActivity()).getCurrentFocus();
                    if (vi!=null){
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
            return false;
        });


        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void search(String param){
        search = new ArrayList<>();
        if (data.size() > 0) {
            for (PaymentProfileModel z : data) {
                if (z.getBank().getBankName().toLowerCase().contains(param.toLowerCase()) ||
                        z.getBank_alias().toLowerCase().contains(param.toLowerCase()) ||
                        z.getBank_number().toLowerCase().contains(param.toLowerCase())) {
                    search.add(z);
                }
            }
        }

        if (search.size() > 0) {
            binding.rvProfile.setAdapter(new PaymentProfileAdapter(getContext(), search, itemCLickListener));
            binding.rvProfile.setVisibility(View.VISIBLE);
            binding.emptyData.setVisibility(View.GONE);
        } else {
            binding.rvProfile.setVisibility(View.GONE);
            binding.emptyData.setVisibility(View.VISIBLE);
        }
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
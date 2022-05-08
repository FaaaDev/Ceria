package com.faadev.ceria.screen.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CategoryAdapter;
import com.faadev.ceria.adapter.ItemCLickListener;
import com.faadev.ceria.adapter.PaymentProfileAdapter;
import com.faadev.ceria.databinding.FragmentCategoryBinding;
import com.faadev.ceria.model.CategoryModel;
import com.faadev.ceria.model.PaymentProfileModel;
import com.faadev.ceria.utils.DismissListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryFragment extends BottomSheetDialogFragment {

    private FragmentCategoryBinding binding;
    private CategoryAdapter adapter;
    private List<CategoryModel> list = new ArrayList<>();
    private List<CategoryModel> search = new ArrayList<>();
    private ItemCLickListener itemCLickListener;
    private int category;
    private DismissListener listener;

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

        binding = FragmentCategoryBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null){
            list = (List<CategoryModel>) getArguments().getSerializable("data");
            if (getArguments().getString("tittle") != null) {
                binding.tittle.setText(getArguments().getString("tittle"));
            }
        }

        itemCLickListener = param -> {
            System.out.println(param);
            category = param;
            dismiss();
        };

        adapter = new CategoryAdapter(getContext(), list, itemCLickListener, true);
        binding.rvCategory.setAdapter(adapter);

        if (list.size() > 5) {
            binding.search.setVisibility(View.VISIBLE);
        }

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
                    adapter = new CategoryAdapter(getContext(), list, itemCLickListener, true);
                    binding.rvCategory.setAdapter(adapter);
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
    }

    private void search(String param){
        search = new ArrayList<>();
        if (list.size() > 0) {
            for (CategoryModel z : list) {
                if (z.getCategory().toLowerCase().contains(param.toLowerCase())) {
                    search.add(z);
                }
            }
        }

        adapter = new CategoryAdapter(getContext(), search, itemCLickListener, true);
        binding.rvCategory.setAdapter(adapter);
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
        listener.onDismissSheet("options:"+category);
    }
}
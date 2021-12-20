package com.faadev.ceria.screen.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CategoryAdapter;
import com.faadev.ceria.adapter.ItemCLickListener;
import com.faadev.ceria.databinding.FragmentCategoryBinding;
import com.faadev.ceria.model.CategoryModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends BottomSheetDialogFragment {

    private FragmentCategoryBinding binding;
    private CategoryAdapter adapter;
    private List<CategoryModel> list = new ArrayList<>();
    private ItemCLickListener itemCLickListener;
    private int category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
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
        }

        itemCLickListener = param -> category = Integer.parseInt(param);

        adapter = new CategoryAdapter(getContext(), list, itemCLickListener, true);
        binding.rvCategory.setAdapter(adapter);
    }

}
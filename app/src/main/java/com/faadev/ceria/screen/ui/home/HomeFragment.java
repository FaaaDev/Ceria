package com.faadev.ceria.screen.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CategoryAdapter;
import com.faadev.ceria.adapter.ItemCLickListener;
import com.faadev.ceria.adapter.TabAdapter;
import com.faadev.ceria.databinding.FragmentHomeBinding;
import com.faadev.ceria.model.CategoryModel;
import com.faadev.ceria.screen.activity.CreatePostActivity;
import com.faadev.ceria.screen.fragment.DisimpanFragment;
import com.faadev.ceria.screen.fragment.DisukaiFragment;
import com.faadev.ceria.screen.fragment.PopularFragment;
import com.faadev.ceria.screen.fragment.TerbaruFragment;
import com.faadev.ceria.utils.LoginRequired;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TabAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> cmod;
    private ItemCLickListener itemCLickListener;
    private String category = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        implement();
    }

    private void implement(){
        adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new TerbaruFragment(), "Terbaru");
        adapter.addFragment(new PopularFragment(), "Popular");
        adapter.addFragment(new DisukaiFragment(), "Disukai");
        adapter.addFragment(new DisimpanFragment(), "Disimpan");
        binding.included.vp.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.included.vp);

        cmod = new ArrayList<>();
        cmod.add(new CategoryModel("Semua", true));
        cmod.add(new CategoryModel("Cerpen", false));
        cmod.add(new CategoryModel("Novel", false));
        cmod.add(new CategoryModel("Puisi", false));
        cmod.add(new CategoryModel("Fabel", false));
        cmod.add(new CategoryModel("Dongeng", false));
        cmod.add(new CategoryModel("Cerita Rakyat", false));
        cmod.add(new CategoryModel("Agama", false));

        itemCLickListener = param -> category = cmod.get(param).getCategory();

        categoryAdapter = new CategoryAdapter(getContext(), cmod, itemCLickListener);
        binding.rvCategory.setAdapter(categoryAdapter);

        binding.fabAdd.setOnClickListener(v -> {
            LoginRequired.start(getContext(), new Intent(getContext(), CreatePostActivity.class));
        });
    }
}
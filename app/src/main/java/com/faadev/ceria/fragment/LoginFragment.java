package com.faadev.ceria.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        implement();
    }

    private void implement(){
        binding.btnAction.setOnClickListener(v -> {
            binding.btnAction.setEnabled(false);
            setLoading(true);
        });
    }

    private void setLoading (boolean loading){
        if (loading){
            binding.btnLoading.setVisibility(View.VISIBLE);
        } else {
            binding.btnLoading.setVisibility(View.GONE);
        }
    }
}
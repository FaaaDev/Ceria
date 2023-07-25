package com.faadev.ceria.screen.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityAuthBinding;
import com.faadev.ceria.databinding.FragmentRegisterBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.screen.activity.AuthActivity;
import com.faadev.ceria.utils.ShowDialog;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    private ActivityAuthBinding authBinding;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        implement();
    }

    private void implement() {
        apiService = new ApiService(getContext());

        AuthActivity activity = (AuthActivity) getActivity();
        if (activity != null) {
            authBinding = activity.getBinding();
            // Now you can use activityBinding to interact with the Activity's views.
        }

        binding.btnAction.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.name.getText())
                    || TextUtils.isEmpty(binding.email.getText())
                    || TextUtils.isEmpty(binding.password.getText())) {
                ShowDialog.showError(getChildFragmentManager(), 0, "Data yang kamu inputkan belum lengkap");
            } else {
                doRegister();
            }
        });
    }

    private void setLoading(boolean loading) {
        if (loading) {
            binding.btnLoading.setVisibility(View.VISIBLE);
        } else {
            binding.btnLoading.setVisibility(View.GONE);
        }
    }

    private void doRegister() {
        binding.btnAction.setEnabled(false);
        setLoading(true);
        apiService.register(
                binding.name.getText().toString(),
                binding.email.getText().toString(),
                binding.password.getText().toString(),
                new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        System.out.println("onSuccess ======= " + response.code());

                        if (response.body().getCode() == 200) {
                            Toast.makeText(getContext(), "Berhasil melakukan pendaftaran! Login untuk melanjutkan", Toast.LENGTH_LONG).show();
                            authBinding.vp.setCurrentItem(0);
                        } else if (response.body().getCode() == 400) {
                            ShowDialog.showError(getChildFragmentManager(), response.code(), response.body().getMessage());
                        } else {
                            ShowDialog.showError(getChildFragmentManager(), response.code(), "Pendaftaran belum berhasil, Coba lagi nanti!");
                        }
                        binding.btnAction.setEnabled(true);
                        setLoading(false);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        System.out.println(t.toString());
                        binding.btnAction.setEnabled(true);
                        setLoading(false);
                    }
                });
    }
}
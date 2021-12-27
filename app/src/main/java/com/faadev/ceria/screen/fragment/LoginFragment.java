package com.faadev.ceria.screen.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.databinding.FragmentLoginBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.utils.Preferences;
import com.faadev.ceria.utils.ShowDialog;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private ApiService apiService;
    private Preferences preferences = new Preferences();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        implement();
    }

    private void implement() {
        apiService = new ApiService(getContext());
        binding.btnAction.setOnClickListener(v -> {
            doLogin();
        });
    }

    private void setLoading(boolean loading) {
        if (loading) {
            binding.btnLoading.setVisibility(View.VISIBLE);
        } else {
            binding.btnLoading.setVisibility(View.GONE);
        }
    }

    private void doLogin() {
        binding.btnAction.setEnabled(false);
        setLoading(true);
        apiService.doLogin(
                binding.name.getText().toString(),
                binding.password.getText().toString(), new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                System.out.println("onSuccess ======= "+response.code());

                if (response.isSuccessful()){
                    preferences.saveUser(response.body().getData(), getContext());
                    System.out.println("onSuccess ======= "+response.body().getMessage());
                    Objects.requireNonNull(getActivity()).finish();
                } else {
                    ShowDialog.showError(getChildFragmentManager(), response.code(), "User atau email yang kamu masukkan salah");
                }
                binding.btnAction.setEnabled(true);
                setLoading(false);
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                System.out.println(t.toString());
                binding.btnAction.setEnabled(true);
                setLoading(false);
            }
        });
    }
}
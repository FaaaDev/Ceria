package com.faadev.ceria.screen.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.adapter.CardAdapter;
import com.faadev.ceria.adapter.SharedViewModel;
import com.faadev.ceria.databinding.FragmentTerbaruBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.PostResponse;
import com.faadev.ceria.model.Post;
import com.faadev.ceria.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TerbaruFragment extends Fragment {

    private FragmentTerbaruBinding binding;
    private CardAdapter cra1;
    private List<Post> postList;
    private ApiService apiService = new ApiService();
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTerbaruBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel.getRefreshData().observe(getViewLifecycleOwner(), isRefreshing -> {
            if (isRefreshing) {
                System.out.println("refresh start");
                getPost();
            }
        });
    }

    private void getPost(){
        postList = new ArrayList<>();
        Callback<PostResponse> callback = new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()){
                    postList = response.body().getData();
                    cra1 = new CardAdapter(getContext(), postList);
                    binding.rvContent1.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    binding.rvContent1.setAdapter(cra1);
                    sharedViewModel.doneRefreshing();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                sharedViewModel.doneRefreshing();
            }
        };
        if (Preferences.isLogedIn(getContext())){
            apiService.getAllPostWithId(Preferences.getId(getContext()), callback);
         } else {
            apiService.getAllPost(callback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPost();
    }
}
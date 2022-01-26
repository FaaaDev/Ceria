package com.faadev.ceria.screen.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CardAdapter;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTerbaruBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        _prep();
    }


    private void _prep(){
        getPost();
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
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

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
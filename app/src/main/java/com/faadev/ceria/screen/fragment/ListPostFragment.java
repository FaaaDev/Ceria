package com.faadev.ceria.screen.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.MyPostAdapter;
import com.faadev.ceria.databinding.FragmentListPostBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.MyPostResponse;
import com.faadev.ceria.model.MyPost;
import com.faadev.ceria.utils.ShowDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPostFragment extends Fragment {

    private FragmentListPostBinding binding;
    private MyPostAdapter adapter;
    private ApiService apiService;
    private List<MyPost> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = new ArrayList<>();
        if (getArguments() != null) {
            if (getArguments().getSerializable("data") != null)
            list = (List<MyPost>) getArguments().getSerializable("data");
        }

        hasData(list.size() > 0);

        adapter = new MyPostAdapter(getContext(), list);
        binding.rvPost.setAdapter(adapter);
    }

    private void hasData(boolean data) {
        if (data) {
            binding.rvPost.setVisibility(View.VISIBLE);
            binding.emptyData.setVisibility(View.GONE);
        } else {
            binding.rvPost.setVisibility(View.GONE);
            binding.emptyData.setVisibility(View.VISIBLE);
        }
    }

}
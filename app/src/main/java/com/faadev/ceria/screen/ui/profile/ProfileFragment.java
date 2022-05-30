package com.faadev.ceria.screen.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CardAdapter;
import com.faadev.ceria.databinding.FragmentProfileBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.PostResponse;
import com.faadev.ceria.http.response.ProfileIdResponse;
import com.faadev.ceria.model.Post;
import com.faadev.ceria.utils.GlideApp;
import com.faadev.ceria.utils.Preferences;
import com.faadev.ceria.utils.ShowDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private CardAdapter cra1;
    private List<Post> postList;
    private ApiService apiService = new ApiService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    private void getProfile() {
        apiService = new ApiService(getContext());
        postList = new ArrayList<>();
        apiService.getUserId(Preferences.getId(getContext()), new Callback<ProfileIdResponse>() {
            @Override
            public void onResponse(Call<ProfileIdResponse> call, Response<ProfileIdResponse> response) {
                if (response.body().getCode() == 200){
                    postList = response.body().getData().getPost();
                    cra1 = new CardAdapter(getContext(), postList);
                    binding.rvContent1.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    binding.rvContent1.setAdapter(cra1);
                    binding.username.setText(response.body().getData().getName());
                    binding.email.setText(response.body().getData().getEmail());
                    binding.postCount.setText(response.body().getData().getPost().size()+"");
                    if (postList.size() > 0) {
                        binding.rvContent1.setVisibility(View.VISIBLE);
                        binding.emptyData.setVisibility(View.GONE);
                    } else {
                        binding.rvContent1.setVisibility(View.GONE);
                        binding.emptyData.setVisibility(View.VISIBLE);
                    }
                    if (response.body().getData().getImage() != null) {
                        GlideApp.with(getContext())
                                .load(response.body().getData().getImage())
                                .into(binding.profileImage);
                    }
                } else {
                    ShowDialog.showError(getChildFragmentManager(), response.body().getCode(), "Error " + response.code() + "-Gagal medapatkan data");
                }
            }

            @Override
            public void onFailure(Call<ProfileIdResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                ShowDialog.showError(getChildFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfile();
    }
}
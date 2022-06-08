package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CardAdapter;
import com.faadev.ceria.databinding.ActivityProfileDetailBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.http.response.ProfileIdResponse;
import com.faadev.ceria.model.Post;
import com.faadev.ceria.model.User;
import com.faadev.ceria.utils.GlideApp;
import com.faadev.ceria.utils.Preferences;
import com.faadev.ceria.utils.ShowDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailActivity extends AppCompatActivity {

    private ActivityProfileDetailBinding binding;
    private ApiService apiService;
    private CardAdapter cra1;
    private List<Post> postList;
    private int profile_id = 0;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View main = findViewById(R.id.container);
            int flags = main.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                this.getWindow().setNavigationBarColor(Color.WHITE);
            }
            main.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.WHITE);
        }

        profile_id = getIntent().getIntExtra("profile_id", 0);

        System.out.println(profile_id);
        implement();
    }

    private void implement() {
        binding.backBtn.setOnClickListener(v -> onBackPressed());
        binding.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            intent.putExtra("data", user);
            startActivity(intent);
        });
    }

    private void setFollow(boolean isFollowed) {
        if (isFollowed) {
            binding.includedLayout.followLabel.setText("Mengikuti");
            binding.includedLayout.followIcon.setImageResource(R.drawable.ic_round_group_24_black);
            binding.includedLayout.btnFollow.setOnClickListener(v -> {
                unFollowUser();
            });
        } else {
            binding.includedLayout.followLabel.setText("Ikuti");
            binding.includedLayout.followIcon.setImageResource(R.drawable.ic_round_group_add_24_black);
            binding.includedLayout.btnFollow.setOnClickListener(v -> {
                followUser();
            });
        }
    }

    private void getProfile() {
        apiService = new ApiService(getApplicationContext());
        postList = new ArrayList<>();
        apiService.getUserId(profile_id == 0 ? Preferences.getId(getApplicationContext()) : profile_id, new Callback<ProfileIdResponse>() {
            @Override
            public void onResponse(Call<ProfileIdResponse> call, Response<ProfileIdResponse> response) {
                if (response.body().getCode() == 200){
                    postList = response.body().getData().getPost();
                    cra1 = new CardAdapter(getApplicationContext(), postList);
                    binding.includedLayout.rvContent1.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    binding.includedLayout.rvContent1.setAdapter(cra1);
                    binding.includedLayout.username.setText(response.body().getData().getName());
                    binding.includedLayout.email.setText(response.body().getData().getEmail());
                    binding.includedLayout.postCount.setText(response.body().getData().getPost().size()+"");
                    if (response.body().getData().getId() == Preferences.getId(getApplicationContext())) {
                        binding.editBtn.setVisibility(View.VISIBLE);
                    } else {
                        binding.includedLayout.btnFollow.setVisibility(View.VISIBLE);
                    }
                    if (postList.size() > 0) {
                        binding.includedLayout.rvContent1.setVisibility(View.VISIBLE);
                        binding.includedLayout.emptyData.setVisibility(View.GONE);
                    } else {
                        binding.includedLayout.rvContent1.setVisibility(View.GONE);
                        binding.includedLayout.emptyData.setVisibility(View.VISIBLE);
                    }
                    user = response.body().getData();
                    if (user.getImage() != null) {
                        GlideApp.with(getApplicationContext())
                                .load(user.getImage())
                                .into(binding.includedLayout.profileImage);
                    }
                    binding.includedLayout.follower.setText(user.getFollowers()+"");
                    setFollow(user.isFollowed());
                } else {
                    ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.code() + "-Gagal medapatkan data");
                }
            }

            @Override
            public void onFailure(Call<ProfileIdResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    private void followUser() {
        binding.includedLayout.btnFollow.setEnabled(false);
        apiService.followUser(profile_id, new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getCode() == 200) {
                    getProfile();
                    binding.includedLayout.btnFollow.setEnabled(true);
                } else {
                    binding.includedLayout.btnFollow.setEnabled(true);
                    ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal mengikuti penulis");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                binding.includedLayout.btnFollow.setEnabled(true);
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    private void unFollowUser() {
        binding.includedLayout.btnFollow.setEnabled(false);
        apiService.unfollowUser(profile_id, new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getCode() == 200) {
                    getProfile();
                    binding.includedLayout.btnFollow.setEnabled(true);
                } else {
                    binding.includedLayout.btnFollow.setEnabled(true);
                    ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal mengikuti penulis");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                binding.includedLayout.btnFollow.setEnabled(true);
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfile();
    }
}
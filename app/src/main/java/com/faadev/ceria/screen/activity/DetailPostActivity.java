package com.faadev.ceria.screen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityDetailPostBinding;
import com.faadev.ceria.databinding.BottomDetailBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.model.Post;
import com.faadev.ceria.utils.GlideApp;
import com.faadev.ceria.utils.Preferences;
import com.faadev.ceria.utils.ShowDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPostActivity extends AppCompatActivity {

    private ActivityDetailPostBinding binding;
    private BottomDetailBinding bottom;
    private Post post = new Post();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPostBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View main = findViewById(R.id.container);
            int flags = main.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                this.getWindow().setNavigationBarColor(Color.WHITE);
            }
            main.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.WHITE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        post = (Post) getIntent().getSerializableExtra("data");

        implement();
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiService = new ApiService(this);
    }

    private void implement(){
//        bottom = BottomDetailBinding.inflate(getLayoutInflater());
        if (post != null){
            binding.includedLayout.tittle.setText(post.getTittle());
            binding.includedLayout.secondaryTittle.setText(post.getTittle());
            binding.includedLayout.content.setText(post.getArticle());
            binding.includedLayout.writer.setText(post.getWriter());
            GlideApp.with(this)
                    .load(post.getIllustration())
                    .into(binding.imageContent);
            setLiked(post.isLiked());
            if (post.getUserId() != Preferences.getId(getApplicationContext())) {
                binding.includedLayout.actionContainer.setVisibility(View.VISIBLE);
            }
        }

        binding.includedLayout.btnReward.setOnClickListener(v -> {});
        binding.includedLayout.btnLike.setOnClickListener(v -> {
            if (post.isLiked()){
                unlikePost();
            } else {
                likePost();
            }
        });
        binding.includedLayout.btnBookmark.setOnClickListener(v -> {});

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(binding.includedLayout.getRoot());
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == 4 ){
                    binding.includedLayout.tittle.setVisibility(View.VISIBLE);
                    binding.includedLayout.secondaryTittle.setVisibility(View.GONE);
                } else {
                    binding.includedLayout.tittle.setVisibility(View.GONE);
                    binding.includedLayout.secondaryTittle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setLiked(boolean isLiked){
        if (isLiked) {
            binding.includedLayout.iconLike.setImageResource(R.drawable.ic_round_favorite_white);
            post.setLiked(true);
        } else {
            binding.includedLayout.iconLike.setImageResource(R.drawable.ic_round_favorite_border_24);
            post.setLiked(false);
        }
    }

    private void likePost(){
        binding.includedLayout.btnLike.setEnabled(false);
        apiService.likePost(post.getId(), new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getCode() == 200){
                    binding.includedLayout.btnLike.setEnabled(true);
                    setLiked(true);
                } else {
                    binding.includedLayout.btnLike.setEnabled(true);
                    ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal menambah ke daftar disukai");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                binding.includedLayout.btnLike.setEnabled(true);
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    private void unlikePost(){
        binding.includedLayout.btnLike.setEnabled(false);
        apiService.unlikePost(post.getId(), new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getCode() == 200){
                    binding.includedLayout.btnLike.setEnabled(true);
                    setLiked(false);
                } else {
                    binding.includedLayout.btnLike.setEnabled(true);
                    ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal menambah ke daftar disukai");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                binding.includedLayout.btnLike.setEnabled(true);
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }
}
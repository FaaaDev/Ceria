package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityDetailPostBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.model.Post;
import com.faadev.ceria.utils.GlideApp;
import com.faadev.ceria.utils.ShowDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPostActivity extends AppCompatActivity {

    private ActivityDetailPostBinding binding;
    private  Post post = new Post();
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
            this.getWindow().setStatusBarColor(Color.argb(255, 0, 0, 23));
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
        if (post != null){
            binding.tittle.setText(post.getTittle());
            binding.content.setText(post.getArticle());
            GlideApp.with(this)
                    .load(post.getIllustration())
                    .into(binding.imageContent);
        }

        binding.btnReward.setOnClickListener(v -> {});
        binding.btnLike.setOnClickListener(v -> likePost());
        binding.btnBookmark.setOnClickListener(v -> {});
    }

    private void likePost(){
        binding.btnLike.setEnabled(false);
        apiService.likePost(post.getId(), new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getCode() == 200){
                    binding.btnLike.setEnabled(true);
                    binding.iconLike.setImageResource(R.drawable.ic_round_favorite_white);
                } else {
                    binding.btnLike.setEnabled(true);
                    ShowDialog.showError(getSupportFragmentManager(), response.body().getCode(), "Error " + response.body().getCode() + "-Gagal menambah ke daftar disukai");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                binding.btnLike.setEnabled(true);
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }
}
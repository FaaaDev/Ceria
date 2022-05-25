package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityDetailMyPostBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.http.response.MyPostIdResponse;
import com.faadev.ceria.model.MyPost;
import com.faadev.ceria.utils.GlideApp;
import com.faadev.ceria.utils.ShowDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMyPostActivity extends AppCompatActivity {

    private ActivityDetailMyPostBinding binding;
    private MyPost post;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailMyPostBinding.inflate(getLayoutInflater());
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

        apiService = new ApiService(getApplicationContext());

        post = (MyPost) getIntent().getSerializableExtra("data");

        implement();
    }

    private void implement() {
        binding.monetize.setOnClickListener(v -> {
            if (post.getUser().isMonetized()) {
                updateMonetize();
                post.setMonetized(!post.isMonetized());
                setMonetize(post.isMonetized());
            } else {
                ShowDialog.showError(getSupportFragmentManager(), 0, "Sepertinya akunmu belum memenuhi syarat monetisasi, coba lagi nanti yaa..");
            }
        });

        binding.close.setOnClickListener(v -> {
            finish();
        });

        binding.tittle.setText(post.getTittle());
        binding.content.setText(post.getArticle());
        binding.totalLike.setText(post.getLikes() + "");
        binding.totalReward.setText(post.getTotalReward() + " Coin");

        GlideApp.with(this)
                .load(post.getIllustration())
                .into(binding.illustration);

        int status = post.getApprove();
        if (status == 1) {
            binding.status.setBackgroundColor(Color.parseColor("#302DCCFF"));
            binding.statusText.setTextColor(Color.parseColor("#2DCCFF"));
            binding.statusText.setText("Dipublikasi");
        } else if (status == 0) {
            binding.status.setBackgroundColor(Color.parseColor("#3004BD00"));
            binding.statusText.setTextColor(Color.parseColor("#04BD00"));
            binding.statusText.setText("Ditinjau");
        } else {
            binding.status.setBackgroundColor(Color.parseColor("#30FF0076"));
            binding.statusText.setTextColor(Color.parseColor("#FF0076"));
            binding.statusText.setText("Ditolak");
        }

        setMonetize(post.isMonetized());

        binding.btnAction.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CreatePostActivity.class);
            intent.putExtra("data", post);
            startActivity(intent);
        });
    }

    private void setMonetize(boolean monetize) {
        Handler hh = new Handler();
        hh.postDelayed(() -> {
            binding.isMonetize.setImageResource(monetize ? R.drawable.ic_toggle_on_green : R.drawable.ic_toggle_off_black);
        }, 500);

    }

    private void getPostDetail() {
        apiService.getMyPostId(
                post.getId(),
                new Callback<MyPostIdResponse>() {
                    @Override
                    public void onResponse(Call<MyPostIdResponse> call, Response<MyPostIdResponse> response) {
                        if (response.body().getCode() == 200) {
                            post = response.body().getData();

                            binding.tittle.setText(post.getTittle());
                            binding.content.setText(post.getArticle());
                            binding.totalLike.setText(post.getLikes() + "");
                            binding.totalReward.setText(post.getTotalReward() + " Coin");

                            GlideApp.with(getApplicationContext())
                                    .load(post.getIllustration())
                                    .into(binding.illustration);

                            int status = post.getApprove();
                            if (status == 1) {
                                binding.status.setBackgroundColor(Color.parseColor("#302DCCFF"));
                                binding.statusText.setTextColor(Color.parseColor("#2DCCFF"));
                                binding.statusText.setText("Dipublikasi");
                            } else if (status == 0) {
                                binding.status.setBackgroundColor(Color.parseColor("#3004BD00"));
                                binding.statusText.setTextColor(Color.parseColor("#04BD00"));
                                binding.statusText.setText("Ditinjau");
                            } else {
                                binding.status.setBackgroundColor(Color.parseColor("#30FF0076"));
                                binding.statusText.setTextColor(Color.parseColor("#FF0076"));
                                binding.statusText.setText("Ditolak");
                            }

                            setMonetize(post.isMonetized());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyPostIdResponse> call, Throwable t) {

                    }
                }
        );
    }

    private void updateMonetize() {
        apiService.setMonetized(
                post.getId(),
                post.isMonetized(),
                new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.body().getCode() == 200) {
                            getPostDetail();
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPostDetail();
    }
}
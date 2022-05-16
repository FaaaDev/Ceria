package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityDetailMyPostBinding;
import com.faadev.ceria.model.MyPost;
import com.faadev.ceria.utils.GlideApp;

public class DetailMyPostActivity extends AppCompatActivity {

    private ActivityDetailMyPostBinding binding;
    private MyPost post;

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

        post = (MyPost) getIntent().getSerializableExtra("data");
        implement();
    }

    private void implement() {
        binding.monetize.setOnClickListener(v -> {
            post.setMonetized(!post.isMonetized());
            setMonetize(post.isMonetized());
        });

        binding.close.setOnClickListener(v -> {
            finish();
        });

        binding.tittle.setText(post.getTittle());
        binding.content.setText(post.getArticle());
        binding.totalLike.setText(post.getLikes()+"");
        binding.totalReward.setText(post.getTotalReward()+ " Coin");

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

    }

    private void setMonetize(boolean monetize) {
        Handler hh = new Handler();
        hh.postDelayed(() -> {
            binding.isMonetize.setImageResource(monetize ? R.drawable.ic_toggle_on_green : R.drawable.ic_toggle_off_black);
        }, 500);

    }
}
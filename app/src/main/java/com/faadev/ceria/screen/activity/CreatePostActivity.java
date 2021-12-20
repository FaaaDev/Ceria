package com.faadev.ceria.screen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityCreatePostBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.model.CategoryModel;
import com.faadev.ceria.screen.fragment.CategoryFragment;
import com.faadev.ceria.utils.ShowDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity {

    private ActivityCreatePostBinding binding;
    private ApiService apiService = new ApiService();
    private List<CategoryModel> categoryModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View main = binding.container;
            int flags = main.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                this.getWindow().setNavigationBarColor(Color.WHITE);
            }
            main.setSystemUiVisibility(flags);
            this.getWindow().setStatusBarColor(Color.argb(255, 0, 0, 23));
        }

        implement();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void implement(){
        getCategory();
        binding.content.setOnTouchListener((v, event) -> {
            if (binding.content.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            }
            return false;
        });

        binding.btnAction.setOnClickListener(v->{});

        binding.category.setOnClickListener(v -> {
            CategoryFragment categoryFragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) categoryModelList);
            categoryFragment.setArguments(bundle);
            categoryFragment.show(getSupportFragmentManager(), categoryFragment.getTag());
        });
    }

    private void getCategory(){
        categoryModelList = new ArrayList<>();
        apiService.getCategory(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()){
                    categoryModelList = response.body().getData();
                } else {
                    ShowDialog.showError(getSupportFragmentManager(), response.code(), "Error "+response.code()+"-Gagal medapatkan data");
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }
}
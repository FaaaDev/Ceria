package com.faadev.ceria.screen.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ActivityCreatePostBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.model.CategoryModel;
import com.faadev.ceria.screen.fragment.CategoryFragment;
import com.faadev.ceria.utils.DismissListener;
import com.faadev.ceria.utils.ShowDialog;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity implements DismissListener {

    private ActivityCreatePostBinding binding;
    private ApiService apiService;
    private List<CategoryModel> categoryModelList;
    private CategoryModel categoryModel = new CategoryModel();
    private String cameraFilePath;
    private Uri uri;
    private int categoryId;

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
        apiService = new ApiService(this);
        implement();
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiService = new ApiService(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void implement() {
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

        binding.btnAction.setOnClickListener(v -> {
            addPost();
        });

        binding.category.setOnClickListener(v -> {
            CategoryFragment categoryFragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) categoryModelList);
            categoryFragment.setArguments(bundle);
            categoryFragment.show(getSupportFragmentManager(), categoryFragment.getTag());
        });

        binding.close.setOnClickListener(v -> finish());

        binding.pickImage.setOnClickListener(v -> ShowDialog.showPicker(getSupportFragmentManager()));
    }

    private void setLoading(boolean loading) {
        if (loading) {
            binding.btnLoading.setVisibility(View.VISIBLE);
        } else {
            binding.btnLoading.setVisibility(View.GONE);
        }
    }

    private void getCategory() {
        categoryModelList = new ArrayList<>();
        apiService.getCategory(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    categoryModelList = response.body().getData();
                } else {
                    ShowDialog.showError(getSupportFragmentManager(), response.code(), "Error " + response.code() + "-Gagal medapatkan data");
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    private void addPost(){
        binding.btnAction.setEnabled(false);
        setLoading(true);
        apiService.addPost(
                binding.tittle.getText().toString(),
                binding.content.getText().toString(),
                uri,
                categoryId,
                new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Postingan berhasil dibuat!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            ShowDialog.showError(getSupportFragmentManager(), response.code(), "Error " + response.code() + "-Gagal medapatkan data");
                        }
                        binding.btnAction.setEnabled(true);
                        setLoading(false);
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        System.out.println(t.toString());
                        ShowDialog.showError(getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
                        binding.btnAction.setEnabled(true);
                        setLoading(false);
                    }
                }
        );
    }

    private void pickFromCamera() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    1);

        } else {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.faadev.ceria", createImageFile()));
                startActivityForResult(intent, 1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 2);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == 1) {
                uri = Uri.parse(cameraFilePath);
                binding.noImage.setVisibility(View.GONE);
                binding.illustration.setVisibility(View.VISIBLE);
                binding.illustration.setImageURI(uri);
            } else if (requestCode == 2) {
                if (data != null) {
                    uri = data.getData();
                    binding.noImage.setVisibility(View.GONE);
                    binding.illustration.setVisibility(View.VISIBLE);
                    binding.illustration.setImageURI(uri);
                }
            }
    }

    @Override
    public void onDismissSheet(String from) {
        if (from != null) {
            if (from.equals("gallery")) {
                pickFromGallery();
            } else if (from.equals("camera")) {
                pickFromCamera();
            } else {
                if (categoryModelList.size() > 0) {
                    for (int i = 0; i < categoryModelList.size(); i++) {
                        if (from.equals(String.valueOf(categoryModelList.get(i).getId()))) {
                            categoryModel = categoryModelList.get(i);
                            categoryId = categoryModel.getId();
                            binding.category.setText(categoryModel.getCategory());
                        }
                    }
                }
            }
        }
    }
}
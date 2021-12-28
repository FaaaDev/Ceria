package com.faadev.ceria.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.utils.CompressImage;
import com.faadev.ceria.utils.FileUtils;
import com.faadev.ceria.utils.Preferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ApiService {

    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private String token = "";
    private Context context;

    public ApiService(Context context) {
        if (Preferences.isLogedIn(context)){
            token = Preferences.getToken(context);
        }
        this.context = context;
    }

    public void doLogin(String email, String password, Callback<AuthResponse> callback) {
        Call<AuthResponse> auth = apiInterface.goAuth(email, password);
        auth.enqueue(callback);
    }

    public void getCategory(Callback<CategoryResponse> callback) {
        Call<CategoryResponse> category = apiInterface.getCategory();
        category.enqueue(callback);
    }

    public void addPost(String tittle, String article, Uri illustration, int category, Callback<GeneralResponse> callback) {
        File file = CompressImage.compress(context, illustration);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Call<GeneralResponse> post = apiInterface.addPost(token, tittle, article, body, category);
        post.enqueue(callback);
    }
}

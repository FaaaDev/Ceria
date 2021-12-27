package com.faadev.ceria.http;

import android.content.Context;

import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.utils.Preferences;

import retrofit2.Call;
import retrofit2.Callback;

public class ApiService {

    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private String token = "";

    public ApiService(Context context) {
        if (Preferences.isLogedIn(context)){
            token = Preferences.getToken(context);
        }
    }

    public void doLogin(String email, String password, Callback<AuthResponse> callback) {
        Call<AuthResponse> auth = apiInterface.goAuth(email, password);
        auth.enqueue(callback);
    }

    public void getCategory(Callback<CategoryResponse> callback) {
        Call<CategoryResponse> category = apiInterface.getCategory();
        category.enqueue(callback);
    }

    public void addPost(String tittle, String article, String illustration, int category, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> post = apiInterface.addPost(token, tittle, article, illustration, category);
        post.enqueue(callback);
    }
}

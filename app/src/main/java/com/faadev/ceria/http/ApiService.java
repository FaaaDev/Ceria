package com.faadev.ceria.http;

import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.CategoryResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class ApiService {

    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    public void doLogin(String email, String password, Callback<AuthResponse> callback){
        Call<AuthResponse> auth = apiInterface.goAuth(email, password);
        auth.enqueue(callback);
    }

    public void getCategory(Callback<CategoryResponse> callback){
        Call<CategoryResponse> category = apiInterface.getCategory();
        category.enqueue(callback);
    }
}

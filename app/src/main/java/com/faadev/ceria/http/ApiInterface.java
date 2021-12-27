package com.faadev.ceria.http;

import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.http.response.GeneralResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    Call<AuthResponse> goAuth(@Field("email") String email,
                              @Field("password") String password);

    @GET("category")
    Call<CategoryResponse> getCategory();

    @FormUrlEncoded
    @POST("post")
    Call<GeneralResponse> addPost(@Header("x-access-token") String token,
                                  @Field("tittle") String tittle,
                                  @Field("article") String article,
                                  @Field("ilustration") String ilustration,
                                  @Field("category") int category);
}

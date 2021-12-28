package com.faadev.ceria.http;

import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.http.response.GeneralResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    Call<AuthResponse> goAuth(@Field("email") String email,
                              @Field("password") String password);

    @GET("category")
    Call<CategoryResponse> getCategory();

    @Multipart
    @POST("post")
    Call<GeneralResponse> addPost(@Header("x-access-token") String token,
                                  @Part("tittle") String tittle,
                                  @Part("article") String article,
                                  @Part MultipartBody.Part image,
                                  @Part("category") int category);
}

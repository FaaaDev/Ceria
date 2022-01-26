package com.faadev.ceria.http;

import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.http.response.PostResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
                                  @Field("illustration") String illustration,
                                  @Field("category") int category);

    @Multipart
    @POST("upload")
    Call<GeneralResponse> uploadImage(@Header("x-access-token") String token,
                                      @Part MultipartBody.Part image);

    @GET("all-post")
    Call<PostResponse> getApprovedPost();

    @GET("all-post/{user_id}")
    Call<PostResponse> getAllPost(@Path(value = "user_id") int id);

    @POST("like/{post_id}")
    Call<GeneralResponse> likePost(@Header("x-access-token") String token,
                                   @Path(value = "post_id") int postId);

    @DELETE("like/{post_id}")
    Call<GeneralResponse> unlikePost(@Header("x-access-token") String token,
                                     @Path(value = "post_id") int postId);
}

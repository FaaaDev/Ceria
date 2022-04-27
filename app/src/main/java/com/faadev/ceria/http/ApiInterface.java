package com.faadev.ceria.http;

import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.BankResponse;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.http.response.MyPurchaseResponse;
import com.faadev.ceria.http.response.PostResponse;
import com.faadev.ceria.http.response.PurchaseIdResponse;
import com.faadev.ceria.http.response.PurchaseResponse;
import com.faadev.ceria.http.response.TransactionResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("bank")
    Call<BankResponse> getBank(@Header("x-access-token") String token);

    @GET("purchase/{purchase_id}")
    Call<PurchaseIdResponse> getPurchaseId(@Header("x-access-token") String token,
                                           @Path(value = "purchase_id") int purchase_id);

    @FormUrlEncoded
    @POST("purchase/{purchase_id}")
    Call<GeneralResponse> confirmPurchase(@Header("x-access-token") String token,
                                          @Path(value = "purchase_id") int purchase_id,
                                          @Field("bukti") String bukti);

    @PUT("cancel-purchase/{purchase_id}")
    Call<GeneralResponse> cancelPurchase(@Header("x-access-token") String token,
                                          @Path(value = "purchase_id") int purchase_id);

    @FormUrlEncoded
    @POST("purchase")
    Call<PurchaseResponse> addPurchase(@Header("x-access-token") String token,
                                       @Field("total") int total,
                                       @Field("bank_type") int bank);

    @GET("mypurchase")
    Call<MyPurchaseResponse> getPurchase(@Header("x-access-token") String token);

    @GET("transaction")
    Call<TransactionResponse> getTransaction(@Header("x-access-token") String token);
}

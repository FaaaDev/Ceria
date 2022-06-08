package com.faadev.ceria.http;

import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.BankListResponse;
import com.faadev.ceria.http.response.BankResponse;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.http.response.MyCoinResponse;
import com.faadev.ceria.http.response.MyPostIdResponse;
import com.faadev.ceria.http.response.MyPostResponse;
import com.faadev.ceria.http.response.MyPurchaseResponse;
import com.faadev.ceria.http.response.PaymentProfileResponse;
import com.faadev.ceria.http.response.PostDetailResponse;
import com.faadev.ceria.http.response.PostResponse;
import com.faadev.ceria.http.response.ProfileIdResponse;
import com.faadev.ceria.http.response.PurchaseIdResponse;
import com.faadev.ceria.http.response.PurchaseResponse;
import com.faadev.ceria.http.response.RateResponse;
import com.faadev.ceria.http.response.TransactionResponse;
import com.faadev.ceria.http.response.WithdrawIdResponse;

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

    @GET("all-post/{post_id}/{user_id}")
    Call<PostDetailResponse> getPostDetail(@Path(value = "user_id") int id, @Path(value = "post_id") int postId);

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

    @GET("mycoin")
    Call<MyCoinResponse> getCoin(@Header("x-access-token") String token);

    @GET("rate")
    Call<RateResponse> getRate(@Header("x-access-token") String token);

    @GET("bank-list")
    Call<BankListResponse> getBankList(@Header("x-access-token") String token);

    @FormUrlEncoded
    @POST("payment-profile")
    Call<GeneralResponse> paymentProfile(@Header("x-access-token") String token,
                                         @Field("bank") String bank,
                                         @Field("bank_alias") String alias,
                                         @Field("bank_number") String number);

    @GET("payment-profile")
    Call<PaymentProfileResponse> getPaymentProfile(@Header("x-access-token") String token);

    @FormUrlEncoded
    @POST("withdraw")
    Call<GeneralResponse> withdrawRequest(@Header("x-access-token") String token,
                                          @Field("profile_id") int profile,
                                          @Field("coin_affected") int coin);

    @GET("withdraw/{withdraw_id}")
    Call<WithdrawIdResponse> getWithdrawId(@Header("x-access-token") String token,
                                           @Path(value = "withdraw_id") int withdraw);

    @GET("post")
    Call<MyPostResponse> getMyPost(@Header("x-access-token") String token);

    @FormUrlEncoded
    @PUT("post/{post_id}")
    Call<GeneralResponse> editPost(@Path(value = "post_id") int post_id,
                                   @Header("x-access-token") String token,
                                   @Field("tittle") String tittle,
                                   @Field("article") String article,
                                   @Field("illustration") String illustration,
                                   @Field("category") int category,
                                   @Field("approve") int approve);

    @FormUrlEncoded
    @PUT("post/{post_id}")
    Call<GeneralResponse> editPostNoImage(@Path(value = "post_id") int post_id,
                                          @Header("x-access-token") String token,
                                          @Field("tittle") String tittle,
                                          @Field("article") String article,
                                          @Field("category") int category,
                                          @Field("approve") int approve);

    @GET("post/{post_id}")
    Call<MyPostIdResponse> getMyPostId(@Header("x-access-token") String token,
                                       @Path(value = "post_id") int post_id);

    @FormUrlEncoded
    @PUT("post/{post_id}")
    Call<GeneralResponse> setMonetized(@Path(value = "post_id") int post_id,
                                       @Header("x-access-token") String token,
                                       @Field("monetized") Boolean monetized);

    @GET("user/{user_id}")
    Call<ProfileIdResponse> getUserId(@Header("x-access-token") String token,
                                      @Path(value = "user_id") int user_id);

    @FormUrlEncoded
    @PUT("user/{user_id}")
    Call<ProfileIdResponse> editProfile(@Path(value = "user_id") int user_id,
                                      @Header("x-access-token") String token,
                                      @Field("name") String name,
                                      @Field("old_password") String old_password,
                                      @Field("password") String password,
                                      @Field("image") String image,
                                      @Field("monetized") Boolean monetized);

    @POST("follow/{user_id}")
    Call<GeneralResponse> followUser(@Header("x-access-token") String token,
                                     @Path(value = "user_id") int userId);

    @DELETE("follow/{user_id}")
    Call<GeneralResponse> unfollowUser(@Header("x-access-token") String token,
                                     @Path(value = "user_id") int userId);
}

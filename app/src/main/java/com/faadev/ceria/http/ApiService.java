package com.faadev.ceria.http;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.faadev.ceria.http.response.AuthResponse;
import com.faadev.ceria.http.response.BankListResponse;
import com.faadev.ceria.http.response.BankResponse;
import com.faadev.ceria.http.response.CategoryResponse;
import com.faadev.ceria.http.response.GeneralResponse;
import com.faadev.ceria.http.response.MyCoinResponse;
import com.faadev.ceria.http.response.MyPostResponse;
import com.faadev.ceria.http.response.MyPurchaseResponse;
import com.faadev.ceria.http.response.PaymentProfileResponse;
import com.faadev.ceria.http.response.PostResponse;
import com.faadev.ceria.http.response.PurchaseIdResponse;
import com.faadev.ceria.http.response.PurchaseResponse;
import com.faadev.ceria.http.response.RateResponse;
import com.faadev.ceria.http.response.TransactionResponse;
import com.faadev.ceria.http.response.WithdrawIdResponse;
import com.faadev.ceria.utils.CompressImage;
import com.faadev.ceria.utils.FileUtils;
import com.faadev.ceria.utils.Preferences;
import com.faadev.ceria.utils.ShowDialog;

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
import retrofit2.Response;

public class ApiService {

    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private String token = "";
    private Context context;

    public ApiService() {
    }

    public ApiService(Context context) {
        if (Preferences.isLogedIn(context)) {
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

        uploadImage(illustration, new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    Call<GeneralResponse> post = apiInterface.addPost(token, tittle, article, response.body().getMessage(), category);
                    post.enqueue(callback);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                ShowDialog.showError(((FragmentActivity) context).getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    public void getAllPost(Callback<PostResponse> callback) {
        Call<PostResponse> getAllPost = apiInterface.getApprovedPost();
        getAllPost.enqueue(callback);
    }

    public void getAllPostWithId(int id, Callback<PostResponse> callback) {
        Call<PostResponse> getAllPost = apiInterface.getAllPost(id);
        getAllPost.enqueue(callback);
    }

    public void uploadImage(Uri uri, Callback<GeneralResponse> callback) {
        File file = CompressImage.compressImage(context, uri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);


        Call<GeneralResponse> upload = apiInterface.uploadImage(token, body);
        upload.enqueue(callback);

    }

    public void likePost(int postId, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> likePost = apiInterface.likePost(token, postId);
        likePost.enqueue(callback);
    }

    public void unlikePost(int postId, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> likePost = apiInterface.unlikePost(token, postId);
        likePost.enqueue(callback);
    }

    public void getBankList(Callback<BankResponse> callback) {
        Call<BankResponse> bank = apiInterface.getBank(token);
        bank.enqueue(callback);
    }

    public void addPurchase(int total, int bank, Callback<PurchaseResponse> callback) {
        Call<PurchaseResponse> purchase = apiInterface.addPurchase(token, total, bank);
        purchase.enqueue(callback);
    }

    public void myPurchase(Callback<MyPurchaseResponse> callback) {
        Call<MyPurchaseResponse> purchase = apiInterface.getPurchase(token);
        purchase.enqueue(callback);
    }

    public void purchaseId(int id, Callback<PurchaseIdResponse> callback) {
        Call<PurchaseIdResponse> purchase = apiInterface.getPurchaseId(token, id);
        purchase.enqueue(callback);
    }

    public void getTransaction(Callback<TransactionResponse> callback) {
        Call<TransactionResponse> transaction = apiInterface.getTransaction(token);
        transaction.enqueue(callback);
    }

    public void confirmPayment(int id, Uri illustration, Callback<GeneralResponse> callback) {

        uploadImage(illustration, new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()) {
                    Call<GeneralResponse> confirm = apiInterface.confirmPurchase(token, id, response.body().getMessage());
                    confirm.enqueue(callback);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                ShowDialog.showError(((FragmentActivity) context).getSupportFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }

    public void cancelPurchase(int id, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> cancel = apiInterface.cancelPurchase(token, id);
        cancel.enqueue(callback);
    }

    public void getMyCoin(Callback<MyCoinResponse> callback) {
        Call<MyCoinResponse> coin = apiInterface.getCoin(token);
        coin.enqueue(callback);
    }

    public void getRate(Callback<RateResponse> callback) {
        Call<RateResponse> rate = apiInterface.getRate(token);
        rate.enqueue(callback);
    }

    public void getBank(Callback<BankListResponse> callback) {
        Call<BankListResponse> bank = apiInterface.getBankList(token);
        bank.enqueue(callback);
    }

    public void addPaymentProfile(String bank, String name, String number, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> profile = apiInterface.paymentProfile(token, bank, name, number);
        profile.enqueue(callback);
    }

    public void getPayment(Callback<PaymentProfileResponse> callback) {
        Call<PaymentProfileResponse> profile = apiInterface.getPaymentProfile(token);
        profile.enqueue(callback);
    }

    public void withdrawRequest(int profile, int coin, Callback<GeneralResponse> callback) {
        Call<GeneralResponse> wd = apiInterface.withdrawRequest(token, profile, coin);
        wd.enqueue(callback);
    }

    public void withdrawId(int id, Callback<WithdrawIdResponse> callback) {
        Call<WithdrawIdResponse> wd = apiInterface.getWithdrawId(token, id);
        wd.enqueue(callback);
    }

    public void getMyPost(Callback<MyPostResponse> callback) {
        Call<MyPostResponse> post = apiInterface.getMyPost(token);
        post.enqueue(callback);
    }

}

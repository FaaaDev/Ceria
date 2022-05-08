package com.faadev.ceria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WithdrawModel implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int user;
    @SerializedName("profile_id")
    private PaymentProfileModel profile;
    @SerializedName("withdraw_amount")
    private int amount;
    @SerializedName("coin_affected")
    private int coin;
    @SerializedName("withdraw_date")
    private String date;
    @SerializedName("status")
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public PaymentProfileModel getProfile() {
        return profile;
    }

    public void setProfile(PaymentProfileModel profile) {
        this.profile = profile;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

package com.faadev.ceria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionModel implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private String date;
    @SerializedName("type")
    private String type;
    @SerializedName("coin_affected")
    private String coinAffected;
    @SerializedName("status")
    private int status;
    @SerializedName("purchase")
    private PurchaseModel purchase;
    @SerializedName("withdraw")
    private WithdrawModel withdraw;

    public TransactionModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoinAffected() {
        return coinAffected;
    }

    public void setCoinAffected(String coinAffected) {
        this.coinAffected = coinAffected;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PurchaseModel getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseModel purchase) {
        this.purchase = purchase;
    }

    public WithdrawModel getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(WithdrawModel withdraw) {
        this.withdraw = withdraw;
    }
}

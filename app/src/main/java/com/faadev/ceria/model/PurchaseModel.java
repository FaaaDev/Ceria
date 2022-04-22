package com.faadev.ceria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PurchaseModel implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("invoice")
    private String invoice;
    @SerializedName("total_purchase")
    private int totalPurchase;
    @SerializedName("purchase_date")
    private String purchaseDate;
    @SerializedName("status")
    private int status;
    @SerializedName("coin_gained")
    private int coinGained;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public int getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(int totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCoinGained() {
        return coinGained;
    }

    public void setCoinGained(int coinGained) {
        this.coinGained = coinGained;
    }
}

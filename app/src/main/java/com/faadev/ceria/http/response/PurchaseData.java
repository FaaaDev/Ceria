package com.faadev.ceria.http.response;

import com.faadev.ceria.model.BankModel;
import com.faadev.ceria.model.PurchaseModel;
import com.faadev.ceria.model.User;
import com.google.gson.annotations.SerializedName;

public class PurchaseData {
    @SerializedName("user")
    private User user;
    @SerializedName("purchase")
    private PurchaseModel purchase;
    @SerializedName("bank")
    private BankModel bank;
    @SerializedName("bukti")
    private String bukti;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PurchaseModel getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseModel purchase) {
        this.purchase = purchase;
    }

    public BankModel getBank() {
        return bank;
    }

    public void setBank(BankModel bank) {
        this.bank = bank;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }
}

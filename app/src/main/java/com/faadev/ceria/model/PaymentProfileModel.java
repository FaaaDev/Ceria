package com.faadev.ceria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentProfileModel implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("user")
    private int user;
    @SerializedName("bank_alias")
    private String bank_alias;
    @SerializedName("bank_number")
    private String bank_number;
    @SerializedName("bank")
    private BankListModel bank;
    private boolean isSelected = false;


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

    public String getBank_alias() {
        return bank_alias;
    }

    public void setBank_alias(String bank_alias) {
        this.bank_alias = bank_alias;
    }

    public String getBank_number() {
        return bank_number;
    }

    public void setBank_number(String bank_number) {
        this.bank_number = bank_number;
    }

    public BankListModel getBank() {
        return bank;
    }

    public void setBank(BankListModel bank) {
        this.bank = bank;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

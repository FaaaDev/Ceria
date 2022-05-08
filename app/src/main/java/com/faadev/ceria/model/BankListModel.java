package com.faadev.ceria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BankListModel implements Serializable {
    @SerializedName("code")
    private String code;
    @SerializedName("name")
    private String bankName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}

package com.faadev.ceria.http.response;

import com.faadev.ceria.model.BankModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankResponse {
    @SerializedName("status")
    Boolean status;
    @SerializedName("data")
    List<BankModel> data;
    @SerializedName("message")
    String message;
    @SerializedName("code")
    int code;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<BankModel> getData() {
        return data;
    }

    public void setData(List<BankModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

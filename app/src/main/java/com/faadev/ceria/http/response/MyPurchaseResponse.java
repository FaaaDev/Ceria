package com.faadev.ceria.http.response;

import com.faadev.ceria.model.PurchaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyPurchaseResponse {
    @SerializedName("status")
    Boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("code")
    int code;
    @SerializedName("data")
    List<PurchaseData> data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public List<PurchaseData> getData() {
        return data;
    }

    public void setData(List<PurchaseData> data) {
        this.data = data;
    }
}

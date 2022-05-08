package com.faadev.ceria.http.response;

import com.faadev.ceria.model.BankListModel;
import com.faadev.ceria.model.PaymentProfileModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentProfileResponse {
    @SerializedName("status")
    Boolean status;
    @SerializedName("data")
    List<PaymentProfileModel> data;
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

    public List<PaymentProfileModel> getData() {
        return data;
    }

    public void setData(List<PaymentProfileModel> data) {
        this.data = data;
    }
}

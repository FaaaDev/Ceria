package com.faadev.ceria.http.response;

import com.faadev.ceria.model.WithdrawModel;
import com.google.gson.annotations.SerializedName;

public class WithdrawIdResponse {
    @SerializedName("status")
    Boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("code")
    int code;
    @SerializedName("data")
    WithdrawModel data;

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

    public WithdrawModel getData() {
        return data;
    }

    public void setData(WithdrawModel data) {
        this.data = data;
    }
}

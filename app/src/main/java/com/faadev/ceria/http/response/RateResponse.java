package com.faadev.ceria.http.response;

import com.faadev.ceria.model.CoinModel;
import com.faadev.ceria.model.RateModel;
import com.google.gson.annotations.SerializedName;

public class RateResponse {
    @SerializedName("status")
    Boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("code")
    int code;
    @SerializedName("data")
    RateModel data;

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

    public RateModel getData() {
        return data;
    }

    public void setData(RateModel data) {
        this.data = data;
    }
}

package com.faadev.ceria.http.response;

import com.faadev.ceria.model.MyPost;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyPostResponse {
    @SerializedName("status")
    Boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("code")
    int code;
    @SerializedName("data")
    List<MyPost> data;

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

    public List<MyPost> getData() {
        return data;
    }

    public void setData(List<MyPost> data) {
        this.data = data;
    }
}

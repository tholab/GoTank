package com.example.black.go_tankuser.converter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WrappedListResponse<T> {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private List<T> data;

    public WrappedListResponse() {
    }

    public WrappedListResponse(String message, int status, List<T> data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

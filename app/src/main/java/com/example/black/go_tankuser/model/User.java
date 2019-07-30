package com.example.black.go_tankuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("api_token")
    private String api_token;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("phone")
    private String phone;
    @SerializedName("address")
    private String address;
    @SerializedName("api_token_gcm")
    private String api_token_gcm;

    public User() {
    }

    public User(int id, String name, String email, String password, String api_token, String avatar,
                String phone, String address, String api_token_gcm) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.api_token = api_token;
        this.avatar = avatar;
        this.phone = phone;
        this.address = address;
        this.api_token_gcm = api_token_gcm;
    }

    public User(String name, String email, String password, String api_token, String avatar, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.api_token = api_token;
        this.avatar = avatar;
        this.phone = phone;
        this.address = address;
    }

    public String getApi_token_gcm() {
        return api_token_gcm;
    }

    public void setApi_token_gcm(String api_token_gcm) {
        this.api_token_gcm = api_token_gcm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

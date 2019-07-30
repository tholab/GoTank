package com.example.black.go_tankuser.model;

import com.google.gson.annotations.SerializedName;

public class Company {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("harga")
    private int harga;
    @SerializedName("description")
    private String description;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;

    public Company() {
    }

    public Company(int id, String name, int harga, String description, String avatar, String address, String phone) {
        this.id = id;
        this.name = name;
        this.harga = harga;
        this.description = description;
        this.avatar = avatar;
        this.address = address;
        this.phone = phone;
    }

    public Company(String name, int harga, String description, String avatar, String address, String phone) {
        this.name = name;
        this.harga = harga;
        this.description = description;
        this.avatar = avatar;
        this.address = address;
        this.phone = phone;
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

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

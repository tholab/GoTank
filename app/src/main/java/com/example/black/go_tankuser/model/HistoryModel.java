package com.example.black.go_tankuser.model;

import com.google.gson.annotations.SerializedName;

public class HistoryModel {

    @SerializedName("id")
    private int id;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("name")
    private String name;
    @SerializedName("status")
    private String status;
    @SerializedName("tgl_pesan")
    private String tgl_pesan;
    @SerializedName("user")
    private User user;
    @SerializedName("driver")
    private Driver driver;
    @SerializedName("harga")
    private int harga;
    @SerializedName("komentar")
    private String komentar;

    public HistoryModel() {
    }


    public HistoryModel(int id, String avatar, String name, String status, String tgl_pesan, User user, Driver driver, int harga, String komentar) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.status = status;
        this.tgl_pesan = tgl_pesan;
        this.user = user;
        this.driver = driver;
        this.harga = harga;
        this.komentar = komentar;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTgl_pesan() {
        return tgl_pesan;
    }

    public void setTgl_pesan(String tgl_pesan) {
        this.tgl_pesan = tgl_pesan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}

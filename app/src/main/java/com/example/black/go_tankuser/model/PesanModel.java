package com.example.black.go_tankuser.model;

import com.google.gson.annotations.SerializedName;

public class PesanModel {
    @SerializedName("id")
    private int id;
    @SerializedName("company_id")
    private int company_id;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("tgl_pesan")
    private String tgl_pesan;
    @SerializedName("jam_id")
    private String jam_id;
    @SerializedName("name")
    private String name;
    @SerializedName("status")
    private String status;
    @SerializedName("avatar")
    private String avatar;

    public PesanModel() { }

    public PesanModel(int company_id, int user_id, String tgl_pesan, String jam_id) {
        this.company_id = company_id;
        this.user_id = user_id;
        this.tgl_pesan = tgl_pesan;
        this.jam_id = jam_id;
    }

    public PesanModel(int id, int company_id, int user_id, String tgl_pesan, String jam_id) {
        this.id = id;
        this.company_id = company_id;
        this.user_id = user_id;
        this.tgl_pesan = tgl_pesan;
        this.jam_id = jam_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTgl_pesan() {
        return tgl_pesan;
    }

    public void setTgl_pesan(String tgl_pesan) {
        this.tgl_pesan = tgl_pesan;
    }

    public String getJam_id() {
        return jam_id;
    }

    public void setJam_id(String jam_id) {
        this.jam_id = jam_id;
    }
}

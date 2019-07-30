package com.example.black.go_tankuser.model;

import com.google.gson.annotations.SerializedName;

public class KomentarModel {

    @SerializedName("id")
    private int id;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("name")
    private String name;
    @SerializedName("komentar")
    private String komentar;

    public KomentarModel() {
    }

    public KomentarModel(int id, String avatar, String name, String komentar) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.komentar = komentar;
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

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }
}

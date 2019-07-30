package com.example.black.go_tankuser.model;

import com.google.gson.annotations.SerializedName;

public class JamModel {
    @SerializedName("id")
    private int id;
    @SerializedName("jam")
    private String jam;

    public JamModel() {
    }

    public JamModel(String jam) {
        this.jam = jam;
    }

    public JamModel(int id, String jam) {
        this.id = id;
        this.jam = jam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}

package com.example.black.go_tankuser.model;

import com.google.gson.annotations.SerializedName;

public class Driver {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public Driver() { }

    public Driver(int id, String name) {
        this.id = id;
        this.name = name;
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
}

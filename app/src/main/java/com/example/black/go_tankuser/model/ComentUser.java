package com.example.black.go_tankuser.model;

public class ComentUser {
    private int id;
    private String namaUser,coment;
    private int rating;
    private int image;

    public ComentUser(int id, String namaUser, String coment, int rating, int image) {
        this.id = id;
        this.namaUser = namaUser;
        this.coment = coment;
        this.rating = rating;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

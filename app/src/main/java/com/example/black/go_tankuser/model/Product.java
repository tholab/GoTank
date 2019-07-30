package com.example.black.go_tankuser.model;

public class Product {

    private int id;
    private String title,shortdesc;
    private int rating;
    private int image;

    public Product(int id, String title, String shortdesc, int rating, int image) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.rating = rating;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public int getRating() {
        return rating;
    }

    public int getImage() {
        return image;
    }

}

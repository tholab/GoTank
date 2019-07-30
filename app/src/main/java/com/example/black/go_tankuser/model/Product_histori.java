package com.example.black.go_tankuser.model;

public class Product_histori {
    private int id;
    private String nama, harga, tgl_pesan, status;
    private int image;

    public Product_histori(int id, String nama, String harga, String tgl_pesan, String status, int image) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.tgl_pesan = tgl_pesan;
        this.status = status;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getHarga() {
        return harga;
    }

    public String getTgl_pesan() {
        return tgl_pesan;
    }

    public String getStatus() {
        return status;
    }

    public int getImage() {
        return image;
    }
}

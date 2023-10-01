package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class Shop {
    @SerializedName("_id")
    private String id;
    private String nameshop;
    private String address;
     private String id_user;

    public Shop() {
    }

    public Shop(String id, String nameshop, String address, String id_user) {
        this.id = id;
        this.nameshop = nameshop;
        this.address = address;
        this.id_user = id_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameshop() {
        return nameshop;
    }

    public void setNameshop(String nameshop) {
        this.nameshop = nameshop;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}

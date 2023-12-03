package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTOcheck_productshop {
    @SerializedName("_id")
    String id;
    @SerializedName("id_shop")
    Shop id_shop;

    public DTOcheck_productshop() {
    }

    public DTOcheck_productshop(String id, Shop id_shop) {
        this.id = id;
        this.id_shop = id_shop;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Shop getId_shop() {
        return id_shop;
    }

    public void setId_shop(Shop id_shop) {
        this.id_shop = id_shop;
    }
}

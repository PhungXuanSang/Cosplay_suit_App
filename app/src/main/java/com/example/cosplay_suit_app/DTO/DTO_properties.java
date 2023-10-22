package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_properties {
    @SerializedName("_id")
    String id_properties;
    String nameproperties;
    int amount;
    @SerializedName("id_product")
    DTO_SanPham dtoSanPham;

    public DTO_properties() {
    }

    public DTO_properties(String id_properties, String nameproperties, int amount, DTO_SanPham dtoSanPham) {
        this.id_properties = id_properties;
        this.nameproperties = nameproperties;
        this.amount = amount;
        this.dtoSanPham = dtoSanPham;
    }

    public String getId_properties() {
        return id_properties;
    }

    public void setId_properties(String id_properties) {
        this.id_properties = id_properties;
    }

    public String getNameproperties() {
        return nameproperties;
    }

    public void setNameproperties(String nameproperties) {
        this.nameproperties = nameproperties;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public DTO_SanPham getDtoSanPham() {
        return dtoSanPham;
    }

    public void setDtoSanPham(DTO_SanPham dtoSanPham) {
        this.dtoSanPham = dtoSanPham;
    }
}

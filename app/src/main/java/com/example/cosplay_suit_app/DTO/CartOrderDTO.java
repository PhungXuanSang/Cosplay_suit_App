package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class CartOrderDTO {
    String _id,id_user;
    @SerializedName("product_id")
    DTO_SanPham dtoSanPham;
    int amount, totalPayment;
    @SerializedName("properties_id")
    DTO_properties dtoProperties;

    public CartOrderDTO() {
    }

    public CartOrderDTO(String _id, String id_user, DTO_SanPham dtoSanPham, int amount, int totalPayment, DTO_properties dtoProperties) {
        this._id = _id;
        this.id_user = id_user;
        this.dtoSanPham = dtoSanPham;
        this.amount = amount;
        this.totalPayment = totalPayment;
        this.dtoProperties = dtoProperties;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public DTO_SanPham getDtoSanPham() {
        return dtoSanPham;
    }

    public void setDtoSanPham(DTO_SanPham dtoSanPham) {
        this.dtoSanPham = dtoSanPham;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public DTO_properties getDtoProperties() {
        return dtoProperties;
    }

    public void setDtoProperties(DTO_properties dtoProperties) {
        this.dtoProperties = dtoProperties;
    }

    public String getId_cart() {
        return _id;
    }

    public void setId_cart(String id_cart) {
        this._id = id_cart;
    }
}

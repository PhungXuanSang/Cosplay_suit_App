package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_inbuynow {
    String _id,id_user;
    @SerializedName("id_product")
    DTO_SanPham dtoSanPham;
    int amount, totalPayment;
    @SerializedName("id_properties")
    DTO_properties dtoProperties;

    public DTO_inbuynow() {
    }

    public DTO_inbuynow(String _id, String id_user, DTO_SanPham dtoSanPham, int amount, int totalPayment, DTO_properties dtoProperties) {
        this._id = _id;
        this.id_user = id_user;
        this.dtoSanPham = dtoSanPham;
        this.amount = amount;
        this.totalPayment = totalPayment;
        this.dtoProperties = dtoProperties;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public DTO_properties getDtoProperties() {
        return dtoProperties;
    }

    public void setDtoProperties(DTO_properties dtoProperties) {
        this.dtoProperties = dtoProperties;
    }
}

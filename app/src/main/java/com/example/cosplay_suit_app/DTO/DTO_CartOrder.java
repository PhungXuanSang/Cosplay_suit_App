package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_CartOrder {

    String id_user;
    String product_id;
    @SerializedName("id_product")
    DTO_SanPham dtoSanPham;
    int amount;
    String properties_id;
    @SerializedName("id_properties")
    DTO_properties dtoProperties;

    public DTO_CartOrder() {
    }

    public DTO_CartOrder(String id_user, String product_id, DTO_SanPham dtoSanPham, int amount, String properties_id, DTO_properties dtoProperties) {
        this.id_user = id_user;
        this.product_id = product_id;
        this.dtoSanPham = dtoSanPham;
        this.amount = amount;
        this.properties_id = properties_id;
        this.dtoProperties = dtoProperties;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getProperties_id() {
        return properties_id;
    }

    public void setProperties_id(String properties_id) {
        this.properties_id = properties_id;
    }

    public DTO_properties getDtoProperties() {
        return dtoProperties;
    }

    public void setDtoProperties(DTO_properties dtoProperties) {
        this.dtoProperties = dtoProperties;
    }
}

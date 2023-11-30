package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_voucher {
    @SerializedName("_id")
    String id;
    @SerializedName("id_shop")
    String id_shop;
    @SerializedName("id_user")
    String id_user;
    String discount,amount,content;

    public DTO_voucher() {
    }

    public DTO_voucher(String id_shop, String discount, String amount, String content) {
        this.id_shop = id_shop;
        this.discount = discount;
        this.amount = amount;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_shop() {
        return id_shop;
    }

    public void setId_shop(String id_shop) {
        this.id_shop = id_shop;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

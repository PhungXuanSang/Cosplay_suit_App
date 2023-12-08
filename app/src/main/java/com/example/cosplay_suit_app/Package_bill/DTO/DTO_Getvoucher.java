package com.example.cosplay_suit_app.Package_bill.DTO;

import com.example.cosplay_suit_app.DTO.Shop;
import com.google.gson.annotations.SerializedName;

public class DTO_Getvoucher {
    @SerializedName("_id")
    String id;
    @SerializedName("id_shop")
    Shop shop;
    String discount,amount,content;

    public DTO_Getvoucher(String id, Shop shop, String discount, String amount, String content) {
        this.id = id;
        this.shop = shop;
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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
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

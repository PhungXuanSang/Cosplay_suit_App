package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class ProDoneDTO {

    @SerializedName("_id")
    String id;
    String id_category,nameproduct,amount,image,description,time_product;

    @SerializedName("id_shop")
    Shop shop;
    int price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime_product() {
        return time_product;
    }

    public void setTime_product(String time_product) {
        this.time_product = time_product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

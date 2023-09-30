package com.example.cosplay_suit_app.DTO;

public class DTO_SanPham {
    String id;
    String id_shop,id_category,nameproduct,amount,image,description,time_product;
    int price;

    public DTO_SanPham() {
    }

    public DTO_SanPham(String id, String id_shop, String id_category, String nameproduct, String amount, String image, String description, int price, String time_product) {
        this.id = id;
        this.id_shop = id_shop;
        this.id_category = id_category;
        this.nameproduct = nameproduct;
        this.amount = amount;
        this.image = image;
        this.description = description;
        this.price = price;
        this.time_product = time_product;
    }

    public String getTime_product() {
        return time_product;
    }

    public void setTime_product(String time_product) {
        this.time_product = time_product;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

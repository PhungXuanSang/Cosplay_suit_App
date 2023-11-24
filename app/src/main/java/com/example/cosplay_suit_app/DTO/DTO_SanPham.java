package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DTO_SanPham {
    @SerializedName("_id")
    String id;
    String id_shop,id_category,nameproduct,image,description,time_product;
    int price,amount;

    String size;
    List<ItemImageDTO> listImage;

    List<DTO_properties> listProp;

    public DTO_SanPham(String id, String id_shop, String id_category, String nameproduct, int amount, String image, String description, String time_product, int price, String size, List<ItemImageDTO> listImage, List<DTO_properties> listProp) {
        this.id = id;
        this.id_shop = id_shop;
        this.id_category = id_category;
        this.nameproduct = nameproduct;
        this.amount = amount;
        this.image = image;
        this.description = description;
        this.time_product = time_product;
        this.price = price;
        this.size = size;
        this.listImage = listImage;
        this.listProp = listProp;
    }

    public List<DTO_properties> getListProp() {
        return listProp;
    }

    public void setListProp(List<DTO_properties> listProp) {
        this.listProp = listProp;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public DTO_SanPham() {
    }

    public List<ItemImageDTO> getListImage() {
        return listImage;
    }

    public void setListImage(List<ItemImageDTO> listImage) {
        this.listImage = listImage;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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


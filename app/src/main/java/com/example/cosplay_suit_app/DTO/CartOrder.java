package com.example.cosplay_suit_app.DTO;

public class CartOrder {

    String id_user;
    String id_product;
    int amount;
    String properties;

    public CartOrder() {
    }

    public CartOrder(String id_user, String id_product, int amount, String properties) {
        this.id_user = id_user;
        this.id_product = id_product;
        this.amount = amount;
        this.properties = properties;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}

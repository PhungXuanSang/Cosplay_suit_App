package com.example.cosplay_suit_app.DTO;

public class DTO_CartOrder {
    String id_user;
    String id_product;
    int amount, totalPayment;
    String id_properties;

    public DTO_CartOrder() {
    }

    public DTO_CartOrder(String id_user, String id_product, int amount, int totalPayment, String id_properties) {
        this.id_user = id_user;
        this.id_product = id_product;
        this.amount = amount;
        this.totalPayment = totalPayment;
        this.id_properties = id_properties;
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

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getId_properties() {
        return id_properties;
    }

    public void setId_properties(String id_properties) {
        this.id_properties = id_properties;
    }
}

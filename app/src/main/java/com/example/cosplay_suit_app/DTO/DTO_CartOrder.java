package com.example.cosplay_suit_app.DTO;

public class DTO_CartOrder {
    String id_user;
    String product_id;
    int amount, totalPayment;
    String properties_id;

    public DTO_CartOrder() {
    }

    public DTO_CartOrder(String id_user, String product_id, int amount, int totalPayment, String properties_id) {
        this.id_user = id_user;
        this.product_id = product_id;
        this.amount = amount;
        this.totalPayment = totalPayment;
        this.properties_id = properties_id;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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
}

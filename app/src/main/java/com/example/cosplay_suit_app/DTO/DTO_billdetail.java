package com.example.cosplay_suit_app.DTO;

public class DTO_billdetail {
    int amount, totalPayment;
    String id_product, id_bill, size;

    public DTO_billdetail() {
    }

    public DTO_billdetail(int amount, int totalPayment, String id_product, String id_bill, String size) {
        this.amount = amount;
        this.totalPayment = totalPayment;
        this.id_product = id_product;
        this.id_bill = id_bill;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getId_bill() {
        return id_bill;
    }

    public void setId_bill(String id_bill) {
        this.id_bill = id_bill;
    }
}

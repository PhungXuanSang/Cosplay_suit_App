package com.example.cosplay_suit_app.DTO;

public class SignUpShop {
    Shop shop;
    String response;

    public SignUpShop() {
    }

    public SignUpShop(Shop shop, String response) {
        this.shop = shop;
        this.response = response;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

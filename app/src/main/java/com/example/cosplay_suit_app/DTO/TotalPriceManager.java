package com.example.cosplay_suit_app.DTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TotalPriceManager {
    private static TotalPriceManager instance;
    private int totalOrderPrice;
    private ArrayList<String> listcart = new ArrayList<>();
    private TotalPriceManager() {
        // Private constructor to prevent instantiation
    }
    public static TotalPriceManager getInstance() {
        if (instance == null) {
            instance = new TotalPriceManager();
        }
        return instance;
    }

    public int getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void updateTotalPriceTrue(int totalPrice) {
        totalOrderPrice += totalPrice;
    }
    public void updateTotalPriceFalse(int totalPrice) {
        totalOrderPrice -= totalPrice;
    }

    public void setTotalOrderPrice(int totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public void setListcart(ArrayList<String> listcart) {
        this.listcart = listcart;
    }

    public ArrayList<String> getListcart() {
        return listcart;
    }

    public void updateIdcartTrue(String idcart) {
        listcart.add(idcart);
    }
    public void updateIdcartFalse(String idcart) {
        listcart.remove(idcart);
    }
}

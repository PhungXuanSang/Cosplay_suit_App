package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_properties {
    @SerializedName("_id")
    String id_properties;
    String nameproperties;
    @SerializedName("amount")
    int amount;
    String id_product;

    public DTO_properties() {
    }

    public DTO_properties(String id_properties, String nameproperties, int amount, String id_product) {
        this.id_properties = id_properties;
        this.nameproperties = nameproperties;
        this.amount = amount;
        this.id_product = id_product;
    }

    public String getId_properties() {
        return id_properties;
    }

    public void setId_properties(String id_properties) {
        this.id_properties = id_properties;
    }

    public String getNameproperties() {
        return nameproperties;
    }

    public void setNameproperties(String nameproperties) {
        this.nameproperties = nameproperties;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }
}

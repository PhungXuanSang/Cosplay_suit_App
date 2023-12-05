package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NameProDTO {

    @SerializedName("nameproduct")
    private List<String> nameProductList;

    public List<String> getNameProductList() {
        return nameProductList;
    }
}

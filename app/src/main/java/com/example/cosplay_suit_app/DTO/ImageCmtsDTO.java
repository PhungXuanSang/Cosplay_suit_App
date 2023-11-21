package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class ImageCmtsDTO {
    @SerializedName("anhCmts")
    private String anhCmts;

    public String getAnhCmts() {
        return anhCmts;
    }

    public void setAnhCmts(String anhCmts) {
        this.anhCmts = anhCmts;
    }
}

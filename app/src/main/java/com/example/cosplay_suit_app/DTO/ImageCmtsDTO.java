package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class ImageCmtsDTO {
    @SerializedName("anhCmts")
    private String anhCmts;

    // Thêm constructor với đường dẫn URL
    public ImageCmtsDTO(String anhCmts) {
        this.anhCmts = anhCmts;
    }

    public ImageCmtsDTO() {
    }

    public String getAnhCmts() {
        return anhCmts;
    }

    public void setAnhCmts(String anhCmts) {
        this.anhCmts = anhCmts;
    }
}

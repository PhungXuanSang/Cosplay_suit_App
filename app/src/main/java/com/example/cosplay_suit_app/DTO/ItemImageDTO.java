package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class ItemImageDTO {
    @SerializedName("anhnd")
    String image;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ItemImageDTO(String image) {
        this.image = image;
    }

    public ItemImageDTO() {
    }
}

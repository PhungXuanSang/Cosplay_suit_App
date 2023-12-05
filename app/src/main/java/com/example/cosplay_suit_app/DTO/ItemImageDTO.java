package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemImageDTO implements Serializable {
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

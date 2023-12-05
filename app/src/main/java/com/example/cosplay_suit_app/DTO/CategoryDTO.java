package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class CategoryDTO {
    @SerializedName("_id")
    String _id;
    String time_category,name;

    String imageCategory;

    public CategoryDTO() {
    }


    public CategoryDTO(String _id, String name, String time_category, String imageCategory) {
        this._id = _id;
        this.name = name;
        this.time_category = time_category;
        this.imageCategory = imageCategory;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime_category() {
        return time_category;
    }

    public void setTime_category(String time_category) {
        this.time_category = time_category;
    }


    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }
}

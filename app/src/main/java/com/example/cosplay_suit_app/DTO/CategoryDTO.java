package com.example.cosplay_suit_app.DTO;

public class CategoryDTO {
    String _id,name;
    String time_category;

    public CategoryDTO() {
    }

    public CategoryDTO(String id, String name, String time_category) {
        this._id = id;
        this.name = name;
        this.time_category = time_category;
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

}

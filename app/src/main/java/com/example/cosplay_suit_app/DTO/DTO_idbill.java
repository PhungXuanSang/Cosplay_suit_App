package com.example.cosplay_suit_app.DTO;

public class DTO_idbill {
    String _id;
    String id_shop;

    public DTO_idbill() {
    }

    public DTO_idbill(String _id, String id_shop) {
        this._id = _id;
        this.id_shop = id_shop;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId_shop() {
        return id_shop;
    }

    public void setId_shop(String id_shop) {
        this.id_shop = id_shop;
    }
}

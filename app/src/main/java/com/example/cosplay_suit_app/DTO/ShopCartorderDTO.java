package com.example.cosplay_suit_app.DTO;

import java.util.List;

public class ShopCartorderDTO {
    String _id, nameshop, address, id_user;
    List<CartOrderDTO> list;

    public ShopCartorderDTO() {
    }

    public ShopCartorderDTO(String id, String name_shop, String address, String id_user, List<CartOrderDTO> list) {
        this._id = id;
        this.nameshop = name_shop;
        this.address = address;
        this.id_user = id_user;
        this.list = list;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName_shop() {
        return nameshop;
    }

    public void setName_shop(String name_shop) {
        this.nameshop = name_shop;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public List<CartOrderDTO> getList() {
        return list;
    }

    public void setList(List<CartOrderDTO> list) {
        this.list = list;
    }
}

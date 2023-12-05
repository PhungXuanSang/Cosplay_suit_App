package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_Address {
    @SerializedName("_id")
    String _id;
    String address,phone,fullname;

    public DTO_Address() {
    }

    public DTO_Address(String _id, String address, String phone, String fullname) {
        this._id = _id;
        this.address = address;
        this.phone = phone;
        this.fullname = fullname;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}

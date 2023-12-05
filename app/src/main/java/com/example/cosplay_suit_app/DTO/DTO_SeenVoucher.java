package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_SeenVoucher {
    @SerializedName("_id")
    String id;

    String id_voucher;

    String id_user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_voucher() {
        return id_voucher;
    }

    public void setId_voucher(String id_voucher) {
        this.id_voucher = id_voucher;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}

package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class Favorite {
    @SerializedName("_id")
    private String id;
    private String tb_user;
    @SerializedName("tb_product")
    private  DTO_SanPham sanPham;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTb_user() {
        return tb_user;
    }

    public void setTb_user(String tb_user) {
        this.tb_user = tb_user;
    }

    public DTO_SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(DTO_SanPham sanPham) {
        this.sanPham = sanPham;
    }
}

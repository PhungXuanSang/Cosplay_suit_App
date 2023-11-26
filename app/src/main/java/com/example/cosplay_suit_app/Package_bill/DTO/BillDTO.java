package com.example.cosplay_suit_app.Package_bill.DTO;

import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.DTO.User;
import com.google.gson.annotations.SerializedName;

public class BillDTO {
    @SerializedName("_id")
    String _id;
    @SerializedName("id_user")
    User user;
    @SerializedName("id_shop")
    Shop shop;
    String timestart, timeend, status, ma_voucher;
    int totalPayment;

    public BillDTO() {
    }

    public BillDTO(String _id, User user, Shop shop, String timestart, String timeend, String status, String ma_voucher, int totalPayment) {
        this._id = _id;
        this.user = user;
        this.shop = shop;
        this.timestart = timestart;
        this.timeend = timeend;
        this.status = status;
        this.ma_voucher = ma_voucher;
        this.totalPayment = totalPayment;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMa_voucher() {
        return ma_voucher;
    }

    public void setMa_voucher(String ma_voucher) {
        this.ma_voucher = ma_voucher;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }
}

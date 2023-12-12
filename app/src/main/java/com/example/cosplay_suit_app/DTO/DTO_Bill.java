package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_Bill {
    @SerializedName("_id")
    String _id;
    String id_user, id_shop,id_thanhtoan, timestart, timeend, status, ma_voucher, id_address, discount;
    int totalPayment;

    public DTO_Bill() {
    }

    public DTO_Bill(String _id, String id_user, String id_shop, String id_thanhtoan, String timestart, String timeend, String status, String ma_voucher, String id_address, String discount, int totalPayment) {
        this._id = _id;
        this.id_user = id_user;
        this.id_shop = id_shop;
        this.id_thanhtoan = id_thanhtoan;
        this.timestart = timestart;
        this.timeend = timeend;
        this.status = status;
        this.ma_voucher = ma_voucher;
        this.id_address = id_address;
        this.discount = discount;
        this.totalPayment = totalPayment;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getId_thanhtoan() {
        return id_thanhtoan;
    }

    public void setId_thanhtoan(String id_thanhtoan) {
        this.id_thanhtoan = id_thanhtoan;
    }

    public String getId_address() {
        return id_address;
    }

    public void setId_address(String id_address) {
        this.id_address = id_address;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_shop() {
        return id_shop;
    }

    public void setId_shop(String id_shop) {
        this.id_shop = id_shop;
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

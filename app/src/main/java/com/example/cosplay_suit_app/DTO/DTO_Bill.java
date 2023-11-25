package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_Bill {
    @SerializedName("_id")
    String _id;
    String id_user, id_shop, timestart, timeend, status, ma_voucher;
    int totalPayment;

    public DTO_Bill() {
    }

    public DTO_Bill(String _id, String id_user, String id_shop, String timestart, String timeend, String status, String ma_voucher, int totalPayment) {
        this._id = _id;
        this.id_user = id_user;
        this.id_shop = id_shop;
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

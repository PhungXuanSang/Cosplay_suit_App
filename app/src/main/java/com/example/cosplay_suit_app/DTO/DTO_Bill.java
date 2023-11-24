package com.example.cosplay_suit_app.DTO;

public class DTO_Bill {
    String _id,id_user, id_shop, timestart, timeend, status, id_voucher;
    int totalPayment;

    public DTO_Bill() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public DTO_Bill(String _id, String id_user, String id_shop, String timestart, String timeend, String status, String id_voucher, int totalPayment) {
        this._id = _id;
        this.id_user = id_user;
        this.id_shop = id_shop;
        this.timestart = timestart;
        this.timeend = timeend;
        this.status = status;
        this.id_voucher = id_voucher;
        this.totalPayment = totalPayment;
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

    public String getId_voucher() {
        return id_voucher;
    }

    public void setId_voucher(String id_voucher) {
        this.id_voucher = id_voucher;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }
}

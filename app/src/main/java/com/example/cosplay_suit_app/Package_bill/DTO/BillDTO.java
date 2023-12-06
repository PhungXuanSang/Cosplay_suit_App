package com.example.cosplay_suit_app.Package_bill.DTO;

import com.example.cosplay_suit_app.DTO.DTO_Address;
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
    @SerializedName("id_address")
    DTO_Address address;
    String timestart, timeend, status, ma_voucher,vnp_TxnRef;
    int totalPayment;

    public BillDTO() {
    }

    public BillDTO(String _id, User user, Shop shop, DTO_Address address, String timestart, String timeend, String status, String ma_voucher, String vnp_TxnRef, int totalPayment) {
        this._id = _id;
        this.user = user;
        this.shop = shop;
        this.address = address;
        this.timestart = timestart;
        this.timeend = timeend;
        this.status = status;
        this.ma_voucher = ma_voucher;
        this.vnp_TxnRef = vnp_TxnRef;
        this.totalPayment = totalPayment;
    }

    public String getVnp_TxnRef() {
        return vnp_TxnRef;
    }

    public void setVnp_TxnRef(String vnp_TxnRef) {
        this.vnp_TxnRef = vnp_TxnRef;
    }

    public DTO_Address getAddress() {
        return address;
    }

    public void setAddress(DTO_Address address) {
        this.address = address;
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

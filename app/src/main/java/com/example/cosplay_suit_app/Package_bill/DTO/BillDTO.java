package com.example.cosplay_suit_app.Package_bill.DTO;

import com.example.cosplay_suit_app.DTO.DTO_Address;
import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.ThanhtoanVNpay.DTO_thanhtoan;
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
    @SerializedName("id_thanhtoan")

    DTO_thanhtoan thanhtoan;
    String timestart, timeend, status, ma_voucher, discount;
    int totalPayment;

    public BillDTO() {
    }

    public BillDTO(String _id, User user, Shop shop, DTO_Address address, DTO_thanhtoan thanhtoan, String timestart, String timeend, String status, String ma_voucher, String discount, int totalPayment) {
        this._id = _id;
        this.user = user;
        this.shop = shop;
        this.address = address;
        this.thanhtoan = thanhtoan;
        this.timestart = timestart;
        this.timeend = timeend;
        this.status = status;
        this.ma_voucher = ma_voucher;
        this.discount = discount;
        this.totalPayment = totalPayment;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public DTO_thanhtoan getThanhtoan() {
        return thanhtoan;
    }

    public void setThanhtoan(DTO_thanhtoan thanhtoan) {
        this.thanhtoan = thanhtoan;
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

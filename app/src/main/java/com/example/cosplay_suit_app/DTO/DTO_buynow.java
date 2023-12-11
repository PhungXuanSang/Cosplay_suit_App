package com.example.cosplay_suit_app.DTO;

import java.util.List;

public class DTO_buynow {
    String _id, nameshop, address, id_user;
    int tongbill;
    List<CartOrderDTO> list;
    private GetVoucher_DTO selectedVoucher;

    public DTO_buynow() {
    }

    public DTO_buynow(String _id, String nameshop, String address, String id_user, List<CartOrderDTO> list, int tongbill, GetVoucher_DTO selectedVoucher) {
        this._id = _id;
        this.nameshop = nameshop;
        this.address = address;
        this.id_user = id_user;
        this.list = list;
        this.tongbill = tongbill;
        this.selectedVoucher = selectedVoucher;
    }

    public GetVoucher_DTO getSelectedVoucher() {
        return selectedVoucher;
    }

    public void setSelectedVoucher(GetVoucher_DTO selectedVoucher) {
        this.selectedVoucher = selectedVoucher;
    }

    public int getTongbill() {
        return tongbill;
    }

    public void setTongbill(int tongbill) {
        this.tongbill = tongbill;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNameshop() {
        return nameshop;
    }

    public void setNameshop(String nameshop) {
        this.nameshop = nameshop;
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

package com.example.cosplay_suit_app.DTO;

import com.example.cosplay_suit_app.Package_bill.DTO.BillDTO;
import com.google.gson.annotations.SerializedName;

public class BillDetailDTO {
    @SerializedName("id_product")
    DTO_SanPham dtoSanPham;
    @SerializedName("id_bill")
    BillDTO dtoBill;
    int amount, totalPayment;

    public BillDetailDTO() {
    }

    public BillDetailDTO(DTO_SanPham dtoSanPham, BillDTO dtoBill, int amount, int totalPayment) {
        this.dtoSanPham = dtoSanPham;
        this.dtoBill = dtoBill;
        this.amount = amount;
        this.totalPayment = totalPayment;
    }

    public DTO_SanPham getDtoSanPham() {
        return dtoSanPham;
    }

    public void setDtoSanPham(DTO_SanPham dtoSanPham) {
        this.dtoSanPham = dtoSanPham;
    }

    public BillDTO getDtoBill() {
        return dtoBill;
    }

    public void setDtoBill(BillDTO dtoBill) {
        this.dtoBill = dtoBill;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }
}

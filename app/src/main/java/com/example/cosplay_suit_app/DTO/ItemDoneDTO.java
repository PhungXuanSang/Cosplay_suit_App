package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class ItemDoneDTO {
    @SerializedName("_id")
    String id;
    @SerializedName("id_bill")
    String idBill;
    @SerializedName("id_product")
    ProDoneDTO proDoneDTO;

    int amount;

    long totalPayment;

    public ItemDoneDTO() {
    }

    public ItemDoneDTO(String id, String idBill, ProDoneDTO proDoneDTO, int amount, long totalPayment) {
        this.id = id;
        this.idBill = idBill;
        this.proDoneDTO = proDoneDTO;
        this.amount = amount;
        this.totalPayment = totalPayment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public ProDoneDTO getProDoneDTO() {
        return proDoneDTO;
    }

    public void setProDoneDTO(ProDoneDTO proDoneDTO) {
        this.proDoneDTO = proDoneDTO;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(long totalPayment) {
        this.totalPayment = totalPayment;
    }
}

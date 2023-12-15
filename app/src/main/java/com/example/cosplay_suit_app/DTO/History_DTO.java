package com.example.cosplay_suit_app.DTO;

import com.example.cosplay_suit_app.Package_bill.DTO.BillDTO;
import com.example.cosplay_suit_app.ThanhtoanVNpay.DTO_thanhtoan;
import com.google.gson.annotations.SerializedName;

public class History_DTO {
    @SerializedName("_id")
    String _id;
    @SerializedName("id_bill")
    BillDTO id_bill;
    @SerializedName("receiver_wallet")
    DTO_Wallet dtoWalletreceiver_wallet;
    @SerializedName("sender_wallet")
    DTO_Wallet dtoWalletsender_wallet;
    @SerializedName("implementer")
    User user;
    String  status,transaction_amount,time_transaction;

    public History_DTO() {
    }

    public History_DTO(String _id, BillDTO id_bill, DTO_Wallet dtoWalletreceiver_wallet, DTO_Wallet dtoWalletsender_wallet, User user, String status, String transaction_amount, String time_transaction) {
        this._id = _id;
        this.id_bill = id_bill;
        this.dtoWalletreceiver_wallet = dtoWalletreceiver_wallet;
        this.dtoWalletsender_wallet = dtoWalletsender_wallet;
        this.user = user;
        this.status = status;
        this.transaction_amount = transaction_amount;
        this.time_transaction = time_transaction;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public BillDTO getId_bill() {
        return id_bill;
    }

    public void setId_bill(BillDTO id_bill) {
        this.id_bill = id_bill;
    }

    public DTO_Wallet getDtoWalletreceiver_wallet() {
        return dtoWalletreceiver_wallet;
    }

    public void setDtoWalletreceiver_wallet(DTO_Wallet dtoWalletreceiver_wallet) {
        this.dtoWalletreceiver_wallet = dtoWalletreceiver_wallet;
    }

    public DTO_Wallet getDtoWalletsender_wallet() {
        return dtoWalletsender_wallet;
    }

    public void setDtoWalletsender_wallet(DTO_Wallet dtoWalletsender_wallet) {
        this.dtoWalletsender_wallet = dtoWalletsender_wallet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getTime_transaction() {
        return time_transaction;
    }

    public void setTime_transaction(String time_transaction) {
        this.time_transaction = time_transaction;
    }
}

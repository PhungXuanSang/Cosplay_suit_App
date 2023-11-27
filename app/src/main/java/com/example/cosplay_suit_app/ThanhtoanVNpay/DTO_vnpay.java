package com.example.cosplay_suit_app.ThanhtoanVNpay;

public class DTO_vnpay {
    String bankCode;
    int amount;
    String dataurl;

    public DTO_vnpay() {
    }

    public DTO_vnpay(String bankCode, int amount, String dataurl) {
        this.bankCode = bankCode;
        this.amount = amount;
        this.dataurl = dataurl;
    }

    public String getDataurl() {
        return dataurl;
    }

    public void setDataurl(String dataurl) {
        this.dataurl = dataurl;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

package com.example.cosplay_suit_app.ThanhtoanVNpay;

public class DTO_vnpay {
    String bankCode;
    double amount;
    String dataurl;

    public DTO_vnpay() {
    }

    public DTO_vnpay(String bankCode, double amount, String dataurl) {
        this.bankCode = bankCode;
        this.amount = amount;
        this.dataurl = dataurl;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDataurl() {
        return dataurl;
    }

    public void setDataurl(String dataurl) {
        this.dataurl = dataurl;
    }
}

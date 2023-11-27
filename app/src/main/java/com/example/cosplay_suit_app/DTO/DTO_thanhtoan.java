package com.example.cosplay_suit_app.DTO;

public class DTO_thanhtoan {
    int totalPayment;
    String id_bill, CardType,vnp_BankTranNo,vnp_BankCode,vnp_OrderInfo,vnp_PayDate,vnp_TmnCode,vnp_TxnRef,vnp_SecureHash;

    public DTO_thanhtoan() {
    }

    public DTO_thanhtoan(int totalPayment, String id_bill, String cardType, String vnp_BankTranNo, String vnp_BankCode, String vnp_OrderInfo, String vnp_PayDate, String vnp_TmnCode, String vnp_TxnRef, String vnp_SecureHash) {
        this.totalPayment = totalPayment;
        this.id_bill = id_bill;
        CardType = cardType;
        this.vnp_BankTranNo = vnp_BankTranNo;
        this.vnp_BankCode = vnp_BankCode;
        this.vnp_OrderInfo = vnp_OrderInfo;
        this.vnp_PayDate = vnp_PayDate;
        this.vnp_TmnCode = vnp_TmnCode;
        this.vnp_TxnRef = vnp_TxnRef;
        this.vnp_SecureHash = vnp_SecureHash;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getId_bill() {
        return id_bill;
    }

    public void setId_bill(String id_bill) {
        this.id_bill = id_bill;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getVnp_BankTranNo() {
        return vnp_BankTranNo;
    }

    public void setVnp_BankTranNo(String vnp_BankTranNo) {
        this.vnp_BankTranNo = vnp_BankTranNo;
    }

    public String getVnp_BankCode() {
        return vnp_BankCode;
    }

    public void setVnp_BankCode(String vnp_BankCode) {
        this.vnp_BankCode = vnp_BankCode;
    }

    public String getVnp_OrderInfo() {
        return vnp_OrderInfo;
    }

    public void setVnp_OrderInfo(String vnp_OrderInfo) {
        this.vnp_OrderInfo = vnp_OrderInfo;
    }

    public String getVnp_PayDate() {
        return vnp_PayDate;
    }

    public void setVnp_PayDate(String vnp_PayDate) {
        this.vnp_PayDate = vnp_PayDate;
    }

    public String getVnp_TmnCode() {
        return vnp_TmnCode;
    }

    public void setVnp_TmnCode(String vnp_TmnCode) {
        this.vnp_TmnCode = vnp_TmnCode;
    }

    public String getVnp_TxnRef() {
        return vnp_TxnRef;
    }

    public void setVnp_TxnRef(String vnp_TxnRef) {
        this.vnp_TxnRef = vnp_TxnRef;
    }

    public String getVnp_SecureHash() {
        return vnp_SecureHash;
    }

    public void setVnp_SecureHash(String vnp_SecureHash) {
        this.vnp_SecureHash = vnp_SecureHash;
    }
}

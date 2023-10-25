package com.example.cosplay_suit_app.DTO;

public class DTO_Bill {
    String id_user, id_shop, timestart, timeend, status;
    int totalPayment;

    public DTO_Bill() {
    }

    public DTO_Bill(String id_user, String id_shop, String timestart, String timeend, String status, int totalPayment) {
        this.id_user = id_user;
        this.id_shop = id_shop;
        this.timestart = timestart;
        this.timeend = timeend;
        this.status = status;
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

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }
}

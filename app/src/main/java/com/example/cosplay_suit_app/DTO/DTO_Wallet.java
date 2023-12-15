package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class DTO_Wallet {
    @SerializedName("_id")
    String _id;
    String id_user, money, currency, passwd;

    public DTO_Wallet() {
    }

    public DTO_Wallet(String _id, String id_user, String money, String currency, String passwd) {
        this._id = _id;
        this.id_user = id_user;
        this.money = money;
        this.currency = currency;
        this.passwd = passwd;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
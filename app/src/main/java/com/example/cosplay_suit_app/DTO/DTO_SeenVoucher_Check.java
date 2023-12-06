package com.example.cosplay_suit_app.DTO;

public class DTO_SeenVoucher_Check {
    DTO_SeenVoucher seenVoucher;
    String msg;
    public DTO_SeenVoucher getSeenVoucher() {
        return seenVoucher;
    }

    public void setSeenVoucher(DTO_SeenVoucher seenVoucher) {
        this.seenVoucher = seenVoucher;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

package com.example.cosplay_suit_app.DTO;

import java.util.Map;

public class FcmMessage {

    private String to;
    private Map<String, String> data;

    public FcmMessage() {

    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}

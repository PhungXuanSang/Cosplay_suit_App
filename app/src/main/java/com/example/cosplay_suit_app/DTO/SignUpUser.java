package com.example.cosplay_suit_app.DTO;

public class SignUpUser {
    User user;

    String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SignUpUser(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public SignUpUser() {
    }
}

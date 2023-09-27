package com.example.cosplay_suit_app.DTO;

public class LoginUser {
    User user;

    public LoginUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

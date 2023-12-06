package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

public class ProfileDTO {
    @SerializedName("_id")
    String id;

    @SerializedName("id_user")
    User id_user;

    boolean check = false;

    String phone,diachi,email,fullname;
//    String id_user;

    public ProfileDTO() {
    }

    public ProfileDTO( String phone, String diachi, String email, String fullname, User id_user) {
        this.phone = phone;
        this.diachi = diachi;
        this.email = email;
        this.fullname = fullname;
        this.id_user = id_user;
    }


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public User getId_user() {
        return id_user;
    }

    public void setId_user(User id_user) {
        this.id_user = id_user;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}

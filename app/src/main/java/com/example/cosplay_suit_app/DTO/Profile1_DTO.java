package com.example.cosplay_suit_app.DTO;



import com.google.gson.annotations.SerializedName;

public class Profile1_DTO {
    @SerializedName("_id")
    String id;

    @SerializedName("id_user")
    String id_user;

    String phone,diachi,email,fullname;

    public Profile1_DTO() {
    }

    public Profile1_DTO(String id, String id_user, String phone, String diachi, String email, String fullname) {
        this.id = id;
        this.id_user = id_user;
        this.phone = phone;
        this.diachi = diachi;
        this.email = email;
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
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
}

package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CmtsDTO {

    @SerializedName("_id")
    String id;
    @SerializedName("id_product")
    String idPro;
    @SerializedName("id_user")
    User user;

    String content;
    String time;
    List<ImageCmtsDTO> image;
    int star;

    public CmtsDTO() {
    }

    public CmtsDTO(String idPro, User user, String content, String time, List<ImageCmtsDTO> image, int star) {
        this.idPro = idPro;
        this.user = user;
        this.content = content;
        this.time = time;
        this.image = image;
        this.star = star;
    }

    public String getIdPro() {
        return idPro;
    }

    public void setIdPro(String idPro) {
        this.idPro = idPro;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ImageCmtsDTO> getImage() {
        return image;
    }

    public void setImage(List<ImageCmtsDTO> image) {
        this.image = image;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}

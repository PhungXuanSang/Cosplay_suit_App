package com.example.cosplay_suit_app.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCmtsDTO {
    @SerializedName("_id")
    String id;
    @SerializedName("id_product")
    DTO_SanPham idPro;
    @SerializedName("id_user")
    User user;

    String id_bill;
    String content;
    String time;
    List<ImageCmtsDTO> image;
    int star;

    public GetCmtsDTO() {
    }


    public GetCmtsDTO(DTO_SanPham idPro, User user, String content, String time, List<ImageCmtsDTO> image, int star) {
        this.idPro = idPro;
        this.user = user;
        this.content = content;
        this.time = time;
        this.image = image;
        this.star = star;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_bill() {
        return id_bill;
    }

    public void setId_bill(String id_bill) {
        this.id_bill = id_bill;
    }

    public DTO_SanPham getIdPro() {
        return idPro;
    }

    public void setIdPro(DTO_SanPham idPro) {
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

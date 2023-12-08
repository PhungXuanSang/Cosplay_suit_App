package com.example.cosplay_suit_app.DTO;

import com.example.cosplay_suit_app.Package_bill.DTO.DTO_Getvoucher;
import com.google.gson.annotations.SerializedName;

public class GetVoucher_DTO {
    String _id;
    @SerializedName("id_voucher")
    DTO_Getvoucher dtoVoucher;
    @SerializedName("id_user")
    User user;

    public GetVoucher_DTO() {
    }

    public GetVoucher_DTO(String _id, DTO_Getvoucher dtoVoucher, User user) {
        this._id = _id;
        this.dtoVoucher = dtoVoucher;
        this.user = user;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public DTO_Getvoucher getDtoVoucher() {
        return dtoVoucher;
    }

    public void setDtoVoucher(DTO_Getvoucher dtoVoucher) {
        this.dtoVoucher = dtoVoucher;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

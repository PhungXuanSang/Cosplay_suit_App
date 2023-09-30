package com.example.cosplay_suit_app.DTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SanPhamInterface {
    @GET("getlistsp")
    Call<List<DTO_SanPham>> lay_danh_sach ();

}

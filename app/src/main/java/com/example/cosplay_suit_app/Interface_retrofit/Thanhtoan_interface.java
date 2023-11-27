package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_thanhtoan;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Thanhtoan_interface {
    @POST("Addthanhtoan")
    Call<DTO_thanhtoan> Addthanhtoan(@Body DTO_thanhtoan dtoThanhtoan);
}

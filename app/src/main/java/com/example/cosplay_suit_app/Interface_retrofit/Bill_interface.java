package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Bill_interface {
    @POST("addbill")
    Call<DTO_Bill> addbill(@Body DTO_Bill dtoBill);
}

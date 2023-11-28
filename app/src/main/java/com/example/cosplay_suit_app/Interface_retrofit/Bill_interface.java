package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Bill_interface {
    @POST("addbill")
    Call<DTO_Bill> addbill(@Body DTO_Bill dtoBill);

    @PUT("upstatusbill/{id}")
    Call<DTO_Bill> upstatusbill(@Path("id")String id, @Body DTO_Bill dtoBill);
}

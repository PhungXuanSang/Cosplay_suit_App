package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.ThanhtoanVNpay.DTO_thanhtoan;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Thanhtoan_interface {
    @POST("Addthanhtoan")
    Call<DTO_thanhtoan> Addthanhtoan(@Body DTO_thanhtoan dtoThanhtoan);

    @GET("getidaddress/{id}")
    Call<ProfileDTO> getidaddress(@Path("id") String id);
}

package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.DTO_Wallet;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Wallet_interface {
    @POST("addwallet")
    Call<DTO_Wallet> addwallet(@Body DTO_Wallet dtoWallet);
    @GET("getwallet/{iduser}")
    Call<DTO_Wallet> getwallet(@Path("iduser") String iduser);
}

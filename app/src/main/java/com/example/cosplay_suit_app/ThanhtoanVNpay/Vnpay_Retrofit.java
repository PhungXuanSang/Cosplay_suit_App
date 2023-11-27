package com.example.cosplay_suit_app.ThanhtoanVNpay;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Vnpay_Retrofit {
    @POST("create_payment_url")
    Call<DTO_vnpay> createpaymenturl(@Body DTO_vnpay objU);
    @POST("vnpay_refund")
    Call<DTO_hoantien> apirefund(@Body DTO_hoantien dtoHoantien);
}

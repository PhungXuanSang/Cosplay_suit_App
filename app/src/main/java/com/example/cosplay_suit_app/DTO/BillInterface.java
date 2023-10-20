package com.example.cosplay_suit_app.DTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BillInterface {
    @GET("getlistcartorder")
    Call<List<CartOrder>> getlistcartorder(@Body CartOrder objT);

    @POST("addcart")
    Call<CartOrder> addcart(@Body CartOrder objT);

    @PUT("updatecart/{id}")
    Call<CartOrder> updatecart(@Path("id") String id, @Body CartOrder objT);

    @DELETE("deletecart/{id}")
    Call<CartOrder> deletecart(@Path("id") String id, @Body CartOrder objT);

}

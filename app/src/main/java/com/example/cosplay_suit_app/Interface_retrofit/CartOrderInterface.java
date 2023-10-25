package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartOrderInterface {
    @GET("getlistcartorder")
    Call<List<CartOrderDTO>> getlistcartorder();

    @GET("getusercartorder/{id}")
    Call<List<CartOrderDTO>> getusercartorder(@Path("id") String id);
    @POST("addcart")
    Call<DTO_CartOrder> addcart(@Body DTO_CartOrder objT);

    @PUT("updatecart/{id}")
    Call<CartOrderDTO> updatecart(@Path("id") String id, @Body CartOrderDTO objT);

    @DELETE("deletecart/{id}")
    Call<CartOrderDTO> deletecart(@Path("id") String id, @Body CartOrderDTO objT);

}

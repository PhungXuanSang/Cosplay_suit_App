package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;

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
    Call<List<DTO_CartOrder>> getlistcartorder();

    @GET("getusercartorder/{id}")
    Call<List<DTO_CartOrder>> getusercartorder(@Path("id") String id);
    @POST("addcart")
    Call<CartOrderDTO> addcart(@Body CartOrderDTO objT);

    @PUT("updatecart/{id}")
    Call<DTO_CartOrder> updatecart(@Path("id") String id, @Body DTO_CartOrder objT);

    @DELETE("deletecart/{id}")
    Call<DTO_CartOrder> deletecart(@Path("id") String id, @Body DTO_CartOrder objT);

}

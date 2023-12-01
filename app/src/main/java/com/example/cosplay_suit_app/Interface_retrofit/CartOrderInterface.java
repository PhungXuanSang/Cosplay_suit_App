package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.DTO_buynow;
import com.example.cosplay_suit_app.DTO.DTO_inbuynow;
import com.example.cosplay_suit_app.DTO.ShopCartorderDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartOrderInterface {
    @GET("getShop/{id}")
    Call<List<DTO_buynow>> getShopBuynow(@Path("id") String id);
    @GET("getShop/{id}")
    Call<List<ShopCartorderDTO>> getShop(@Path("id") String id);
    @GET("getidCartOder/{id}")
    Call<List<CartOrderDTO>> getidCartOder(@Path("id") String id);
    @GET("getusercartorder/{id}")
    Call<List<CartOrderDTO>> getusercartorder(@Path("id") String id);
    @GET("getusercartorder/{id}")
    Call<List<DTO_inbuynow>> getShopidcart(@Path("id") String id);
    @POST("addcart")
    Call<DTO_CartOrder> addcart(@Body DTO_CartOrder objT);
    @POST("checkaddcart/{id}")
    Call<CartOrderDTO> checkaddcart(@Path("id") String id);
    @PUT("updatecart/{id}")
    Call<CartOrderDTO> updatecart(@Path("id") String id, @Body CartOrderDTO objT);

    @DELETE("deletecart/{id}")
    Call<CartOrderDTO> deletecart(@Path("id") String id);


}

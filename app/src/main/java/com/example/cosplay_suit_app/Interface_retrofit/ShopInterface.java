package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.Shop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShopInterface {

    @GET("listShop/{id_user}")
    Call<List<Shop>> listShop(@Path("id_user") String iduser);

    @GET("getShopById/{id}")
    Call<Shop> shopById(@Path("id") String id);
}

package com.example.cosplay_suit_app.DTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SanPhamInterface {
    @GET("getlistsp")
    Call<List<DTO_SanPham>> lay_danh_sach ();

    @GET("favorite/{tb_user}/{tb_product}")
    Call<Favorite> list_favorite(@Path("tb_user") String tb_user ,@Path("tb_product") String tb_product);

    @DELETE("favorite/{tb_user}/{tb_product}")
    Call<Void> delete_favorite(@Path("tb_user") String tb_user ,@Path("tb_product") String tb_product);

    @POST("favorite")
    Call<Favorite> add_favorite(@Body Favorite favorite);

}

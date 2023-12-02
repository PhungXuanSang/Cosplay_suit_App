package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.ProByCatDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryInterface {

    @GET("getListCat")
    Call<List<CategoryDTO>> getListCat();

    @GET("getlistprobyidcat/{id}")
    Call<List<ProByCatDTO>> getListProByIdCat(@Path("id") String id);
}

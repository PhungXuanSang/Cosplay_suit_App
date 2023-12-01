package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryInterface {

    @GET("getListCat")
    Call<List<CategoryDTO>> getListCat();

}

package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.CmtsDTO;
import com.example.cosplay_suit_app.DTO.GetCmtsDTO;
import com.example.cosplay_suit_app.DTO.ItemDoneDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CmtsInterface {

    @GET("listCmts/{id}")
    Call<List<CmtsDTO>> getListCmts(@Path("id") String id);

    @GET("listCDG/{id}")
    Call<List<ItemDoneDTO>> getListDhWithoutCmts(@Path("id") String id);

    @GET("listDDG/{id}")
    Call<List<ItemDoneDTO>> getListDhWithCmts(@Path("id") String id);

    @GET("listCmtsFU/{id}")
    Call<List<GetCmtsDTO>> getListCmtsFU(@Path("id") String id);

    @POST("addCmts")
    Call<CmtsDTO> addCmts(@Body CmtsDTO cmtsDTO);



}

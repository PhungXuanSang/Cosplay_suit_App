package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.CmtsDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CmtsInterface {

    @GET("listCmts/{id}")
    Call<List<BillDetailDTO>> getListCmts(@Path("id") String id);

    @POST("addCmts")
    Call<List<BillDetailDTO>> addCmts(@Body CmtsDTO cmtsDTO);



}

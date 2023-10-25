package com.example.cosplay_suit_app.Interface_retrofit;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Billdentail_Interfece {
    @GET("getbilldentail/{id}")
    Call<List<BillDetailDTO>> getbilldentail(@Path("id") String id);
}

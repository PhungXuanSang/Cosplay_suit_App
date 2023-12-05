package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.Package_bill.DTO.BillDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TkeInterface {
    @GET("gettkebill/{id_shop}")
    Call<List<BillDTO>> getBills(
            @Path("id_shop")String getTke,
            @Query("timeend[gte]") String ngaybatdau,
            @Query("timeend[lte]") String ngayketthuc
    );
}

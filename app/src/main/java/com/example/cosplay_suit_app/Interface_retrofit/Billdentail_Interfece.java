package com.example.cosplay_suit_app.Interface_retrofit;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Billdentail_Interfece {
    @GET("getstatuswait/{id}")
    Call<List<BillDetailDTO>> getstatuswait(@Path("id") String id);

    @GET("getstatusDelivery/{id}")
    Call<List<BillDetailDTO>> getstatusDelivery(@Path("id") String id);

    @GET("getstatusPack/{id}")
    Call<List<BillDetailDTO>> getstatusPack(@Path("id") String id);

    @GET("getstatusDone/{id}")
    Call<List<BillDetailDTO>> getstatusDone(@Path("id") String id);
}

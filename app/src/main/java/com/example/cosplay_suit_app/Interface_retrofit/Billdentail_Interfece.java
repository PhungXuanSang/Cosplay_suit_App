package com.example.cosplay_suit_app.Interface_retrofit;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_billdetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Billdentail_Interfece {
    @GET("getstatuswait/{type}/{id}")
    Call<List<BillDetailDTO>> getstatuswait(@Path("type") String type,@Path("id") String id);

    @GET("getstatusDelivery/{type}/{id}")
    Call<List<BillDetailDTO>> getstatusDelivery(@Path("type") String type,@Path("id") String id);

    @GET("getstatusPack/{type}/{id}")
    Call<List<BillDetailDTO>> getstatusPack(@Path("type") String type,@Path("id") String id);

    @GET("getstatusDone/{type}/{id}")
    Call<List<BillDetailDTO>> getstatusDone(@Path("type") String type,@Path("id") String id);

    @POST("addbilldetail")
    Call<DTO_billdetail> addbilldetail(@Body DTO_billdetail dtoBill);

    @GET("getdsmualaisp/{id}")
    Call<List<BillDetailDTO>> getdsmualaisp(@Path("id") String id, @Query("limit") int limit);
    @GET("getallmualaisp/{id}")
    Call<List<BillDetailDTO>> getallmualaisp(@Path("id") String id);
}

package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.DTOcheck_productshop;
import com.example.cosplay_suit_app.DTO.GetVoucher_DTO;
import com.example.cosplay_suit_app.DTO.ProfileDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Bill_interface {
    @POST("addbill")
    Call<DTO_Bill> addbill(@Body DTO_Bill dtoBill);
    @PUT("upstatusbill/{id}")
    Call<DTO_Bill> upstatusbill(@Path("id")String id, @Body DTO_Bill dtoBill);
    @GET("getdskhach/{id}")
    Call<List<ProfileDTO>> getdskhach(@Path("id")String id);
    @POST("upsoluongproduct")
    Call<Void> Upsoluongproduct(@Body List<String> idList);
    @POST("upproducts/{id}")
    Call<DTO_properties> updateProduct(@Path("id") String productId, @Body DTO_properties updateRequest);
    @GET("checkspuser/{id}")
    Call<DTOcheck_productshop> getcheckproduct(@Path("id") String id);
    @GET("getvoucher/{id}")
    Call<List<GetVoucher_DTO>> getVoucher(@Path("id") String id);
}

package com.example.cosplay_suit_app.Interface_retrofit;


import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.DTO_SeenVoucher;
import com.example.cosplay_suit_app.DTO.DTO_SeenVoucher_Check;
import com.example.cosplay_suit_app.DTO.DTO_voucher;
import com.example.cosplay_suit_app.DTO.GetVoucher_DTO;
import com.example.cosplay_suit_app.DTO.ProfileDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VoucherInterface {
    @GET("voucher/{id}")
    Call<List<DTO_voucher>> getVoucherByShop(@Path("id") String id);
    @POST("postvoucher")
    Call<DTO_voucher> postVoucherByShop(@Body DTO_voucher dto);
    @PUT("upvoucher/{id}")
    Call<DTO_voucher> updateVoucherByShop(@Path("id") String id,@Body DTO_voucher dtoVoucher);
    @DELETE("delvoucher/{id}")
    Call<DTO_voucher> deleteVoucherByShop(@Path("id") String id);

    @POST("seenvoucher")
    Call<DTO_SeenVoucher_Check> postSeen(@Body DTO_SeenVoucher dto);

    @PUT("updatevoucherSeen/{id}")
    Call<DTO_voucher> updateVoucherSeen(@Path("id") String id,@Body DTO_voucher dtoVoucher);

    @GET("listvoucher/{id_voucher}")
    Call<List<DTO_SeenVoucher>> getlistSeen(@Path("id_voucher") String id_voucher);
    @DELETE("deleteseenvoucher/{id}")
    Call<DTO_SeenVoucher> deleteseenvoucher(@Path("id") String id);
    @GET("getlistseenvoucheruser/{id_user}")
    Call<List<GetVoucher_DTO>> getlistseenvoucheruser(@Path("id_user") String id_user);
}

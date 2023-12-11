package com.example.cosplay_suit_app.bill.controller;

import android.content.Context;
import android.util.Log;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.DTO_SeenVoucher;
import com.example.cosplay_suit_app.DTO.GetVoucher_DTO;
import com.example.cosplay_suit_app.Interface_retrofit.VoucherInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Voucher_controller {
    static String url = API.URL;
    static final String BASE_URL_VOUCHER = url + "/Voucher/";
    Context context;
    public Voucher_controller(Context context) {
        this.context = context;
    }
    public void Deleteseenvoucher(String id){
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_VOUCHER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        VoucherInterface voucherInterface = retrofit.create(VoucherInterface.class);

        //tạo đối tượng
        Call<DTO_SeenVoucher> objCall = voucherInterface.deleteseenvoucher(id);
        objCall.enqueue(new Callback<DTO_SeenVoucher>() {
            @Override
            public void onResponse(Call<DTO_SeenVoucher> call, Response<DTO_SeenVoucher> response) {
                if (response.isSuccessful()) {

                } else {
                    Log.e("Deleteseenvoucher", response.message());
                }
            }
            @Override
            public void onFailure(Call<DTO_SeenVoucher> call, Throwable t) {
                Log.e("Deleteseenvoucher", t.getLocalizedMessage());
            }
        });
    }
    public void Getlistseenvoucheruser(String id, Apiseenvoucheruser apiseenvoucheruser){
        // tạo gson
        Gson gson = new GsonBuilder().setLenient().create();

        // Create a new object from HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_VOUCHER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        VoucherInterface voucherInterface = retrofit.create(VoucherInterface.class);

        // tạo đối tượng
        Call<List<GetVoucher_DTO>> objCall = voucherInterface.getlistseenvoucheruser(id);
        objCall.enqueue(new Callback<List<GetVoucher_DTO>>() {
            @Override
            public void onResponse(Call<List<GetVoucher_DTO>> call, Response<List<GetVoucher_DTO>> response) {
                if (response.isSuccessful()) {
                    if (apiseenvoucheruser != null) {
                        if (response.isSuccessful()) {
                            apiseenvoucheruser.onApiseenvoucheruserl(response.body());
                        }
                    }
                } else {
                    Log.d("Getlistseenvoucheruser", "Không lấy được dữ liệu: ");
                }
            }

            @Override
            public void onFailure(Call<List<GetVoucher_DTO>> call, Throwable t) {
                Log.d("Getlistseenvoucheruser", "onFailure: " + t);
            }
        });
    }
    public interface Apiseenvoucheruser {
        void onApiseenvoucheruserl(List<GetVoucher_DTO> billDTO);
    }
}

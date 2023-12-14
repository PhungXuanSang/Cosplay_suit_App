package com.example.cosplay_suit_app.bill.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
import com.example.cosplay_suit_app.DTO.DTO_Wallet;
import com.example.cosplay_suit_app.DTO.GetVoucher_DTO;
import com.example.cosplay_suit_app.Interface_retrofit.Bill_interface;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.Interface_retrofit.Wallet_interface;
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

public class Wallet_controller {
    private static final String TAG = "wallet";
    static String url = API.URL;
    static final String BASE_URL_WALLET = url + "/vitrunggianapi/";
    Context mContext;

    public Wallet_controller(Context mContext) {
        this.mContext = mContext;
    }

    public void getWallet(String id, ApiGetwalet apiGetwalet) {

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
                .baseUrl(BASE_URL_WALLET)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        Wallet_interface walletInterface = retrofit.create(Wallet_interface.class);

        // tạo đối tượng
        Call<DTO_Wallet> objCall = walletInterface.getwallet(id);
        objCall.enqueue(new Callback<DTO_Wallet>() {
            @Override
            public void onResponse(@NonNull Call<DTO_Wallet> call, @NonNull Response<DTO_Wallet> response) {
                if (apiGetwalet != null) {
                    if (response.isSuccessful()) {
                        apiGetwalet.onApiGetwalet(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<DTO_Wallet> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }
    public interface ApiGetwalet {
        void onApiGetwalet(DTO_Wallet getVoucherDto);
    }
    public void AddWallet(DTO_Wallet dtoWallet) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_WALLET)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Wallet_interface walletInterface = retrofit.create(Wallet_interface.class);
        Call<DTO_Wallet> objCall = walletInterface.addwallet(dtoWallet);

        objCall.enqueue(new Callback<DTO_Wallet>() {
            @Override
            public void onResponse(Call<DTO_Wallet> call, Response<DTO_Wallet> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "thêm ví wallet thành công: ");
                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_Wallet> call, Throwable t) {
                // Sử dụng mContext để hiển thị thông báo lỗi
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }

}

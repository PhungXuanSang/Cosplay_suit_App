package com.example.cosplay_suit_app.bill.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
import com.example.cosplay_suit_app.DTO.DTO_billdetail;
import com.example.cosplay_suit_app.Interface_retrofit.Bill_interface;
import com.example.cosplay_suit_app.Interface_retrofit.Billdentail_Interfece;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Bill_controller {
    private static final String TAG = "addbill";
    static String url = API.URL;
    static final String BASE_URL_CARTORDER = url + "/bill/";
    Context mContext;

    // Constructor để khởi tạo context và base URL
    public Bill_controller(Context context) {
        this.mContext = context;
    }

    public void AddCart(DTO_CartOrder objcart) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CartOrderInterface billInterface = retrofit.create(CartOrderInterface.class);
        Call<DTO_CartOrder> objCall = billInterface.addcart(objcart);

        objCall.enqueue(new Callback<DTO_CartOrder>() {
            @Override
            public void onResponse(Call<DTO_CartOrder> call, Response<DTO_CartOrder> response) {
                if (response.isSuccessful()) {
                    // Sử dụng mContext để hiển thị Toast
                    Toast.makeText(mContext, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_CartOrder> call, Throwable t) {
                // Sử dụng mContext để hiển thị thông báo lỗi
                Toast.makeText(mContext, "Lỗi: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }
    public void Addbill(DTO_Bill dtoBill) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Bill_interface billInterface = retrofit.create(Bill_interface.class);
        Call<DTO_Bill> objCall = billInterface.addbill(dtoBill);

        objCall.enqueue(new Callback<DTO_Bill>() {
            @Override
            public void onResponse(Call<DTO_Bill> call, Response<DTO_Bill> response) {
                if (response.isSuccessful()) {
                    // Sử dụng mContext để hiển thị Toast

                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_Bill> call, Throwable t) {
                // Sử dụng mContext để hiển thị thông báo lỗi
                Toast.makeText(mContext, "Lỗi: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }

    public void addbilldetail(DTO_billdetail dtoBill) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Billdentail_Interfece billInterface = retrofit.create(Billdentail_Interfece.class);
        Call<DTO_billdetail> objCall = billInterface.addbilldetail(dtoBill);

        objCall.enqueue(new Callback<DTO_billdetail>() {
            @Override
            public void onResponse(Call<DTO_billdetail> call, Response<DTO_billdetail> response) {
                if (response.isSuccessful()) {
                    // Sử dụng mContext để hiển thị Toast

                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_billdetail> call, Throwable t) {
                // Sử dụng mContext để hiển thị thông báo lỗi
                Toast.makeText(mContext, "Lỗi: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }

}

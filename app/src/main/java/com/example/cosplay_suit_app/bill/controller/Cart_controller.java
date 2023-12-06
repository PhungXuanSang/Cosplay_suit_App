package com.example.cosplay_suit_app.bill.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.BuynowActivity;
import com.example.cosplay_suit_app.Activity.CartOrderActivity;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.DTO_Address;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
import com.example.cosplay_suit_app.DTO.DTO_buynow;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.Package_bill.DTO.BillDTO;
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

public class Cart_controller {
    String TAG ="Cart_controller";
    static String url = API.URL;
    static final String BASE_URL_CARTORDER = url + "/bill/";
    Context context;

    public Cart_controller(Context context) {
        this.context = context;
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
                    String title = "Thông báo giỏ hàng";
                    String message = "Thêm vào giỏ hàng thành công";
                    Dialogthongbao.showSuccessDialog(context, title, message);
                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_CartOrder> call, Throwable t) {
                // Sử dụng mContext để hiển thị thông báo lỗi
                Toast.makeText(context, "Lỗi: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }

    public void DeleteCartorder(String idcart){
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        CartOrderInterface cartOrderInterface = retrofit.create(CartOrderInterface.class);

        //tạo đối tượng
        Call<CartOrderDTO> objCall = cartOrderInterface.deletecart(idcart);
        objCall.enqueue(new Callback<CartOrderDTO>() {
            @Override
            public void onResponse(Call<CartOrderDTO> call, Response<CartOrderDTO> response) {
                if (response.isSuccessful()) {
                } else {
                    Log.e(TAG, response.message());
                }
            }
            @Override
            public void onFailure(Call<CartOrderDTO> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void chonDeleteCartorder(String idcart){
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        CartOrderInterface cartOrderInterface = retrofit.create(CartOrderInterface.class);

        //tạo đối tượng
        Call<CartOrderDTO> objCall = cartOrderInterface.deletecart(idcart);
        objCall.enqueue(new Callback<CartOrderDTO>() {
            @Override
            public void onResponse(Call<CartOrderDTO> call, Response<CartOrderDTO> response) {
                if (response.isSuccessful()) {
                    String title = "Thông báo giỏ hàng";
                    String message = "Xóa sản phẩm khỏi giỏ hàng thành công";
                    Dialogthongbao.showSuccessDialog(context, title, message);
                } else {
                    Log.e(TAG, response.message());
                }
            }
            @Override
            public void onFailure(Call<CartOrderDTO> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void getidbill(String id, Apigetidbill apigetidbill){
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
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        CartOrderInterface billInterface = retrofit.create(CartOrderInterface.class);

        // tạo đối tượng
        Call<BillDTO> objCall = billInterface.getidbill(id);
        objCall.enqueue(new Callback<BillDTO>() {
            @Override
            public void onResponse(Call<BillDTO> call, Response<BillDTO> response) {
                if (response.isSuccessful()) {
                    if (apigetidbill != null) {
                        if (response.isSuccessful()) {
                            apigetidbill.onApigetidbill(response.body());
                        }
                    }
                } else {
                    Log.d(TAG, "Không lấy được dữ liệu: ");
                }
            }

            @Override
            public void onFailure(Call<BillDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }
    public interface Apigetidbill {
        void onApigetidbill(BillDTO billDTO);
    }

}

package com.example.cosplay_suit_app.bill.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.MualaiActivity;
import com.example.cosplay_suit_app.Adapter.Adapter_buynow;
import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_billdetail;
import com.example.cosplay_suit_app.DTO.DTO_idbill;
import com.example.cosplay_suit_app.Interface_retrofit.Bill_interface;
import com.example.cosplay_suit_app.Interface_retrofit.Billdentail_Interfece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Mualai_controller {
    static String url = API.URL;
    static final String BASE_URL_CARTORDER = url + "/bill/";
    Context context;
    private static final String TAG = "Mualai_controller";
    DTO_idbill dtoIdbill;
    private MualaiActivity.OnAddBillCompleteListener onAddBillCompleteListener;
    public void setOnAddBillCompleteListener(MualaiActivity.OnAddBillCompleteListener listener) {
        this.onAddBillCompleteListener = listener;
    }
    public Mualai_controller(Context context) {
        this.context = context;
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
                    DTO_Bill result = response.body();
                    dtoIdbill = new DTO_idbill();
                    dtoIdbill.set_id(result.get_id());
                    dtoIdbill.setId_shop(result.getId_shop());
                    if (onAddBillCompleteListener != null) {
                        onAddBillCompleteListener.onAddBillComplete();
                    }
                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_Bill> call, Throwable t) {
                // Sử dụng mContext để hiển thị thông báo lỗi
                Toast.makeText(context, "Lỗi: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }
    public void databilldetail(int amount, String selectedNameProperties, int totalPayment, String idproduct){
        DTO_billdetail dtoBilldetail = new DTO_billdetail();
        dtoBilldetail.setAmount(amount);
        dtoBilldetail.setSize(selectedNameProperties);
        dtoBilldetail.setTotalPayment(totalPayment);
        dtoBilldetail.setId_bill(dtoIdbill.get_id());
        dtoBilldetail.setId_product(idproduct);

        Addbilldetail(dtoBilldetail);
    }

    public void Addbilldetail(DTO_billdetail dtoBill) {
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
                Toast.makeText(context, "Lỗi: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }
}

package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.AdapterCartorder;
import com.example.cosplay_suit_app.Adapter.AdapterKhachHang;
import com.example.cosplay_suit_app.Adapter.Adapter_dskhach;
import com.example.cosplay_suit_app.DTO.DTO_SeenVoucher;
import com.example.cosplay_suit_app.DTO.Product_Page;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.Interface_retrofit.VoucherInterface;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeenVoucherActivity extends AppCompatActivity implements AdapterKhachHang.OnlickCheckVoucher{
    static String url = API.URL;
    static final String BASE_URL = url + "/Voucher/";
    ImageView id_back;
    RecyclerView recyclerView;
    ArrayList<ProfileDTO> list;

    AdapterKhachHang adapterKhachHang;
    List<String> stringList = new ArrayList<>();
    String id_voucher, amount;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_voucher);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng chờ...");
        Anhxa();
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        Intent intent = getIntent();
        id_voucher = intent.getStringExtra("id_voucher");
        amount = intent.getStringExtra("amount");
        list = new ArrayList<>();
        adapterKhachHang = new AdapterKhachHang(list,SeenVoucherActivity.this,this);
        recyclerView.setAdapter(adapterKhachHang);
        adapterKhachHang.notifyDataSetChanged();
        Bill_controller billController = new Bill_controller(this);
        billController.GetdskhachVoucher(id,list, adapterKhachHang);


        findViewById(R.id.id_btn_gui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringList.size()==0){
                    Toast.makeText(SeenVoucherActivity.this, "Vui lòng chọn ít nhất 1 khách hàng để gửi voucher!!", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    for (int i = 0; i < stringList.size();i++){
//                        Log.e("manh", "onClick1: " + stringList.get(i));
                        DTO_SeenVoucher seenVoucher = new DTO_SeenVoucher();
                        seenVoucher.setId_user(stringList.get(i));
                        Log.e("manh", "onClick: " + id_voucher );
                        seenVoucher.setId_voucher(id_voucher);
                        CallSeenVoucher(seenVoucher);
                        if (i == stringList.size()-1){
                            progressDialog.dismiss();
                        }
                    }
                }

            }
        });

    }

    public void Anhxa(){
//        id_back = findViewById(R.id.id_back);
        recyclerView = findViewById(R.id.id_recyclerGui);
    }

    @Override
    public void Check_ID(String id_user) {
        stringList.add(id_user);
    }

    @Override
    public void Check_Delete_ID(String id_user) {
        stringList.remove(id_user);
    }


    void CallSeenVoucher(DTO_SeenVoucher dto_seenVoucher){
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
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        VoucherInterface voucherInterface = retrofit.create(VoucherInterface.class);

        // tạo đối tượng
        Call<DTO_SeenVoucher> objCall = voucherInterface.postSeen(dto_seenVoucher);
        objCall.enqueue(new Callback<DTO_SeenVoucher>() {
            @Override
            public void onResponse(Call<DTO_SeenVoucher> call, Response<DTO_SeenVoucher> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SeenVoucherActivity.this,
                            "Gửi thành công!!", Toast.LENGTH_SHORT).show();
                    Log.e("manh", "onResponse: Gửi thành công!!" );
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SeenVoucherActivity.this,
                            "Gửi không thành công, xảy ra lỗi!!!", Toast.LENGTH_SHORT).show();
                    Log.e("manh", "onResponse: " + response.message() );
                }
            }
            @Override
            public void onFailure(Call<DTO_SeenVoucher> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SeenVoucherActivity.this,
                        "Gửi không thành công, xảy ra lỗi!!!", Toast.LENGTH_SHORT).show();
                Log.e("manh", "onFailure: " + t.getLocalizedMessage() );
            }
        });
    }

}
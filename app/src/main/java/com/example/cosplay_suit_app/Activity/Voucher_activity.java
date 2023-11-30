package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_voucher;
import com.example.cosplay_suit_app.Adapter.QlspAdapter;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_voucher;
import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.Interface_retrofit.ShopInterface;
import com.example.cosplay_suit_app.Interface_retrofit.VoucherInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Voucher_activity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url + "/Voucher/";

    static final String BASE_URL_SHOP = url + "/shop/";

    RecyclerView rclvList;
    List<DTO_voucher> mlist;
    Adapter_voucher adapter;
    SwipeRefreshLayout srlQlsp;
    ImageView iv_back;
    Button btnAddVoucher;
    String idshop;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        SharedPreferences sharedPreferences2 = this.getSharedPreferences("shops", MODE_PRIVATE);
        idshop = sharedPreferences2.getString("id", "");

        btnAddVoucher=findViewById(R.id.btnAddVoucher);
        iv_back = findViewById(R.id.ivBack1);
        rclvList = findViewById(R.id.recyclerViewVouchers);
        srlQlsp=findViewById(R.id.srlQlspp);
        mlist = new ArrayList<DTO_voucher>();
        adapter = new Adapter_voucher(mlist,Voucher_activity.this);
        rclvList.setAdapter(adapter);
        refresh();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Voucher_activity.this, AddVoucher_Activity.class);
                startActivity(intent);
            }
        });


        callApiVoucher();
    }
    private void callApiVoucher() {

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
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        VoucherInterface anInterface = retrofit.create(VoucherInterface.class);

        // tạo đối tượng
        Call<List<DTO_voucher>> objCall = anInterface.getVoucherByShop(idshop);
        objCall.enqueue(new Callback<List<DTO_voucher>>() {
            @Override
            public void onResponse(@NonNull Call<List<DTO_voucher>> call, @NonNull Response<List<DTO_voucher>> response) {
                if (response.isSuccessful()) {

                    mlist.clear();
                    mlist.addAll(response.body());
                    Log.d("TAG", "onResponse: " + response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("TAG", "onResponse: " + mlist.size() + "-----------" + id);

                }

            }

            @Override
            public void onFailure(Call<List<DTO_voucher>> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });

    }
    private void refresh() {

        srlQlsp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mlist = new ArrayList<DTO_voucher>();
                adapter = new Adapter_voucher(mlist, Voucher_activity.this);
                rclvList.setAdapter(adapter);
//                mlist.clear();
                callApiVoucher();
                srlQlsp.setRefreshing(false);
            }
        });

    }

}
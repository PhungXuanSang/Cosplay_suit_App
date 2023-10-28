package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_Cartorder;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class CartOrderActivity extends AppCompatActivity implements Adapter_Cartorder.OnclickCheck{
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    String TAG = "cartorderactivity";
    List<CartOrderDTO> list;
    Adapter_Cartorder arrayAdapter;
    RecyclerView recyclerView;
    ImageView img_back;
    TextView tvtongtien;
    Button btnbuynow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);
        Anhxa();
        //danh sách sản phẩm
        list = new ArrayList<>();
        arrayAdapter = new Adapter_Cartorder(list,CartOrderActivity.this, (Adapter_Cartorder.OnclickCheck) this);
        recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        GetUserSanPham(id);
    }

    public void Anhxa(){
        recyclerView = findViewById(R.id.rcv_cart);
        img_back = findViewById(R.id.id_back);
        tvtongtien = findViewById(R.id.tv_tongtien);
        btnbuynow = findViewById(R.id.btn_buynow);
    }

    void GetUserSanPham(String id) {
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
        CartOrderInterface billInterface = retrofit.create(CartOrderInterface.class);

        // tạo đối tượng
        Call<List<CartOrderDTO>> objCall = billInterface.getusercartorder(id);
        objCall.enqueue(new Callback<List<CartOrderDTO>>() {
            @Override
            public void onResponse(Call<List<CartOrderDTO>> call, Response<List<CartOrderDTO>> response) {
                if (response.isSuccessful()) {

                    list.clear();
                    list.addAll(response.body());
                    arrayAdapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: "+list.size());

                } else {
                    Toast.makeText(CartOrderActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartOrderDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }
    @Override
    public void onCheckboxTrue(int tongtien) {
        tvtongtien.setText(tongtien + " VND");
    }

    @Override
    public void onCheckboxFalse(int tongtien) {
        tvtongtien.setText(tongtien + " VND");
    }

    @Override
    public void onIdCart(ArrayList<String> idcart) {
        btnbuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartOrderActivity.this, BuynowActivity.class);
                intent.putStringArrayListExtra("arridcart", idcart);
                startActivity(intent);
            }
        });
    }
}
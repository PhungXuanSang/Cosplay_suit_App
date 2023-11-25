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
import com.example.cosplay_suit_app.Adapter.Adapter_buynow;
import com.example.cosplay_suit_app.DTO.DTO_buynow;
import com.example.cosplay_suit_app.DTO.ShopCartorderDTO;
import com.example.cosplay_suit_app.DTO.TotalPriceManager;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.bson.types.ObjectId;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuynowActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    String TAG = "buynowactivity";
    ImageView img_back;
    TextView tv_tongtien;
    Button btnbuynow;
    List<DTO_buynow> list;
    Adapter_buynow arrayAdapter;
    RecyclerView recyclerView;
    private TotalPriceManager totalPriceManager;
    Set<String> listidshop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buynow);
        Anhxa();

        list = new ArrayList<>();
        arrayAdapter = new Adapter_buynow(list, (Context) BuynowActivity.this);
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

        btnbuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi phương thức trong Adapter khi nút được nhấn
                arrayAdapter.performActionOnAllItems();
            }
        });
        totalPriceManager = TotalPriceManager.getInstance();
        listidshop = totalPriceManager.getListidshop();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tv_tongtien.setText(decimalFormat.format(totalPriceManager.getTotalOrderPrice()) + " VND");

        getShopBuynow(id);
    }
    public void Anhxa(){
        img_back = findViewById(R.id.id_back);
        btnbuynow = findViewById(R.id.btn_buynow);
        recyclerView = findViewById(R.id.rcv_cart);
        tv_tongtien = findViewById(R.id.tv_tongtien);
    }

    public void getShopBuynow(String id){
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
        Call<List<DTO_buynow>> objCall = billInterface.getShopBuynow(id);
        objCall.enqueue(new Callback<List<DTO_buynow>>() {
            @Override
            public void onResponse(Call<List<DTO_buynow>> call, Response<List<DTO_buynow>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    List<DTO_buynow> dtoBuynows = response.body();
                    // Kiểm tra nếu danh sách chưa được thêm
                    for (DTO_buynow dtoBuyNow : dtoBuynows) {
                        String idshop = dtoBuyNow.get_id();
                        if (listidshop.contains(idshop)){
                            list.add(dtoBuyNow);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(BuynowActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DTO_buynow>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

}
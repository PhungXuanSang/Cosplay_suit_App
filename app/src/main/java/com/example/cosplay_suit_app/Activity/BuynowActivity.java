package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
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

public class BuynowActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    String TAG = "buynowactivity";
    RecyclerView recyclerView;
    List<CartOrderDTO> list;
    Adapter_buynow arrayAdapter;
    ImageView img_back;
    TextView tvtongtien;
    Button btnbuynow;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buynow);
        Anhxa();
        //Lấy iduser hiện tại đang đăng nhập
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        //Đón idcart từ bên cartactivity
        Intent intent = getIntent();
        arrayList = intent.getStringArrayListExtra("arridcart");

        list = new ArrayList<>();
        arrayAdapter = new Adapter_buynow(list,BuynowActivity.this);
        recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        GetIdCartOrder();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    public void Anhxa(){
        recyclerView = findViewById(R.id.rcv_cart);
        img_back = findViewById(R.id.id_back);
        tvtongtien = findViewById(R.id.tv_tongtien);
        btnbuynow = findViewById(R.id.btn_buynow);
    }

    void GetIdCartOrder() {
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
        for (String id: arrayList){
            // tạo đối tượng
            Call<List<CartOrderDTO>> objCall = billInterface.getidCartOder(id);
            objCall.enqueue(new Callback<List<CartOrderDTO>>() {
                @Override
                public void onResponse(Call<List<CartOrderDTO>> call, Response<List<CartOrderDTO>> response) {
                    if (response.isSuccessful()) {

                        list.addAll(response.body());
                        arrayAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(BuynowActivity.this,
                                "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CartOrderDTO>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                }
            });

        }
        }
}
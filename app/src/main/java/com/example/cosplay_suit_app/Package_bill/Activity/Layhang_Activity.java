package com.example.cosplay_suit_app.Package_bill.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_Bill;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.Interface_retrofit.Billdentail_Interfece;
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

public class Layhang_Activity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    String TAG = "Danhgiaactivity";
    List<BillDetailDTO> list;
    Adapter_Bill arrayAdapter;
    RecyclerView recyclerView;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layhang);
        Anhxa();
        //danh sách sản phẩm
        list = new ArrayList<>();
        arrayAdapter = new Adapter_Bill(list, this);
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
        GetUserBill(id);
    }
    public void Anhxa(){
        recyclerView = findViewById(R.id.rcv_danhgia);
        img_back = findViewById(R.id.id_back);
    }
    void GetUserBill(String id) {
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
        Billdentail_Interfece billDetailDTO = retrofit.create(Billdentail_Interfece.class);

        // tạo đối tượng
        Call<List<BillDetailDTO>> objCall = billDetailDTO.getstatusPack(id);
        objCall.enqueue(new Callback<List<BillDetailDTO>>() {
            @Override
            public void onResponse(Call<List<BillDetailDTO>> call, Response<List<BillDetailDTO>> response) {
                if (response.isSuccessful()) {

                    list.clear();
                    list.addAll(response.body());
                    arrayAdapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: "+list.size());

                } else {
                    Toast.makeText(Layhang_Activity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BillDetailDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }
}
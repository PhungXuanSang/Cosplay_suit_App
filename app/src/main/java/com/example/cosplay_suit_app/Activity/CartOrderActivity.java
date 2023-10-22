package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_Cartorder;
import com.example.cosplay_suit_app.Interface_retrofit.BillInterface;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
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

public class CartOrderActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    ArrayList<DTO_CartOrder> list;
    Adapter_Cartorder arrayAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);
        Anhxa();
        //danh sách sản phẩm
        list = new ArrayList<>();
        arrayAdapter = new Adapter_Cartorder(list, this);
        recyclerView.setAdapter(arrayAdapter);
        GetListSanPham();
    }

    public void Anhxa(){

    }

    void GetListSanPham() {
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
        BillInterface billInterface = retrofit.create(BillInterface.class);

        // tạo đối tượng
        Call<List<DTO_CartOrder>> objCall = billInterface.getlistcartorder();
        objCall.enqueue(new Callback<List<DTO_CartOrder>>() {
            @Override
            public void onResponse(Call<List<DTO_CartOrder>> call, Response<List<DTO_CartOrder>> response) {
                if (response.isSuccessful()) {

                    list.clear();
                    list.addAll(response.body());
                    arrayAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(CartOrderActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DTO_CartOrder>> call, Throwable t) {

            }
        });

    }

}
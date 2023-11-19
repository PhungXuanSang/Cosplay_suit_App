package com.example.cosplay_suit_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.QlspAdapter;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
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

public class QlspActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url + "/product/";
    ImageView iv_back, iv_add;
    RecyclerView rclvList;
    List<DTO_SanPham> mlist;
    QlspAdapter adapter;

    TextView tvQuantity;

    SwipeRefreshLayout srlQlsp;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlsp);
        iv_add = findViewById(R.id.ivAdd);
        iv_back = findViewById(R.id.ivBack);
        rclvList = findViewById(R.id.rclvQlspListproduct);
        tvQuantity = findViewById(R.id.tvQlspQuantity);
        srlQlsp = findViewById(R.id.srlQlsp);

        SharedPreferences sharedPreferences = this.getSharedPreferences("User", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");

        mlist = new ArrayList<DTO_SanPham>();
        adapter = new QlspAdapter(mlist, this);
        rclvList.setAdapter(adapter);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QlspActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        refresh();
        callApiProduct();

    }

    private void callApiProduct() {

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
        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);

        // tạo đối tượng
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.GetProduct(id);
        objCall.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(@NonNull Call<List<DTO_SanPham>> call, @NonNull Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {

                    mlist.clear();
                    mlist.addAll(response.body());
                    tvQuantity.setText(mlist.size() + " Sản phẩm");
                    Log.d("TAG", "onResponse: " + response.body());
                    adapter.notifyDataSetChanged();

                    Log.d("TAG", "onResponse: " + mlist.size() + "-----------" + id);

                }

            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {

            }
        });

    }

    private void refresh() {

        srlQlsp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mlist = new ArrayList<DTO_SanPham>();
                adapter = new QlspAdapter(mlist, QlspActivity.this);
                rclvList.setAdapter(adapter);
//                mlist.clear();
                callApiProduct();
                srlQlsp.setRefreshing(false);
            }
        });

    }


}
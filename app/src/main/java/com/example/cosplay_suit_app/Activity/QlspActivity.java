package com.example.cosplay_suit_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.QlspAdapter;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.LoginUser;
import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.Interface_retrofit.ShopInterface;
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

public class QlspActivity extends AppCompatActivity implements QlspAdapter.Onclick {
    static String url = API.URL;
    static final String BASE_URL = url + "/product/";

    static final String BASE_URL_SHOP = url + "/shop/";
    ImageView iv_back, iv_add;
    RecyclerView rclvList;
    List<DTO_SanPham> mlist;
    QlspAdapter adapter;

    TextView tvQuantity;

    EditText search;

    SwipeRefreshLayout srlQlsp;
    String idshop;
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
        search = findViewById(R.id.edtQlspSearch);
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        SharedPreferences sharedPreferences2 = this.getSharedPreferences("shops", MODE_PRIVATE);
        idshop = sharedPreferences2.getString("id", "");
        DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rclvList.addItemDecoration(dividerItemDecoration);
        mlist = new ArrayList<DTO_SanPham>();
        adapter = new QlspAdapter(mlist, this,this);
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

        callApiShop();
        callApiProduct();
//        putProduct();
        searchProduct();
        

    }

    private void searchProduct() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.GetProduct(idshop);
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
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }

    private void callApiShop() {

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
                .baseUrl(BASE_URL_SHOP)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        ShopInterface shopInterface = retrofit.create(ShopInterface.class);

        // tạo đối tượng
        Call<List<Shop>> objCall = shopInterface.listShop(id);
        objCall.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(@NonNull Call<List<Shop>> call, @NonNull Response<List<Shop>> response) {
                List<Shop> shopList = response.body();

                if (response.isSuccessful()) {

                    if (shopList != null && !shopList.isEmpty()) {
                        remenber(shopList.get(0).getId());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id", shopList.get(0).getId());
//                        idshop = String.valueOf(shopList.get(0).getId());

                        Log.d("TAG", "onResponse: " + idshop);// Lưu giá trị vào biến instance
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Log.d("TAG", t.getLocalizedMessage());
            }
        });

    }


    public void remenber(String id) {
        SharedPreferences preferences = getSharedPreferences("shops", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", id);

        editor.apply();
    }


    private void refresh() {

        srlQlsp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mlist = new ArrayList<DTO_SanPham>();
                adapter = new QlspAdapter(mlist, QlspActivity.this, new QlspAdapter.Onclick() {
                    @Override
                    public void status(DTO_SanPham dtoSanPham,String idproduct) {

                    }
                });
                rclvList.setAdapter(adapter);
//                mlist.clear();
                callApiProduct();
                srlQlsp.setRefreshing(false);
            }
        });

    }

    @Override
    public void status(DTO_SanPham dtoSanPham , String idProduct) {
        Gson gson = new GsonBuilder().setLenient().create();
        //Tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Khởi  tạo interface

        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);


        // Tạo Call
        Call<DTO_SanPham> objCall = sanPhamInterface.updateProduct(idProduct, dtoSanPham);
        // Thực hiện gửi dữ liệu lên server
        objCall.enqueue(new Callback<DTO_SanPham>() {
            @Override
            public void onResponse(Call<DTO_SanPham> call, Response<DTO_SanPham> response) {
                // kết quả server trả về ở đây

                if (response.isSuccessful()) {
                    // lấy kết quả trả về

                } else {
                    Log.e("TAG", response.message());
                }
            }
            @Override
            public void onFailure(Call<DTO_SanPham> call, Throwable t) {
                // nếu xảy ra lỗi sẽ thông báo ở đây

                Log.e("TAG", t.getLocalizedMessage());
            }
        });
    }

}
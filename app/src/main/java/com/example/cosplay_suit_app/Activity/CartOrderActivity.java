package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.AdapterCartorder;
import com.example.cosplay_suit_app.Adapter.Adapter_ShopCartOrder;
import com.example.cosplay_suit_app.DTO.CartShopManager;
import com.example.cosplay_suit_app.DTO.ShopCartorderDTO;
import com.example.cosplay_suit_app.DTO.TotalPriceManager;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Cart_controller;
import com.example.cosplay_suit_app.bill.controller.Dialogthongbao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
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

public class CartOrderActivity extends AppCompatActivity implements AdapterCartorder.OnclickCheck {
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    String TAG = "cartorderactivity";
    List<ShopCartorderDTO> list;
    Adapter_ShopCartOrder arrayAdapter;
    RecyclerView recyclerView;
    ImageView img_back;
    TextView tvtongtien;
    Button btnbuynow;
    private TotalPriceManager totalPriceManager;
    ArrayList<String> totalPriceManagerListcart = new ArrayList<>();
    LinearLayout noProductMessage;
    CheckBox cbkSelectAll;
    String id;
    ProgressBar loadingProgressBar;
    SwipeRefreshLayout setOnRefreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);
        Anhxa();
        totalPriceManager = TotalPriceManager.getInstance();

        //Đặt lại listidshop thành rỗng
        CartShopManager.getInstance().clearData();
        //Đặt lại listcart thành rỗng
        ArrayList<String> listcart = new ArrayList<>();
        TotalPriceManager.getInstance().setListcart(listcart);
        //Đặt lại TotalOrderPrice thành rỗng
        int resertgia = 0;
        TotalPriceManager.getInstance().setTotalOrderPrice(resertgia);


        list = new ArrayList<>();
        arrayAdapter = new Adapter_ShopCartOrder(list, (Context) CartOrderActivity.this);
        recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        id = sharedPreferences.getString("id","");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnbuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalPriceManagerListcart = totalPriceManager.getListcart();
                if (totalPriceManagerListcart == null || totalPriceManagerListcart.isEmpty()){
                    String title = "Thông báo giỏ hàng";
                    String message = "Bạn chưa chọn sản phẩm nào!";
                    Dialogthongbao.showSuccessDialog(CartOrderActivity.this, title, message);
                }else {
                    Intent intent = new Intent(CartOrderActivity.this, BuynowActivity.class);
                    startActivity(intent);
                }

            }
        });
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        reloadBillList();
    }

    public void Anhxa(){
        recyclerView = findViewById(R.id.rcv_cart);
        img_back = findViewById(R.id.id_back);
        tvtongtien = findViewById(R.id.tv_tongtien);
        btnbuynow = findViewById(R.id.btn_buynow);
        noProductMessage = findViewById(R.id.noProductMessage);
        cbkSelectAll = findViewById(R.id.cbkcart);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        setOnRefreshListener = findViewById(R.id.restartbill);
    }

    public void getShop(String id){
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
        Call<List<ShopCartorderDTO>> objCall = billInterface.getShop(id);
        objCall.enqueue(new Callback<List<ShopCartorderDTO>>() {
            @Override
            public void onResponse(Call<List<ShopCartorderDTO>> call, Response<List<ShopCartorderDTO>> response) {
                if (response.isSuccessful()) {

                    list.clear();
                    list.addAll(response.body());
                    arrayAdapter.notifyDataSetChanged();
                    if (list.isEmpty()) {
                        noProductMessage.setVisibility(LinearLayout.VISIBLE);
                        recyclerView.setVisibility(ListView.GONE);
                    } else {
                        noProductMessage.setVisibility(LinearLayout.GONE);
                        recyclerView.setVisibility(ListView.VISIBLE);
                    }
                    loadingProgressBar.setVisibility(View.GONE); // Ẩn ProgressBar sau khi tải xong
                } else {
                    Toast.makeText(CartOrderActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ShopCartorderDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }
    public void reloadBillList() {
        loadingProgressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar trước khi tải dữ liệu
        getShop(id);
    }
    private void fetchData() {
        getShop(id);
        // Kết thúc quá trình làm mới
        setOnRefreshListener.setRefreshing(false);
    }
    @Override
    public void onCheckboxTrue() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        double tt = totalPriceManager.getTotalOrderPrice();
        tvtongtien.setText(decimalFormat.format(tt) + " VND");
    }
    @Override
    public void onCheckboxFalse() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        double tt = totalPriceManager.getTotalOrderPrice();
        tvtongtien.setText(decimalFormat.format(tt) + " VND");
    }
    @Override
    public void onClickXoa(String idcart){
        new AlertDialog.Builder(CartOrderActivity.this)
                .setTitle("Thông Báo")
                .setMessage("Bạn có muôn xóa sản phẩm này khỏi giỏ hàng không ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Cart_controller cartController = new Cart_controller(CartOrderActivity.this);
                        cartController.chonDeleteCartorder(idcart);
                        // Thông báo về sự thay đổi trong danh sách
                        if (CartOrderActivity.this instanceof CartOrderActivity) {
                            ((CartOrderActivity) CartOrderActivity.this).onResume();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getShop(id);
    }
}
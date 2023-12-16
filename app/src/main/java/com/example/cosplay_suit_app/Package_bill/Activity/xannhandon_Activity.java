package com.example.cosplay_suit_app.Package_bill.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Package_bill.Adapter.Adapter_Bill;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.Interface_retrofit.Billdentail_Interfece;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
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

public class xannhandon_Activity extends AppCompatActivity {
    String TAG = "Danhgiaactivity";
    List<BillDetailDTO> list;
    Adapter_Bill arrayAdapter;
    RecyclerView recyclerView;
    ImageView img_back;
    String checkactivity = "user" , checkstatus = "";
    LinearLayout noProductMessage;
    String id;
    ProgressBar loadingProgressBar;
    SwipeRefreshLayout setOnRefreshListener;
    Bill_controller billController = new Bill_controller(xannhandon_Activity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhandon);
        Anhxa();
        //danh sách sản phẩm
        list = new ArrayList<>();
        arrayAdapter = new Adapter_Bill(list, this, checkactivity, checkstatus);
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
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        reloadBillList();
    }

    public void Anhxa(){
        recyclerView = findViewById(R.id.rcv_danhgia);
        img_back = findViewById(R.id.id_back);
        noProductMessage = findViewById(R.id.noProductMessage);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        setOnRefreshListener = findViewById(R.id.restartbill);
    }
    public void getList(){
        if (id != null && !id.isEmpty()) {
            // ... (Các xử lý khác)
            billController.GetUserBillWait(id, checkactivity, new Bill_controller.ApiGetUserBillWait() {
                @Override
                public void onApiGetUserBillWait(List<BillDetailDTO> profileDTO) {
                    list.clear();
                    if (profileDTO != null && !profileDTO.isEmpty()) {
                        for (BillDetailDTO billDetail : profileDTO) {
                            list.add(billDetail);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                    if (list.isEmpty()) {
                        noProductMessage.setVisibility(LinearLayout.VISIBLE);
                        recyclerView.setVisibility(ListView.GONE);
                    } else {
                        noProductMessage.setVisibility(LinearLayout.GONE);
                        recyclerView.setVisibility(ListView.VISIBLE);
                    }
                    loadingProgressBar.setVisibility(View.GONE); // Ẩn ProgressBar sau khi tải xong
                }
            });
        }
    }
    public void reloadBillList() {
        loadingProgressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar trước khi tải dữ liệu
        getList();
    }
    private void fetchData() {
        getList();
        // Kết thúc quá trình làm mới
        setOnRefreshListener.setRefreshing(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }
}
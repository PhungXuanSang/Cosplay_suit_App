package com.example.cosplay_suit_app.Package_bill.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_Bill;
import com.example.cosplay_suit_app.Adapter.PagerCmtsAdapter;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.Interface_retrofit.Billdentail_Interfece;
import com.example.cosplay_suit_app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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

public class Danhgia_Activity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    String TAG = "Danhgiaactivity";
    List<BillDetailDTO> list;
    Adapter_Bill arrayAdapter;
    RecyclerView recyclerView;
    ImageView img_back;
    PagerCmtsAdapter adapter;
    ViewPager2 viewPager2;
    TabLayout tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhgia);
        initView();




        getViewPager();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewPagerCmts);
        tab = findViewById(R.id.tabLayoutCmts);
        img_back = findViewById(R.id.ic_back);
    }

    private void getViewPager() {

        adapter = new PagerCmtsAdapter(this);
        viewPager2.setAdapter(adapter);

        TabLayoutMediator mediator = new TabLayoutMediator(tab, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if(position==0){
                            tab.setText("Chưa đánh giá");
                        }else{
                            tab.setText("Đã đánh giá");
                        }
                    }
                });
        mediator.attach();
    }

}
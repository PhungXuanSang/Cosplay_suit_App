package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.AdapterCartorder;
import com.example.cosplay_suit_app.Adapter.AdapterKhachHang;
import com.example.cosplay_suit_app.Adapter.Adapter_Shop_SanPham1;
import com.example.cosplay_suit_app.Adapter.Adapter_dskhach;
import com.example.cosplay_suit_app.Adapter.Adapter_voucher;
import com.example.cosplay_suit_app.DTO.DTO_SeenVoucher;
import com.example.cosplay_suit_app.DTO.DTO_SeenVoucher_Check;
import com.example.cosplay_suit_app.DTO.DTO_voucher;
import com.example.cosplay_suit_app.DTO.Product_Page;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.Interface_retrofit.Bill_interface;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.Interface_retrofit.VoucherInterface;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeenVoucherActivity extends AppCompatActivity implements AdapterKhachHang.OnlickCheckVoucher{
    static String url = API.URL;
    static final String BASE_URL = url + "/Voucher/";
    static final String BASE_URL_CARTORDER = url + "/bill/";
    ImageView id_back,send_voucher;
    RecyclerView recyclerView;

    CheckBox id_checkAll;
    ArrayList<ProfileDTO> list;
    long startTime = System.currentTimeMillis();
    long duration,durationCall;
    AdapterKhachHang adapterKhachHang;
    List<String> stringList = new ArrayList<>();

    ConstraintLayout id_bg_load;
    AppCompatButton id_btn_cannel,id_btn_gui;
    TextView tv_listrong;

    String id_voucher, amount;

    int amount1;
    int size;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_voucher);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Quá trình có thể tốn nhều time!Vui lòng chờ...");
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Anhxa();
                Intent intent = getIntent();
                id_voucher = intent.getStringExtra("id_voucher");
                amount = intent.getStringExtra("amount");
                list = new ArrayList<>();
                adapterKhachHang = new AdapterKhachHang(list,SeenVoucherActivity.this,SeenVoucherActivity.this,id_voucher);
                GetdsKhach(id);
                try {
                    Log.e("manh1", "time shop san pham: " + durationCall );
                    Thread.sleep(durationCall + 600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //tuong tac voi giao dien
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapterKhachHang);
                        id_btn_cannel.setVisibility(View.VISIBLE);
                        id_btn_gui.setVisibility(View.VISIBLE);
                        id_bg_load.setVisibility(View.GONE);
                        id_checkAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapterKhachHang.updateCheckBoxes(id_checkAll.isChecked());
                                Log.e("manh", "onClick: " + stringList );
                            }
                        });
                        findViewById(R.id.id_btn_gui).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (stringList.size()==0){
                                    Toast.makeText(SeenVoucherActivity.this, "Vui lòng chọn ít nhất 1 khách hàng để gửi voucher!!", Toast.LENGTH_SHORT).show();
                                }else{
                                    progressDialog.show();
                                    amount1 = stringList.size();
                                    Log.e("manh", "Amount : " + amount1 );
                                    if (Integer.parseInt(amount)<amount1){
                                        Toast.makeText(SeenVoucherActivity.this, "Bạn không đủ số lượng voucher!!Loại bỏ bớt khách hàng", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }else{
                                        for (int i = 0; i < stringList.size();i++){
                                            DTO_SeenVoucher seenVoucher = new DTO_SeenVoucher();
                                            seenVoucher.setId_user(stringList.get(i));
                                            Log.e("manh", "onClick: " + id_voucher );
                                            seenVoucher.setId_voucher(id_voucher);
                                            CallSeenVoucher(seenVoucher);
                                            duration+=duration;
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.e("manh", "run: " + duration );
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SeenVoucherActivity.this, "Gửi voucher thành công!!", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                }
                                            },duration+500);
                                        }
                                    }

                                }
                            }
                        });

                        id_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onBackPressed();
                            }
                        });
                    }
                });
            }
        });



    }

    public void Anhxa(){
        id_back = findViewById(R.id.id_back);
        recyclerView = findViewById(R.id.id_recyclerGui);
        id_bg_load = findViewById(R.id.id_bg_loadSeen);
        id_btn_cannel = findViewById(R.id.id_btn_cannel);
        id_btn_gui = findViewById(R.id.id_btn_gui);
        tv_listrong = findViewById(R.id.tv_listrong);
        id_checkAll = findViewById(R.id.id_checkAll);
    }

    @Override
    public void Check_ID(String id_user) {
        stringList.add(id_user);
        Log.e("manh", "Check_ID: " + stringList );
    }

    @Override
    public void Check_Delete_ID(String id_user) {
        stringList.remove(id_user);
        Log.e("manh", "Check_ID: " + stringList );
    }


    void CallSeenVoucher(DTO_SeenVoucher dto_seenVoucher) {
        long startTime1 = 0;
        try {
            startTime1 = System.currentTimeMillis();
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
            VoucherInterface voucherInterface = retrofit.create(VoucherInterface.class);

            // tạo đối tượng
            Call<DTO_SeenVoucher_Check> objCall = voucherInterface.postSeen(dto_seenVoucher);
            objCall.enqueue(new Callback<DTO_SeenVoucher_Check>() {
                @Override
                public void onResponse(Call<DTO_SeenVoucher_Check> call, Response<DTO_SeenVoucher_Check> response) {
                    DTO_SeenVoucher_Check check = response.body();
                    if (response.isSuccessful()) {
                        if (check.getSeenVoucher() != null) {
                            DTO_voucher voucher = new DTO_voucher();
                            voucher.setAmount(String.valueOf(Integer.parseInt(amount) - amount1));
                            CallUpdateVoucherSeen(voucher);
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SeenVoucherActivity.this,
                                "Gửi không thành công, xảy ra lỗi!!!", Toast.LENGTH_SHORT).show();
                        Log.e("manh", "onResponse: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<DTO_SeenVoucher_Check> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("manh", "onFailure: " + t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime1;
            Log.e("manh", "Thời gian thực hiện API Seen Voucher: " + duration + "ms");
        }

    }
    void CallUpdateVoucherSeen(DTO_voucher dtoVoucher){
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
        VoucherInterface voucherInterface = retrofit.create(VoucherInterface.class);

        // tạo đối tượng
        Call<DTO_voucher> objCall = voucherInterface.updateVoucherSeen(id_voucher,dtoVoucher);
        objCall.enqueue(new Callback<DTO_voucher>() {
            @Override
            public void onResponse(Call<DTO_voucher> call, Response<DTO_voucher> response) {
                if (response.isSuccessful()) {
                    Log.e("manh", "onResponse: Sửa thành công!!" );
                } else {
                    Log.e("manh", "onResponse: " + response.message() );
                }
            }
            @Override
            public void onFailure(Call<DTO_voucher> call, Throwable t) {
                Log.e("manh", "onFailure: " + t.getLocalizedMessage() );
            }
        });
    }



    void GetdsKhach(String id){
        try {
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
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        Bill_interface billDetailDTO = retrofit.create(Bill_interface.class);

        // tạo đối tượng
        Call<List<ProfileDTO>> objCall = billDetailDTO.getdskhach(id);
        objCall.enqueue(new Callback<List<ProfileDTO>>() {
            @Override
            public void onResponse(Call<List<ProfileDTO>> call, Response<List<ProfileDTO>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    List<ProfileDTO> profileDTOS = response.body();
                    if (profileDTOS != null && !profileDTOS.isEmpty()) {
                        for (ProfileDTO profileDTO : profileDTOS) {
                            list.add(profileDTO);
                            size = list.size();
                        }

                        adapterKhachHang.notifyDataSetChanged();
                    } else {

                    }
                } else {
                    Toast.makeText(SeenVoucherActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProfileDTO>> call, Throwable t) {
                Log.d("manh", "onFailure: " + t);
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long endTime = System.currentTimeMillis();
            durationCall = endTime - startTime;
            Log.e("manh", "Thời gian thực hiện API Seen Voucher: " + duration + "ms");
        }
    }

//    private void refresh() {
//
//        recyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mlist = new ArrayList<DTO_voucher>();
//                adapter = new Adapter_voucher(mlist, Voucher_activity.this);
//                rclvList.setAdapter(adapter);
////                mlist.clear();
//                callApiVoucher();
//                srlQlsp.setRefreshing(false);
//            }
//        });
//
//    }
}
package com.example.cosplay_suit_app.Activity;

import static com.example.cosplay_suit_app.Activity.Chitietsanpham.id;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_ThongKeShop;
import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.Interface_retrofit.ShopInterface;
import com.example.cosplay_suit_app.Interface_retrofit.TkeInterface;
import com.example.cosplay_suit_app.Package_bill.DTO.BillDTO;
import com.example.cosplay_suit_app.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class ThongKeShopActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url + "/thongke/";

    static final String BASE_URL_SHOP = url + "/shop/";
    ImageView img_lich;
    TextView tv_lich, tv_tongdoanhthu, tv_donhang, tv3, tv4;
    int tongdoanhthu;
    private List<BillDTO> billDTOList;
    private RecyclerView rcvTke;
    private Adapter_ThongKeShop mAdapterTKe;

     String idshop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_shop);

        img_lich = findViewById(R.id.img_lichtke);
        tv_lich = findViewById(R.id.tv_lich);
        tv_tongdoanhthu = findViewById(R.id.tv_tongdoanhthu);
        tv_donhang = findViewById(R.id.tv_donhang);

        SharedPreferences sharedPreferences2 = this.getSharedPreferences("shops", MODE_PRIVATE);
        idshop = sharedPreferences2.getString("id", "");
        Log.d("idshoppp", "onCreate: "+idshop);

        callApiTke();

        ImageView img_back = findViewById(R.id.img_backtke);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img_lich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ThongKeShopActivity.this);
                bottomSheetDialog.setContentView(R.layout.dialog_lich_tke);

                TextView tv_ngaybatdau = bottomSheetDialog.findViewById(R.id.tv_ngaybatdau);
                TextView tv_ngayketthuc = bottomSheetDialog.findViewById(R.id.tv_ngayketthuc);
                ImageView img_done = bottomSheetDialog.findViewById(R.id.img_done);
                tv3 = bottomSheetDialog.findViewById(R.id.tv3);
                tv4 = bottomSheetDialog.findViewById(R.id.tv4);

                tv_ngaybatdau.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialogDatePicker(tv3);

                    }
                });

                tv_ngayketthuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialogDatePicker(tv4);
                    }
                });

                img_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_lich.setText(tv3.getText().toString() + " - " + tv4.getText().toString());
                        Log.d("Timeee", "onClick: "+ tv3.getText().toString());
                        bottomSheetDialog.dismiss();
                        getListThongKe(tv3.getText().toString().trim(), tv4.getText().toString().trim());
                    }
                });

                bottomSheetDialog.show();
            }
        });

        billDTOList = new ArrayList<>();
        rcvTke = findViewById(R.id.rcv_thongke);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTke.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvTke.addItemDecoration(dividerItemDecoration);

        billDTOList = new ArrayList<>();
        mAdapterTKe = new Adapter_ThongKeShop(billDTOList);

        rcvTke.setAdapter(mAdapterTKe);

    }

    public  void getListThongKe(String ngaybatdau, String ngayketthuc) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Thay "your_mongodb_url" bằng URL của MongoDB
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TkeInterface service = retrofit.create(TkeInterface.class);

        Call<List<BillDTO>> call = service.getBills(idshop,ngaybatdau, ngayketthuc);
        call.enqueue(new Callback<List<BillDTO>>() {
            @Override
            public void onResponse(Call<List<BillDTO>> call, Response<List<BillDTO>> response) {
                if (response.isSuccessful()) {
                    List<BillDTO> billDTOList = response.body();
                    double tongdoanhthu = 0;

                    for (BillDTO billDTO : billDTOList) {
                        tongdoanhthu += billDTO.getTotalPayment();
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    float giatien = Float.parseFloat(String.valueOf(tongdoanhthu));
                    System.out.println(decimalFormat.format(giatien) + " vnđ");

                    tv_tongdoanhthu.setText(giatien+" vnđ");
                    tv_donhang.setText(billDTOList.size()+ " đơn hàng");
                    Log.d("thongkee", "tongtien: "+tongdoanhthu);
                    Log.d("thongkee", "donhang: "+billDTOList.size());
                } else {
                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BillDTO>> call, Throwable t) {
                t.printStackTrace();
                Log.d("onfaill", "onFailure: "+t.getMessage());
            }
        });
    }



    void showDialogDatePicker(TextView targetTextView) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        DatePickerDialog dialog = new DatePickerDialog(
                ThongKeShopActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        targetTextView.setText(i2 + "-" + (i1 + 1) + "-" + i);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
        );

        dialog.show();
    }
    private void callApiTke() {

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

                if (shopList != null && !shopList.isEmpty()) {
                    idshop = shopList.get(0).getId();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", shopList.get(0).getId());


                    Log.d("TAG", "onResponse: " + idshop);// Lưu giá trị vào biến instance
                }

            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Log.d("TAG", t.getLocalizedMessage());
            }
        });

    }

}
package com.example.cosplay_suit_app.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.DhWithoutCmtsAdapter;
import com.example.cosplay_suit_app.DTO.ItemDoneDTO;
import com.example.cosplay_suit_app.Interface_retrofit.CmtsInterface;
import com.example.cosplay_suit_app.Package_bill.Activity.Danhgia_Activity;
import com.example.cosplay_suit_app.Package_bill.Activity.Giaohang_Activity;
import com.example.cosplay_suit_app.Package_bill.Activity.Layhang_Activity;
import com.example.cosplay_suit_app.Package_bill.Activity.xannhandon_Activity;
import com.example.cosplay_suit_app.Package_bill.donhang.Collection_adapter_bill;
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

public class Fragment_donhang extends Fragment {
    static String url = API.URL;
    static final String BASE_URL_SLDG = url +"/comments/";
    DhWithoutCmtsAdapter adapter;
    ArrayList<ItemDoneDTO> list;
    static String id="";
    String username_u;
    RelativeLayout rlhoanthanh, rlxacnhandon, rllayhang, rldanggiao;
    TextView tv_sldanhgia, tvdonhangmua;

    public Fragment_donhang() {
    }

    public static Fragment_donhang newInstance(){
        Fragment_donhang fragmentDonhang = new Fragment_donhang();
        return fragmentDonhang;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_donhang, container, false);
        rlhoanthanh = viewok.findViewById(R.id.rl_hoanthanh);
        rlxacnhandon = viewok.findViewById(R.id.rl_xacnhandon);
        rllayhang = viewok.findViewById(R.id.rl_layhang);
        rldanggiao = viewok.findViewById(R.id.rl_danggiao);
        tv_sldanhgia = viewok.findViewById(R.id.tv_sldanhgia);
        tvdonhangmua = viewok.findViewById(R.id.donhangmua);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        username_u = sharedPreferences.getString("fullname", "");
        id = sharedPreferences.getString("id", "");
        rlxacnhandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), xannhandon_Activity.class);
                startActivity(intent);
            }
        });
        rllayhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Layhang_Activity.class);
                startActivity(intent);
            }
        });
        rldanggiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Giaohang_Activity.class);
                startActivity(intent);
            }
        });
        rlhoanthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Danhgia_Activity.class);
                startActivity(intent);
            }
        });
        tvdonhangmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Collection_adapter_bill.class);
                startActivity(intent);
            }
        });
        list = new ArrayList<>();
        adapter = new DhWithoutCmtsAdapter(getContext(),list);
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_sldanhgia.setVisibility(View.GONE);
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        if (id != null && !id.isEmpty()) {
            getsoluongdg();
        }
    }
    private void getsoluongdg() {
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
                .baseUrl(BASE_URL_SLDG)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();


        CmtsInterface cmtsInterface = retrofit.create(CmtsInterface.class);

        Call<List<ItemDoneDTO>> objCall = cmtsInterface.getListDhWithoutCmts(id);
        objCall.enqueue(new Callback<List<ItemDoneDTO>>() {
            @Override
            public void onResponse(Call<List<ItemDoneDTO>> call, Response<List<ItemDoneDTO>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    int soLuongDanhGia = list.size();
                    if (soLuongDanhGia > 0) {
                        tv_sldanhgia.setVisibility(View.VISIBLE);
                        tv_sldanhgia.setText(String.valueOf(soLuongDanhGia));
                    } else {
                        tv_sldanhgia.setVisibility(View.GONE);
                    }
                    Log.d("CDG", "onResponse: "+list.size());

                } else {
                    Toast.makeText(getContext(),
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<ItemDoneDTO>> call, Throwable t) {
                Log.d("CDG", "onFailure: " + t);
            }
        });
    }

}

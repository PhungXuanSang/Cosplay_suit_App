package com.example.cosplay_suit_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_Shop_SanPham;
import com.example.cosplay_suit_app.Adapter.Adapter_Shop_SanPham1;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Shop_SanPham#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Shop_SanPham extends Fragment {
    static String url = API.URL;
    static final String BASE_URL = url +"/product/";
    RecyclerView id_recyclerShop_SanPham;

    List<DTO_SanPham> mlist;

    String id_shop;

    Adapter_Shop_SanPham1 shopSanPham1;

    public Fragment_Shop_SanPham() {
        // Required empty public constructor
    }


    public static Fragment_Shop_SanPham newInstance() {
        Fragment_Shop_SanPham fragment = new Fragment_Shop_SanPham();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__shop__san_pham, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        id_shop = intent.getStringExtra("id_shop");
        id_recyclerShop_SanPham = view.findViewById(R.id.id_recyclerShop_SanPham);
        mlist = new ArrayList<>();
        shopSanPham1  = new Adapter_Shop_SanPham1(getContext());
        shopSanPham1.updateData(mlist);
        id_recyclerShop_SanPham.setAdapter(shopSanPham1);
        GetListSanPham(id_shop);
    }


    void GetListSanPham(String id_shop) {
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
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.GetProduct(id_shop);
        objCall.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(Call<List<DTO_SanPham>> call, Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {

                    mlist.clear();
                    mlist.addAll(response.body());
                    shopSanPham1.notifyDataSetChanged();
                    Log.d("list", "onResponse3: "+mlist.size());

                } else {
                    Toast.makeText(getContext(),
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }

//                GetListSanPham();
            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {

            }
        });

    }
}
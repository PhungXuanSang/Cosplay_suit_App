package com.example.cosplay_suit_app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.CartOrderActivity;
import com.example.cosplay_suit_app.Adapter.Adapter_SanPham;
import com.example.cosplay_suit_app.Adapter.Adapter_TrenddingProduct;
import com.example.cosplay_suit_app.Adapter.CategoryAdapter;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.Product_Page;
import com.example.cosplay_suit_app.Interface_retrofit.CategoryInterface;
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

public class Fragment_trangchu extends Fragment {
    static String url = API.URL;
    static final String BASE_URL = url +"/product/";
    static final String BASE_URL_CAT = url +"/category/api/";
    RecyclerView rcv1,rcv_2,rcv_3;
    List<DTO_SanPham> mlist;
    Adapter_SanPham arrayAdapter;
    View viewok;
    SearchView sv_pro;
    ImageView img_giohang;
    ArrayList<CategoryDTO> listCat;

    ArrayList<DTO_SanPham> listProduct;
    RecyclerView rcv_cat;
    CategoryAdapter categoryAdapter;

    RecyclerView rcv_trending;
    Adapter_TrenddingProduct adapterTrenddingProduct;
    long duration;
    long startTime = System.currentTimeMillis();
    public Fragment_trangchu() {

    }

    public static Fragment_trangchu newInstance(){
        Fragment_trangchu fragmentTrangchu = new Fragment_trangchu();
        return fragmentTrangchu;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_trangchu, container, false);
        rcv_3 = viewok.findViewById(R.id.rcv3);
        img_giohang = viewok.findViewById(R.id.ic_giohang);
        rcv_cat = viewok.findViewById(R.id.rcv_cat);
        rcv_trending = viewok.findViewById(R.id.rcv_trending);
        img_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),CartOrderActivity.class );
                startActivity(intent);
            }
        });

        //danh sách sản phẩm
        mlist = new ArrayList<DTO_SanPham>();
        arrayAdapter = new Adapter_SanPham(getContext());
        arrayAdapter.updateData(mlist);
        rcv_3.setAdapter(arrayAdapter);
        GetListSanPham();

        listCat = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(),listCat);
        rcv_cat.setAdapter(categoryAdapter);
        getListCat();

        listProduct = new ArrayList<>();
        adapterTrenddingProduct = new Adapter_TrenddingProduct(getContext());
        adapterTrenddingProduct.updateData(listProduct);
        getListProductLimit();
        rcv_trending.setAdapter(adapterTrenddingProduct);

        return viewok;
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void getListProductLimit() {
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
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client) // Set HttpClient to be used by Retrofit
                    .build();
            SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);
            Call<List<DTO_SanPham>> objCall = sanPhamInterface.GetProductLimit();
            objCall.enqueue(new Callback<List<DTO_SanPham>>() {
                @Override
                public void onResponse(Call<List<DTO_SanPham>> call, Response<List<DTO_SanPham>> response) {
                    if (response.isSuccessful()){
                        listProduct.clear();
                        listProduct.addAll(response.body());
                        adapterTrenddingProduct.notifyDataSetChanged();
                        Log.d("list2", "onResponse2: "+listProduct.size());
                    }
                }

                @Override
                public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            Log.e("manh", "Thời gian thực hiện API Trang Chu: " + duration + "ms");
        }
    }
    private void getListCat() {
        Gson gson = new GsonBuilder().setLenient().create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CAT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


        CategoryInterface categoryInterface = retrofit.create(CategoryInterface.class);


        Call<List<CategoryDTO>> objCall = categoryInterface.getListCat();
        objCall.enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                if (response.isSuccessful()) {

                    listCat.clear();
                    listCat.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(),
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {

            }
        });


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
        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);

        // tạo đối tượng
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.lay_danh_sach();
        objCall.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(Call<List<DTO_SanPham>> call, Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {

                    mlist.clear();
                    mlist.addAll(response.body());
                    arrayAdapter.notifyDataSetChanged();
                    Log.d("list", "onResponse: "+mlist.size());

                } else {
                    Toast.makeText(getContext(),
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }

//                GetListSanPham();
            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {
                Log.e("manh", "onFailure: " + t.getLocalizedMessage() );
            }
        });

    }
}

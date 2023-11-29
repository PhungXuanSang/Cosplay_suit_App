package com.example.cosplay_suit_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_SanPham;
import com.example.cosplay_suit_app.Adapter.Adapter_Shop_SanPham;
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
 * Use the {@link Fragment_Shop_Shop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Shop_Shop extends Fragment {
    static String url = API.URL;
    static final String BASE_URL = url +"/product/";
    RecyclerView id_recyclerShop1, id_recyclerShop2, id_recyclerShop3;

    List<DTO_SanPham> mlist1;
    List<DTO_SanPham> mlist3;
    Adapter_Shop_SanPham adapterShopSanPham3 ,adapterShopSanPham1;
    String id_shop;
    boolean isLoading = false;
    boolean isLastPage = false;
    int VISIBLE_THRESHOLD;
    public Fragment_Shop_Shop() {

    }

    public static Fragment_Shop_Shop newInstance() {
        Fragment_Shop_Shop fragment = new Fragment_Shop_Shop();
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
        return inflater.inflate(R.layout.fragment__shop__shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        id_shop = intent.getStringExtra("id_shop");
        Log.e("manh", "onShop: " + id_shop );
        id_recyclerShop1 = view.findViewById(R.id.id_recyclerShop1);
        id_recyclerShop2 = view.findViewById(R.id.id_recyclerShop2);
        id_recyclerShop3 = view.findViewById(R.id.id_recyclerShop3);
        mlist1 = new ArrayList<DTO_SanPham>();
        mlist3 = new ArrayList<>();
        adapterShopSanPham3 = new Adapter_Shop_SanPham(getContext());

        id_recyclerShop3.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                VISIBLE_THRESHOLD = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    // Đây là nơi bạn gọi hàm để tải thêm dữ liệu
                    // Ví dụ: loadMoreData();
                    if (isLoading || isLoading){
                        return;
                    }
                    loadMoreData();
                }
            }
        });




        adapterShopSanPham1 = new Adapter_Shop_SanPham(getContext());
        adapterShopSanPham1.updateData(mlist1);
        id_recyclerShop1.setAdapter(adapterShopSanPham1);
        GetListSanPhamLimit(id_shop);




    }


    private void loadMoreData() {
        adapterShopSanPham3.updateData(mlist3);
        id_recyclerShop3.setAdapter(adapterShopSanPham3);
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

                    mlist3.clear();
                    mlist3.addAll(response.body());
                    adapterShopSanPham3.notifyDataSetChanged();
                    Log.d("list", "onResponse3: "+mlist3.size());

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

    void GetListSanPhamLimit(String id_shop) {
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
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.GetProductLimit(id_shop);
        objCall.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(Call<List<DTO_SanPham>> call, Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {

                    mlist1.clear();
                    mlist1.addAll(response.body());
                    adapterShopSanPham1.notifyDataSetChanged();
                    Log.d("list", "onResponse1: "+mlist1.size());

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
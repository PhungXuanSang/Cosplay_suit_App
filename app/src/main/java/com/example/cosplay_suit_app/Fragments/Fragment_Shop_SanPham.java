package com.example.cosplay_suit_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_Shop_SanPham1;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.Product_Page;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Shop_SanPham#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Shop_SanPham extends Fragment {
    static String url = API.URL;
    static final String BASE_URL = url + "/product/";
    RecyclerView id_recyclerShop_SanPham;

    List<DTO_SanPham> mlist;

    String id_shop;

    Adapter_Shop_SanPham1 shopSanPham1;
    long startTime = System.currentTimeMillis();

    ProgressBar id_progressBar;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int totalPage;

    long duration;

    ConstraintLayout id_bg_load;

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
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //cong viec background viet o day
                id_bg_load = view.findViewById(R.id.id_bg_load);
                id_recyclerShop_SanPham = view.findViewById(R.id.id_recyclerShop_SanPham);
                mlist = new ArrayList<>();
                shopSanPham1 = new Adapter_Shop_SanPham1(getContext());
                shopSanPham1.updateData(mlist);
                GetListSanPham(id_shop);
                try {
                    Log.e("manh1", "time shop san pham: " + duration );
                    Thread.sleep(duration + 500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //tuong tac voi giao dien
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        id_bg_load.setVisibility(View.GONE);
                        id_recyclerShop_SanPham.setAdapter(shopSanPham1);
                        id_recyclerShop_SanPham.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                                GridLayoutManager layoutManager = (GridLayoutManager) id_recyclerShop_SanPham.getLayoutManager();
                                int visibleItemCount = layoutManager.getChildCount();
                                int totalItemCount = layoutManager.getItemCount();
                                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                                if (!isLoading && !isLastPage) {
                                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount && firstVisibleItem >= 0) {
                                        // RecyclerView đã cuộn đến cuối trang, thực hiện "load more"
                                        if (totalPage>1){
                                            currentPage += 1;
                                            isLoading = true;
//                        id_progressBar.setVisibility(View.VISIBLE);
                                            GetListSanPhamNext(id_shop);
                                        }

                                    }
                                }
                            }
                        });
                    }
                });
            }
        });









    }

    private void GetListSanPhamNext(String id_shop) {
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
            Call<Product_Page> objCall = sanPhamInterface.GetProductPage(id_shop, currentPage);
            objCall.enqueue(new Callback<Product_Page>() {
                @Override
                public void onResponse(Call<Product_Page> call, Response<Product_Page> response) {
                    if (response.isSuccessful()) {
                        Product_Page productPage = response.body();
                        mlist.addAll(productPage.getDtoSanPham());
                        shopSanPham1.notifyDataSetChanged();
                        Log.d("list", "onResponse3: " + mlist.size());

//                        totalPage = response.headers().get("X-Total-Pages") != null ?
//                                Integer.parseInt(response.headers().get("X-Total-Pages")) : totalPage;
                        isLoading = false;
//                        id_progressBar.setVisibility(View.GONE);
                        if (currentPage == totalPage){
                            isLoading = false;
                            isLastPage = true;
                        }
                    } else {
                        Toast.makeText(getContext(),
                                "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<Product_Page> call, Throwable t) {
                    Log.e("manh", "onFailure: " + t.getLocalizedMessage() );
                }
            });
    }

    void GetListSanPham(String id_shop) {
        // tạo gson
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
            Call<Product_Page> objCall = sanPhamInterface.GetProductPage(id_shop, currentPage);
            objCall.enqueue(new Callback<Product_Page>() {
                @Override
                public void onResponse(Call<Product_Page> call, Response<Product_Page> response) {
                    if (response.isSuccessful()) {

                        Product_Page productPage = (Product_Page) response.body();
                        totalPage = productPage.getPage_length();
                        mlist.addAll(productPage.getDtoSanPham());
                        shopSanPham1.notifyDataSetChanged();
                        Log.d("list", "onResponse3: " + mlist.size());
                    } else {
                        Toast.makeText(getContext(),
                                "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Product_Page> call, Throwable t) {
                    Log.e("manh", "onFailure: " + t.getLocalizedMessage() );
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            Log.e("manh", "Thời gian thực hiện API Shop San Pham: " + duration + "ms");
        }

    }

}
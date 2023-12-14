package com.example.cosplay_suit_app.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.CategoryAdapter;
import com.example.cosplay_suit_app.Adapter.LocCategoryAdapter;
import com.example.cosplay_suit_app.Adapter.QlspAdapter;
import com.example.cosplay_suit_app.Adapter.SpinnerCategotyAdapter;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.LoginUser;
import com.example.cosplay_suit_app.DTO.Product_Page;
import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.Fragments.Fragment_profile;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.Interface_retrofit.CategoryInterface;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.Interface_retrofit.ShopInterface;
//import com.example.cosplay_suit_app.PhanTrang;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
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

public class QlspActivity extends AppCompatActivity implements LocCategoryAdapter.Onclick{
    static String url = API.URL;
    static final String BASE_URL = url + "/product/";

    static final String BASE_URL_SHOP = url + "/shop/";
    static final String BASE_URL_CAT = url +"/category/api/";

    ImageView iv_back, iv_add;
    RecyclerView rclvList;
    List<DTO_SanPham> mlist;
    ArrayList<CategoryDTO> listCat = new ArrayList<>();
    QlspAdapter adapter;
    LocCategoryAdapter categoryAdapter;
    TextView tvQuantity,tv_voucher,tvQlspCancel,tvQlspCategory;

    EditText search;

    SwipeRefreshLayout srlQlsp;

    LinearLayoutManager linearLayoutManager;
    String idshop;
    String id;
    GridLayoutManager layoutManager;
    private int currentPage =1 ;
    private boolean isLoading ;
    private boolean isLastPage ;

    private int totalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlsp);
        iv_add = findViewById(R.id.ivAdd);
        iv_back = findViewById(R.id.ivBack);
        rclvList = findViewById(R.id.rclvQlspListproduct);
        tvQuantity = findViewById(R.id.tvQlspQuantity);
        srlQlsp = findViewById(R.id.srlQlsp);
        search = findViewById(R.id.edtQlspSearch);
        tv_voucher = findViewById(R.id.tv_voucher);
        tvQlspCancel = findViewById(R.id.tvQlspCancel);
        tvQlspCategory = findViewById(R.id.tvQlspCategory);
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        SharedPreferences sharedPreferences2 = this.getSharedPreferences("shops", MODE_PRIVATE);
        idshop = sharedPreferences2.getString("id", "");
        linearLayoutManager = new LinearLayoutManager(this);
        rclvList.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rclvList.addItemDecoration(dividerItemDecoration);

        searchProduct();
        mlist = new ArrayList<DTO_SanPham>();
        adapter = new QlspAdapter(mlist, this);
        rclvList.setAdapter(adapter);

        onClick();
        refresh();
        callApiProduct();


    }

    private void onClick(){
        tv_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QlspActivity.this,Voucher_activity.class));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity(new Intent(QlspActivity.this,Shopcuatoi_Activity.class));
                onBackPressed();
            }
        });


        tvQlspCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
                tvQlspCancel.setVisibility(View.GONE);
            }
        });

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QlspActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
        tvQlspCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(QlspActivity.this);
                dialog.setContentView(R.layout.dialog_category);



                RecyclerView rlcvCategory = dialog.findViewById(R.id.rclvCategoryListCategory);
                LinearLayout llLocCategory = dialog.findViewById(R.id.llLocCategory);
                 layoutManager = new GridLayoutManager(QlspActivity.this, 1);

                listCat = new ArrayList<>();
                categoryAdapter = new LocCategoryAdapter(QlspActivity.this,listCat,dialog,new Handler(),QlspActivity.this::onClickItem);
                rlcvCategory.setLayoutManager(layoutManager);
                rlcvCategory.setAdapter(categoryAdapter);
                categoryAdapter.showLoading();
                getListCat();
//                adapter.clearlistProduct();
//                callApiProductCategory();
                dialog.show();

            }
        });
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
                    // Tạo đối tượng đặc biệt "Hiển thị Tất cả"
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setName("Tất cả loại"); // Đặt tên cho item đặc biệt
                    categoryDTO.setId(String.valueOf(-1));
                    categoryDTO.setImageCategory("https://png.pngtree.com/png-vector/20221114/ourmid/pngtree-atom-model-connection-sign-vector-png-image_34645666.png");
                    // Thêm item đặc biệt vào đầu danh sách
                    response.body().add(0, categoryDTO);

                    // Cập nhật Adapter
                    listCat.clear();
                    listCat.addAll(response.body());
                    categoryAdapter.hideLoading();
                    layoutManager.setSpanCount(3);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(QlspActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {
                // Xử lý lỗi khi gọi API không thành công
            }
        });
    }

    private void searchProduct() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.clearlistProduct();
                // Lọc sản phẩm khi thay đổi nội dung tìm kiếm

                String textSearch = charSequence.toString();
                callApiSearchProduct(textSearch);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void callApiProduct() {

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
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.GetProduct(idshop);
        objCall.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(@NonNull Call<List<DTO_SanPham>> call, @NonNull Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {

                    mlist.clear();
                    mlist.addAll(response.body());
                    tvQuantity.setText(mlist.size() + " Sản phẩm");
                    Log.d("TAG", "onResponse: " + response.body());
                    adapter.notifyDataSetChanged();

                    Log.d("TAG", "onResponse: " + mlist.size() + "-----------" + id);

                }

            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }
    private void callApiProductCategory(String idCategory) {

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
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.GetProductCtegory(idshop,idCategory);
        objCall.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(@NonNull Call<List<DTO_SanPham>> call, @NonNull Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {

                    mlist.clear();
                    mlist.addAll(response.body());
                    tvQuantity.setText(mlist.size() + " Sản phẩm");
                    Log.d("TAG", "onResponse: " + response.body());
                    adapter.notifyDataSetChanged();

                    Log.d("TAG", "onResponse: " + mlist.size() + "-----------" + id);

                }

            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }
    private void callApiSearchProduct(String name) {
        // Tạo Gson
        Gson gson = new GsonBuilder().setLenient().create();

        // Tạo HttpLoggingInterceptor và thiết lập mức độ ghi log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Tạo OkHttpClient với các tham số timeout và interceptor
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        // Tạo Retrofit với baseUrl, GsonConverterFactory và OkHttpClient
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        // Tạo đối tượng SanPhamInterface
        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);

        // Gọi phương thức searchProduct với idshop và name
        Call<List<DTO_SanPham>> call = sanPhamInterface.searchProduct(idshop, name);
        call.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(Call<List<DTO_SanPham>> call, Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {
                    List<DTO_SanPham> productList = response.body();
                    if (productList != null) {

                        mlist.clear();
                        mlist.addAll(productList);
                        tvQuantity.setText(mlist.size() + " Sản phẩm");
                        adapter.notifyDataSetChanged();
                        Log.d("TAG", "onResponse: " + mlist.size() + "-----------" + id);
                    }
                } else {
                    Log.d("TAG", "API call failed");
                }
            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {
                Log.d("TAG", "API call failed: " + t.getMessage());
            }
        });
    }

    private void refresh() {

        srlQlsp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mlist = new ArrayList<DTO_SanPham>();
                adapter = new QlspAdapter(mlist,QlspActivity.this);
                rclvList.setAdapter(adapter);
//                mlist.clear();
                callApiProduct();
                srlQlsp.setRefreshing(false);
            }
        });

    }
//    private void loadMore(){
//        rclvList.addOnScrollListener(new PhanTrang(linearLayoutManager) {
//            @Override
//            public void loadMoreItem() {
//                isLoading = true;
//                currentPage +=1;
//                loadNextPage();
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//        });
//    }
    private void loadNextPage(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },2000);
    }
    @Override
    public void onClickItem(CategoryDTO categoryDTO) {
//
//        adapter.clearlistProduct();
//        callApiProductCategory(categoryDTO.getId());
//        tvQlspCategory.setText(categoryDTO.getName());
//        Log.d("TAG", "onClickItem: "+categoryDTO.getId());
                // Nếu là một Category khác
        if (categoryDTO.getId().equals("-1")){
            adapter.clearlistProduct();
            callApiProduct();
            tvQlspCategory.setText(categoryDTO.getName());
        }else {
            adapter.clearlistProduct();
            callApiProductCategory(categoryDTO.getId());
            tvQlspCategory.setText(categoryDTO.getName());
            Log.d("TAG", "onClickItem: " + categoryDTO.getId());

        }

    }

}
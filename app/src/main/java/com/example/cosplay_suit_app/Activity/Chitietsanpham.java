package com.example.cosplay_suit_app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_ImageList;
import com.example.cosplay_suit_app.Adapter.Adapter_properties;
import com.example.cosplay_suit_app.Adapter.DanhgiaAdapter;
import com.example.cosplay_suit_app.DTO.CmtsDTO;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.DTO.UserIdResponse;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.Interface_retrofit.CmtsInterface;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.Interface_retrofit.ShopInterface;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Cart_controller;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

public class Chitietsanpham extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL_properties = url + "/product/";
    static final String BASE_URL_FAVoRITE = url + "/user/api/";
    static final String BASE_URL_CARTORDER = url + "/bill/";
    static final String BASE_URL_CMTS = url +"/comments/";
    static final String BASE_URL_SHOP = url +"/shop/";
    static String TAG = "chitietsp";
    ImageView img_backsp, img_pro, img_favorite, img_chat, img_themgiohang;
    TextView tv_price, tv_name,tv_slcmts ,tv_nameShop, tv_diachiShop ,tvSlSPShop, tv_noidung;
    String idproduct, nameproduct, imageproduct, aboutproduct, id_shop, time_product, id_category,stringsize, listImageJson;
    Dialog fullScreenDialog;
    int priceproduct, slkho;
    boolean isMyFavorite = false;
    long startTime = System.currentTimeMillis();
    long elapsedTime;
    static String id;
    // Thêm biến cho RecyclerView
    private RecyclerView rvImageList;
    private Adapter_ImageList adapterImageList;
    RecyclerView rcv_bl;
    ArrayList<CmtsDTO> listCmts;
    DanhgiaAdapter danhgiaAdapter;
    String iduser;
    List<ItemImageDTO> listImage;
    ArrayList<DTO_properties> listsize = new ArrayList<>();
    Adapter_properties adapterProperties;
    String checkcart;
    String nameShop,diachiShop,soluongSPShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);

        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        showDialog();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                idproduct = intent.getStringExtra("id_product");
                nameproduct = intent.getStringExtra("name");
                priceproduct = intent.getIntExtra("price", 0);
                slkho = intent.getIntExtra("slkho", 0);
                imageproduct = intent.getStringExtra("image");
                aboutproduct = intent.getStringExtra("about");
                id_shop = intent.getStringExtra("id_shop");
                time_product = intent.getStringExtra("time_product");
                id_category = intent.getStringExtra("id_category");
                // Lấy chuỗi JSON từ Intent
                listImageJson = intent.getStringExtra("listImage");
                // Chuyển chuỗi JSON thành danh sách đối tượng
                listImage = new Gson().fromJson(listImageJson,
                        new TypeToken<List<ItemImageDTO>>() {}.getType());
                // Lấy chuỗi JSON từ Intent
                stringsize = intent.getStringExtra("listsize");
                listCmts = new ArrayList<>();
                danhgiaAdapter = new DanhgiaAdapter(Chitietsanpham.this,listCmts);
// Khởi tạo và thiết lập RecyclerView
                rvImageList = findViewById(R.id.rvImageList);
                adapterImageList = new Adapter_ImageList(listImage);
                rvImageList.setAdapter(adapterImageList);
                rvImageList.setLayoutManager(new LinearLayoutManager(Chitietsanpham.this, LinearLayoutManager.HORIZONTAL, false));

// Sử dụng PagerSnapHelper để giảm thời gian dừng lại
                PagerSnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(rvImageList);
                loadFavorite();
                callApiProduct(id_shop);
                getListCmts(idproduct);
                getIdUserByShop(id_shop);
                getShopById(id_shop);
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //cong viec background viet o day
                Anhxa();

                try {
                    Log.e(TAG, "run12: " + elapsedTime);
                    Thread.sleep(1999);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //tuong tac voi giao dien
                handler.post(new Runnable() {
                    @Override
                    public void run() {


                        rcv_bl.setAdapter(danhgiaAdapter);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Chitietsanpham.this, LinearLayoutManager.VERTICAL);
                        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(Chitietsanpham.this, R.drawable.devider1));
                        rcv_bl.addItemDecoration(dividerItemDecoration);
                        fullScreenDialog.dismiss();
                        // Tiến hành tải và hiển thị ảnh từ URL bằng Glide
                        if (!TextUtils.isEmpty(imageproduct)) {
                            Glide.with(getApplicationContext())
                                    .load(imageproduct)
                                    .error(R.drawable.image)
                                    .placeholder(R.drawable.image)
                                    .centerCrop()
                                    .into(img_pro);
                        } else {
                            // Xử lý khi imageproduct là rỗng hoặc null
                        }
                        tv_name.setText(" " + nameproduct + " ");
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                        tv_price.setText("" + decimalFormat.format(priceproduct));
                        tv_nameShop.setText(nameShop);
                        tv_diachiShop.setText(diachiShop);
                        tvSlSPShop.setText(soluongSPShop);
                        tv_noidung.setText(aboutproduct);
                        if (isMyFavorite) {
                            img_favorite.setImageResource(R.drawable.favorite_24);
                        } else {
                            img_favorite.setImageResource(R.drawable.ic_no_favorite_24);
                        }
                        img_backsp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onBackPressed();
                            }
                        });
                        img_favorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!id.equalsIgnoreCase("")) {
                                    if (isMyFavorite) {
                                        removeToFavorite(Chitietsanpham.this, idproduct);
                                    } else {
                                        addToFavorite(Chitietsanpham.this, idproduct);
                                    }
                                } else {
                                    new AlertDialog.Builder(Chitietsanpham.this).setTitle("Notification!!")
                                            .setMessage("You need to log in to add favorites,Do you want to log in??")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(Chitietsanpham.this, LoginActivity.class));
                                                    dialogInterface.dismiss();
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            }).show();
                                }
                            }
                        });


                        img_chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!id.equalsIgnoreCase("")) {
                                    Intent intent = new Intent(Chitietsanpham.this, ChatActivity.class);
                                    intent.putExtra("idShop", iduser);
                                    startActivity(intent);
                                }else{
                                    new AlertDialog.Builder(Chitietsanpham.this).setTitle("Thông Báo!!")
                                            .setMessage("Bạn cần đăng nhập để liên hệ với shop")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(Chitietsanpham.this, LoginActivity.class));
                                                    dialogInterface.dismiss();
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            }).show();
                                }
                            }
                        });
                        img_themgiohang.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogaddcart();
                            }
                        });
                        findViewById(R.id.your_button_id).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Chitietsanpham.this, ShowShopActivity.class);
                                intent.putExtra("id_shop", id_shop);
                                intent.putExtra("name_shop", nameShop);
                                intent.putExtra("slsp_shop", soluongSPShop);
                                intent.putExtra("id_user", iduser);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }



    public void Anhxa() {
        img_pro = findViewById(R.id.img_pro);
        tv_price = findViewById(R.id.tv_price);
        tv_name = findViewById(R.id.tv_name);
        img_favorite = findViewById(R.id.img_favorite);
        img_backsp = findViewById(R.id.img_backsp);
        img_chat = findViewById(R.id.img_chat);
        rcv_bl = findViewById(R.id.rcv_bl);
        tv_slcmts = findViewById(R.id.tv_slcmts);
        img_themgiohang = findViewById(R.id.img_themgiohang);
        tv_nameShop = findViewById(R.id.your_first_textview_id);
        tv_diachiShop = findViewById(R.id.your_second_textview_id);
        tvSlSPShop = findViewById(R.id.tv_soluongSPShop);
        tv_noidung = findViewById(R.id.tv_noidung);
    }
    private void getIdUserByShop(String idproduct) {
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
        CmtsInterface cmtsInterface = retrofit.create(CmtsInterface.class);

        // tạo đối tượng
        Call<UserIdResponse> objCall = cmtsInterface.getidU(idproduct);
        objCall.enqueue(new Callback<UserIdResponse>() {
            @Override
            public void onResponse(Call<UserIdResponse> call, Response<UserIdResponse> response) {
                if (response.isSuccessful()) {
                    UserIdResponse userIdResponse = response.body();
                    iduser = userIdResponse.getIdUser();
                    Log.d(TAG, "onResponse: "+iduser);
                } else {
                    Toast.makeText(Chitietsanpham.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserIdResponse> call, Throwable t) {
                Log.d("CDG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
    private void getShopById(String id) {
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
        Call<Shop> objCall = shopInterface.shopById(id);
        objCall.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                if (response.isSuccessful()) {
                    Shop shop = response.body();
                    nameShop = shop.getNameshop();
                    diachiShop = shop.getAddress();
                    Log.e("manh", "Name: " + shop.getNameshop() );
                } else {
                    Toast.makeText(Chitietsanpham.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                Log.d("CDG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
    private void callApiProduct(String id) {

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
                .baseUrl(BASE_URL_properties)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);

        // tạo đối tượng
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.GetProduct(id);
        objCall.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(@NonNull Call<List<DTO_SanPham>> call, @NonNull Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {
                    List<DTO_SanPham> list = response.body();
                    soluongSPShop = String.valueOf(list.size());
                    Log.d("TAG", "onResponse: " + list.size() + "-----------");

                }

            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });

    }
    private void getListCmts(String idproduct) {
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
                .baseUrl(BASE_URL_CMTS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        CmtsInterface cmtsInterface = retrofit.create(CmtsInterface.class);

        // tạo đối tượng
        Call<List<CmtsDTO>> objCall = cmtsInterface.getListCmts(idproduct);
        objCall.enqueue(new Callback<List<CmtsDTO>>() {
            @Override
            public void onResponse(Call<List<CmtsDTO>> call, Response<List<CmtsDTO>> response) {
                if (response.isSuccessful()) {
                    listCmts.clear();
                    listCmts.addAll(response.body());
                    danhgiaAdapter.notifyDataSetChanged();
                    tv_slcmts.setText(listCmts.size()+" đánh giá ");
                    Log.d("CDG", "onResponse: "+listCmts.size());

                } else {
                    Toast.makeText(Chitietsanpham.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CmtsDTO>> call, Throwable t) {
                Log.d("CDG", "onFailure: " + t);
            }
        });

    }
    public void loadFavorite() {
        if (id.equalsIgnoreCase("")) {
//            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(id).child("Favorites").child(idproduct)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isMyFavorite = snapshot.exists();
                            if (isMyFavorite) {
                                img_favorite.setImageResource(R.drawable.favorite_24);
                            } else {
                                img_favorite.setImageResource(R.drawable.ic_no_favorite_24);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


    }
    public void addToFavorite(Context context,String idProduct){
        if (id.equalsIgnoreCase("")){
            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
        }else{
            long timestamp = System.currentTimeMillis();

            HashMap<String , Object> hashMap = new HashMap<>();
            hashMap.put("idProduct",idProduct);
            hashMap.put("timeStamp",timestamp);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(id).child("Favorites").child(idProduct)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Added to your favorites list...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "failed to add to favorite due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public void removeToFavorite(Context context,String idProduct ){
        if (id.equalsIgnoreCase("")){
            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(id).child("Favorites").child(idProduct)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Removed to your favorites list...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "failed to remove to favorite due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public void showDialog() {
        fullScreenDialog = new Dialog(Chitietsanpham.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        fullScreenDialog.setContentView(R.layout.dialog_load_chi_tiet_product);
        ImageView imageView = fullScreenDialog.findViewById(R.id.img_pro);
        ImageView imageView1 = fullScreenDialog.findViewById(R.id.img_bl);
        TextView tv_name = fullScreenDialog.findViewById(R.id.tv_name);
        TextView id_comment = fullScreenDialog.findViewById(R.id.id_comment);
        TextView id_daban = fullScreenDialog.findViewById(R.id.id_daban);
        LinearLayout id_li = fullScreenDialog.findViewById(R.id.id_li);
        TextView tv_product = fullScreenDialog.findViewById(R.id.tv_product);
        ImageView img_backsp = fullScreenDialog.findViewById(R.id.img_backsp);
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(900);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        imageView.startAnimation(mAnimation);
        imageView1.startAnimation(mAnimation);
        tv_name.startAnimation(mAnimation);
        id_comment.startAnimation(mAnimation);
        id_daban.startAnimation(mAnimation);
        id_li.startAnimation(mAnimation);
        tv_product.startAnimation(mAnimation);
        img_backsp.startAnimation(mAnimation);
        Window window = fullScreenDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        fullScreenDialog.show();
    }
    public void CheckAddCart(String id, String idproduct, int priceproduct, String selectedNameProperties){
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
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        CartOrderInterface cartOrderInterface = retrofit.create(CartOrderInterface.class);

        // tạo đối tượng
        Call<String> objCall = cartOrderInterface.checkaddcart(this.idproduct);
        objCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                    if (result.equals("No")){
                        Cart_controller cartController = new Cart_controller(Chitietsanpham.this);
                        if (selectedNameProperties != null) {

                            DTO_CartOrder cartOrder = new DTO_CartOrder();
                            cartOrder.setId_user(id);
                            cartOrder.setId_product(idproduct);
                            cartOrder.setTotalPayment(priceproduct);
                            cartOrder.setAmount(1);
                            cartOrder.setId_properties(selectedNameProperties);

                            cartController.AddCart(cartOrder);
                        }
                    }else {
                        Toast.makeText(Chitietsanpham.this, "Sản phẩm đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Chitietsanpham.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }
    public void dialogaddcart(){
        Dialog dialog = new Dialog(Chitietsanpham.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addcart);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        // Hiển thị giá sản phẩm
        TextView tvgiasp = dialog.findViewById(R.id.tv_giasp);
        tvgiasp.setText(priceproduct + " VNĐ");

        // Hiển thị số lượng sản phẩm
        TextView tvslkho = dialog.findViewById(R.id.tv_slkho);
        tvslkho.setText("Số lượng: " + slkho);

        // Hiển thị image sản phẩm
        ImageView imgsp = dialog.findViewById(R.id.imgproduct);

        ItemImageDTO firstImage = listImage.get(0);
        String imageUrl = firstImage.getImage();
            // Tiến hành tải và hiển thị ảnh từ URL bằng Glide
        Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.image)
                .placeholder(R.drawable.image)
                .centerCrop()
                .into(imgsp);

        //Hiển thị danh sách size
        RecyclerView rcv_properties = dialog.findViewById(R.id.rc_size);

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<DTO_properties>>() {}.getType();
            List<DTO_properties> myObjects = gson.fromJson(stringsize, listType);
            // Kiểm tra NULL trước khi sử dụng
            if (rcv_properties != null) {
                listsize.clear();
                rcv_properties.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                listsize.addAll(myObjects);
                adapterProperties = new Adapter_properties(listsize, this);
                rcv_properties.setAdapter(adapterProperties);
                adapterProperties.notifyDataSetChanged();
            } else {
                // Xử lý rcv_properties là NULL
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ khi có vấn đề với cú pháp JSON
        }

        //thêm vào giỏ hàng
        Button btnaddcart = dialog.findViewById(R.id.btndialog_addcart);
        btnaddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedNameProperties = adapterProperties.getSelectedNameProperties();
                CheckAddCart(id,idproduct, priceproduct, selectedNameProperties);

            }
        });

        // Chiều rộng full màn hình
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        // Chiều cao theo dialog màn hình
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Đặt vị trí của dialog ở phía dưới cùng của màn hình
        layoutParams.gravity = Gravity.BOTTOM;

        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }
}
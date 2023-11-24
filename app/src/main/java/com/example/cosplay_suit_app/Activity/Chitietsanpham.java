package com.example.cosplay_suit_app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_ImageList;
import com.example.cosplay_suit_app.Adapter.Adapter_SanPham;
import com.example.cosplay_suit_app.Adapter.Adapter_properties;
import com.example.cosplay_suit_app.Adapter.DanhgiaAdapter;
import com.example.cosplay_suit_app.Adapter.ImageAdapter;
import com.example.cosplay_suit_app.DTO.CmtsDTO;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.ItemDoneDTO;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.DTO.UserIdResponse;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.Favorite;
import com.example.cosplay_suit_app.Interface_retrofit.CmtsInterface;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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
    ImageView img_backsp, img_pro, img_favorite, img_chat;
    TextView tv_price, tv_name,tv_slcmts;
    String idproduct, nameproduct, imageproduct, aboutproduct, id_shop, time_product, id_category;
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
                String listImageJson = intent.getStringExtra("listImage");
             // Chuyển chuỗi JSON thành danh sách đối tượng
                List<ItemImageDTO> listImage = new Gson().fromJson(listImageJson, new TypeToken<List<ItemImageDTO>>() {}.getType());


// Khởi tạo và thiết lập RecyclerView
                rvImageList = findViewById(R.id.rvImageList);
                adapterImageList = new Adapter_ImageList(listImage);
                rvImageList.setAdapter(adapterImageList);
                rvImageList.setLayoutManager(new LinearLayoutManager(Chitietsanpham.this, LinearLayoutManager.HORIZONTAL, false));

// Sử dụng PagerSnapHelper để giảm thời gian dừng lại
                PagerSnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(rvImageList);
                loadFavorite();

                listCmts = new ArrayList<>();
                danhgiaAdapter = new DanhgiaAdapter(Chitietsanpham.this,listCmts);
                rcv_bl.setAdapter(danhgiaAdapter);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Chitietsanpham.this, LinearLayoutManager.VERTICAL);
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(Chitietsanpham.this, R.drawable.devider1));
                rcv_bl.addItemDecoration(dividerItemDecoration);
                getListCmts(idproduct);
                getIdUserByShop(id_shop);
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //cong viec background viet o day
                Anhxa();

                TextView tv_product = findViewById(R.id.tv_product);

                try {
                    Log.e(TAG, "run12: " + elapsedTime);
                    Thread.sleep(1111);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //tuong tac voi giao dien
                handler.post(new Runnable() {
                    @Override
                    public void run() {
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
                        Log.e("BL1", "run: " + isMyFavorite);
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
}
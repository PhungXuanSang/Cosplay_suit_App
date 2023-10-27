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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_properties;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.Favorite;
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
    static String TAG = "chitietsp";
    ImageView img_backsp, img_pro, img_favorite, img_chat;
    TextView tv_price, tv_name;
    RecyclerView rcv_properties;
    String idproduct, nameproduct, imageproduct, aboutproduct, id_shop, time_product, id_category;
    BottomSheetDialog bottomSheetDialog;
    Dialog fullScreenDialog;
    int priceproduct, slkho;
    boolean isMyFavorite = false;
    long startTime = System.currentTimeMillis();
    long elapsedTime;
    static String id;
    List<DTO_properties> listproperties;
    Adapter_properties adapterProperties;

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
                loadFavorite();

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

                        Glide.with(Chitietsanpham.this).load(imageproduct).centerCrop().into(img_pro);
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
                        tv_product.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Gọi hàm showDialog() và truyền các giá trị cần thiết
                                Intent intent = getIntent();
                                String idproduct = intent.getStringExtra("id_product");
                                String nameproduct = intent.getStringExtra("name");
                                int priceproduct = intent.getIntExtra("price", 0);
                                String imageproduct = intent.getStringExtra("image");
                                String about = intent.getStringExtra("about");

                                showDialog(Chitietsanpham.this, idproduct, nameproduct, priceproduct, imageproduct, about);
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
                                    intent.putExtra("idShop", id_shop);
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


    public void showDialog(Context context, String idproduct, String nameproduct,
                           int priceproduct, String imageproduct, String about) {
        bottomSheetDialog = new BottomSheetDialog(Chitietsanpham.this);

        View view = getLayoutInflater().inflate(R.layout.dialog_productdetail, null);
        bottomSheetDialog.setContentView(view);


        float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        float halfScreenHeight = screenHeight / 1.5f;
        View bottomSheetInternal = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheetInternal != null) {
            ViewGroup.LayoutParams layoutParams = bottomSheetInternal.getLayoutParams();

            if (layoutParams != null) {
                layoutParams.height = (int) halfScreenHeight;
                bottomSheetInternal.setLayoutParams(layoutParams);

            }
        }

        // Hiển thị tên sản phẩm
        TextView tv_name = bottomSheetDialog.findViewById(R.id.tv_name);
        tv_name.setText("" + nameproduct);

        // Hiển thị giá sản phẩm
        TextView tv_price = bottomSheetDialog.findViewById(R.id.tv_price);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tv_price.setText("" + decimalFormat.format(priceproduct));

        // Hiển thị thông tin sản phẩm
        TextView tv_about = bottomSheetDialog.findViewById(R.id.tv_about);
        tv_about.setText(about);
        tv_about.setMaxLines(1);
        tv_about.setEllipsize(TextUtils.TruncateAt.END);
        TextView tv_read = bottomSheetDialog.findViewById(R.id.tv_read);
        final int[] count = {0};
        tv_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count[0] == 0) {
                    tv_about.setMaxLines(1000);
                    tv_read.setText("Return");
                    count[0] = 1;
                } else {
                    tv_about.setMaxLines(1);
                    tv_read.setText("Read more");
                    count[0] = 0;
                }
            }
        });

        //Thêm sản phẩm vào giỏ hàng
        Button btnaddcart = bottomSheetDialog.findViewById(R.id.btn_addcart);
        btnaddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddcart(context, idproduct, priceproduct, slkho, imageproduct);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    public void dialogAddcart(Context context, String idproduct,
                              int priceproduct, int slkho, String imageproduct) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addcart);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        // Hiển thị giá sản phẩm
        TextView tvgiasp = dialog.findViewById(R.id.tv_giasp);
        tvgiasp.setText(+priceproduct + " VNĐ");

        // Hiển thị số lượng sản phẩm
        TextView tvslkho = dialog.findViewById(R.id.tv_slkho);
        tvslkho.setText("Số lượng: " + slkho);

        // Hiển thị image sản phẩm
        ImageView imgsp = dialog.findViewById(R.id.imgproduct);
        Glide.with(context).load(imageproduct).centerCrop().into(imgsp);

        //Hiển thị danh sách size
        rcv_properties = dialog.findViewById(R.id.rc_size);

        listproperties = new ArrayList<>();
        adapterProperties = new Adapter_properties(listproperties, this);
        rcv_properties.setAdapter(adapterProperties);
        adapterProperties.notifyDataSetChanged();
        Getproperties(idproduct);

        //thêm vào giỏ hàng
        Button btnaddcart = dialog.findViewById(R.id.btndialog_addcart);
        btnaddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DTO_CartOrder cartOrder = new DTO_CartOrder();
                cartOrder.setId_user(id);
                cartOrder.setProduct_id(idproduct);
                cartOrder.setTotalPayment(priceproduct);
                cartOrder.setAmount(1);
                cartOrder.setProperties_id("6535324abdcedbd6d8302681");

                AddCart(cartOrder);
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

    void AddCart(DTO_CartOrder objcart) {
        //tạo dđối towngj chuyển đổi kiểu dữ liệu
        Gson gson = new GsonBuilder().setLenient().create();
        //Tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //Khởi tạo Interface
        CartOrderInterface billInterface = retrofit.create(CartOrderInterface.class);
        //Tạo Call
        Call<DTO_CartOrder> objCall = billInterface.addcart(objcart);

        //Thực hiệnửi dữ liệu lên server
        objCall.enqueue(new Callback<DTO_CartOrder>() {
            @Override
            public void onResponse(Call<DTO_CartOrder> call, Response<DTO_CartOrder> response) {
                //Kết quẳ server trả về ở đây
                if (response.isSuccessful()) {
                    //Lấy kết quar trả về
                    Toast.makeText(Chitietsanpham.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_CartOrder> call, Throwable t) {
                //Nếu say ra lỗi sẽ thông báo ở đây
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
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

    void Getproperties(String idproduct) {
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
        Call<List<DTO_properties>> objCall = sanPhamInterface.getproperties(idproduct);
        objCall.enqueue(new Callback<List<DTO_properties>>() {
            @Override
            public void onResponse(Call<List<DTO_properties>> call, Response<List<DTO_properties>> response) {
                if (response.isSuccessful()) {

                    listproperties.clear();
                    listproperties.addAll(response.body());
                    adapterProperties.notifyDataSetChanged();

                } else {
                    Toast.makeText(Chitietsanpham.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DTO_properties>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }
}
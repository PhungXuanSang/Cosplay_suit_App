package com.example.cosplay_suit_app.Activity;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chitietsanpham extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url + "/product/";

    String TAG = "chitietsp";
    ImageView img_backsp, img_pro;
    TextView  tv_price, tv_name;
    ArrayList<DTO_SanPham> mlist;
    Adapter_SanPham adapter;
    RecyclerView rcv_5;

    String idproduct,nameproduct, imageproduct;
    int  priceproduct;
    static int slkho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);
        Log.d(TAG, "onCreate: Đã vào chi tiết sp");
        Anhxa();

        Intent intent = getIntent();
        idproduct = intent.getStringExtra("id_product");
        nameproduct = intent.getStringExtra("name");
        priceproduct = intent.getIntExtra("price",0);
        slkho = intent.getIntExtra("slkho", 0);
        imageproduct = intent.getStringExtra("image");
        Glide.with(Chitietsanpham.this).load(imageproduct).centerCrop().into(img_pro);
        tv_name.setText("Tên: " + nameproduct);
        tv_price.setText("Giá: " + priceproduct + "đ");
        img_backsp = findViewById(R.id.img_backsp);
        img_backsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        rcv_5 =findViewById(R.id.rcv_5);
        mlist = new ArrayList<DTO_SanPham>();
        adapter = new Adapter_SanPham(mlist, Chitietsanpham.this);
        rcv_5.setAdapter(adapter);
        GetListSanPham();

        TextView tv_product = findViewById(R.id.tv_product);
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

                showDialog(Chitietsanpham.this, idproduct, nameproduct, priceproduct, imageproduct,about);
            }
        });


    }
    public void Anhxa(){
        img_pro = findViewById(R.id.img_pro);
        tv_price = findViewById(R.id.tv_price);
        tv_name = findViewById(R.id.tv_name);
    }
    void GetListSanPham() {
        // tạo gson
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
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
                    adapter.notifyDataSetChanged();
                    Log.d("list", "onResponse: "+mlist.size());

                } else {
                    Toast.makeText(Chitietsanpham.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }

//                GetListSanPham();
            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {

            }
        });

    }
    public static void showDialog(Context context, String idproduct, String nameproduct, int priceproduct, String imageproduct, String about) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_productdetail);

        // Hiển thị tên sản phẩm
        TextView tv_name = dialog.findViewById(R.id.tv_name);
        tv_name.setText("name: " + nameproduct);

        // Hiển thị giá sản phẩm
        TextView tv_price = dialog.findViewById(R.id.tv_price);
        tv_price.setText("price: " + priceproduct + " VNĐ");

        // Hiển thị thông tin sản phẩm
        TextView tv_about = dialog.findViewById(R.id.tv_about);
        tv_about.setText("about: " + about);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        // Chiều rộng full màn hình
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        // Đặt vị trí của dialog ở phía dưới cùng của màn hình
        layoutParams.gravity = Gravity.BOTTOM;

        // Lấy chiều cao màn hình
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;

        // Đặt chiều dài của dialog thành 75% chiều cao màn hình
        layoutParams.height = (int) (0.75 * screenHeight);

        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        //Thêm sản phẩm vào giỏ hàng
        Button btnaddcart = dialog.findViewById(R.id.btn_addcart);
        btnaddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddcart(context, priceproduct, slkho, imageproduct);
            }
        });

        dialog.show();
    }

    public static void dialogAddcart(Context context, int priceproduct, int slkho, String imageproduct){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addcart);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        // Hiển thị giá sản phẩm
        TextView tvgiasp = dialog.findViewById(R.id.tv_giasp);
        tvgiasp.setText(+ priceproduct + " VNĐ");

        // Hiển thị số lượng sản phẩm
        TextView tvslkho = dialog.findViewById(R.id.tv_slkho);
        tvslkho.setText("WareHouse: " + slkho);

        // Hiển thị image sản phẩm
        ImageView imgsp = dialog.findViewById(R.id.imgproduct);
        Glide.with(context).load(imageproduct).centerCrop().into(imgsp);

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
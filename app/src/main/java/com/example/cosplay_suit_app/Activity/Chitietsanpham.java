package com.example.cosplay_suit_app.Activity;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
        // Tạo một AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Sử dụng LayoutInflater để nạp layout dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_productdetail, null);
        builder.setView(dialogView);

        // Thiết lập kích thước dialog để tràn màn hình
        AlertDialog dialog = builder.create();
        dialog.show(); // Hiển thị dialog trước khi thiết lập kích thước
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);




        // Tùy chỉnh các thuộc tính của dialog
        builder.setTitle("Tiêu đề dialog");
        builder.setPositiveButton("Đồng ý", (dialogInterface, which) -> {
            // Xử lý khi người dùng nhấn nút Đồng ý
        });
        builder.setNegativeButton("Hủy", (dialogInterface, which) -> {
            // Xử lý khi người dùng nhấn nút Hủy
        });

        // Hiển thị dữ liệu trong dialog
//        ImageView img_pro = dialogView.findViewById(R.id.img_pro);
//        TextView tv_name = dialogView.findViewById(R.id.tv_name);
//        TextView tv_price = dialogView.findViewById(R.id.tv_price);
//        TextView tv_about = dialogView.findViewById(R.id.tv_about);

//        Glide.with(context).load(imageproduct).centerCrop().into(img_pro);
//        tv_name.setText("Tên: " + nameproduct);
//        tv_price.setText("Giá: " + priceproduct + "đ");
//        tv_about.setText("About: " + about);

        // Hiển thị dialog
        dialog.show();
    }



}
package com.example.cosplay_suit_app.bill.controller;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_properties;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.DTO_CartOrder;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.DTO.TotalPriceManager;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Dialog_mualai {
    public static void dialogmualai(Context context, String id , String idproduct, Dialog dialog, int priceproduct, int slkho, String stringsize
            , List<ItemImageDTO> listImage, ArrayList<DTO_properties> listsize, Adapter_properties adapterProperties){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mualai);

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
        Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.image)
                .placeholder(R.drawable.image)
                .centerCrop()
                .into(imgsp);

        //Chọn số lượng
        ImageView imgcong, imgtru;
        TextView tvsoluong;
        imgcong = dialog.findViewById(R.id.imgcong);
        imgtru = dialog.findViewById(R.id.imgtru);
        tvsoluong = dialog.findViewById(R.id.tv_soluong);
        imgcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(tvsoluong.getText().toString().trim()) + 1;
                if (soluong <= slkho){
                    String slmoi = String.valueOf(soluong);
                    tvsoluong.setText(slmoi);
                }
            }
        });
        imgtru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(tvsoluong.getText().toString().trim()) - 1;
                if (soluong > 0){
                    String slmoi = String.valueOf(soluong);
                    tvsoluong.setText(slmoi);
                }
            }
        });

        //Hiển thị danh sách size
        RecyclerView rcv_properties = dialog.findViewById(R.id.rc_size);

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<DTO_properties>>() {}.getType();
            List<DTO_properties> myObjects = gson.fromJson(stringsize, listType);
            // Kiểm tra NULL trước khi sử dụng
            if (rcv_properties != null) {
                listsize.clear();
                rcv_properties.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                listsize.addAll(myObjects);
                adapterProperties = new Adapter_properties(listsize, context);
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
        Adapter_properties finalAdapterProperties = adapterProperties;
        btnaddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedNameProperties = finalAdapterProperties.getSelectedNameProperties();
                Cart_controller cartController = new Cart_controller(context);
                if (selectedNameProperties != null) {

//                    DTO_CartOrder cartOrder = new DTO_CartOrder();
//                    cartOrder.setId_user(id);
//                    cartOrder.setId_product(idproduct);
//                    cartOrder.setTotalPayment(priceproduct);
//                    cartOrder.setAmount(1);
//                    cartOrder.setId_properties(selectedNameProperties);
//
//                    cartController.AddCart(cartOrder);
                }else {
                    String title = "Thông báo mua hàng";
                    String msg = "Bạn phải chọn kích thước";
                    Dialogthongbao.showSuccessDialog(context, title, msg);
                }

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

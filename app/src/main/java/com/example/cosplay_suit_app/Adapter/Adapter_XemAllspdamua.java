package com.example.cosplay_suit_app.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.Activity.Chitietsanpham;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Dialog_cartorder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter_XemAllspdamua extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<BillDetailDTO> list;
    Context context;
    Dialog dialog;
    String id ="";
    ArrayList<DTO_properties> arrayList;
    Adapter_properties adapterProperties;

    public Adapter_XemAllspdamua(List<BillDetailDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allmualai, parent, false);
        return new ItemViewHoldel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BillDetailDTO billDetailDTO = list.get(position);
        ItemViewHoldel viewHoldel = (ItemViewHoldel) holder;

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHoldel.tv_gia.setText(decimalFormat.format(billDetailDTO.getTotalPayment()) + " VND");
        if (billDetailDTO.getDtoSanPham().getListImage() != null && !billDetailDTO.getDtoSanPham().getListImage().isEmpty()) {
            ItemImageDTO firstImage = billDetailDTO.getDtoSanPham().getListImage().get(0);
            String imageUrl = firstImage.getImage();

            // Tiến hành tải và hiển thị ảnh từ URL bằng Glide
            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(viewHoldel.img_sp);
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        String listsizeJson = new Gson().toJson(billDetailDTO.getDtoSanPham().getListProp());
        dialog = new Dialog(context);
        // Lấy chuỗi JSON từ Intent
        String listImageJson = new Gson().toJson(billDetailDTO.getDtoSanPham().getListImage());
        // Chuyển chuỗi JSON thành danh sách đối tượng
        List<ItemImageDTO> listImage = new Gson().fromJson(listImageJson, new TypeToken<List<ItemImageDTO>>() {}.getType());
        arrayList = new ArrayList<>();
        viewHoldel.imggiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_cartorder.dialogaddcart(context,id ,billDetailDTO.getDtoSanPham().getId(),dialog, (int) billDetailDTO.getDtoSanPham().getPrice(),
                billDetailDTO.getDtoSanPham().getAmount(), listsizeJson, listImage,  arrayList, adapterProperties);
            }
        });
        viewHoldel.ll_xemchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chitietsanpham.class);
                intent.putExtra("id_product", billDetailDTO.getDtoSanPham().getId());
                intent.putExtra("name", billDetailDTO.getDtoSanPham().getNameproduct());
                intent.putExtra("price", billDetailDTO.getDtoSanPham().getPrice());
                intent.putExtra("about", billDetailDTO.getDtoSanPham().getDescription());
                intent.putExtra("slkho", billDetailDTO.getDtoSanPham().getAmount());
                intent.putExtra("id_shop",billDetailDTO.getDtoSanPham().getId_shop());
                intent.putExtra("time_product",billDetailDTO.getDtoSanPham().getTime_product());
                intent.putExtra("id_category",billDetailDTO.getDtoSanPham().getId_category());
                // Chuyển danh sách thành JSON
                String listImageJson = new Gson().toJson(billDetailDTO.getDtoSanPham().getListImage());
                // Đặt chuỗi JSON vào Intent
                intent.putExtra("listImage", listImageJson);

                String listsizeJson = new Gson().toJson(billDetailDTO.getDtoSanPham().getListProp());
                intent.putExtra("listsize", listsizeJson);
                Log.d("check", "onClick: " + listsizeJson);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ItemViewHoldel extends RecyclerView.ViewHolder{
        ImageView img_sp, imggiohang;
        TextView tv_gia;
        LinearLayout ll_xemchitiet;

        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            img_sp = itemView.findViewById(R.id.img_sp);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            imggiohang = itemView.findViewById(R.id.imggiohang);
            ll_xemchitiet = itemView.findViewById(R.id.ll_xemchitiet);
        }
    }
}

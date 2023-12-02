package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.Chitietsanpham;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.Favorite;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter_TopProduct extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int stt = 1;
    private List<DTO_SanPham> mlist;
    Context context;

    public Adapter_TopProduct( Context context) {

        this.context = context;
    }
    public void updateData(List<DTO_SanPham> mlist){
        this.mlist = mlist;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ban_chay, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DTO_SanPham sanPham = mlist.get(position);

        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.tv_stt_sanpham.setText(""+stt++);


        if (stt == 2){
            int colorRes = R.color.FFD700;
            int colorValue = ContextCompat.getColor(context, colorRes);
            viewHolder.id_top.setBackgroundColor(colorValue);
            viewHolder.tv_stt_sanpham.setBackgroundColor(colorValue);
            viewHolder.img_tamgiac.setImageResource(R.drawable.tamgiac1);
        }

        if (stt == 3){
            int colorRes = R.color.C0C0C0;
            int colorValue = ContextCompat.getColor(context, colorRes);
            viewHolder.id_top.setBackgroundColor(colorValue);
            viewHolder.tv_stt_sanpham.setBackgroundColor(colorValue);
            viewHolder.img_tamgiac.setImageResource(R.drawable.tamgiac2);
        }
        if (stt == 4){
            int colorRes = R.color.a805D49;
            int colorValue = ContextCompat.getColor(context, colorRes);
            viewHolder.id_top.setBackgroundColor(colorValue);
            viewHolder.tv_stt_sanpham.setBackgroundColor(colorValue);
            viewHolder.img_tamgiac.setImageResource(R.drawable.tamgiac3);
        }
        viewHolder.tv_name_sanpham.setText(sanPham.getNameproduct()+"                                        ");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tv_gia_sanpham.setText(""+decimalFormat.format(sanPham.getPrice()));
        viewHolder.tv_sl_ban.setText(""+sanPham.getSold());
        if (sanPham.getListImage() != null && !sanPham.getListImage().isEmpty()) {
            ItemImageDTO firstImage = sanPham.getListImage().get(0);
            String imageUrl = firstImage.getImage();
            // Tiến hành tải và hiển thị ảnh từ URL bằng Glide
            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(viewHolder.img_image_sanpham);
        }

        viewHolder.ll_chitietsp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Chitietsanpham.class);
                intent.putExtra("id_product", sanPham.getId());
                intent.putExtra("name", sanPham.getNameproduct());
                intent.putExtra("price", sanPham.getPrice());
                intent.putExtra("about", sanPham.getDescription());
                intent.putExtra("slkho", sanPham.getAmount());
                intent.putExtra("id_shop",sanPham.getId_shop());
                intent.putExtra("time_product",sanPham.getTime_product());
                intent.putExtra("id_category",sanPham.getId_category());
                // Chuyển danh sách thành JSON
                String listImageJson = new Gson().toJson(sanPham.getListImage());
                // Đặt chuỗi JSON vào Intent
                intent.putExtra("listImage", listImageJson);

                String listsizeJson = new Gson().toJson(sanPham.getListProp());
                intent.putExtra("listsize", listsizeJson);
                Log.d("check", "onClick: " + listsizeJson);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gia_sanpham, tv_name_sanpham, tv_sl_ban, id_top ,tv_stt_sanpham;
        ImageView img_image_sanpham, img_tamgiac;
        ConstraintLayout ll_chitietsp;


        public ItemViewHolder(View view) {
            super(view);

            tv_name_sanpham = view.findViewById(R.id.tv_name_sanpham);
            img_image_sanpham = view.findViewById(R.id.img_image_sanpham);
            ll_chitietsp = view.findViewById(R.id.ll_chitietsp);
            tv_gia_sanpham = view.findViewById(R.id.tv_gia_sanpham);
            tv_sl_ban = view.findViewById(R.id.tv_sl_ban);
            id_top = view.findViewById(R.id.id_top);
            tv_stt_sanpham = view.findViewById(R.id.tv_stt_sanpham);
            img_tamgiac = view.findViewById(R.id.img_tamgiac);
        }

    }

}

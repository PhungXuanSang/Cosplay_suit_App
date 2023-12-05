package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.Activity.Chitietsanpham;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

public class ProSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DTO_SanPham> mlist;
    Context context;

    public ProSearchAdapter( Context context,List<DTO_SanPham> mlist) {
        this.context = context;
        this.mlist = mlist;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tkdx, parent, false);

        return new ProSearchAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DTO_SanPham sanPham = mlist.get(position);

        ProSearchAdapter.ItemViewHolder viewHolder = (ProSearchAdapter.ItemViewHolder) holder;

        viewHolder.tv_name_sanpham.setText(sanPham.getNameproduct());
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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

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
        TextView tv_name_sanpham;
        ImageView img_image_sanpham;



        public ItemViewHolder(View view) {
            super(view);

            tv_name_sanpham = view.findViewById(R.id.tv_nameSearch);
            img_image_sanpham = view.findViewById(R.id.img_proSearch);

        }

    }

}

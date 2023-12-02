package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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
import com.example.cosplay_suit_app.Activity.XemAllspdamuaActivity;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_billdetail;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

public class Adapter_mualai extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<BillDetailDTO> list;
    Context context;

    public Adapter_mualai(List<BillDetailDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_mualai, parent, false);
        return new Adapter_mualai.ItemViewHoldel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            // Binding cho item bình thường
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
    public class ItemViewHoldel extends RecyclerView.ViewHolder{
        ImageView img_sp;
        TextView tv_gia;
        LinearLayout ll_xemchitiet;
        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            img_sp = itemView.findViewById(R.id.img_sp);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            ll_xemchitiet = itemView.findViewById(R.id.ll_xemchitiet);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

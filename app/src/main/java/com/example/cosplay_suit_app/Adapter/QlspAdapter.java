package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.Activity.Chitietsanpham;
import com.example.cosplay_suit_app.Activity.DetailProductActivity;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

public class QlspAdapter extends RecyclerView.Adapter<QlspAdapter.ItemViewHolder> {
    private List<DTO_SanPham> mlist;
    private Context context;


    public QlspAdapter(List<DTO_SanPham> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_qlsp, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DTO_SanPham sanPham = mlist.get(position);

        if (sanPham.getSize() == null) {
            holder.tvName.setText(sanPham.getNameproduct());
        } else {
            holder.tvName.setText(sanPham.getNameproduct() + "--" + sanPham.getSize());
        }

        holder.tvAmount.setText(String.valueOf(sanPham.getAmount()));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(decimalFormat.format(sanPham.getPrice())+" vnđ");
        if (sanPham.getListImage() != null && !sanPham.getListImage().isEmpty()) {
            ItemImageDTO firstImage = sanPham.getListImage().get(0);
            String imageUrl = firstImage.getImage();

            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(holder.img_AnhSp);
        }

        holder.lllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_click_animation);
                holder.itemView.startAnimation(animation);

                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("id_product", sanPham.getId());
                intent.putExtra("name", sanPham.getNameproduct());
                intent.putExtra("price", sanPham.getPrice());
                intent.putExtra("about", sanPham.getDescription());
                intent.putExtra("slkho", sanPham.getAmount());
                intent.putExtra("id_shop",sanPham.getId_shop());
                intent.putExtra("time_product",sanPham.getTime_product());
                intent.putExtra("id_category",sanPham.getId_category());
                intent.putExtra("status",sanPham.isStatus());
                intent.putExtra("sold",sanPham.getSold());
                intent.putExtra("dtoSanpham",sanPham);
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

    public void clearlistProduct(){
            int itemCount = mlist.size();
mlist.clear();
notifyItemRangeRemoved(0,itemCount);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice, tvAmount, tvName;
        ImageView img_AnhSp;
        LinearLayout lllayout;

        public ItemViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvListQlspName);
            tvPrice = view.findViewById(R.id.tvListQlspPrice);
            img_AnhSp = view.findViewById(R.id.ivListQlspImage);
            tvAmount = view.findViewById(R.id.tvListQlspAmount);
            lllayout = view.findViewById(R.id.itemlistproduct);
        }
    }

}
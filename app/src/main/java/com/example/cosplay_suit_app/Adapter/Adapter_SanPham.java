package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.Activity.Chitietsanpham;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapter_SanPham extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<DTO_SanPham> mlist;
    Context context;

    public Adapter_SanPham(List<DTO_SanPham> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DTO_SanPham sanPham = mlist.get(position);

        Adapter_SanPham.ItemViewHolder viewHolder = (Adapter_SanPham.ItemViewHolder) holder;
        viewHolder.tv_nameSanPham.setText(sanPham.getNameproduct());
        Glide.with(context).load(sanPham.getImage()).centerCrop().into(viewHolder.img_AnhSp);
        viewHolder.ll_chitietsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Chitietsanpham.class);
                intent.putExtra("id_product", sanPham.getId());
                intent.putExtra("name", sanPham.getNameproduct());
                intent.putExtra("price", sanPham.getPrice());
                intent.putExtra("image", sanPham.getImage());
                intent.putExtra("about", sanPham.getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameSanPham;
        ImageView img_AnhSp;
        LinearLayout ll_chitietsp;


        public ItemViewHolder(View view) {
            super(view);

            tv_nameSanPham = view.findViewById(R.id.tv_nameSp);
            img_AnhSp = view.findViewById(R.id.anh_sp);
            ll_chitietsp = view.findViewById(R.id.id_chitietsp);

        }

    }
}

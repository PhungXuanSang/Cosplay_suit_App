package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapter_Cartorder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CartOrderDTO> list;
    Context context;

    public Adapter_Cartorder(List<CartOrderDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cartorder, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartOrderDTO dtoCartOrder = list.get(position);

        Adapter_Cartorder.ItemViewHolder viewHolder = (Adapter_Cartorder.ItemViewHolder) holder;
        Glide.with(context).load(dtoCartOrder.getDtoSanPham().getImage()).centerCrop().into(viewHolder.imgproduct);
        viewHolder.tvnamepro.setText(dtoCartOrder.getDtoSanPham().getNameproduct());
        viewHolder.tvsize.setText(dtoCartOrder.getDtoProperties().getNameproperties());
        viewHolder.tvprice.setText(dtoCartOrder.getDtoSanPham().getPrice() + "VND");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgproduct;
        TextView tvnamepro, tvsize, tvprice;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.img_product);
            tvnamepro = itemView.findViewById(R.id.tvname_product);
            tvprice = itemView.findViewById(R.id.tvprice_product);
            tvsize = itemView.findViewById(R.id.tvsize_product);
        }
    }
}

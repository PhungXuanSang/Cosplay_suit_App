package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
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

import java.text.DecimalFormat;
import java.util.List;

public class Adapter_buynow extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CartOrderDTO> list;
    Context context;

    public Adapter_buynow(List<CartOrderDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buynow, parent, false);
        return new ItemViewHoldel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartOrderDTO dtoCartOrder = list.get(position);

        Adapter_buynow.ItemViewHoldel viewHolder = (Adapter_buynow.ItemViewHoldel) holder;

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Glide.with(context).load(dtoCartOrder.getDtoSanPham().getImage()).centerCrop().into(viewHolder.imgproduct);
        viewHolder.tvnamepro.setText(dtoCartOrder.getDtoSanPham().getNameproduct());
        viewHolder.tvsize.setText("Size: "+dtoCartOrder.getDtoProperties().getNameproperties());
        viewHolder.tvprice.setText(decimalFormat.format(dtoCartOrder.getDtoSanPham().getPrice()) + " VND");
        viewHolder.tvsoluong.setText("Số lượng: "+dtoCartOrder.getAmount());
        viewHolder.tvthanhtien.setText("Thành tiền: "+decimalFormat.format(dtoCartOrder.getTotalPayment()) + " VND");
        viewHolder.tvtonggia.setText(decimalFormat.format(dtoCartOrder.getTotalPayment()) + " VND");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHoldel extends RecyclerView.ViewHolder{
        ImageView imgproduct;
        TextView tvnamepro, tvsize, tvprice, tvtonggia, tvsoluong, tvthanhtien;
        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.img_product);
            tvnamepro = itemView.findViewById(R.id.tvname_product);
            tvprice = itemView.findViewById(R.id.tvprice_product);
            tvsize = itemView.findViewById(R.id.tvsize_product);
            tvtonggia = itemView.findViewById(R.id.tv_tonggia);
            tvsoluong = itemView.findViewById(R.id.tv_soluong);
            tvthanhtien = itemView.findViewById(R.id.tv_thanhtien);
        }
    }
}

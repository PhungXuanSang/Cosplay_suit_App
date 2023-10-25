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
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapter_Bill extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<BillDetailDTO> list;
    Context context;

    public Adapter_Bill(List<BillDetailDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BillDetailDTO billDetailDTO = list.get(position);
        Adapter_Bill.ItemViewHolder holder1 = (ItemViewHolder) holder;
        Glide.with(context).load(billDetailDTO.getDtoSanPham().getImage()).centerCrop().into(holder1.imgproduct);
        holder1.tvnamepro.setText(billDetailDTO.getDtoSanPham().getNameproduct());
        holder1.tvprice.setText(""+billDetailDTO.getDtoSanPham().getPrice());
        holder1.tvtongbill.setText(""+billDetailDTO.getDtoBill().getTotalPayment());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgproduct;
        TextView tvnamepro, tvsize, tvprice, tvtongbill;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.img_product);
            tvnamepro = itemView.findViewById(R.id.tvname_product);
            tvprice = itemView.findViewById(R.id.tvprice_product);
            tvsize = itemView.findViewById(R.id.tvsize_product);
            tvtongbill = itemView.findViewById(R.id.tv_tongbill);
        }
    }
}

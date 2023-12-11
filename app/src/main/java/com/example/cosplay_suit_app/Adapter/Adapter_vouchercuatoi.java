package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.DTO.GetVoucher_DTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapter_vouchercuatoi extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<GetVoucher_DTO> list;
    Context context;

    public Adapter_vouchercuatoi(List<GetVoucher_DTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chonvoucher, parent, false);
        return new ItemViewHoldel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GetVoucher_DTO getVoucherDto = list.get(position);
        ItemViewHoldel viewHoldel = (ItemViewHoldel) holder;
        viewHoldel.giamgiaint.setText(getVoucherDto.getDtoVoucher().getDiscount() + "%");
        viewHoldel.conten_id.setText(getVoucherDto.getDtoVoucher().getContent());
        viewHoldel.nameshop.setText(getVoucherDto.getDtoVoucher().getShop().getNameshop());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ItemViewHoldel extends RecyclerView.ViewHolder{
        TextView giamgiaint, nameshop, conten_id, iddungngay;
        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            conten_id = itemView.findViewById(R.id.conten_id);
            nameshop = itemView.findViewById(R.id.nameshop);
            giamgiaint = itemView.findViewById(R.id.giamgiaint);
            iddungngay = itemView.findViewById(R.id.iddungngay);
        }
    }

}

package com.example.cosplay_suit_app.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.Activity.MualaiActivity;
import com.example.cosplay_suit_app.DTO.GetVoucher_DTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapterchonvoucher extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<GetVoucher_DTO> list;
    Context context;
    Onclickchonvoucher onclickchonvoucher;
    Dialog dialog;
    String idshop;

    public Adapterchonvoucher(List<GetVoucher_DTO> list, Context context, Onclickchonvoucher onclickchonvoucher,
                              Dialog dialog, String idshop){
        this.list = list;
        this.context = context;
        this.onclickchonvoucher = onclickchonvoucher;
        this.dialog = dialog;
        this.idshop = idshop;
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

        if (getVoucherDto.getDtoVoucher().getShop().getId().equals(idshop)){
            viewHoldel.iddungngay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    onclickchonvoucher.onclickdungngay(getVoucherDto);
                }
            });
        }else {
            viewHoldel.idthongbao.setVisibility(View.VISIBLE);
            viewHoldel.iddungngay.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ItemViewHoldel extends RecyclerView.ViewHolder{
        TextView giamgiaint, nameshop, conten_id, iddungngay, idthongbao;
        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            conten_id = itemView.findViewById(R.id.conten_id);
            nameshop = itemView.findViewById(R.id.nameshop);
            giamgiaint = itemView.findViewById(R.id.giamgiaint);
            iddungngay = itemView.findViewById(R.id.iddungngay);
            idthongbao = itemView.findViewById(R.id.idthongbao);
        }
    }
    public interface Onclickchonvoucher{
        void onclickdungngay(GetVoucher_DTO getVoucherDto);
    }
}

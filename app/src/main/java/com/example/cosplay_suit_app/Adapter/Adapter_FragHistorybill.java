package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.Activity.Chitietbill_Activity;
import com.example.cosplay_suit_app.Activity.ShowShopActivity;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.History_DTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;

import java.util.List;

public class Adapter_FragHistorybill extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<History_DTO> list;
    Context context;

    public Adapter_FragHistorybill(List<History_DTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fraghistorybill, parent, false);
        return new ItemViewHoldel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        History_DTO historyDto = list.get(position);
        ItemViewHoldel viewHolder = (ItemViewHoldel) holder;
        viewHolder.tv_madon.setText("Mã đơn: "+historyDto.getId_bill().get_id());
        viewHolder.tv_payment.setText("+"+historyDto.getTransaction_amount());
        viewHolder.tv_time.setText(historyDto.getTime_transaction());

        viewHolder.cartitemhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chitietbill_Activity.class);
                intent.putExtra("idbill", historyDto.getId_bill().get_id());
                intent.putExtra("tongbill", historyDto.getId_bill().getTotalPayment());
                intent.putExtra("stringstatus","Done");
                intent.putExtra("checkactivity", "shop");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ItemViewHoldel extends RecyclerView.ViewHolder{
        ImageView imghistory;
        TextView tv_payment,tv_madon, tv_time;
        CardView cartitemhistory;
        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            imghistory = itemView.findViewById(R.id.imghistory);
            tv_payment = itemView.findViewById(R.id.tv_payment);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_madon = itemView.findViewById(R.id.tv_madon);
            cartitemhistory =  itemView.findViewById(R.id.cartitemhistory);
        }
    }
}

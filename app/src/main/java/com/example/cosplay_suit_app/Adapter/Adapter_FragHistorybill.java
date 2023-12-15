package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.History_DTO;
import com.example.cosplay_suit_app.R;

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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ItemViewHoldel extends RecyclerView.ViewHolder{
        ImageView imghistory;
        TextView tv_payment,tv_madon, tv_time;
        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            imghistory = itemView.findViewById(R.id.imghistory);
            tv_payment = itemView.findViewById(R.id.tv_payment);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_madon = itemView.findViewById(R.id.tv_madon);
        }
    }
}

package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.Activity.Chitietbill_Activity;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_buynow;
import com.example.cosplay_suit_app.Package_bill.DTO.BillDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapter_FragDoneBill extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<BillDetailDTO> list;
    Context context;

    public Adapter_FragDoneBill(List<BillDetailDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragdonebill, parent, false);
        return new ItemViewHoldel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BillDetailDTO billDTO = list.get(position);
        ItemViewHoldel viewHolder = (ItemViewHoldel) holder;
        viewHolder.tv_giatribill.setText("Giá trị hóa đơn: "+billDTO.getDtoBill().getTotalPayment() + " VND");
        viewHolder.tv_giatrithuc.setText("Số tiền thanh toán: " + billDTO.getDtoBill().getThanhtoan().getVnp_Amount()+ " VND");
        viewHolder.tv_nguoimua.setText("Người mua: " + billDTO.getDtoBill().getUser().getFullname());
        viewHolder.tv_time.setText("Thời gian hoàn thành: " + billDTO.getDtoBill().getTimeend());
        if (billDTO.getDtoBill().getThanhtoan().getStatus().equals("NotDisbursed")){
            viewHolder.tv_trangthai.setText("Chưa giải ngân");
        }else {
            viewHolder.tv_trangthai.setText("Đã giải ngân");
        }
        viewHolder.carddonebill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chitietbill_Activity.class);
                intent.putExtra("idbill", billDTO.getDtoBill().get_id());
                intent.putExtra("tongbill", billDTO.getDtoBill().getTotalPayment());
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
        TextView tv_trangthai,tv_giatrithuc,tv_giatribill, tv_time, tv_nguoimua;
        CardView carddonebill;
        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            tv_trangthai = itemView.findViewById(R.id.tv_trangthai);
            tv_giatrithuc = itemView.findViewById(R.id.tv_giatrithuc);
            tv_giatribill = itemView.findViewById(R.id.tv_giatribill);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_nguoimua = itemView.findViewById(R.id.tv_nguoimua);
            carddonebill = itemView.findViewById(R.id.carddonebill);
        }
    }

}

package com.example.cosplay_suit_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cosplay_suit_app.Package_bill.DTO.BillDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapter_ThongKeShop extends RecyclerView.Adapter<Adapter_ThongKeShop.UserViewHolder>{
    private List<BillDTO> listBillDTO;

    public Adapter_ThongKeShop(List<BillDTO> listBillDTO) {
        this.listBillDTO = listBillDTO;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thongke, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        BillDTO billDTO = listBillDTO.get(position);
        if (billDTO == null){
            return;
        }
        holder.tv_ngaytke.setText(""+ billDTO.getTimeend());
        holder.tv_giatke.setText("ToltalPayment: "+ billDTO.getTotalPayment());
    }

    @Override
    public int getItemCount() {
        if (listBillDTO != null){
            return listBillDTO.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_ngaytke, tv_giatke;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ngaytke = itemView.findViewById(R.id.tv_ngaytke);
            tv_giatke = itemView.findViewById(R.id.tv_giatke);
        }
    }
}

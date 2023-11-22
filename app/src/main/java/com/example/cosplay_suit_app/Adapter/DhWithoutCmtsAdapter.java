package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.ItemDoneDTO;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.Package_bill.Activity.AddDanhGiaActivity;
import com.example.cosplay_suit_app.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DhWithoutCmtsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    Context context;
    ArrayList<ItemDoneDTO> list;


    public DhWithoutCmtsAdapter(Context context, ArrayList<ItemDoneDTO> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhangdone,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemDoneDTO itemDoneDTO = list.get(position);

        DhWithoutCmtsAdapter.ItemViewHolder viewHolder = (DhWithoutCmtsAdapter.ItemViewHolder) holder;

        String imageUrl = null;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        viewHolder.tv_nameShop.setText(itemDoneDTO.getProDoneDTO().getShop().getNameshop());
        viewHolder.tv_nameDone.setText(itemDoneDTO.getProDoneDTO().getNameproduct());
        viewHolder.tv_gia.setText(decimalFormat.format(itemDoneDTO.getProDoneDTO().getPrice())+"đ");
        if (itemDoneDTO.getProDoneDTO().getListImage() != null && !itemDoneDTO.getProDoneDTO().getListImage().isEmpty()) {
            ItemImageDTO firstImage = itemDoneDTO.getProDoneDTO().getListImage().get(0);
            imageUrl = firstImage.getImage();


            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(viewHolder.img_proDone);
        }
        viewHolder.tv_slDone.setText("Số lượng: x"+itemDoneDTO.getAmount());
        viewHolder.tv_ttPayment.setText(decimalFormat.format(itemDoneDTO.getTotalPayment())+"đ");
        String finalImageUrl = imageUrl;
        viewHolder.btn_danhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddDanhGiaActivity.class);
                intent.putExtra("idbill",itemDoneDTO.getIdBill());
                intent.putExtra("idproduct",itemDoneDTO.getProDoneDTO().getId());
                intent.putExtra("anhsp", finalImageUrl);
                intent.putExtra("namesp",itemDoneDTO.getProDoneDTO().getNameproduct());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameShop,tv_nameDone,tv_slDone,tv_gia,tv_ttPayment;
        ImageView img_proDone;
        Button btn_danhgia;


        public ItemViewHolder(View view) {
            super(view);

            tv_nameShop = view.findViewById(R.id.tv_nameshopdone);
            img_proDone = view.findViewById(R.id.img_proDone);
            btn_danhgia = view.findViewById(R.id.btn_danhgia);
            tv_gia = view.findViewById(R.id.tv_priceDone);
            tv_nameDone = view.findViewById(R.id.tv_nameDone);
            tv_slDone = view.findViewById(R.id.tv_sldone);
            tv_ttPayment = view.findViewById(R.id.tv_totalpayment);

        }

    }
}
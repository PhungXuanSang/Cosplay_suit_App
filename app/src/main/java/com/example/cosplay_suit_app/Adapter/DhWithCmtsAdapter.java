package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
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
import com.example.cosplay_suit_app.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DhWithCmtsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    Context context;
    ArrayList<ItemDoneDTO> list;


    public DhWithCmtsAdapter(Context context, ArrayList<ItemDoneDTO> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang_dacmt,parent,false);

        return new DhWithCmtsAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemDoneDTO itemDoneDTO = list.get(position);

        DhWithCmtsAdapter.ItemViewHolder viewHolder = (DhWithCmtsAdapter.ItemViewHolder) holder;


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        viewHolder.tv_nameShop.setText(itemDoneDTO.getProDoneDTO().getShop().getNameshop());
        viewHolder.tv_nameDone.setText(itemDoneDTO.getProDoneDTO().getNameproduct());
        viewHolder.tv_gia.setText(decimalFormat.format(itemDoneDTO.getProDoneDTO().getPrice())+"đ");
        if (itemDoneDTO.getProDoneDTO().getListImage() != null && !itemDoneDTO.getProDoneDTO().getListImage().isEmpty()) {
            ItemImageDTO firstImage = itemDoneDTO.getProDoneDTO().getListImage().get(0);
            String imageUrl = firstImage.getImage();


            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(viewHolder.img_proDone);
        }
        viewHolder.tv_slDone.setText("Số lượng: x"+itemDoneDTO.getAmount());
        viewHolder.tv_ttPayment.setText(decimalFormat.format(itemDoneDTO.getTotalPayment())+"đ");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameShop,tv_nameDone,tv_slDone,tv_gia,tv_ttPayment;
        ImageView img_proDone;


        public ItemViewHolder(View view) {
            super(view);

            tv_nameShop = view.findViewById(R.id.tv_nameshopdone);
            img_proDone = view.findViewById(R.id.img_proDone);
            tv_gia = view.findViewById(R.id.tv_priceDone);
            tv_nameDone = view.findViewById(R.id.tv_nameDone);
            tv_slDone = view.findViewById(R.id.tv_sldone);
            tv_ttPayment = view.findViewById(R.id.tv_totalpayment);

        }

    }
}

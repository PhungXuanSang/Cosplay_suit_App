package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.CmtsDTO;
import com.example.cosplay_suit_app.DTO.GetCmtsDTO;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;

import java.util.ArrayList;

public class DanhgiaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    Context context;
    ArrayList<CmtsDTO> list;


    public DanhgiaAdapter(Context context, ArrayList<CmtsDTO> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_binh_luan,parent,false);

        return new DanhgiaAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CmtsDTO cmtsDTO = list.get(position);

        DanhgiaAdapter.ItemViewHolder viewHolder = (DanhgiaAdapter.ItemViewHolder) holder;



        viewHolder.tv_nameU.setText(cmtsDTO.getUser().getFullname());
        viewHolder.tv_date.setText(cmtsDTO.getTime());
        viewHolder.tv_content.setText(cmtsDTO.getContent());
        viewHolder.ratingBar.setRating(cmtsDTO.getStar());


        if (cmtsDTO.getImage() != null && !cmtsDTO.getImage().isEmpty()) {
            DanhgiaImageAdapter danhgiaImageAdapter = new DanhgiaImageAdapter(cmtsDTO.getImage());
            viewHolder.rcv_listanh.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            viewHolder.rcv_listanh.setAdapter(danhgiaImageAdapter);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL);
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
            viewHolder.rcv_listanh.addItemDecoration(dividerItemDecoration);
            viewHolder.rcv_listanh.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rcv_listanh.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameU,tv_date,tv_content;
        ImageView img_avt;
        RatingBar ratingBar;
        RecyclerView rcv_listanh;


        public ItemViewHolder(View view) {
            super(view);

            tv_nameU = view.findViewById(R.id.tv_nameUserCmts);
            tv_date = view.findViewById(R.id.tv_time);
            ratingBar = view.findViewById(R.id.ratingBarForDeltails);
            rcv_listanh = view.findViewById(R.id.rcv_imageCmts);
            tv_content = view.findViewById(R.id.tv_content);
            img_avt = view.findViewById(R.id.img_avtUserCmts);
        }

    }
}

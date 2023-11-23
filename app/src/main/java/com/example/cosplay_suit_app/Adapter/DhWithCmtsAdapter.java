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
import com.example.cosplay_suit_app.DTO.GetCmtsDTO;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DhWithCmtsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    Context context;
    ArrayList<GetCmtsDTO> list;


    public DhWithCmtsAdapter(Context context, ArrayList<GetCmtsDTO> list) {
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
        GetCmtsDTO cmtsDTO = list.get(position);

        DhWithCmtsAdapter.ItemViewHolder viewHolder = (DhWithCmtsAdapter.ItemViewHolder) holder;



        viewHolder.tv_nameU.setText("Fullname: "+cmtsDTO.getUser().getFullname());
        viewHolder.tv_date.setText(cmtsDTO.getTime());
        viewHolder.tv_namePro.setText(cmtsDTO.getIdPro().getNameproduct());
        viewHolder.tv_content.setText(cmtsDTO.getContent());
        viewHolder.ratingBar.setRating(cmtsDTO.getStar());

        if (cmtsDTO.getIdPro().getListImage() != null && !cmtsDTO.getIdPro().getListImage().isEmpty()) {
            ItemImageDTO firstImage = cmtsDTO.getIdPro().getListImage().get(0);
            String imageUrl = firstImage.getImage();


            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(viewHolder.img_proDone);
        }


        ImageCmtsUserAdapter imageCmtsUserAdapter = new ImageCmtsUserAdapter(cmtsDTO.getImage());
        viewHolder.rcv_listanh.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewHolder.rcv_listanh.setAdapter(imageCmtsUserAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        viewHolder.rcv_listanh.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameU, tv_namePro,tv_date,tv_content;
        ImageView img_proDone;
        RatingBar ratingBar;
        RecyclerView rcv_listanh;

        public ItemViewHolder(View view) {
            super(view);

            tv_nameU = view.findViewById(R.id.tv_nameshopdone);
            img_proDone = view.findViewById(R.id.img_proDone);
            tv_date = view.findViewById(R.id.tv_ngayCmts);
            tv_namePro = view.findViewById(R.id.tv_nameDone);
            ratingBar = view.findViewById(R.id.ratingBarU);
            rcv_listanh = view.findViewById(R.id.rcv_listimage);
            tv_content = view.findViewById(R.id.tv_contentCmts);
        }

    }
}

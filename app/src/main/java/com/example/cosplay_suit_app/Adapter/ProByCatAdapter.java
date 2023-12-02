package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.Chitietsanpham;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.Favorite;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.DTO.ProByCatDTO;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProByCatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProByCatDTO> list;
    Context context;

    public ProByCatAdapter( Context context,List<ProByCatDTO> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pro_by_cat, parent, false);

        return new ProByCatAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProByCatDTO sanPham = list.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        ProByCatAdapter.ItemViewHolder viewHolder = (ProByCatAdapter.ItemViewHolder) holder;

        viewHolder.tv_nameProByCat.setText(sanPham.getNameproduct());

        viewHolder.tv_priceProByCat.setText(decimalFormat.format(sanPham.getPrice())+"đ");


        if (sanPham.getAvgStars() != -1) {
            viewHolder.tv_avgStar.setText(sanPham.getAvgStars() + "/5");
            viewHolder.ratingBar.setRating(sanPham.getAvgStars());
        } else {

            viewHolder.tv_avgStar.setText("5/5");
            viewHolder.ratingBar.setRating(5);
        }
        viewHolder.tv_starCount.setText("( "+sanPham.getStarCount()+" đánh giá )");
        if (sanPham.getListImage() != null && !sanPham.getListImage().isEmpty()) {
            ItemImageDTO firstImage = sanPham.getListImage().get(0);
            String imageUrl = firstImage.getImage();

            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(viewHolder.img_proByCat);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameProByCat,tv_priceProByCat,tv_avgStar,tv_starCount;
        ImageView img_proByCat;
        RatingBar ratingBar;


        public ItemViewHolder(View view) {
            super(view);

            tv_nameProByCat = view.findViewById(R.id.tv_nameProByCat);
            tv_priceProByCat = view.findViewById(R.id.tv_priceBycat);

            tv_avgStar = view.findViewById(R.id.tv_avgStar);
            tv_starCount = view.findViewById(R.id.tv_countStar);
            ratingBar = view.findViewById(R.id.ratingBarProByCat);
            img_proByCat = view.findViewById(R.id.img_probycat);

        }

    }



}


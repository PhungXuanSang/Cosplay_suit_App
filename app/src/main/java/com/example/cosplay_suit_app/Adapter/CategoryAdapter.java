package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.cosplay_suit_app.Activity.ProductsByCatActivity;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.CmtsDTO;
import com.example.cosplay_suit_app.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    Context context;
    ArrayList<CategoryDTO> list;


    public CategoryAdapter(Context context, ArrayList<CategoryDTO> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);

        return new CategoryAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryDTO categoryDTO = list.get(position);

        CategoryAdapter.ItemViewHolder viewHolder = (CategoryAdapter.ItemViewHolder) holder;

        viewHolder.tv_nameCat.setText(categoryDTO.getName());
        Glide.with(viewHolder.itemView.getContext())
                .load(categoryDTO.getImageCategory())
                .into(viewHolder.img_cat);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductsByCatActivity.class);
                intent.putExtra("idCat",categoryDTO.getId());
                intent.putExtra("nameCat",categoryDTO.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameCat;
        ImageView img_cat;


        public ItemViewHolder(View view) {
            super(view);

            tv_nameCat = view.findViewById(R.id.tv_nameCat);
            img_cat = view.findViewById(R.id.img_Cat);

        }

    }
}

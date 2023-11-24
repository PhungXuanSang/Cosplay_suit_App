package com.example.cosplay_suit_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapter_ImageList extends RecyclerView.Adapter<Adapter_ImageList.ImageViewHolder> {
    private List<ItemImageDTO> imageList;

    public Adapter_ImageList(List<ItemImageDTO> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image1, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ItemImageDTO imageDTO = imageList.get(position);
        if (imageDTO != null && imageDTO.getImage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(imageDTO.getImage())
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(holder.imgImage);
        } else {
            // Xử lý khi imageDTO hoặc imageDTO.getImage() là null
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgImage = itemView.findViewById(R.id.img_pro1); // Đặt ID của ImageView trong item_image.xml
        }
    }
}
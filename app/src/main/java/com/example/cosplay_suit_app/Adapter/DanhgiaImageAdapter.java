package com.example.cosplay_suit_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.ImageCmtsDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class DanhgiaImageAdapter extends RecyclerView.Adapter<DanhgiaImageAdapter.ImageViewHolder> {
    private List<ImageCmtsDTO> imageList;

    public DanhgiaImageAdapter(List<ImageCmtsDTO> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public DanhgiaImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_binh_luan, parent, false);
        return new DanhgiaImageAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhgiaImageAdapter.ImageViewHolder holder, int position) {
        ImageCmtsDTO image = imageList.get(position);


        Glide.with(holder.itemView.getContext())
                .load(image.getAnhCmts())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_binhluan);
        }
    }
}

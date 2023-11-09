package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
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

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<ItemImageDTO> imageList;
    private Context context;

    public ImageAdapter(List<ItemImageDTO> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ItemImageDTO itemImage = imageList.get(position);
//        String base64Image = itemImage.getImage();

        Glide.with(context).load(itemImage.getImage()).centerCrop().into(holder.imageView);

//        // Giải mã chuỗi Base64 thành mảng byte
//        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
//
//        // Chuyển đổi mảng byte thành Bitmap
//        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//
//        // Hiển thị Bitmap lên ImageView trong item_image.xml
//        holder.imageView.setImageBitmap(decodedBitmap);

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivImage);
        }
    }
}
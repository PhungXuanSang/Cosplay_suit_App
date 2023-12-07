package com.example.cosplay_suit_app.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.MainActivity;
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
        Log.d("DetailProductActivity", "Binding image at position: " + position);
//        Glide.with(context).load(itemImage.getImage()).centerCrop().into(holder.imageView);

        if (itemImage.getImage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(itemImage.getImage())
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            // Xử lý khi imageDTO hoặc imageDTO.getImage() là null
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có muốn xóa ảnh này không?");

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int adapterPosition = holder.getAdapterPosition();

                        if (adapterPosition != RecyclerView.NO_POSITION) {

                            imageList.remove(adapterPosition);

                            notifyItemRemoved(adapterPosition);

                            Toast.makeText(context, "Ảnh đã được xóa", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Hủy xóa ảnh này", Toast.LENGTH_SHORT).show();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });
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
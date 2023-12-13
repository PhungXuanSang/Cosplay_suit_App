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
import com.example.cosplay_suit_app.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<ItemImageDTO> imageList;
    private Context context;
    ItemImageDTO itemImage;
    private AlertDialog.Builder builderImage;
    private AlertDialog deleteConfirmationDialog;

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
        itemImage = imageList.get(position);
        Log.d("DetailProductActivity", "Binding image at position: " + position);

        if (itemImage.getImage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(itemImage.getImage())
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            // Xử lý khi itemImage hoặc itemImage.getImage() là null
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            private AlertDialog alertDialog; // Tham chiếu đến AlertDialog

            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view1 = inflater.inflate(R.layout.dialog_image, null);

                // Tạo AlertDialog.Builder và thiết lập các thuộc tính
                builderImage = new AlertDialog.Builder(context);
                builderImage.setView(view1);

                // Thiết lập các thành phần trong layout tùy chỉnh
                ImageView ivImage = view1.findViewById(R.id.ivdialogImage);
                ImageView ivDelete = view1.findViewById(R.id.ivImageDetele);
                ImageView ivImageBack = view1.findViewById(R.id.ivImageBack);
                int adapterPosition = holder.getAdapterPosition();

                Glide.with(context).load(imageList.get(adapterPosition).getImage()).centerCrop().into(ivImage);

                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Xác nhận xóa");
                        builder.setMessage("Bạn có muốn xóa ảnh này không?");

                        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (adapterPosition != RecyclerView.NO_POSITION) {
                                    imageList.remove(adapterPosition);
                                    notifyItemRemoved(adapterPosition);
                                    Toast.makeText(context, "Ảnh đã được xóa", Toast.LENGTH_SHORT).show();

                                    // Đóng cả hai AlertDialog
                                    alertDialog.dismiss();
                                    dialog.dismiss();
                                }
                            }
                        });

                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        // Tạo AlertDialog từ builder
                        deleteConfirmationDialog = builder.create();
                        deleteConfirmationDialog.show();
                    }
                });

                ivImageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Đóng AlertDialog khi nút back được bấm
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });

                // Hiển thị AlertDialog
                alertDialog = builderImage.create();
                alertDialog.show();
            }
        });

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

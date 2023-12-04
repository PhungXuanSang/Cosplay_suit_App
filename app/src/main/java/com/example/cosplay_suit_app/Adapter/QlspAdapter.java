package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.Activity.DetailProductActivity;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class QlspAdapter extends RecyclerView.Adapter<QlspAdapter.ItemViewHolder> {
    private List<DTO_SanPham> mlist;
    private Context context;
    private Onclick onclick;

    public QlspAdapter(List<DTO_SanPham> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_qlsp, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DTO_SanPham sanPham = mlist.get(position);

        if (sanPham.getSize() == null) {
            holder.tvName.setText(sanPham.getNameproduct());
        } else {
            holder.tvName.setText(sanPham.getNameproduct() + "--" + sanPham.getSize());
        }
        holder.tvAmount.setText(String.valueOf(sanPham.getAmount()));
        holder.tvPrice.setText(String.valueOf(sanPham.getPrice()));

        if (sanPham.getListImage() != null && !sanPham.getListImage().isEmpty()) {
            ItemImageDTO firstImage = sanPham.getListImage().get(0);
            String imageUrl = firstImage.getImage();

            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(holder.img_AnhSp);
        }

        holder.lllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_click_animation);
                holder.itemView.startAnimation(animation);
                onclick.onClickItem(sanPham);
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("idProduct", sanPham.getId());
                context.startActivity(intent);
            }
        });
    }

    public void clearlistProduct(){
            int itemCount = mlist.size();
mlist.clear();
notifyItemRangeRemoved(0,itemCount);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice, tvAmount, tvName;
        ImageView img_AnhSp;
        LinearLayout lllayout;

        public ItemViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvListQlspName);
            tvPrice = view.findViewById(R.id.tvListQlspPrice);
            img_AnhSp = view.findViewById(R.id.ivListQlspImage);
            tvAmount = view.findViewById(R.id.tvListQlspAmount);
            lllayout = view.findViewById(R.id.itemlistproduct);
        }
    }
    public interface Onclick {
        void onClickItem(DTO_SanPham dtoSanPham);
    }

}
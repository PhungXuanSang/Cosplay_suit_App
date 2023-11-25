package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class QlspAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<DTO_SanPham> mlist;
    Context context;

    public QlspAdapter(List<DTO_SanPham> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_qlsp, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DTO_SanPham sanPham = mlist.get(position);


        ItemViewHolder viewHolder = (ItemViewHolder) holder;

        if (sanPham.getSize() == null){
            viewHolder.tvName.setText(sanPham.getNameproduct());
        }else {
            viewHolder.tvName.setText(sanPham.getNameproduct() +"--"+ sanPham.getSize());
        }
        viewHolder.tvAmount.setText(String.valueOf(sanPham.getAmount()));
        viewHolder.tvPrice.setText(sanPham.getPrice()+"");

        if (sanPham.getListImage() != null && !sanPham.getListImage().isEmpty()) {
            ItemImageDTO firstImage = sanPham.getListImage().get(0);
            String imageUrl = firstImage.getImage();

            // Tiến hành tải và hiển thị ảnh từ URL bằng Glide
            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(viewHolder.img_AnhSp);
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice, tvAmount, tvName;
        ImageView img_AnhSp;


        public ItemViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvListQlspName);
            tvPrice = view.findViewById(R.id.tvListQlspPrice);
            img_AnhSp = view.findViewById(R.id.ivListQlspImage);
            tvAmount = view.findViewById(R.id.tvListQlspAmount);

        }

    }
}

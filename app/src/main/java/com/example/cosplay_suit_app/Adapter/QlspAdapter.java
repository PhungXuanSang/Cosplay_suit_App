package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    Onclick onclickStatus;

    public QlspAdapter(List<DTO_SanPham> mlist, Context context, Onclick onclickStatus) {
        this.mlist = mlist;
        this.context = context;
        this.onclickStatus = onclickStatus;
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

        if (sanPham.getSize() == null) {
            viewHolder.tvName.setText(sanPham.getNameproduct());
        } else {
            viewHolder.tvName.setText(sanPham.getNameproduct() + "--" + sanPham.getSize());
        }
        viewHolder.tvAmount.setText(String.valueOf(sanPham.getAmount()));
        viewHolder.tvPrice.setText(sanPham.getPrice() + "");

        if (sanPham.getListImage() != null && !sanPham.getListImage().isEmpty()) {
            ItemImageDTO firstImage = sanPham.getListImage().get(0);
            String imageUrl = firstImage.getImage();

            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(viewHolder.img_AnhSp);
        }

        viewHolder.tvHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.llButtom.setVisibility(View.GONE);
            }
        });


        if (sanPham.isStatus()) {
            viewHolder.tvStatus.setText("Ẩn");
        } else {
            viewHolder.tvStatus.setText("Hiện");
        }

        viewHolder.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sanPham.isStatus()) {
                    viewHolder.tvStatus.setText("Hiện");
                    sanPham.setStatus(false);
                    Toast.makeText(context, "Sản phẩm '"+sanPham.getNameproduct()+"' đã bị ẩn đi.", Toast.LENGTH_SHORT).show();
                } else {
                    viewHolder.tvStatus.setText("Ẩn");
                    Toast.makeText(context, "Sản phẩm '"+sanPham.getNameproduct()+"' đã được hiển thị.", Toast.LENGTH_SHORT).show();
                    sanPham.setStatus(true);
                }

                viewHolder.llButtom.setVisibility(View.GONE);
                onclickStatus.status(sanPham, sanPham.getId());
            }
        });
    }
    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice, tvAmount, tvName, tvStatus, tvHuy;
        ImageView img_AnhSp;
        LinearLayout lllayout, llButtom;

        public ItemViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvListQlspName);
            tvPrice = view.findViewById(R.id.tvListQlspPrice);
            img_AnhSp = view.findViewById(R.id.ivListQlspImage);
            tvAmount = view.findViewById(R.id.tvListQlspAmount);
            tvStatus = view.findViewById(R.id.tvStutus);
            tvHuy = view.findViewById(R.id.tvHuy);
            lllayout = view.findViewById(R.id.itemlistproduct);
            llButtom = view.findViewById(R.id.llButtom);
        }
    }

    public interface Onclick {
        void status(DTO_SanPham dtoSanPham, String idproduct);
    }

}

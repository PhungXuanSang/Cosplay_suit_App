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
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_billdetail;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.Package_bill.Adapter.Adapter_Bill;
import com.example.cosplay_suit_app.R;

import java.text.DecimalFormat;
import java.util.List;

public class Adapter_chitietbill extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<BillDetailDTO> list;

    public Adapter_chitietbill(Context context, List<BillDetailDTO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbuynow, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BillDetailDTO billDetailDTO = list.get(position);
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        if (billDetailDTO.getDtoSanPham().getListImage() != null && !billDetailDTO.getDtoSanPham().getListImage().isEmpty()) {
            ItemImageDTO firstImage = billDetailDTO.getDtoSanPham().getListImage().get(0);
            String imageUrl = firstImage.getImage();

            // Tiến hành tải và hiển thị ảnh từ URL bằng Glide
            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(viewHolder.img_product);
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvname_product.setText(billDetailDTO.getDtoSanPham().getNameproduct());
        viewHolder.tv_soluong.setText("Số lượng: " + billDetailDTO.getAmount());
        viewHolder.tvsize_product.setText("Kích thước: " + billDetailDTO.getSize());
        viewHolder.tvprice_product.setText("Giá: " + decimalFormat.format(billDetailDTO.getTotalPayment()) + " VND");
        viewHolder.tv_thanhtien.setText("Thành tiền: "+decimalFormat.format((billDetailDTO.getTotalPayment()) * (billDetailDTO.getAmount())) + " VND");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView img_product;
        TextView tvname_product,tvsize_product, tv_soluong, tvprice_product, tv_thanhtien;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            tvname_product = itemView.findViewById(R.id.tvname_product);
            tvsize_product = itemView.findViewById(R.id.tvsize_product);
            tv_soluong = itemView.findViewById(R.id.tv_soluong);
            tvprice_product = itemView.findViewById(R.id.tvprice_product);
            tv_thanhtien = itemView.findViewById(R.id.tv_thanhtien);
        }
    }
}

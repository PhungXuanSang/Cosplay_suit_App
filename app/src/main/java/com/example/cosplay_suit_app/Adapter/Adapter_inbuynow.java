package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.DTO_inbuynow;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
public class Adapter_inbuynow extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    List<DTO_inbuynow> list;
    Context context;
    String TAG = "adaptercartorder";

    public Adapter_inbuynow(List<DTO_inbuynow> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbuynow, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        DTO_inbuynow order = list.get(position);

        if (order != null) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            if (order.getDtoSanPham().getListImage() != null && !order.getDtoSanPham().getListImage().isEmpty()) {
                ItemImageDTO firstImage = order.getDtoSanPham().getListImage().get(0);
                String imageUrl = firstImage.getImage();

                // Tiến hành tải và hiển thị ảnh từ URL bằng Glide
                Glide.with(context)
                        .load(imageUrl)
                        .error(R.drawable.image)
                        .placeholder(R.drawable.image)
                        .centerCrop()
                        .into(viewHolder.imgproduct);
            }
            viewHolder.tvnamepro.setText(order.getDtoSanPham().getNameproduct());
            viewHolder.tvsize.setText("Size: "+order.getDtoProperties().getNameproperties());
            viewHolder.tvprice.setText(decimalFormat.format(order.getDtoSanPham().getPrice()) + " VND");
            viewHolder.tvsoluong.setText("Số lượng: "+order.getAmount());

            int thanhtien = order.getAmount() * order.getDtoSanPham().getPrice();
            viewHolder.tv_thanhtien.setText("Thành tiền: " + decimalFormat.format(thanhtien) + " VND");
        } else {
            Log.d("Adapter_ShopCartOrder", "No orders found for shop with ID: ");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgproduct;
        TextView tvnamepro, tvsize, tvprice, tvsoluong, tv_thanhtien;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.img_product);
            tvnamepro = itemView.findViewById(R.id.tvname_product);
            tvprice = itemView.findViewById(R.id.tvprice_product);
            tvsize = itemView.findViewById(R.id.tvsize_product);
            tvsoluong = itemView.findViewById(R.id.tv_soluong);
            tv_thanhtien = itemView.findViewById(R.id.tv_thanhtien);
        }
    }
}

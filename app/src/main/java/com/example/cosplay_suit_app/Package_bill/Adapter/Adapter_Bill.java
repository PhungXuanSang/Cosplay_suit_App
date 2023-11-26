package com.example.cosplay_suit_app.Package_bill.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapter_Bill extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = "adapterbill";
    List<BillDetailDTO> list;
    Context context;

    public Adapter_Bill(List<BillDetailDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BillDetailDTO billDetailDTO = list.get(position);
        Adapter_Bill.ItemViewHolder holder1 = (ItemViewHolder) holder;
        if (billDetailDTO.getDtoSanPham().getListImage() != null && !billDetailDTO.getDtoSanPham().getListImage().isEmpty()) {
            ItemImageDTO firstImage = billDetailDTO.getDtoSanPham().getListImage().get(0);
            String imageUrl = firstImage.getImage();

            // Tiến hành tải và hiển thị ảnh từ URL bằng Glide
            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .centerCrop()
                    .into(holder1.imgproduct);
        }
        holder1.tvnamepro.setText(billDetailDTO.getDtoSanPham().getNameproduct());
        holder1.tvprice.setText(""+billDetailDTO.getDtoSanPham().getPrice());
        holder1.tvtongbill.setText(""+billDetailDTO.getDtoBill().getTotalPayment());
        holder1.tv_nameshop.setText(billDetailDTO.getDtoBill().getShop().getNameshop());
        if (billDetailDTO.getDtoBill().getStatus().equals("Wait")){
            holder1.tv_trangthai.setText("Đang xử lý");
        } else if (billDetailDTO.getDtoBill().getStatus().equals("Pack")) {
            holder1.tv_trangthai.setText("Đang đóng gói");
        } else if (billDetailDTO.getDtoBill().getStatus().equals("Delivery")) {
            holder1.tv_trangthai.setText("Đang giao");
        }
        if (billDetailDTO.getDtoBill().getStatus().equals("Done")){
            holder1.btnmualai.setVisibility(View.VISIBLE);
        }else {
            holder1.btnmualai.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgproduct;
        TextView tvnamepro, tvsize, tvprice, tvtongbill, tv_nameshop, tv_trangthai;
        Button btnmualai;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.img_product);
            tvnamepro = itemView.findViewById(R.id.tvname_product);
            tvprice = itemView.findViewById(R.id.tvprice_product);
            tvsize = itemView.findViewById(R.id.tvsize_product);
            tvtongbill = itemView.findViewById(R.id.tv_tongbill);
            btnmualai = itemView.findViewById(R.id.btn_mualai);
            tv_nameshop = itemView.findViewById(R.id.tv_nameshop);
            tv_trangthai = itemView.findViewById(R.id.tv_trangthai);
        }
    }
}

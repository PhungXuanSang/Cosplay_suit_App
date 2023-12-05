package com.example.cosplay_suit_app.Package_bill.Adapter;

import static com.example.cosplay_suit_app.Activity.BuynowActivity.formatDateTime;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.Activity.Chitietbill_Activity;
import com.example.cosplay_suit_app.Activity.ShowShopActivity;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Adapter_Bill extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = "adapterbill";
    List<BillDetailDTO> list;
    Context context;
    String checkactivity="", checkstatus ="", stringstatus ="";


    public Adapter_Bill(List<BillDetailDTO> list, Context context, String checkactivity, String checkstatus) {
        this.list = list;
        this.context = context;
        this.checkactivity = checkactivity;
        this.checkstatus = checkstatus;
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
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
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
        holder1.tv_nameshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bill_controller billController = new Bill_controller(context);
                billController.callApiProduct(billDetailDTO.getDtoBill().getShop().getId(), new Bill_controller.Apicheckshop() {
                    @Override
                    public void onApigetshop(List<DTO_SanPham> profileDTO) {
                        String soluongSPShop = String.valueOf(profileDTO.size());
                        Intent intent = new Intent(context, ShowShopActivity.class);
                        intent.putExtra("id_shop", billDetailDTO.getDtoBill().getShop().getId());
                        intent.putExtra("name_shop", billDetailDTO.getDtoBill().getShop().getNameshop());
                        intent.putExtra("slsp_shop", soluongSPShop);
                        intent.putExtra("id_user", billDetailDTO.getDtoBill().getShop().getId_user());
                        context.startActivity(intent);
                    }
                });
            }
        });
        holder1.tvnamepro.setText(billDetailDTO.getDtoSanPham().getNameproduct());
        holder1.tvprice.setText(""+decimalFormat.format(billDetailDTO.getDtoSanPham().getPrice()) + " VND");
        holder1.tvtongbill.setText(""+decimalFormat.format(billDetailDTO.getDtoBill().getTotalPayment()) + " VND");
        holder1.tv_nameshop.setText(billDetailDTO.getDtoBill().getShop().getNameshop());
        Bill_controller billController = new Bill_controller(context);
        //Check xem là bên nào sử dụng adapter shop hay khách hàng
        if ("shop".equals(checkactivity)){
            holder1.btn_upstatus.setVisibility(View.VISIBLE);
            holder1.btnmualai.setVisibility(View.VISIBLE);
            holder1.btnmualai.setText("Hủy đơn hàng");
            DTO_Bill dtoBill = new DTO_Bill();
            // Lấy đối tượng Date hiện tại
            String currentDateTime = getCurrentDateTime();
            if ("Wait".equals(checkstatus)){
                billDetailDTO.getDtoBill().setStatus("Pack");
                dtoBill.setStatus("Pack");
            } else if ("Pack".equals(checkstatus)) {
                dtoBill.setStatus("Delivery");
            }else if ("Delivery".equals(checkstatus)) {
                dtoBill.setStatus("Done");
                dtoBill.setTimeend(currentDateTime);
            }
            holder1.btn_upstatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    billController.UpdateStatusBill(billDetailDTO.getDtoBill().get_id(),dtoBill);
                }
            });
            holder1.btnmualai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dtoBill.setStatus("Cancelled");
                    dtoBill.setTimeend(currentDateTime);
                    billController.UpdateStatusBill(billDetailDTO.getDtoBill().get_id(),dtoBill);
                }
            });
            if (billDetailDTO.getDtoBill().getStatus().equals("Wait")){
                holder1.btn_upstatus.setText(" Xác nhận có hàng ");
                holder1.tv_trangthai.setText("Đang xử lý");
            } else if (billDetailDTO.getDtoBill().getStatus().equals("Pack")) {
                holder1.btn_upstatus.setText(" Xác nhận Vận chuyển ");
                holder1.tv_trangthai.setText("Đang đóng gói");
            } else if (billDetailDTO.getDtoBill().getStatus().equals("Delivery")) {
                holder1.btn_upstatus.setText(" Giao hoàn tất ");
                holder1.tv_trangthai.setText("Đang giao");
            }else {
                holder1.btn_upstatus.setVisibility(View.GONE);
            }
        }else {
            if (billDetailDTO.getDtoBill().getStatus().equals("Done")){
                holder1.btnmualai.setVisibility(View.VISIBLE);
            }else {
                holder1.btnmualai.setVisibility(View.GONE);
            }
            holder1.btn_upstatus.setVisibility(View.GONE);

            if (billDetailDTO.getDtoBill().getStatus().equals("Wait")){
                holder1.btn_upstatus.setText(" Xác nhận có hàng ");
                holder1.tv_trangthai.setText("Đang xử lý");
            } else if (billDetailDTO.getDtoBill().getStatus().equals("Pack")) {
                holder1.btn_upstatus.setText(" Xác nhận Vận chuyển ");
                holder1.tv_trangthai.setText("Đang đóng gói");
            } else if (billDetailDTO.getDtoBill().getStatus().equals("Delivery")) {
                holder1.btn_upstatus.setText(" Giao hoàn tất ");
                holder1.tv_trangthai.setText("Đang giao");
            }else {
                holder1.btn_upstatus.setVisibility(View.GONE);
            }
        }
        if (billDetailDTO.getDtoBill().getStatus().equals("Wait")){
            stringstatus = "Wait";
        } else if (billDetailDTO.getDtoBill().getStatus().equals("Pack")) {
            stringstatus = "Pack";
        } else if (billDetailDTO.getDtoBill().getStatus().equals("Delivery")) {
            stringstatus = "Delivery";
        }else if (billDetailDTO.getDtoBill().getStatus().equals("Done")){
            stringstatus = "Done";
        }else if (billDetailDTO.getDtoBill().getStatus().equals("Cancelled")){
            stringstatus = "Cancelled";
        }else if (billDetailDTO.getDtoBill().getStatus().equals("Returns")){
            stringstatus = "Returns";
        }
        holder1.xemchitietbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Chitietbill_Activity.class);
                intent.putExtra("idbill", billDetailDTO.getDtoBill().get_id());
                intent.putExtra("tongbill", billDetailDTO.getDtoBill().getTotalPayment());
                intent.putExtra("stringstatus",stringstatus);
                intent.putExtra("checkactivity", checkactivity);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgproduct;
        TextView tvnamepro, tvsize, tvprice, tvtongbill, tv_nameshop, tv_trangthai, xemchitietbill;
        Button btnmualai, btn_upstatus;
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
            btn_upstatus = itemView.findViewById(R.id.btn_upstatus);
            xemchitietbill = itemView.findViewById(R.id.xemchitietbill);
        }
    }
    private String getCurrentDateTime() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(currentDate);
    }
}

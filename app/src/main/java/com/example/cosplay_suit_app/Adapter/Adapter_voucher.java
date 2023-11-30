package com.example.cosplay_suit_app.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_inbuynow;
import com.example.cosplay_suit_app.DTO.DTO_voucher;
import com.example.cosplay_suit_app.DTO.Favorite;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.Interface_retrofit.VoucherInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adapter_voucher extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<DTO_voucher> mlist;
    Context context;
    String id;
    String idshop;
    VoucherInterface voucherInterface;
    static String url = API.URL;
    static final String BASE_URL = url + "/Voucher/";

    public Adapter_voucher(List<DTO_voucher> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DTO_voucher voucher = mlist.get(position);
        Adapter_voucher.ItemViewHolder viewHolder = (Adapter_voucher.ItemViewHolder) holder;
         voucher.getId();
        DTO_voucher dtoVoucher = new DTO_voucher();
            viewHolder.amount.setText("Số lượng: "+voucher.getAmount());
            viewHolder.content.setText(voucher.getContent());
            viewHolder.discount.setText("Giảm giá: "+voucher.getDiscount()+"%");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click to show edit and delete buttons
                ((ItemViewHolder) holder).edit.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).delete.setVisibility(View.VISIBLE);
            }
        });

        ((ItemViewHolder) holder).edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate layout từ dialogsua_voucher.xml
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialogsua_voucher, null);

                // Khởi tạo các thành phần trong dialog
                EditText edContent = dialogView.findViewById(R.id.ed_content);
                EditText edAmount = dialogView.findViewById(R.id.ed_amount);
                EditText edDiscount = dialogView.findViewById(R.id.ed_Discount);
                AppCompatButton btnSua = dialogView.findViewById(R.id.btn_sua);
                AppCompatButton btnHuy = dialogView.findViewById(R.id.btn_huy);

                // Tạo AlertDialog.Builder với layout đã inflate
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                builder.setTitle("Chỉnh sửa Voucher");

                // Tạo AlertDialog
                AlertDialog dialog = builder.create();

                dtoVoucher.setId(voucher.getId());
                dtoVoucher.setId_shop(voucher.getId_shop());
                // Set sự kiện cho nút "Sửa"
                btnSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lấy giá trị từ các trường nhập liệu
                        String content = edContent.getText().toString();
                        String amount = edAmount.getText().toString();
                        String discount = edDiscount.getText().toString();

                        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(amount) && !TextUtils.isEmpty(discount)) {
                            // Gọi phương thức để cập nhật voucher
                            dtoVoucher.setAmount(amount);
                            dtoVoucher.setContent(content);
                            dtoVoucher.setDiscount(discount);
                            callUpdateProduct(dtoVoucher);

                            // Đóng hộp thoại sau khi "Sửa"
                            dialog.dismiss();
                        } else {
                            // Hiển thị thông báo nếu có trường nào đó trống
                            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Set sự kiện cho nút "Hủy"
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Đóng hộp thoại khi "Hủy"
                        dialog.dismiss();
                    }
                });

                // Hiển thị AlertDialog
                dialog.show();
            }
        });
        ((ItemViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callDeleteProduct(voucher.getId());
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView amount,content, discount;
        ImageView edit, delete;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
          amount=itemView.findViewById(R.id.amount);
          content = itemView.findViewById(R.id.content);
          discount = itemView.findViewById(R.id.discount);
            edit = itemView.findViewById(R.id.editButton);
            delete = itemView.findViewById(R.id.deleteButton);
        }
    }
    public void callUpdateProduct(DTO_voucher dtoVoucher) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        voucherInterface = retrofit.create(VoucherInterface.class);
        Call<DTO_voucher> objCall = voucherInterface.updateVoucherByShop(dtoVoucher.getId(),dtoVoucher);

        objCall.enqueue(new Callback<DTO_voucher>() {
            @Override
            public void onResponse(@NonNull Call<DTO_voucher> call, Response<DTO_voucher> response) {
                // Xử lý sau khi cập nhật thành công
                Log.d("cc", "onResponse: "+response.message());
            }
            @Override
            public void onFailure(@NonNull Call<DTO_voucher> call, Throwable t) {
                Log.d("cc", "onFailure: "+t.getLocalizedMessage());
            }
        });
    }
    public void callDeleteProduct(String id) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        voucherInterface = retrofit.create(VoucherInterface.class);
        Call<DTO_voucher> objCall = voucherInterface.deleteVoucherByShop(id);

        objCall.enqueue(new Callback<DTO_voucher>() {
            @Override
            public void onResponse(@NonNull Call<DTO_voucher> call, Response<DTO_voucher> response) {
                // Xử lý sau khi xóa thành công
                Log.d("cc", "onResponse: "+response.message());
            }

            @Override
            public void onFailure(@NonNull Call<DTO_voucher> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra
                Log.d("cc", "onFailure: "+t.getLocalizedMessage());
            }
        });
    }

}
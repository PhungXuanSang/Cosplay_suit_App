package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.CartShopManager;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.DTO.TotalPriceManager;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
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

public class AdapterCartorder extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    List<CartOrderDTO> list;
    Context context;
    int tonggia = 0, soluong = 1;
    String TAG = "adaptercartorder";
    OnclickCheck onclickCheck;
    String idcart, idshop;
    List<DTO_properties> listproper;

    public AdapterCartorder(List<CartOrderDTO> list, Context context, OnclickCheck onclickCheck) {
        this.list = list;
        this.context = context;
        this.onclickCheck = onclickCheck;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cartorder, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        CartOrderDTO order = list.get(position);
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
            viewHolder.tvsize.setText("Size: "+order.getId_properties());
            viewHolder.tvprice.setText(decimalFormat.format(order.getDtoSanPham().getPrice()) + " VND");
            viewHolder.tvsoluong.setText(""+order.getAmount());
            viewHolder.imgcong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soluong = Integer.parseInt(viewHolder.tvsoluong.getText().toString().trim()) + 1;
                    if (soluong < 101){
                        String slmoi = String.valueOf(soluong);
                        viewHolder.tvsoluong.setText(slmoi);
                        if (viewHolder.cbkcart.isChecked()){
                            tonggia = order.getDtoSanPham().getPrice();
                            TotalPriceManager.getInstance().updateTotalPriceTrue(tonggia);
                            onclickCheck.onCheckboxTrue();
                        }
                        int gia = (order.getDtoSanPham().getPrice()) * soluong;
                        CartOrderDTO dto = new CartOrderDTO();
                        dto.setAmount(soluong);
                        dto.setTotalPayment(gia);
                        updatedata(dto, order.get_id());
                    }
                }
            });
            viewHolder.imgtru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soluong = Integer.parseInt(viewHolder.tvsoluong.getText().toString().trim()) - 1;
                    if (soluong > 0){
                        String slmoi = String.valueOf(soluong);
                        viewHolder.tvsoluong.setText(slmoi);
                        if (viewHolder.cbkcart.isChecked()){
                            tonggia = order.getDtoSanPham().getPrice();
                            TotalPriceManager.getInstance().updateTotalPriceFalse(tonggia);
                            onclickCheck.onCheckboxTrue();
                        }
                        int gia = (order.getDtoSanPham().getPrice()) * soluong;
                        CartOrderDTO dto = new CartOrderDTO();
                        dto.setAmount(soluong);
                        dto.setTotalPayment(gia);
                        updatedata(dto, order.get_id());
                    }
                }
            });
            viewHolder.cbkcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolder.cbkcart.isChecked()) {
                        idcart = order.get_id();
                        idshop = order.getDtoSanPham().getId_shop();
                        tonggia = (order.getDtoSanPham().getPrice()) * Integer.parseInt(viewHolder.tvsoluong.getText().toString().trim());
                        TotalPriceManager.getInstance().updateTotalPriceTrue(tonggia);
                        TotalPriceManager.getInstance().updateIdcartTrue(idcart);
                        onclickCheck.onCheckboxTrue();
                        CartShopManager.getInstance().addCartToShop(order.getDtoSanPham().getId_shop(),order.get_id());
                    } else {
                        idcart = order.get_id();
                        idshop = order.getDtoSanPham().getId_shop();
                        tonggia = (order.getDtoSanPham().getPrice()) * Integer.parseInt(viewHolder.tvsoluong.getText().toString().trim());
                        TotalPriceManager.getInstance().updateTotalPriceFalse(tonggia);
                        TotalPriceManager.getInstance().updateIdcartFalse(idcart);
                        CartShopManager.getInstance().removeCartFromShop(order.getDtoSanPham().getId_shop(),order.get_id());
                        onclickCheck.onCheckboxFalse();
                    }

                }
            });
            viewHolder.tv_xoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclickCheck.onClickXoa(order.get_id());
                }
            });

            listproper = new ArrayList<>();
            listproper = list.get(position).getDtoSanPham().getListProp();

            // Tạo ArrayAdapter cho Spinner sử dụng danh sách thuộc tính từ DTO_SanPham
            ArrayAdapter<DTO_properties> propertyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listproper);
            propertyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Liên kết ArrayAdapter với Spinner
            viewHolder.spinnerproper.setAdapter(propertyAdapter);
            // Xử lý sự kiện khi một mục được chọn trong Spinner
            viewHolder.spinnerproper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id) {
                    // Lấy đối tượng DTO_properties được chọn
                    DTO_properties selectedProperty = (DTO_properties) parentView.getItemAtPosition(pos);
                    viewHolder.tvsize.setText("Size: " + selectedProperty.getNameproperties());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Xử lý khi không có mục nào được chọn
                }
            });
        } else {
            Log.d("Adapter_ShopCartOrder", "No orders found for shop with ID: ");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgproduct, imgcong, imgtru;
        TextView tvnamepro, tvsize, tvprice, tv_xoa;
        Spinner spinnerproper;
        CheckBox cbkcart;
        TextView tvsoluong;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.img_product);
            tvnamepro = itemView.findViewById(R.id.tvname_product);
            tvprice = itemView.findViewById(R.id.tvprice_product);
            tvsize = itemView.findViewById(R.id.tvsize_product);
            imgtru = itemView.findViewById(R.id.imgtru);
            imgcong = itemView.findViewById(R.id.imgcong);
            tvsoluong = itemView.findViewById(R.id.tv_soluong);
            cbkcart = itemView.findViewById(R.id.cbk_cart);
            tv_xoa = itemView.findViewById(R.id.tv_xoa);
            spinnerproper = itemView.findViewById(R.id.spinnerproper);
        }
    }
    public interface OnclickCheck{
        void onCheckboxTrue();
        void onCheckboxFalse();
        void onClickXoa(String idcart);
    }
    public void updatedata(CartOrderDTO dto, String idcart){
        //Tạo đối tượng chuyển đổi kiểu dữ liệu
        Gson gson = new GsonBuilder().setLenient().create();
        //Tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Khởi  tạo interface

        CartOrderInterface cartOrderInterface = retrofit.create(CartOrderInterface.class);


        // Tạo Call
        Call<CartOrderDTO> objCall = cartOrderInterface.updatecart(idcart, dto);
        // Thực hiện gửi dữ liệu lên server
        objCall.enqueue(new Callback<CartOrderDTO>() {
            @Override
            public void onResponse(Call<CartOrderDTO> call, Response<CartOrderDTO> response) {
                // kết quả server trả về ở đây

                if (response.isSuccessful()) {
                    // lấy kết quả trả về

                } else {
                    Log.e(TAG, response.message());
                }
            }
            @Override
            public void onFailure(Call<CartOrderDTO> call, Throwable t) {
                // nếu xảy ra lỗi sẽ thông báo ở đây

                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }
}

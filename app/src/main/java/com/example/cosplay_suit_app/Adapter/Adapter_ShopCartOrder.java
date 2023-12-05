package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.Chitietsanpham;
import com.example.cosplay_suit_app.Activity.ShowShopActivity;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.ShopCartorderDTO;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adapter_ShopCartOrder extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    List<ShopCartorderDTO> list;
    Context context;
    private Map<String, List<CartOrderDTO>> orderMap; // Map lưu trữ danh sách đơn hàng theo idshop
    private List<CartOrderDTO> allOrders;
    AdapterCartorder arrayAdapter;
    String TAG = "adaptershopcartorder";
    CheckBox cbkcart;
    public Adapter_ShopCartOrder(List<ShopCartorderDTO> list, Context context) {
        this.list = list;
        this.context = context;
        orderMap = new HashMap<>();
        allOrders = new ArrayList<>();
        // Lấy danh sách đơn hàng của tất cả người dùng
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        getOrdersByUserId(id, new Callback<List<CartOrderDTO>>() {
            @Override
            public void onResponse(Call<List<CartOrderDTO>> call, Response<List<CartOrderDTO>> response) {
                if (response.isSuccessful()) {
                    allOrders = response.body();
                    for (CartOrderDTO order : allOrders) {
                        handleOrders(order.getDtoSanPham().getId_shop(), order);
                    }
                    notifyDataSetChanged();
                } else {
                    // Xử lý lỗi (nếu có)
                    Log.e("Adapter_ShopCartOrder", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CartOrderDTO>> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                Log.e("Adapter_ShopCartOrder", "Error: " + t.getMessage());
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopcartorder, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShopCartorderDTO shop = list.get(position);
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.tvnameshop.setText(shop.getName_shop());
        viewHolder.tvnameshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bill_controller billController = new Bill_controller(context);
                billController.callApiProduct(shop.getId(), new Bill_controller.Apicheckshop() {
                    @Override
                    public void onApigetshop(List<DTO_SanPham> profileDTO) {
                        String soluongSPShop = String.valueOf(profileDTO.size());
                        Intent intent = new Intent(context, ShowShopActivity.class);
                        intent.putExtra("id_shop", shop.getId());
                        intent.putExtra("name_shop", shop.getName_shop());
                        intent.putExtra("slsp_shop", soluongSPShop);
                        intent.putExtra("id_user", shop.getId_user());
                        context.startActivity(intent);
                    }
                });
            }
        });

        List<CartOrderDTO> ordersForShop = orderMap.get(shop.getId());
        arrayAdapter = new AdapterCartorder(ordersForShop, context, (AdapterCartorder.OnclickCheck) context
                , new AdapterCartorder.OnclickCheckbox() {
            @Override
            public void onUpdateParentCheckbox(boolean allChecked) {
                // Cập nhật trạng thái của checkbox cha dựa trên trạng thái của checkbox con
                updateParentCheckboxStatus(allChecked);
            }
        });
        viewHolder.rcvcart.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        cbkcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cbkcart.isChecked();
                // Cập nhật trạng thái của CheckBox trong Adapter cha
                ordersForShop.get(position).setChecked(isChecked);
                // Gọi method để cập nhật tất cả các CheckBox trong Adapter con
                updateAllChildCheckBoxes(isChecked, ordersForShop.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvnameshop;
        RecyclerView rcvcart;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnameshop = itemView.findViewById(R.id.tv_nameshop);
            rcvcart = itemView.findViewById(R.id.rcv_cart);
            cbkcart = itemView.findViewById(R.id.cbkcart);
        }
    }
    public void getOrdersByUserId(String userId, Callback<List<CartOrderDTO>> callback) {
        // Tạo một OkHttpClient với interceptor để ghi log (nếu cần)
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        // Tạo Gson ConverterFactory để chuyển đổi JSON thành Java objects
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        // Tạo một API service sử dụng Retrofit
        CartOrderInterface service = retrofit.create(CartOrderInterface.class);

        // Gọi API để lấy danh sách đơn hàng dựa trên userId
        Call<List<CartOrderDTO>> call = service.getusercartorder(userId);
        call.enqueue(callback);
    }

    private void handleOrders(String idShop, CartOrderDTO order) {
        if (orderMap.containsKey(idShop)) {
            orderMap.get(idShop).add(order);
        } else {
            List<CartOrderDTO> orders = new ArrayList<>();
            orders.add(order);
            orderMap.put(idShop, orders);
        }
    }
    public void updateAllChildCheckBoxes(boolean isChecked, CartOrderDTO ordera) {
        for (Map.Entry<String, List<CartOrderDTO>> entry : orderMap.entrySet()) {
            for (CartOrderDTO order : entry.getValue()) {
                order.setChecked(isChecked);
                // Thông báo cho Adapter con khi CheckBox cha không được chọn nữa và truyền order
                if (!isChecked) {
                    arrayAdapter.onParentCheckboxUnchecked(order);
                }
            }
        }
        notifyDataSetChanged();
    }
    // Cập nhật trạng thái của checkbox cha dựa trên trạng thái của checkbox con
    private void updateParentCheckboxStatus(boolean allChecked) {
        cbkcart.setChecked(allChecked);
    }
}

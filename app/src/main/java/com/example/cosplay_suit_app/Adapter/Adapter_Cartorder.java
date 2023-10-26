package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
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

public class Adapter_Cartorder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    List<CartOrderDTO> list;
    Context context;
    ArrayList<String> arrayList = new ArrayList<>();
    OnclickCheck onclickCheck;
    int tonggia = 0, soluong = 1;
    String TAG = "adaptercartorder";

    public Adapter_Cartorder(List<CartOrderDTO> list, Context context, OnclickCheck onclickCheck) {
        this.list = list;
        this.context = context;
        this.onclickCheck = onclickCheck;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cartorder, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartOrderDTO dtoCartOrder = list.get(position);

        Adapter_Cartorder.ItemViewHolder viewHolder = (Adapter_Cartorder.ItemViewHolder) holder;

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Glide.with(context).load(dtoCartOrder.getDtoSanPham().getImage()).centerCrop().into(viewHolder.imgproduct);
        viewHolder.tvnamepro.setText(dtoCartOrder.getDtoSanPham().getNameproduct());
        viewHolder.tvsize.setText("Size: "+dtoCartOrder.getDtoProperties().getNameproperties());
        viewHolder.tvprice.setText(decimalFormat.format(dtoCartOrder.getDtoSanPham().getPrice()) + " VND");
        viewHolder.tvsoluong.setText(""+dtoCartOrder.getAmount());
        viewHolder.imgcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soluong = Integer.parseInt(viewHolder.tvsoluong.getText().toString().trim()) + 1;
                if (soluong < 101){
                    String slmoi = String.valueOf(soluong);
                    viewHolder.tvsoluong.setText(slmoi);
                    if (viewHolder.cbkcart.isChecked()){
                        tonggia = tonggia + dtoCartOrder.getDtoSanPham().getPrice();
                        onclickCheck.onCheckboxTrue(tonggia);
                    }
//                    CartOrderDTO dto = new CartOrderDTO();
//                    dto.setAmount(soluong);
//                    dto.setTotalPayment(tonggia);
//                    updatedata(dto, dtoCartOrder.getId_cart());
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
                        tonggia = tonggia - dtoCartOrder.getDtoSanPham().getPrice();
                        onclickCheck.onCheckboxTrue(tonggia);
                    }
//                    CartOrderDTO dto = new CartOrderDTO();
//                    dto.setAmount(soluong);
//                    dto.setTotalPayment(tonggia);
//                    updatedata(dto, dtoCartOrder.getId_cart());
                }
            }
        });
        viewHolder.cbkcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.cbkcart.isChecked()){
                    arrayList.add(dtoCartOrder.getId_cart());
                    tonggia += (dtoCartOrder.getDtoSanPham().getPrice())
                            * Integer.parseInt(viewHolder.tvsoluong.getText().toString().trim());
                    onclickCheck.onCheckboxTrue(tonggia);
                }else {
                    tonggia -= (dtoCartOrder.getDtoSanPham().getPrice())
                            * Integer.parseInt(viewHolder.tvsoluong.getText().toString().trim());
                    onclickCheck.onCheckboxFalse(tonggia);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgproduct, imgcong, imgtru;
        TextView tvnamepro, tvsize, tvprice, tvsoluong, tvtonggia;
        CheckBox cbkcart;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.img_product);
            tvnamepro = itemView.findViewById(R.id.tvname_product);
            tvprice = itemView.findViewById(R.id.tvprice_product);
            tvsize = itemView.findViewById(R.id.tvsize_product);
            imgtru = itemView.findViewById(R.id.imgtru);
            imgcong = itemView.findViewById(R.id.imgcong);
            tvsoluong = itemView.findViewById(R.id.tv_soluong);
            tvtonggia = itemView.findViewById(R.id.tv_tonggia);
            cbkcart = itemView.findViewById(R.id.cbk_cart);
        }
    }

    public interface OnclickCheck{
        void onCheckboxTrue(int tongtien);
        void onCheckboxFalse(int tongtien);

    }

//    public void updatedata(CartOrderDTO dto, String idcart){
//        //Tạo đối tượng chuyển đổi kiểu dữ liệu
//        Gson gson = new GsonBuilder().setLenient().create();
//        //Tạo Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        // Khởi  tạo interface
//
//        CartOrderInterface cartOrderInterface = retrofit.create(CartOrderInterface.class);
//
//
//        // Tạo Call
//        Call<CartOrderDTO> objCall = cartOrderInterface.updatecart(idcart, dto);
//        // Thực hiện gửi dữ liệu lên server
//        objCall.enqueue(new Callback<CartOrderDTO>() {
//            @Override
//            public void onResponse(Call<CartOrderDTO> call, Response<CartOrderDTO> response) {
//                // kết quả server trả về ở đây
//
//                if (response.isSuccessful()) {
//                    // lấy kết quả trả về
//
//                } else {
//                    Log.e(TAG, response.message());
//                }
//            }
//            @Override
//            public void onFailure(Call<CartOrderDTO> call, Throwable t) {
//                // nếu xảy ra lỗi sẽ thông báo ở đây
//
//                Log.e(TAG, t.getLocalizedMessage());
//            }
//        });
//    }

}

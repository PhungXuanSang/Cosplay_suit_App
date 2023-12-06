package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.SeenVoucherActivity;
import com.example.cosplay_suit_app.DTO.DTO_SeenVoucher;
import com.example.cosplay_suit_app.DTO.DTO_SeenVoucher_Check;
import com.example.cosplay_suit_app.DTO.DTO_voucher;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.DTO.ShopCartorderDTO;
import com.example.cosplay_suit_app.Interface_retrofit.VoucherInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterKhachHang extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<ProfileDTO> arrayList;
    Context context;

    static String url = API.URL;
    static final String BASE_URL = url + "/Voucher/";
    OnlickCheckVoucher onlickCheck;
    private String id_voucher;

    List<DTO_SeenVoucher> dto_seenVoucherList = new ArrayList<>();

    // Constructor nhận dữ liệu từ Activity

    public AdapterKhachHang(ArrayList<ProfileDTO> arrayList, Context context, OnlickCheckVoucher onlickCheck,String id_voucher) {
        this.arrayList = arrayList;
        this.context = context;
        this.onlickCheck = onlickCheck;
        this.id_voucher = id_voucher;
    }
    public void updateCheckBoxes(boolean isChecked) {
        for (ProfileDTO profileDTO : arrayList) {
            profileDTO.setCheck(isChecked);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khachhang, parent, false);
        return new ItemViewHoldel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProfileDTO user =  arrayList.get(position);

        ItemViewHoldel viewHoldel = (ItemViewHoldel) holder;
        viewHoldel.tv_fullname.setText(user.getFullname());
        viewHoldel.tv_phone.setText(user.getPhone());
        ///manh
        Gson gson = new GsonBuilder().setLenient().create();

        // Create a new object from HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        VoucherInterface voucherInterface = retrofit.create(VoucherInterface.class);

        // tạo đối tượng
        Call<List<DTO_SeenVoucher>> objCall = voucherInterface.getlistSeen(id_voucher);
        objCall.enqueue(new Callback<List<DTO_SeenVoucher>>() {
            @Override
            public void onResponse(Call<List<DTO_SeenVoucher>> call, Response<List<DTO_SeenVoucher>> response) {

                if (response.isSuccessful()) {
                    dto_seenVoucherList = response.body();
                    for(int i = 0;i<dto_seenVoucherList.size();i++){
                        Log.e("manh", "onBindViewHolder: " + dto_seenVoucherList.get(i).getId_user() );
                        if (user.getId().equalsIgnoreCase(dto_seenVoucherList.get(i).getId_user())){
                            viewHoldel.box.setEnabled(false);

                        }
                    }
                } else {
                    Log.e("manh", "onResponse: " + response.message() );
                }
            }
            @Override
            public void onFailure(Call<List<DTO_SeenVoucher>> call, Throwable t) {
                Log.e("manh", "onFailure: " + t.getLocalizedMessage() );
            }
        });
        viewHoldel.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewHoldel.box.isChecked()){
                    onlickCheck.Check_ID(user.getId());
                }else {
                    onlickCheck.Check_Delete_ID(user.getId());
                }
            }
        });
        viewHoldel.box.setChecked(user.isCheck());
        if(user.isCheck() == true){
            if (viewHoldel.box.isEnabled() == true){
                onlickCheck.Check_ID(user.getId());
                Log.e("manh", "onBindViewHolder 1: " + user.isCheck() );
            }

        }
        if(user.isCheck() == false){
            onlickCheck.Check_Delete_ID(user.getId());
            Log.e("manh", "onBindViewHolder 2: " + user.isCheck() );
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemViewHoldel extends RecyclerView.ViewHolder{
        TextView tv_fullname;
        TextView tv_phone;
        CheckBox box;
        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            tv_fullname = itemView.findViewById(R.id.tv_fullname);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            box = itemView.findViewById(R.id.id_checkbox);
        }
    }

    public interface OnlickCheckVoucher{
        void Check_ID(String id_user);
        void Check_Delete_ID(String id_user);
    }

    void callSeenById_voucher(String id_voucher){
        Gson gson = new GsonBuilder().setLenient().create();

        // Create a new object from HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        VoucherInterface voucherInterface = retrofit.create(VoucherInterface.class);

        // tạo đối tượng
        Call<List<DTO_SeenVoucher>> objCall = voucherInterface.getlistSeen(id_voucher);
        objCall.enqueue(new Callback<List<DTO_SeenVoucher>>() {
            @Override
            public void onResponse(Call<List<DTO_SeenVoucher>> call, Response<List<DTO_SeenVoucher>> response) {

                if (response.isSuccessful()) {
                    dto_seenVoucherList = response.body();
                    for(int i = 0;i<dto_seenVoucherList.size();i++){
                        Log.e("manh", "onBindViewHolder: " + dto_seenVoucherList.get(i).getId_user() );
                    }
                } else {
                    Log.e("manh", "onResponse: " + response.message() );
                }
            }
            @Override
            public void onFailure(Call<List<DTO_SeenVoucher>> call, Throwable t) {
                Log.e("manh", "onFailure: " + t.getLocalizedMessage() );
            }
        });
    }
}

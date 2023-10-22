package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.Chitietsanpham;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.Favorite;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adapter_SanPham extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static String url = API.URL;
    static final String BASE_URL = url + "/user/api/";
    private List<Favorite> favoriteList = new ArrayList<>();
    boolean isMyFavorite = false;
    String id;

    private List<DTO_SanPham> mlist;
    Context context;

    public Adapter_SanPham( Context context) {

        this.context = context;
    }
    public void updateData(List<DTO_SanPham> mlist){
        this.mlist = mlist;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DTO_SanPham sanPham = mlist.get(position);

        Adapter_SanPham.ItemViewHolder viewHolder = (Adapter_SanPham.ItemViewHolder) holder;
        viewHolder.tv_nameSanPham.setText(sanPham.getNameproduct());
        Glide.with(context).load(sanPham.getImage()).centerCrop().into(viewHolder.img_AnhSp);
        viewHolder.ll_chitietsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Chitietsanpham.class);
                intent.putExtra("id_product", sanPham.getId());
                intent.putExtra("name", sanPham.getNameproduct());
                intent.putExtra("price", sanPham.getPrice());
                intent.putExtra("image", sanPham.getImage());
                intent.putExtra("about", sanPham.getDescription());
                intent.putExtra("slkho", sanPham.getAmount());
                intent.putExtra("id_shop",sanPham.getId_shop());
                intent.putExtra("time_product",sanPham.getTime_product());
                intent.putExtra("id_category",sanPham.getId_category());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameSanPham;
        ImageView img_AnhSp, img_favorite;
        FrameLayout ll_chitietsp;


        public ItemViewHolder(View view) {
            super(view);

            tv_nameSanPham = view.findViewById(R.id.tv_nameSp);
            img_AnhSp = view.findViewById(R.id.anh_sp);
            ll_chitietsp = view.findViewById(R.id.id_chitietsp);

        }

    }


    void addFavorite(Context context,Favorite favorite){
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        SanPhamInterface truyenInterface = retrofit.create(SanPhamInterface.class);
        Call<Favorite> objT = truyenInterface.add_favorite(favorite);

        objT.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Added to your favorites list...", Toast.LENGTH_SHORT).show();
//                    context.startActivity(new Intent(context, MainActivity.class));
//                    GetListSanPham();
                    updateData(mlist);
                }
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
                Log.e("bl", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
    void removeFavorite(Context context, String tb_user,String tb_product){
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        SanPhamInterface truyenInterface = retrofit.create(SanPhamInterface.class);
        Call<Void> objT = truyenInterface.delete_favorite(tb_user,tb_product);

        objT.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Removed to your favorites list...", Toast.LENGTH_SHORT).show();
//                    context.startActivity(new Intent(context, MainActivity.class));
//                    GetListSanPham();
                    updateData(mlist);
                }else{
                    Log.e("bl", "Remove: " + response.message() );
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("bl", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    public void addToFavorite(Context context,String idProduct){
        if (id.equalsIgnoreCase("")){
            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
        }else{
            long timestamp = System.currentTimeMillis();

            HashMap<String , Object> hashMap = new HashMap<>();
            hashMap.put("idProduct",idProduct);
            hashMap.put("timeStamp",timestamp);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(id).child("Favorites").child(idProduct)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Added to your favorites list...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "failed to add to favorite due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void removeToFavorite(Context context,String idProduct ){
        if (id.equalsIgnoreCase("")){
            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(id).child("Favorites").child(idProduct)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Removed to your favorites list...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "failed to remove to favorite due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}

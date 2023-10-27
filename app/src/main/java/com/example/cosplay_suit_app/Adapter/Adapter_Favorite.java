package com.example.cosplay_suit_app.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.LoginActivity;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adapter_Favorite extends RecyclerView.Adapter<Adapter_Favorite.HolderFavorite>{
    static String url = API.URL;
    static final String BASE_URL = url +"/product/";
    String id;
    private Context context;

    ProgressDialog dialog;
    private ArrayList<DTO_SanPham> list;
    public Adapter_Favorite(Context context, ArrayList<DTO_SanPham> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public HolderFavorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pro_favorite,parent,false);
        return new HolderFavorite(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFavorite holder, int position) {
        DTO_SanPham sanPham = list.get(position);
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");

        loadPro(sanPham,holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeToFavorite(context,sanPham.getId());
            }
        });
    }

    private void loadPro(DTO_SanPham sanPham, HolderFavorite holder) {
        String proID = sanPham.getId();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create( gson))
                .build();
        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);

        Call<DTO_SanPham> objCall = sanPhamInterface.getbyid(proID);
        objCall.enqueue(new Callback<DTO_SanPham>() {
            @Override
            public void onResponse(Call<DTO_SanPham> call, Response<DTO_SanPham> response) {
                if (response.isSuccessful()){
                    DTO_SanPham dto_sanPham = response.body();
                    holder.img_remove_favorite.setImageResource(R.drawable.favorite_24);
                    holder.pro_name.setText(dto_sanPham.getNameproduct());
                    holder.pro_description.setText(dto_sanPham.getDescription());
                    holder.pro_time_product.setText(dto_sanPham.getTime_product());
                    Glide.with(context).load(dto_sanPham.getImage()).centerCrop().into(holder.img_pro);
                }

            }

            @Override
            public void onFailure(Call<DTO_SanPham> call, Throwable t) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderFavorite extends RecyclerView.ViewHolder{
        ImageView img_pro,img_remove_favorite;

        TextView pro_name,pro_description,pro_time_product;
        public HolderFavorite(@NonNull View itemView) {
            super(itemView);
            img_pro = itemView.findViewById(R.id.pro_img);
            img_remove_favorite = itemView.findViewById(R.id.img_remove_favorite);
            pro_name = itemView.findViewById(R.id.pro_name);
            pro_description = itemView.findViewById(R.id.pro_description);
            pro_time_product = itemView.findViewById(R.id.pro_time_product);
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

    public void showDialog(Context context){
        dialog=new ProgressDialog(context);
        dialog.setMessage("Please wait!");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }
}

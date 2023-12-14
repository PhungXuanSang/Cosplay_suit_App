package com.example.cosplay_suit_app.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.DetailProductActivity;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PropAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DTO_properties> mlist;
    Context context;
    private boolean isClickable = false;
    static String url = API.URL;
    static final String BASE_URL = url + "/product/";

    Onclick onclick;

    public PropAdapter(List<DTO_properties> mlist, Context context, Onclick onclick) {
        this.mlist = mlist;
        this.context = context;
        this.onclick = onclick;
    }
    public void setClickable(boolean clickable) {
        this.isClickable = clickable;
        notifyDataSetChanged();
    }

    public PropAdapter(List<DTO_properties> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_size, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DTO_properties propertiesDTO = mlist.get(position);

        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.llSize.setAlpha(0.5f);
        viewHolder.ivDel.setAlpha(0.5f);
        viewHolder.tvName.setText(propertiesDTO.getNameproperties());
        viewHolder.tvAmount.setText(propertiesDTO.getAmount() + "");
        viewHolder.llSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClickable==true){
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_click_animation);
                    viewHolder.llSize.startAnimation(animation);
                    viewHolder.llSize.setAlpha(1.0f);
                    onclick.onclikSize(propertiesDTO);
//                UpdateSize(propertiesDTO);
                }else {
                    viewHolder.llSize.setAlpha(0.5f);
                }

            }
        });

        viewHolder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClickable==true){
                    onclick.onclikXoa(propertiesDTO);

//                UpdateSize(propertiesDTO);
                }else {

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAmount;

        ImageView ivDel;
        LinearLayout llSize;

        public ItemViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvsizeSize);
            tvAmount = view.findViewById(R.id.tvsizeMount);
            ivDel = view.findViewById(R.id.ivSizedelete);
            llSize = view.findViewById(R.id.llSize);
        }

    }


    public interface  Onclick{
        void onclikSize(DTO_properties dtoProperties);
        void onclikXoa(DTO_properties dtoProperties);
    }


}

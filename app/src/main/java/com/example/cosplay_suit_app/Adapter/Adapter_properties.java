package com.example.cosplay_suit_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class Adapter_properties extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<DTO_properties> list;
    Context context;
    private String selectedNameProperties;

    public String getSelectedNameProperties() {
        return selectedNameProperties;
    }

    public Adapter_properties(List<DTO_properties> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_properties, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DTO_properties dtoProperties = list.get(position);
        Adapter_properties.ItemViewHolder viewHolder = (Adapter_properties.ItemViewHolder) holder;
        viewHolder.btn_size.setText(dtoProperties.getNameproperties());
        viewHolder.btn_size.setText(dtoProperties.getNameproperties());

        viewHolder.btn_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lưu giá trị nameproperties vào biến selectedNameProperties
                selectedNameProperties = dtoProperties.getNameproperties();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        Button btn_size;
        int originalButtonColor;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_size = itemView.findViewById(R.id.btn_size);
            originalButtonColor = btn_size.getCurrentTextColor(); // Lưu màu sắc ban đầu
        }
    }

}

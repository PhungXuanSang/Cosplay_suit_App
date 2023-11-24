package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class PropAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DTO_properties> mlist;
    Context context;

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

        viewHolder.tvName.setText(propertiesDTO.getNameproperties());
        viewHolder.tvAmount.setText(propertiesDTO.getAmount()+"");


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAmount;

        ImageView ivDel;

        public ItemViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvsizeSize);
            tvAmount = view.findViewById(R.id.tvsizeMount);
            ivDel = view.findViewById(R.id.ivSizedelete);
        }

    }
}

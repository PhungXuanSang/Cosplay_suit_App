package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.R;

import java.util.ArrayList;

public class AdapterKhachHang extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<ProfileDTO> arrayList;
    Context context;

    OnlickCheckVoucher onlickCheck;

    public AdapterKhachHang(ArrayList<ProfileDTO> arrayList, Context context, OnlickCheckVoucher onlickCheck) {
        this.arrayList = arrayList;
        this.context = context;
        this.onlickCheck = onlickCheck;
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
        viewHoldel.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewHoldel.box.isChecked()){
                    onlickCheck.Check_ID(user.getId());
                }else{
                    onlickCheck.Check_Delete_ID(user.getId());
                }

            }
        });
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
}

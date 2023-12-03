package com.example.cosplay_suit_app.Package_bill.donhang;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.Package_bill.Adapter.Adapter_Bill;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;

import java.util.ArrayList;
import java.util.List;

public class Fragment_dahuy extends Fragment {
    String TAG = "Danhgiaactivity";
    List<BillDetailDTO> list;
    Adapter_Bill arrayAdapter;
    RecyclerView recyclerView;
    Context context;
    String checkactivity, checkstatus = "Cancelled";
    LinearLayout noProductMessage;

    public Fragment_dahuy(String checkactivity) {
        this.checkactivity = checkactivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_dahuy, container, false);
        recyclerView = viewok.findViewById(R.id.rcv_danhgia);
        //danh sách sản phẩm
        list = new ArrayList<>();
        arrayAdapter = new Adapter_Bill(list, getContext(), checkactivity, checkstatus);
        recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        if (id != null && !id.isEmpty()) {
            Bill_controller billController = new Bill_controller(context);
            billController.GetUserBillCancelled(id, list, arrayAdapter, "Cancelled",checkactivity, recyclerView, noProductMessage);
        } else {
            Toast.makeText(context, "Lỗi id không tồn tại", Toast.LENGTH_SHORT).show();
        }
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

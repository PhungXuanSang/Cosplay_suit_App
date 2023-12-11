package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.cosplay_suit_app.Adapter.Adapter_vouchercuatoi;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.GetVoucher_DTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.example.cosplay_suit_app.bill.controller.Voucher_controller;

import java.util.ArrayList;
import java.util.List;

public class VouchercuatoiActivity extends AppCompatActivity {
    List<GetVoucher_DTO> list;
    Adapter_vouchercuatoi adapterVouchercuatoi;
    RecyclerView recyclerView;
    Voucher_controller voucherController;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchercuatoi);
        Anhxa();
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        id = sharedPreferences.getString("id","");

        list = new ArrayList<>();
        adapterVouchercuatoi = new Adapter_vouchercuatoi(list, VouchercuatoiActivity.this);
        voucherController = new Voucher_controller(VouchercuatoiActivity.this);
        recyclerView.setAdapter(adapterVouchercuatoi);
        voucherController.Getlistseenvoucheruser(id, new Voucher_controller.Apiseenvoucheruser() {
            @Override
            public void onApiseenvoucheruserl(List<GetVoucher_DTO> voucherDtos) {
                list.clear();
                if (voucherDtos != null && !voucherDtos.isEmpty()) {
                    for (GetVoucher_DTO billDetail : voucherDtos) {
                        list.add(billDetail);
                    }
                    adapterVouchercuatoi.notifyDataSetChanged();
                }
            }
        });
    }
    public void Anhxa(){
        recyclerView = findViewById(R.id.rcv_voucherme);
    }
}
package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.cosplay_suit_app.Adapter.AdapterCartorder;
import com.example.cosplay_suit_app.Adapter.AdapterKhachHang;
import com.example.cosplay_suit_app.Adapter.Adapter_dskhach;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;

import java.util.ArrayList;
import java.util.List;

public class SeenVoucherActivity extends AppCompatActivity implements AdapterKhachHang.OnlickCheckVoucher{
    ImageView id_back;
    RecyclerView recyclerView;
    ArrayList<ProfileDTO> list;

    AdapterKhachHang adapterKhachHang;
    List<String> stringList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_voucher);
        Anhxa();
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        list = new ArrayList<>();
        adapterKhachHang = new AdapterKhachHang(list,SeenVoucherActivity.this,this);
        recyclerView.setAdapter(adapterKhachHang);
        adapterKhachHang.notifyDataSetChanged();

        Bill_controller billController = new Bill_controller(this);
        billController.GetdskhachVoucher(id,list, adapterKhachHang);

    }

    public void Anhxa(){
//        id_back = findViewById(R.id.id_back);
        recyclerView = findViewById(R.id.id_recyclerGui);
    }

    @Override
    public void Check_ID(String id_user) {
        stringList.add(id_user);
    }

    @Override
    public void Check_Delete_ID(String id_user) {
        stringList.remove(id_user);
    }

}
package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cosplay_suit_app.Adapter.Adapter_buynow;
import com.example.cosplay_suit_app.Adapter.Adapter_dskhach;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;

import java.util.ArrayList;

public class Dskhach_Activity extends AppCompatActivity {
    ImageView id_back;
    RecyclerView recyclerView;
    ArrayList<ProfileDTO> list;
    Adapter_dskhach arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dskhach);
        Anhxa();
        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        list = new ArrayList<>();
        arrayAdapter = new Adapter_dskhach(list,Dskhach_Activity.this);
        recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        Bill_controller billController = new Bill_controller(this);
        billController.Getdskhach(id,list, arrayAdapter);



    }
    public void Anhxa(){
        id_back = findViewById(R.id.id_back);
        recyclerView = findViewById(R.id.rcv_dskhach);
    }
}
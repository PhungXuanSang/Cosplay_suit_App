package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.cosplay_suit_app.Adapter.Adapter_XemAllspdamua;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;

import java.util.ArrayList;
import java.util.List;

public class XemAllspdamuaActivity extends AppCompatActivity {
    RecyclerView rcv_Allmualai;
    ImageView id_back;
    List<BillDetailDTO> list;
    Adapter_XemAllspdamua adapterXemAllspdamua;
    LinearLayout noProductMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_allspdamua);
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
        adapterXemAllspdamua = new Adapter_XemAllspdamua(list, this);
        rcv_Allmualai.setAdapter(adapterXemAllspdamua);
        Bill_controller billController = new Bill_controller(this);
        billController.GetAllmualaisp(id, list, adapterXemAllspdamua, rcv_Allmualai, noProductMessage);
        if (list.isEmpty()) {
            noProductMessage.setVisibility(LinearLayout.VISIBLE);
            rcv_Allmualai.setVisibility(ListView.GONE);
        } else {
            noProductMessage.setVisibility(LinearLayout.GONE);
            rcv_Allmualai.setVisibility(ListView.VISIBLE);
        }
    }

    private void Anhxa() {
        rcv_Allmualai = findViewById(R.id.rcv_Allmualai);
        id_back = findViewById(R.id.id_back);
        noProductMessage = findViewById(R.id.noProductMessage);
    }
}
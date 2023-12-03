package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cosplay_suit_app.Adapter.Adapter_chitietbill;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Chitietbill_Activity extends AppCompatActivity {
    String idbill ="",stringstatus="",checkactivity="";
    Bill_controller billController;
    List<BillDetailDTO> list;
    Adapter_chitietbill chitietbill;
    RecyclerView recyclerView;
    TextView tvstatusbill,idsotienthanhtoan;
    ImageView imgdonhang, id_back;
    Button btnmualai;
    int tongbill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietbill);
        Anhxa();
        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        idbill = intent.getStringExtra("idbill");
        stringstatus = intent.getStringExtra("stringstatus");
        tongbill = intent.getIntExtra("tongbill", 0);
        checkactivity = intent.getStringExtra("checkactivity");

        list = new ArrayList<>();
        chitietbill = new Adapter_chitietbill(this, list);
        recyclerView.setAdapter(chitietbill);

        billController = new Bill_controller(this);
        billController.GetIdbilldetail(idbill, list, chitietbill);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        idsotienthanhtoan.setText("Tổng tiền: "+decimalFormat.format(tongbill) + " VND");

        if ("shop".equals(checkactivity)){
            btnmualai.setVisibility(Button.GONE);
        } else if ("user".equals(checkactivity)) {
            if ("Wait".equals(stringstatus)){
                tvstatusbill.setText("Đơn hàng đang chờ xác nhận");
                imgdonhang.setImageResource(R.drawable.waitbill);
                btnmualai.setVisibility(Button.GONE);
            } else if ("Pack".equals(stringstatus)) {
                tvstatusbill.setText("Đơn hàng đang đóng gói");
                imgdonhang.setImageResource(R.drawable.packbill);
                btnmualai.setVisibility(Button.GONE);
            }else if ("Delivery".equals(stringstatus)) {
                tvstatusbill.setText("Đơn hàng vận chuyển");
                imgdonhang.setImageResource(R.drawable.deliverybill);
                btnmualai.setVisibility(Button.GONE);
            }else if ("Done".equals(stringstatus)) {
                btnmualai.setVisibility(Button.VISIBLE);
                tvstatusbill.setText("Đơn hàng đã hoàn thành");
                imgdonhang.setImageResource(R.drawable.donebill);
            }else if ("Cancelled".equals(stringstatus)) {
                btnmualai.setVisibility(Button.VISIBLE);
                tvstatusbill.setText("Đơn hàng đã hủy thành công");
                imgdonhang.setImageResource(R.drawable.billcancelled);
            }else if ("Returns".equals(stringstatus)) {
                btnmualai.setVisibility(Button.VISIBLE);
                tvstatusbill.setText("Đơn hàng đã được trả");
                imgdonhang.setImageResource(R.drawable.returnsbill);
            }
        }
    }

    private void Anhxa() {
        recyclerView = findViewById(R.id.rcv_cart);
        tvstatusbill = findViewById(R.id.tvstatusbill);
        imgdonhang = findViewById(R.id.imgdonhang);
        id_back = findViewById(R.id.id_back);
        idsotienthanhtoan = findViewById(R.id.idsotienthanhtoan);
        btnmualai = findViewById(R.id.mualai);
    }
}
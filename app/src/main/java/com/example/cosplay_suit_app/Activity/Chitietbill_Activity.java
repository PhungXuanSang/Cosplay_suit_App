package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cosplay_suit_app.Adapter.Adapter_chitietbill;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.Package_bill.DTO.BillDTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.example.cosplay_suit_app.bill.controller.Cart_controller;

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
    TextView tvstatusbill,idsotienthanhtoan, idchonphuongthuc,tv_thoigianhoanthanh,tv_thanhtoan,tv_thoigiandat, tv_hoten, tv_sdt, tv_diachi;
    ImageView imgdonhang, id_back;
    Button btnmualai;
    int tongbill;
    Cart_controller cartController = new Cart_controller(this);
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

        cartController.getidbill(idbill, new Cart_controller.Apigetidbill() {
            @Override
            public void onApigetidbill(BillDTO billDTO) {
                tv_thoigiandat.setText(billDTO.getTimestart());
                if (billDTO.getVnp_TxnRef().length() > 8){
                    Log.d("cd", "billDTO.getVnp_TxnRef(): " + billDTO.getVnp_TxnRef());
                    idchonphuongthuc.setText("Thanh toán khi nhận hàng");
                }else {
                    idchonphuongthuc.setText("Thanh toán chuyển khoản");
                }
                if (billDTO.getTimeend().equals("")){
                    tv_thoigianhoanthanh.setText("Đơn hàng chưa giao đến");
                }else {
                    Log.d("cd", "billDTO.getTimeend(): " + billDTO.getTimeend());
                    tv_thoigianhoanthanh.setText(billDTO.getTimeend());
                }
                tv_hoten.setText(billDTO.getAddress().getFullname());
                tv_diachi.setText(billDTO.getAddress().getAddress());
                tv_sdt.setText(billDTO.getAddress().getPhone());

            }
        });
    }

    private void Anhxa() {
        recyclerView = findViewById(R.id.rcv_cart);
        tvstatusbill = findViewById(R.id.tvstatusbill);
        imgdonhang = findViewById(R.id.imgdonhang);
        id_back = findViewById(R.id.id_back);
        idsotienthanhtoan = findViewById(R.id.idsotienthanhtoan);
        btnmualai = findViewById(R.id.mualai);
        idchonphuongthuc = findViewById(R.id.idchonphuongthuc);
        tv_thoigiandat = findViewById(R.id.tv_thoigiandat);
        tv_thanhtoan = findViewById(R.id.tv_thanhtoan);
        tv_thoigianhoanthanh = findViewById(R.id.tv_thoigianhoanthanh);
        tv_hoten = findViewById(R.id.tv_hoten);
        tv_sdt = findViewById(R.id.tv_sdt);
        tv_diachi = findViewById(R.id.tv_diachi);
    }
}
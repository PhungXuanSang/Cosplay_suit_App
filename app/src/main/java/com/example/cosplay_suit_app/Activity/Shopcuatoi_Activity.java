package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.cosplay_suit_app.Package_bill.donhang.Collection_adapter_bill;
import com.example.cosplay_suit_app.R;

public class Shopcuatoi_Activity extends AppCompatActivity {

    RelativeLayout rll_qlsp, rll_donhangcuatoi, rll_dskhach, rll_tke;
    ImageView id_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcuatoi);
        Anhxa();
        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rll_qlsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shopcuatoi_Activity.this, QlspActivity.class);
                startActivity(intent);
            }
        });
        rll_donhangcuatoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shopcuatoi_Activity.this, Collection_adapter_bill.class);
                intent.putExtra("checkactivity","shop");
                startActivity(intent);
            }
        });
        rll_dskhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shopcuatoi_Activity.this, Dskhach_Activity.class);
                startActivity(intent);
            }
        });
        rll_tke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shopcuatoi_Activity.this,ThongKeShopActivity.class);
                startActivity(intent);
            }
        });
    }
    public void Anhxa(){
        rll_donhangcuatoi = findViewById(R.id.rll_donhangcuatoi);
        rll_qlsp = findViewById(R.id.rll_qlsp);
        id_back = findViewById(R.id.id_back);
        rll_dskhach = findViewById(R.id.rll_dskhach);
        rll_tke = findViewById(R.id.rll_thongke);
    }
}
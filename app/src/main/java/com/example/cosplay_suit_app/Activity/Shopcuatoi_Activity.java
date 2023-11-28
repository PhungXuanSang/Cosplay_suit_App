package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.cosplay_suit_app.R;

public class Shopcuatoi_Activity extends AppCompatActivity {

    RelativeLayout rll_qlsp;
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
    }
    public void Anhxa(){
        rll_qlsp = findViewById(R.id.rll_qlsp);
        id_back = findViewById(R.id.id_back);
    }
}
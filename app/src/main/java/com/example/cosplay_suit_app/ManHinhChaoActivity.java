package com.example.cosplay_suit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class ManHinhChaoActivity extends AppCompatActivity {
    ConstraintLayout id_constraint;
    TextView tv_batdau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chao);
        id_constraint = findViewById(R.id.id_constraint);
        tv_batdau = findViewById(R.id.tv_batdau);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_batdau.setVisibility(View.VISIBLE);
                tv_batdau.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ManHinhChaoActivity.this, MainActivity.class));
                    }
                });
            }
        },2000);
    }
}
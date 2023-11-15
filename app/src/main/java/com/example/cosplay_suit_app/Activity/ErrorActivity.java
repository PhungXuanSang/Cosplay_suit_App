package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        findViewById(R.id.btn_vehome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ErrorActivity.this, MainActivity.class));
                finishAffinity();
            }
        });

        findViewById(R.id.tv_thuchienlai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ErrorActivity.this, ForgotPasswdActivity.class));
                finishAffinity();
            }
        });
    }
}
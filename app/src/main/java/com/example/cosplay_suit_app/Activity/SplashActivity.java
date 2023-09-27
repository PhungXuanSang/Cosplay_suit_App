package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        },2000);

    }

    private void nextActivity() {

        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finishAffinity();
    }
}
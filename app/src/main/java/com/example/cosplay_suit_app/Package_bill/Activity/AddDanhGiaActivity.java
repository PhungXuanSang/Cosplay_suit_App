package com.example.cosplay_suit_app.Package_bill.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.R;

public class AddDanhGiaActivity extends AppCompatActivity {

    RatingBar ratingBar;
    TextView tv_star;
    int danhgia;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_danh_gia);
        initView();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Xử lý sự kiện khi đánh giá thay đổi
                danhgia = (int) rating;
                if (danhgia==1){
                    tv_star.setText("Rất tệ");
                    tv_star.setTextColor(Color.parseColor("#000000"));
                } else if (danhgia==2) {
                    tv_star.setText("Tệ");
                    tv_star.setTextColor(Color.parseColor("#000000"));
                } else if (danhgia == 3) {
                    tv_star.setText("Bình thường");
                    tv_star.setTextColor(Color.parseColor("#000000"));
                } else if (danhgia == 4) {
                    tv_star.setText("Tốt");
                    tv_star.setTextColor(Color.parseColor("#F86739"));
                }else {
                    tv_star.setText("Tuyệt vời");
                    tv_star.setTextColor(Color.parseColor("#F86739"));
                }

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        ratingBar = findViewById(R.id.ratingBar);
        tv_star = findViewById(R.id.tv_star);
        img_back = findViewById(R.id.ic_back);
    }
}
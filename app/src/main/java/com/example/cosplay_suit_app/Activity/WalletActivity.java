package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cosplay_suit_app.DTO.DTO_Wallet;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Wallet_controller;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {
    ImageSlider imgShow;
    String id="";
    String username_u;
    TextView tv_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        AnhXa();
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        username_u = sharedPreferences.getString("fullname", "");
        id = sharedPreferences.getString("id", "");
        ArrayList<SlideModel> models = new ArrayList<>();
//        models.add(new SlideModel(R.drawable.bg4,null));
        models.add(new SlideModel(R.drawable.banner1,null));
        models.add(new SlideModel(R.drawable.banner2,null));
        models.add(new SlideModel(R.drawable.banner3,null));
        models.add(new SlideModel(R.drawable.banner4,null));
        models.add(new SlideModel(R.drawable.banner5,null));
        imgShow.setImageList(models, ScaleTypes.CENTER_CROP);
        getwallet();
    }
    public void AnhXa(){
        imgShow = findViewById(R.id.image_slider);
        tv_money = findViewById(R.id.tv_money);
    }
    public void  getwallet(){
        Wallet_controller walletController = new Wallet_controller(WalletActivity.this);
        walletController.getWallet(id, new Wallet_controller.ApiGetwalet() {
            @Override
            public void onApiGetwalet(DTO_Wallet getVoucherDto) {
                tv_money.setText(getVoucherDto.getMoney() + getVoucherDto.getCurrency());
            }
        });
    }
}
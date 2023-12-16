package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cosplay_suit_app.Adapter.VitrunggianPagerAdapter;
import com.example.cosplay_suit_app.DTO.DTO_Wallet;
import com.example.cosplay_suit_app.Package_bill.donhang.Collection_adapter_bill;
import com.example.cosplay_suit_app.Package_bill.donhang.PagerAdapter;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Wallet_controller;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {
    ImageSlider imgShow;
    String id="";
    String username_u;
    TextView tv_money;
    ViewPager2 viewPager2;
    // adapter
    VitrunggianPagerAdapter vitrunggianPagerAdapter;
    ImageView icback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        AnhXa();
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        username_u = sharedPreferences.getString("fullname", "");
        id = sharedPreferences.getString("id", "");
        ArrayList<SlideModel> models = new ArrayList<>();
        models.add(new SlideModel(R.drawable.banner1,null));
        models.add(new SlideModel(R.drawable.banner2,null));
        models.add(new SlideModel(R.drawable.banner3,null));
        models.add(new SlideModel(R.drawable.banner4,null));
        models.add(new SlideModel(R.drawable.banner5,null));
        imgShow.setImageList(models, ScaleTypes.CENTER_CROP);
        getwallet();

        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        vitrunggianPagerAdapter = new VitrunggianPagerAdapter(WalletActivity.this);
        viewPager2 = findViewById(R.id.pager2);
        viewPager2.setAdapter( vitrunggianPagerAdapter );

        /// làm việc với Tab
        TabLayout tabLayout01 = findViewById(R.id.idtablayout);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout01, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        // thiết lập tiêu đề tab
                        tab.setText( "Tab thứ ... " + position );
                        switch (position){
                            case 0:
                                tab.setText("Trạng thái hoàn thành");
                                break;
                            case 1:
                                tab.setText("Lịch sử");
                                break;
                        }
                    }
                });

        mediator.attach();
    }
    public void AnhXa(){
        imgShow = findViewById(R.id.image_slider);
        tv_money = findViewById(R.id.tv_money);
        icback = findViewById(R.id.id_back);
    }
    public void  getwallet(){
        Wallet_controller walletController = new Wallet_controller(WalletActivity.this);
        walletController.getWallet(id, new Wallet_controller.ApiGetwalet() {
            @Override
            public void onApiGetwalet(DTO_Wallet getVoucherDto) {
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                tv_money.setText(decimalFormat.format(Integer.parseInt(getVoucherDto.getMoney())) + getVoucherDto.getCurrency());
            }
        });
    }
}
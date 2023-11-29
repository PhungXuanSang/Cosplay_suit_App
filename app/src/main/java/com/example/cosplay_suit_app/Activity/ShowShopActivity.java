package com.example.cosplay_suit_app.Activity;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cosplay_suit_app.Adapter.Adapter_Shop;
import com.example.cosplay_suit_app.Fragments.Fragment_chat;
import com.example.cosplay_suit_app.Fragments.Fragment_donhang;
import com.example.cosplay_suit_app.Fragments.Fragment_profile;
import com.example.cosplay_suit_app.Fragments.Fragment_trangchu;
import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ShowShopActivity extends AppCompatActivity {
    ImageView imgMenu,imgBack;

   TabLayout tabLayout;
    ViewPager viewPager;
    Adapter_Shop adapter;

    TextView tv_name_shop,tv_slSP_Shop;

    String id_shop,name_shop,slsp_shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop);
        Intent intent = getIntent();
        id_shop = intent.getStringExtra("id_shop");
        name_shop = intent.getStringExtra("name_shop");
        slsp_shop = intent.getStringExtra("slsp_shop");
        anhxa();
        registerForContextMenu(imgMenu);

        adapter = new Adapter_Shop(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ShowShopActivity.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.shop_menu, popupMenu.getMenu());

                // Đăng ký lắng nghe sự kiện khi một item trong menu được chọn
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.id_item_chiase){
                            Toast.makeText(ShowShopActivity.this, "Mạnh đẹp zai", Toast.LENGTH_SHORT).show();
                        } else if (id == R.id.id_item_trangchu) {

                        }
                        return true;

                    }
                });

                // Hiển thị PopupMenu
                popupMenu.show();
            }
        });

        tv_name_shop.setText(name_shop);
        tv_slSP_Shop.setText(slsp_shop);

    }

    void anhxa() {

        imgMenu = findViewById(R.id.id_img_menu_shop);
        tv_name_shop = findViewById(R.id.tv_name_shop);
        tv_slSP_Shop = findViewById(R.id.tv_slSP_Shop);
        tabLayout = findViewById(R.id.id_tablLayout);
        viewPager = findViewById(R.id.id_viewPager2);
    }

}
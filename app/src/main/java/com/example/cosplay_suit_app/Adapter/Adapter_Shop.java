package com.example.cosplay_suit_app.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cosplay_suit_app.Fragments.Fragment_Shop_ChitietShop;
import com.example.cosplay_suit_app.Fragments.Fragment_Shop_SanPham;
import com.example.cosplay_suit_app.Fragments.Fragment_Shop_Shop;

public class Adapter_Shop extends FragmentStatePagerAdapter {


    public Adapter_Shop(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case  0 :
                return  new Fragment_Shop_Shop();
            case  1 :
                return  new Fragment_Shop_SanPham();
            case  2 :
                return  new Fragment_Shop_ChitietShop();
            default:
                return  new Fragment_Shop_Shop();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case  0 :
                title = "Shop";
                break;
            case 1 :
                title = "Sản phẩm";
                break;
            case 2 :
                title = "Chi tiết shop";
                break;
        }
        return title;
    }
}
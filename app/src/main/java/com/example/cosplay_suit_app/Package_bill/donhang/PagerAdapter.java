package com.example.cosplay_suit_app.Package_bill.donhang;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {

    int soluongfragment = 6;
    String checkactivity;


    public PagerAdapter(@NonNull FragmentActivity fragmentActivity, String checkactivity) {
        super(fragmentActivity);
        this.checkactivity = checkactivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Fragment_xacnhan(checkactivity); // đặt cho Frag01 hiển thị đầu tiên
            case 1:
                return new Fragment_layhang(checkactivity);
            case 2:
                return new Fragment_danggiao(checkactivity);
            case 3:
                return new Fragment_dagiao(checkactivity);
            case 4:
                return new Fragment_dahuy(checkactivity);
            case 5:
                return new Fragment_trahang(checkactivity);
            default:
                return new Fragment_xacnhan(checkactivity);
        }
    }

    @Override
    public int getItemCount() {
        return soluongfragment;
    }
}

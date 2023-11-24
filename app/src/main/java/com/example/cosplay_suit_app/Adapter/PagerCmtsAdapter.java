package com.example.cosplay_suit_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cosplay_suit_app.Package_bill.Activity.Danhgia_Activity;
import com.example.cosplay_suit_app.Package_bill.donhang.Fragment_ChuaDanhGia;
import com.example.cosplay_suit_app.Package_bill.donhang.Fragment_DaDanhGia;

public class PagerCmtsAdapter extends FragmentStateAdapter {

    int soluongPage = 2;
    public PagerCmtsAdapter(@NonNull Danhgia_Activity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Fragment_ChuaDanhGia();
            case 1:
                return new Fragment_DaDanhGia();
            default:
                return new Fragment_ChuaDanhGia();
        }

    }

    @Override
    public int getItemCount() {
        return soluongPage;
    }
}

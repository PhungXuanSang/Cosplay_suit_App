package com.example.cosplay_suit_app.Package_bill;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {

    int soluongfragment = 6;

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Fragment_xacnhan(); // đặt cho Frag01 hiển thị đầu tiên
            case 1:
                return new Fragment_layhang();
            case 2:
                return new Fragment_danggiao();
            case 3:
                return new Fragment_dagiao();
            case 4:
                return new Fragment_dahuy();
            case 5:
                return new Fragment_trahang();
            default:
                if(position % 2 >0)
                    return new Fragment_dagiao();
                else if(position % 2 >0)
                    return new Fragment_danggiao();
                else if(position % 2 >0)
                    return new Fragment_xacnhan();
                else if(position % 2 >0)
                    return new Fragment_layhang();
                else if(position % 2 >0)
                    return new Fragment_trahang();
                else
                    return new Fragment_xacnhan();
        }
    }

    @Override
    public int getItemCount() {
        return soluongfragment;
    }
}

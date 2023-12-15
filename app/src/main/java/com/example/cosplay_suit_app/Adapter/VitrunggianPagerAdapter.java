package com.example.cosplay_suit_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cosplay_suit_app.Fragments.Fragment_CancelledBill;
import com.example.cosplay_suit_app.Fragments.Fragment_DoneBill;
import com.example.cosplay_suit_app.Fragments.Fragment_HistoryBill;

public class VitrunggianPagerAdapter extends FragmentStateAdapter {
    public VitrunggianPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Fragment_DoneBill(); // đặt cho Frag01 hiển thị đầu tiên
            case 1:
                return new Fragment_HistoryBill();
            default:
                return new Fragment_DoneBill();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

package com.example.cosplay_suit_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cosplay_suit_app.R;

public class Fragment_taikhoan extends Fragment {
    public Fragment_taikhoan() {
    }

    public static Fragment_taikhoan newInstance(){
        Fragment_taikhoan fragmentTaikhoan = new Fragment_taikhoan();
        return fragmentTaikhoan;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_taikhoan, container, false);
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

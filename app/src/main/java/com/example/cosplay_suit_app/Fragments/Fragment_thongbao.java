package com.example.cosplay_suit_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cosplay_suit_app.R;

public class Fragment_thongbao extends Fragment {

    public Fragment_thongbao() {
    }

    public static Fragment_thongbao newInstance(){
        Fragment_thongbao fragmentThongbao = new Fragment_thongbao();
        return fragmentThongbao;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_thongbao, container, false);
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

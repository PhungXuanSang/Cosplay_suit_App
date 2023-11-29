package com.example.cosplay_suit_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cosplay_suit_app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Shop_ChitietShop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Shop_ChitietShop extends Fragment {
    TextView tv_slsp_chitiet_shop,tv_nameshop;
    String id_shop,name_shop,slsp_shop;
    public Fragment_Shop_ChitietShop() {
        // Required empty public constructor
    }

    public static Fragment_Shop_ChitietShop newInstance() {
        Fragment_Shop_ChitietShop fragment = new Fragment_Shop_ChitietShop();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__shop__chitiet_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        id_shop = intent.getStringExtra("id_shop");
        name_shop = intent.getStringExtra("name_shop");
        slsp_shop = intent.getStringExtra("slsp_shop");

        tv_slsp_chitiet_shop = view.findViewById(R.id.tv_slsp_chitiet_shop);
        tv_nameshop = view.findViewById(R.id.tv_nameshop);
        tv_slsp_chitiet_shop.setText(slsp_shop);
        tv_nameshop.setText(name_shop);
    }
}
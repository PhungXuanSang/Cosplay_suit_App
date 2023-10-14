package com.example.cosplay_suit_app.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cosplay_suit_app.Activity.LoginActivity;
import com.example.cosplay_suit_app.Activity.RegisterShopActivity;
import com.example.cosplay_suit_app.Package_bill.Collection_adapter_bill;
import com.example.cosplay_suit_app.R;

public class Fragment_profile extends Fragment {

    private TextView tv_fullname;

    private ImageView img_profile;

    private TextView tv_dky_shop, tv_donhangmua;
    private Button btn_login_profile;

    String username_u;

    public Fragment_profile() {
    }

    public static Fragment_profile newInstance(){
        Fragment_profile fragmentProfile = new Fragment_profile();
        return fragmentProfile;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_profile, container, false);
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_fullname = view.findViewById(R.id.tv_fullname_profile);
        btn_login_profile = view.findViewById(R.id.btn_login_profile);
        tv_dky_shop = view.findViewById(R.id.tv_dky_shop);
        img_profile = view.findViewById(R.id.img_profile);
        tv_donhangmua = view.findViewById(R.id.donhangmua);

        tv_donhangmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Collection_adapter_bill.class);
                startActivity(intent);
            }
        });

        btn_login_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        tv_dky_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RegisterShopActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        username_u = sharedPreferences.getString("fullname","");
        tv_fullname.setText(username_u);
    }
}

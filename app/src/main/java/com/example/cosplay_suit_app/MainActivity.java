package com.example.cosplay_suit_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cosplay_suit_app.Fragments.Fragment_chat;
import com.example.cosplay_suit_app.Fragments.Fragment_donhang;
import com.example.cosplay_suit_app.Fragments.Fragment_profile;
import com.example.cosplay_suit_app.Fragments.Fragment_trangchu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //code giao diện menu bottom navigation
        fragmentManager = getSupportFragmentManager();
        Fragment_trangchu fragmentTrangchu = new Fragment_trangchu();
        fragmentManager.beginTransaction().add(R.id.id_frame, fragmentTrangchu).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.id_bottom_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_trangchu){
                    ralaceFragment(Fragment_trangchu.newInstance());
                } else if (id == R.id.nav_taikhoan) {
                    ralaceFragment(Fragment_profile.newInstance());
                }else if(id == R.id.nav_tinnhan){
                    ralaceFragment(Fragment_chat.newInstance());
                }else if (id == R.id.nav_donhang){
                    ralaceFragment(Fragment_donhang.newInstance());
                }
                return true;
            }
        });
    }

    public void ralaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.id_frame, fragment);
        fragmentTransaction.commit();
    }
}
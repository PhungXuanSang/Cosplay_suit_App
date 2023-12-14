package com.example.cosplay_suit_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.Activity.LoginActivity;
import com.example.cosplay_suit_app.DTO.DTO_Wallet;
import com.example.cosplay_suit_app.Fragments.Fragment_chat;
import com.example.cosplay_suit_app.Fragments.Fragment_donhang;
import com.example.cosplay_suit_app.Fragments.Fragment_profile;
import com.example.cosplay_suit_app.Fragments.Fragment_trangchu;
import com.example.cosplay_suit_app.bill.controller.Wallet_controller;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    Dialog dialog;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAndRequestNotificationPermission();
        setContentView(R.layout.activity_main);


        dialog = new Dialog(this);
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogbanner);

                Window window = dialog.getWindow();
                WindowManager.LayoutParams layoutParams = window.getAttributes();

                ImageView xbanner = dialog.findViewById(R.id.Xbanne);
                xbanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                // Chiều rộng full màn hình
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                // Chiều cao theo dialog màn hình
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                // Đặt vị trí của dialog ở phía dưới cùng của màn hình
                layoutParams.gravity = Gravity.CENTER;

                window.setAttributes(layoutParams);
                window.setBackgroundDrawableResource(android.R.color.transparent);

                dialog.show();
            }
        }, 1000);
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        id = sharedPreferences.getString("id","");
        checkaddwallet();
    }

    public void ralaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.id_frame, fragment);
        fragmentTransaction.commit();
    }

    private void checkAndRequestNotificationPermission() {
        if (!areNotificationsEnabled()) {

            requestNotificationPermission();
        }
    }

    private boolean areNotificationsEnabled() {
        return NotificationManagerCompat.from(this).areNotificationsEnabled();
    }

    private void requestNotificationPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yêu cầu quyền thông báo");
        builder.setMessage("Ứng dụng cần quyền thông báo để hiển thị thông báo. Hãy cấp quyền trong cài đặt để tiếp tục sử dụng ứng dụng.");

        builder.setPositiveButton("Cài đặt", (dialog, which) -> {
            openAppNotificationSettings();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> {
            Toast.makeText(this, "Bạn sẽ không nhận được thông báo!!!", Toast.LENGTH_SHORT).show();
        });

        builder.show();
    }

    private void openAppNotificationSettings() {
        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getPackageName()));
        }

        startActivity(intent);
    }
    public void checkaddwallet(){
        Wallet_controller walletController = new Wallet_controller(MainActivity.this);
        walletController.getWallet(id, new Wallet_controller.ApiGetwalet() {
            @Override
            public void onApiGetwalet(DTO_Wallet dtoWallet) {
                if (dtoWallet == null){
                    DTO_Wallet dtoWallet1 = new DTO_Wallet();
                    dtoWallet1.setId_user(id);
                    dtoWallet1.setCurrenry("VND");
                    dtoWallet1.setMoney("0");
                    dtoWallet1.setPasswd("");
                    walletController.AddWallet(dtoWallet1);
                }
            }
        });
    }

}
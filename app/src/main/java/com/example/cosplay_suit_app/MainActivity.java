package com.example.cosplay_suit_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

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
        checkAndRequestNotificationPermission();
        setContentView(R.layout.activity_main);

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

}
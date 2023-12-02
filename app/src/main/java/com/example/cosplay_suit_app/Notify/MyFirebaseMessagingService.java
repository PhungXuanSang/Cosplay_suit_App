package com.example.cosplay_suit_app.Notify;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cosplay_suit_app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "your_channel_id";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Nhận thông báo từ FCM: " + remoteMessage.getData());


        if (remoteMessage.getData() != null) {
            showNotification(remoteMessage.getData());
        }
    }

    private void showNotification(Map<String, String> data) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("message"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.animemessage);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            channel.setSound(soundUri,audioAttributes);
            notificationManager.createNotificationChannel(channel);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling


            return;
        }
        notificationManager.notify(0, builder.build());
    }
}

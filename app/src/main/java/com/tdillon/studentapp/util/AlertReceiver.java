package com.tdillon.studentapp.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.tdillon.studentapp.R;

public class AlertReceiver extends BroadcastReceiver {

    static int notificationID;
    String CHANNEL_ID = "channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("key"), Toast.LENGTH_LONG).show();
        createNotificationChannel(context, CHANNEL_ID);

        Notification n = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Notification ID: " + notificationID)
                .setContentText(intent.getStringExtra("key"))
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(notificationID++, n);
    }

    public void createNotificationChannel(Context context, String CHANNEL_ID) {
        CharSequence name = "Notification channel";
        String description = "Notification description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }
}
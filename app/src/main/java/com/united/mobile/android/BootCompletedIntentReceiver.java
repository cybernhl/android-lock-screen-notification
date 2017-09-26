package com.united.mobile.android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // The id of the channel.
            String id = "channel_01";
            // The user-visible name of the channel.
            CharSequence name = "Fossil Trigger";
            // The user-visible description of the channel.
            String description = "Fossil Trigger";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mNotificationManager.createNotificationChannel(mChannel);
            Intent pushIntent = new Intent(context, NotificationService.class);
            context.startService(pushIntent);
            pushIntent = new Intent(context, PhoneUnlockService.class);
            context.startService(pushIntent);
        }
    }
}

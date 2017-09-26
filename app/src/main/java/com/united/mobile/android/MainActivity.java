package com.united.mobile.android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        Intent pushIntent = new Intent(context, NotificationService.class);
        context.startService(pushIntent);
        context = getApplicationContext();
        pushIntent = new Intent(context, PhoneUnlockService.class);
        context.startService(pushIntent);
    }
}

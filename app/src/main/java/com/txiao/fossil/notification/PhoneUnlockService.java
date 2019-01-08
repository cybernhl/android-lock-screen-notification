package com.txiao.fossil.notification;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by txiao on 12/14/16.
 */

public class PhoneUnlockService extends Service {

    private BroadcastReceiver mPowerKeyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideNotification();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        configureNotifications();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().unregisterReceiver(mPowerKeyReceiver);
    }

    public void configureNotifications() {
        final IntentFilter screenFilter = new IntentFilter();
        /** System Defined Broadcast */
        screenFilter.addAction(Intent.ACTION_USER_PRESENT);
        getApplicationContext().registerReceiver(mPowerKeyReceiver, screenFilter);
    }

    private void hideNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancelAll();
    }
}

package com.united.mobile.android;

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
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;

/**
 * Created by txiao on 12/14/16.
 */

public class NotificationService extends NotificationListenerService {

    private static final int NOTIFICATION_ID = 3;
    private static final long LOCK_TIME_MILLIS = 20000l;
    private static final String TITLE = "Notification While Screen Locked";
    private static final String MESSAGE = "This notification should automatically close";
    //TODO: replace this with user input instead of hard-coded array of strings
    private static final String[] APPS_TO_IGNORE = {"com.google.android.apps.maps", "com.google.android.music"};
    private static final String NOTIFICATION_RESET_APP = "com.aa.android";

    private boolean hasShownAfterLock = false;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.getPackageName().equals(NOTIFICATION_RESET_APP)) {
            hasShownAfterLock = false;
            hideNotification();
        } else if (isAppAllowed(sbn.getPackageName())
                && locked() && !hasShownAfterLock
                && sbn.getNotification().priority >= Notification.PRIORITY_LOW) {
            showAndHideNotification(TITLE, MESSAGE);
            hasShownAfterLock = true;
        }
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //do nothing
        super.onNotificationRemoved(sbn);
    }

    private boolean locked() {
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    private void showAndHideNotification(String title, String message) {

        // intent triggered, you can add other intent for other actions
        //Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
        //PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.small_icon)

                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(NOTIFICATION_ID, mNotification);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideNotification();
            }
        }, LOCK_TIME_MILLIS);
    }

    private void hideNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancelAll();
    }

    private boolean isAppAllowed(String packageName) {
        for (String s : APPS_TO_IGNORE) {
            if (s.equals(packageName)) {
                return false;
            }
        }
        return !packageName.equals(this.getPackageName());
    }
}

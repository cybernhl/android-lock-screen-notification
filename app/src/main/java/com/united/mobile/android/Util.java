package com.united.mobile.android;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;

/**
 * Created by txiao on 3/13/18.
 */

public class Util {

    private static final int NOTIFICATION_ID = 3;
    private static final long LOCK_TIME_MILLIS = 20000l;
    private static final String TITLE = "Notification While Screen Locked";
    private static final String MESSAGE = "This notification should automatically close";
    private static final String NOTIFICATION_RESET_APP = "com.aa.android";

    private static boolean hasShownAfterLock = false;

    public static void configure(Context context, NotificationManager mNotificationManager) {
        System.out.println("Configuring 1");
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
        Intent pushIntent = new Intent(context, LockedNotificationListenerService.class);
        context.startService(pushIntent);
        pushIntent = new Intent(context, PhoneUnlockService.class);
        context.startService(pushIntent);

        Util.scheduleJob(context);
        System.out.println("Configuring 2");
    }

    public static void scheduleJob(Context context) {

        ComponentName serviceComponent = new ComponentName(context, LockedNotificationTimerService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 3600 * 1000); // wait at least
        builder.setOverrideDeadline(2 * 3600 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());

    }

    public static void logicForNotificationTimer(Service service) {
        if (hasShownAfterLock && locked(service)) {
            showAndHideNotification(TITLE, MESSAGE, service);
        }
    }

    public static void logicForNotification(StatusBarNotification sbn, NotificationListenerService.RankingMap rankingMap, Service service) {
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        rankingMap.getRanking(sbn.getKey(), ranking);
        if (sbn.getPackageName().equals(NOTIFICATION_RESET_APP)) {
            hasShownAfterLock = false;
            hideNotification(service);
        } else if (isAppAllowed(sbn.getPackageName(), service)
                && locked(service) && !hasShownAfterLock
                && ranking.getImportance() >= NotificationManager.IMPORTANCE_LOW) {
            showAndHideNotification(TITLE, MESSAGE, service);
            hasShownAfterLock = true;
        }
    }

    private static boolean locked(Service service) {
        KeyguardManager km = (KeyguardManager) service.getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    private static void showAndHideNotification(String title, String message, Service service) {

        // intent triggered, you can add other intent for other actions
        //Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
        //PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new NotificationCompat.Builder(service)

                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setChannelId("channel_01")
                .build();

        NotificationManager notificationManager = (NotificationManager) service.getSystemService(service.NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(NOTIFICATION_ID, mNotification);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            Service service;
            @Override
            public void run() {
                hideNotification(service);
            }
            public Runnable init(Service service) {
                this.service = service;
                return this;
            }
        }.init(service), LOCK_TIME_MILLIS);
    }

    private static void hideNotification(Service service) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) service.getApplicationContext().getSystemService(ns);
        nMgr.cancelAll();
    }

    private static boolean isAppAllowed(String packageName, Service service) {
        return !packageName.equals(service.getPackageName());
    }
}

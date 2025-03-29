package com.txiao.fossil.notification;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

/**
 * Created by txiao on 12/14/16.
 */

public class LockedNotificationListenerService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, NotificationListenerService.RankingMap rankingMap) {

        Util.logicForNotification(sbn, rankingMap, this);

        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Util.onNotificationRemoved(sbn, this);

        super.onNotificationRemoved(sbn);
    }
}

package com.flash.light.free.good.fashioncallflash.service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.flash.light.free.good.fashioncallflash.content.NotiContent;
import com.flash.light.free.good.fashioncallflash.tool.MessageManager;

import java.util.ArrayList;
import java.util.List;



/**
 * 通知服务
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotiService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
//        if (SharedPref.getBoolean(this, SharedPref.NOTIFY, false)) sendMessage();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationRemoved(sbn, rankingMap);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap, int
            reason) {
        super.onNotificationRemoved(sbn, rankingMap, reason);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendMessage() {
        StatusBarNotification[] activeNotifications = getActiveNotifications();
        if (activeNotifications.length == 0) return;
        List<NotiContent> list = new ArrayList<>();
        for (StatusBarNotification notification : activeNotifications) {
            String packageName = notification.getPackageName();
            if (MessageManager.getInstence().getNoNotifyAPPs().contains(packageName)) continue;
            if (TextUtils.equals(getPackageName(), packageName)) continue;//过滤自己通知
            if (TextUtils.equals("android", packageName)) continue;//过滤系统通知(USB)
            CharSequence contentC = notification.getNotification().tickerText;
            if (contentC == null) continue;
            String content = contentC.toString();
            NotiContent notiContent = new NotiContent();
            notiContent.setPackageName(packageName);
            notiContent.setContent(content);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notiContent.setKey(notification.getKey());
                cancelNotification(notification.getKey());
            }
            list.add(notiContent);
        }
        if (!list.isEmpty()) MessageManager.getInstence().refreshShowList(list);
    }

}

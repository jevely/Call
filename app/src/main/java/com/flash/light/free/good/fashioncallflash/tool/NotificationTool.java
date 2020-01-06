package com.flash.light.free.good.fashioncallflash.tool;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.NotificationManagerCompat;

import com.flash.light.free.good.fashioncallflash.service.NotiService;

import java.util.Set;


/**
 * 通知栏工具
 */
public class NotificationTool {

    /**
     * 开启通知权限
     */
    public static void goToNotification(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            context.startActivityForResult(new Intent(Settings
                    .ACTION_NOTIFICATION_LISTENER_SETTINGS), 3);
//        showTip(context, R.string.permission_open_notify_tip);
    }

    //弹出提示界面
    private static void showTip(Activity activity, int contentID) {
//        Intent intent = new Intent(activity, PermissionTipActivity.class);
//        intent.putExtra("permission_tip", activity.getResources().getString(contentID));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
    }

    /**
     * 判断是否开启通知权限
     */
    public static boolean checkNotificationPermission(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否工作
     */
    public static boolean isNotifyWork(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        return packageNames.contains(context.getPackageName());
    }

    //重新开启NotificationMonitor
    public static void toggleNotificationListenerService(Context context) {
        ComponentName thisComponent = new ComponentName(context, NotiService.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(thisComponent, PackageManager
                .COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(thisComponent, PackageManager
                .COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

}

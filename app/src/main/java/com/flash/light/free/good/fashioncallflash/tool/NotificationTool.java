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

import com.flash.light.free.good.fashioncallflash.R;
import com.flash.light.free.good.fashioncallflash.activity.PermissionTipActivity;
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
        showTip(context);
    }

    //弹出提示界面
    private static void showTip(Activity activity) {
        Intent intent = new Intent(activity, PermissionTipActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
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
}

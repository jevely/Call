package com.flash.light.free.good.flash.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.flash.light.free.good.flash.BuildConfig;
import com.flash.light.free.good.flash.CallApplication;
import com.flash.light.free.good.flash.R;
import com.flash.light.free.good.flash.activity.PermissionTipActivity;

import java.lang.reflect.Method;


/**
 * 工具类
 */
public class DeviceUtils {

    //获取navigationbar高度
    private static int getNavigationBarHeight() {
        Resources resources = CallApplication.getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }


    /**
     * 检查悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }else
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int result = (int) method.invoke(manager, 24, Binder.getCallingUid(), context.getPackageName());
                return AppOpsManager.MODE_ALLOWED == result || result == AppOpsManager.MODE_DEFAULT;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }

    }

    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean checkAlertWindowsPermission(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1));
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }

    /**
     * 开启悬浮窗权限（messenger）
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void openFloatWindowPermission(Activity activity) {
        try {
            Intent intent;
            if (Build.MANUFACTURER.equals("Meizu")) {
                //魅族手机
                intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            } else {
                intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
            }
            activity.startActivityForResult(intent, 1);
            showTip(activity, R.string.permission_open_float_tip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //弹出提示界面
    private static void showTip(Activity activity, int contentID) {
        Intent intent = new Intent(activity, PermissionTipActivity.class);
        intent.putExtra("permission_tip", activity.getResources().getString(contentID));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

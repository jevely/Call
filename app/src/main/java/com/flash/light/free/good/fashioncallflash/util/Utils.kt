package com.flash.light.free.good.fashioncallflash.util

import android.annotation.TargetApi
import android.app.Activity
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.flash.light.free.good.fashioncallflash.BuildConfig
import com.flash.light.free.good.fashioncallflash.CallApplication
import com.flash.light.free.good.fashioncallflash.view.WhiteDialog
import java.io.File
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration


fun getScreen(): Point {
    val dm = CallApplication.getContext().resources.displayMetrics
    val point = Point(dm.widthPixels, dm.heightPixels)
    return point
}

//val saveFloadAddress =
//    Environment.getExternalStorageDirectory().absolutePath + File.separator + WallpaperApplication.getContext().resources.getString(
//        R.string.app_name
//    ) + File.separator

val saveFloadAddress =
    CallApplication.getContext().getFilesDir().getAbsolutePath() + File.separator + "calltheme" + File.separator

//本地读取图片
fun getWallpaperFromLocal(path: String): Bitmap {
    val bitmap = BitmapFactory.decodeFile(path)
    return bitmap
}

/**
 * 删除文件
 */
fun deleteFile(file: File): Boolean {
    if (file.isFile) {
        file.delete()
    } else if (file.isDirectory) {
        val files = file.listFiles()
        for (file1 in files) {
            deleteFile(file1)
        }
    }
    return file.exists()
}

/**
 * 检查悬浮窗权限
 */
fun checkFloatWindowPermission(context: Context): Boolean {
    val version = Build.VERSION.SDK_INT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return Settings.canDrawOverlays(context)
    } else if (version >= 19) {
        val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        try {
            val clazz = AppOpsManager::class.java
            val method = clazz.getDeclaredMethod(
                "checkOp",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                String::class.java
            )
            val result =
                method.invoke(manager, 24, Binder.getCallingUid(), context.packageName) as Int
            return AppOpsManager.MODE_ALLOWED == result || result == AppOpsManager.MODE_DEFAULT
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    } else {
        return true
    }

}

/**
 * 判断 悬浮窗口权限是否打开
 *
 * @param context
 * @return true 允许  false禁止
 */
fun checkAlertWindowsPermission(context: Context): Boolean {
    try {
        val `object` = context.getSystemService(Context.APP_OPS_SERVICE) ?: return false
        val localClass = `object`.javaClass
        val arrayOfClass = arrayOfNulls<Class<*>>(3)
        arrayOfClass[0] = Integer.TYPE
        arrayOfClass[1] = Integer.TYPE
        arrayOfClass[2] = String::class.java
        val method = localClass.getMethod("checkOp", *arrayOfClass) ?: return false
        val arrayOfObject1 = arrayOfNulls<Any>(3)
        arrayOfObject1[0] = 24
        arrayOfObject1[1] = Binder.getCallingUid()
        arrayOfObject1[2] = context.packageName
        val m = method.invoke(`object`, *arrayOfObject1) as Int
        return m == AppOpsManager.MODE_ALLOWED
    } catch (ex: Exception) {

    }

    return false
}

/**
 * 开启悬浮窗权限（messenger）
 */
@TargetApi(Build.VERSION_CODES.M)
fun openFloatWindowPermission(activity: Activity) {
    try {
        val intent: Intent
        if (android.os.Build.MANUFACTURER == "Meizu") {
            //魅族手机
            intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
        } else {
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + activity.packageName)
        }
        activity.startActivityForResult(intent, 1)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 白名单判断加设置
 */
fun setWhite(context: Context) {
    if (!checkIsInWhite()) {
        val whiteDiolog = WhiteDialog(context)
        whiteDiolog.show()
    }
}

/**
 * 判断是否在白名单
 */
private fun checkIsInWhite(): Boolean {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var isIgnoring = false
            val powerManager =
                CallApplication.getContext().getSystemService(Context.POWER_SERVICE) as PowerManager
            if (powerManager != null) {
                isIgnoring =
                    powerManager.isIgnoringBatteryOptimizations(CallApplication.getContext().getPackageName())
            }
            return isIgnoring
        }
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return true
    }
}

/**
 * 前往设置白名单
 */
fun goToWhite() {
    try {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.setData(Uri.parse("package:" + CallApplication.getContext().getPackageName()))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        CallApplication.getContext().startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace();
    }
}

/**
 * 手机后台管理设置
 */
fun backManagerSet() {
    if (Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi")) {
        showActivity(
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
    }

    if (Build.BRAND != null) {
        if (Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor")) {
            try {
                showActivity(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                showActivity(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"
                )
            }
        }
    }

    if (Build.BRAND != null && Build.BRAND.toLowerCase().equals("samsung")) {
        try {
            showActivity("com.samsung.android.sm_cn")
        } catch (e: Exception) {
            showActivity("com.samsung.android.sm")
        }
    }
}

/**
 * 跳转到指定应用的首页
 */
private fun showActivity(packageName: String) {
    val intent = CallApplication.getContext().packageManager.getLaunchIntentForPackage(packageName)
    CallApplication.getContext().startActivity(intent)
}

/**
 * 跳转到指定应用的指定页面
 */
private fun showActivity(packageName: String, activityDir: String) {
    val intent = Intent()
    intent.component = ComponentName(packageName, activityDir)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    CallApplication.getContext().startActivity(intent)
}


fun isNavigationBarShow(activity: Activity): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val display = activity.getWindowManager().getDefaultDisplay()
        val size = Point()
        val realSize = Point()
        display.getSize(size)
        display.getRealSize(realSize)
        return realSize.y != size.y
    } else {
        val menu = ViewConfiguration.get(activity).hasPermanentMenuKey()
        val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return if (menu || back) {
            false
        } else {
            true
        }
    }
}

private fun getNavigationBarHeight(activity: Activity): Int {
    if (!isNavigationBarShow(activity)) {
        return 0
    }
    val resources = activity.resources
    val resourceId = resources.getIdentifier(
        "navigation_bar_height",
        "dimen", "android"
    )
    //获取NavigationBar的高度
    return resources.getDimensionPixelSize(resourceId)
}


fun getSceenHeight(activity: Activity): Int {
    return activity.windowManager.defaultDisplay.height + getNavigationBarHeight(activity)
}


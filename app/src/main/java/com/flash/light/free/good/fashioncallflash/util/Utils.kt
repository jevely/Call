package com.flash.light.free.good.fashioncallflash.util

import android.annotation.TargetApi
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.provider.Settings
import com.flash.light.free.good.fashioncallflash.BuildConfig
import com.flash.light.free.good.fashioncallflash.CallApplication
import java.io.File

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
    Environment.getExternalStorageDirectory().absolutePath + File.separator+"calltheme"+File.separator

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

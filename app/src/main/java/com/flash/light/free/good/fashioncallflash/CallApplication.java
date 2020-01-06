package com.flash.light.free.good.fashioncallflash;

import android.app.ActivityManager;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.flash.light.free.good.fashioncallflash.db.DataBaseTool;
import com.flash.light.free.good.fashioncallflash.tool.DataTool;
import com.flash.light.free.good.fashioncallflash.util.SharedPreTool;

import java.util.List;

public class CallApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //只初始化一次 限主进程
        String processName = getProcessName(this, android.os.Process.myPid());
        if (null != processName) {
            if (!processName.equals(getPackageName())) {
                return;
            }
        }

        SharedPreTool.Companion.getInstance().init(this);

        DataBaseTool.Companion.getInstance().initDataBase(this);
        DataTool.Companion.getInstance().initData();
    }

    /**
     * 获取app进程
     */
    public String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
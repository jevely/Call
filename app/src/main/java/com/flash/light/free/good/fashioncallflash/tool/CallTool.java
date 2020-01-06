package com.flash.light.free.good.fashioncallflash.tool;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;


import com.android.internal.telephony.ITelephony;
import com.flash.light.free.good.fashioncallflash.CallApplication;
import com.flash.light.free.good.fashioncallflash.service.NotiService;

import java.lang.reflect.Method;
import java.util.List;


/**
 * 电话工具类
 */
public class CallTool {

    private static void answerPhone15e(Context context) {
        String enforcedPerm = "android.permission.CALL_PRIVILEGED";
        Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(Intent.EXTRA_KEY_EVENT,
                new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
        Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(Intent.EXTRA_KEY_EVENT,
                new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
        context.sendOrderedBroadcast(btnDown, enforcedPerm);
        context.sendOrderedBroadcast(btnUp, enforcedPerm);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void answerPhone21(Context context) {
        MediaSessionManager mediaSessionManager = (MediaSessionManager) context
                .getApplicationContext().getSystemService(Context.MEDIA_SESSION_SERVICE);
        List<MediaController> mediaControllerList = mediaSessionManager.getActiveSessions(new
                ComponentName(context.getApplicationContext(), NotiService.class));
        for (MediaController m : mediaControllerList) {
            if ("com.android.server.telecom".equals(m.getPackageName())) {
                m.dispatchMediaButtonEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent
                        .KEYCODE_HEADSETHOOK));
                m.dispatchMediaButtonEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent
                        .KEYCODE_HEADSETHOOK));
                break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void answerPhone26(Context context) {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(context
                .TELECOM_SERVICE);
        try {
            Method method = Class.forName("android.telecom.TelecomManager").getMethod
                    ("acceptRingingCall");
            method.invoke(telecomManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //htc单独处理
    private static void broadcastHeadsetConnected(Context context, boolean connected) {
        Intent i = new Intent(Intent.ACTION_HEADSET_PLUG);
        i.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        i.putExtra("state", connected ? 1 : 0);
        i.putExtra("name", "mysms");
        try {
            context.sendOrderedBroadcast(i, null);
        } catch (Exception e) {
        }
    }

    /**
     * 接电话
     */
    public static void answerPhone(Context context) {
        boolean z = true;
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (!TextUtils.equals("HTC", Build.MANUFACTURER) || audioManager.isWiredHeadsetOn()) {
            z = false;
        }
        if (z) {
            broadcastHeadsetConnected(context, false);
        }
        try {
            if (Build.VERSION.SDK_INT >= 21 && NotificationTool
                    .checkNotificationPermission(context)) {
                answerPhone21(context);
                if (z) {
                    broadcastHeadsetConnected(context, false);
                }
            } else if (Build.VERSION.SDK_INT >= 19) {
                KeyEvent keyEvent = new KeyEvent(0, 79);
                KeyEvent keyEvent2 = new KeyEvent(1, 79);
                audioManager.dispatchMediaKeyEvent(keyEvent);
                audioManager.dispatchMediaKeyEvent(keyEvent2);
                if (z) {
                    broadcastHeadsetConnected(context, false);
                }
            } else {
                Runtime.getRuntime().exec("input keyevent " + Integer.toString(79));
                if (z) {
                    broadcastHeadsetConnected(context, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            answerPhone15e(context);
        } catch (Throwable th) {
            if (z) {
                broadcastHeadsetConnected(context, false);
            }
        }
    }

    /**
     * 拒绝接听
     */
    public static void rejectCall() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                TelecomManager tm = (TelecomManager) CallApplication.getContext().getSystemService(Context.TELECOM_SERVICE);
                if (tm != null) {
                    // success == true if call was terminated.
                    boolean success = tm.endCall();
                }
            } else {
                Method method = Class.forName("android.os.ServiceManager").getMethod("getService",
                        String.class);
                IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
                ITelephony telephony = ITelephony.Stub.asInterface(binder);
                telephony.endCall();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

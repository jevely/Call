package com.flash.light.free.good.fashioncallflash.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.flash.light.free.good.fashioncallflash.tool.ContactTool;
import com.flash.light.free.good.fashioncallflash.util.LightUtil;
import com.flash.light.free.good.fashioncallflash.window.LightalkWindow;


/**
 * 来电监听
 */
public class CallBroadCast extends BroadcastReceiver {
    private int style = -1;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
//        if (!SharedPref.getBoolean(context, SharedPref.CALL, false)) return;
//        style = SharedPref.getInt(context, SharedPref.CALL_THEME, -1);
        String action = intent.getAction();
        if (TextUtils.equals(action, Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.d("LJW", "打出去");
        } else {
            //打进来
            Log.d("LJW", "打进来");
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            if (telephonyManager != null)
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("LJW", "挂断");
                    LightalkWindow.getInstence().hidelockScreen();
                    LightUtil.getInstance().stopCallFlash(context);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            if(MainApplication.getInstance().isCallShow) {
//                                DotUtil.sendEvent(DotUtil.CALL_SHOW);
//                                DotUtil.sendEvent(DotUtil.TOUCH_START);
//                                MainApplication.getInstance().isCallShow = false;
//                            }
                        }
                    },3000);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("LJW", "接听");
//                    LightUtil.getInstance().stopCallFlash(context);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    LightalkWindow.getInstence().setData(ContactTool.getInstence().checkContact(incomingNumber), incomingNumber, style);
                    LightalkWindow.getInstence().showlockScreen();
                    LightUtil.getInstance().callFlash(context);
                    break;
            }
        }
    };
}

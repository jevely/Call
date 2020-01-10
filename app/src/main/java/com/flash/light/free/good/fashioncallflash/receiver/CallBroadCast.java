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
import com.flash.light.free.good.fashioncallflash.util.Logger;
import com.flash.light.free.good.fashioncallflash.util.SharedPreTool;
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
        if (!SharedPreTool.Companion.getInstance().getBoolean(SharedPreTool.CALL_THEME_SWITCH)) {
            Logger.INSTANCE.d("call theme switch 开关没有开");
            return;
        }

        String action = intent.getAction();
        if (TextUtils.equals(action, Intent.ACTION_NEW_OUTGOING_CALL)) {
            Logger.INSTANCE.d("打出去");
        } else {
            //打进来
            Logger.INSTANCE.d("打进来");
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
                    Logger.INSTANCE.d("挂断");
                    LightalkWindow.getInstence().hidelockScreen();
                    LightUtil.getInstance().stopCallFlash(context);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Logger.INSTANCE.d("接听");
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

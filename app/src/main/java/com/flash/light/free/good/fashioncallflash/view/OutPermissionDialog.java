package com.flash.light.free.good.fashioncallflash.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flash.light.free.good.fashioncallflash.R;
import com.flash.light.free.good.fashioncallflash.tool.NotificationTool;
import com.flash.light.free.good.fashioncallflash.util.DeviceUtils;
import com.flash.light.free.good.fashioncallflash.util.UtilsKt;

/**
 *
 */
public class OutPermissionDialog extends Dialog implements View.OnClickListener {

    private TextView permission_close;
    private RelativeLayout permission_screen_re, permission_call_re;
    private ImageView permission_float_bt, permission_notify_bt;

    public static int FLOAT = 1;
    public static int NOTIFY = 2;
    public static int ALL = 3;
    private int state;

    private Activity context;

    public OutPermissionDialog(Activity context, int state) {
        super(context, R.style.dialog_screen);
        this.context = context;
        this.state = state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_permission_dialog_layout);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = UtilsKt.getScreen().x;
        lp.height = UtilsKt.getScreen().y;
        getWindow().setAttributes(lp);

        setCanceledOnTouchOutside(false);

        permission_close = findViewById(R.id.permission_close);
        permission_screen_re = findViewById(R.id.permission_screen_re);
        permission_call_re = findViewById(R.id.permission_call_re);
        permission_float_bt = findViewById(R.id.permission_float_bt);
        permission_notify_bt = findViewById(R.id.permission_notify_bt);

        if (state == FLOAT) {
            permission_screen_re.setVisibility(View.VISIBLE);
            permission_call_re.setVisibility(View.GONE);
        } else if (state == NOTIFY) {
            permission_screen_re.setVisibility(View.GONE);
            permission_call_re.setVisibility(View.VISIBLE);
        } else {
            permission_screen_re.setVisibility(View.VISIBLE);
            permission_call_re.setVisibility(View.VISIBLE);
        }

        permission_close.setOnClickListener(this);
        permission_float_bt.setOnClickListener(this);
        permission_notify_bt.setOnClickListener(this);
        permission_screen_re.setOnClickListener(this);
        permission_call_re.setOnClickListener(this);
    }

    public void setBtUnclick(int state) {
        if (FLOAT == state) {
            permission_float_bt.setBackgroundResource(R.mipmap.out_no);
            permission_float_bt.setClickable(false);
        } else if (NOTIFY == state) {
            permission_notify_bt.setBackgroundResource(R.mipmap.out_no);
            permission_notify_bt.setClickable(false);
        } else {
            permission_float_bt.setBackgroundResource(R.mipmap.out_no);
            permission_notify_bt.setBackgroundResource(R.mipmap.out_no);
            permission_float_bt.setClickable(false);
            permission_notify_bt.setClickable(false);
        }
    }

    public void setBtClick(int state) {
        if (FLOAT == state) {
            permission_float_bt.setBackgroundResource(R.mipmap.out_yes);
            permission_float_bt.setClickable(true);
        } else if (NOTIFY == state) {
            permission_notify_bt.setBackgroundResource(R.mipmap.out_yes);
            permission_notify_bt.setClickable(true);
        } else {
            permission_float_bt.setBackgroundResource(R.mipmap.out_yes);
            permission_notify_bt.setBackgroundResource(R.mipmap.out_yes);
            permission_float_bt.setClickable(true);
            permission_notify_bt.setClickable(true);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (callBack != null) callBack.cancel();
    }

    private OutPermissionDialog.OnCallBack callBack;

    public OutPermissionDialog setCallBack(OutPermissionDialog.OnCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.permission_float_bt:
            case R.id.permission_screen_re:
                DeviceUtils.openFloatWindowPermission(context);
                if (callBack != null) callBack.click();
                dismiss();
                break;
            case R.id.permission_notify_bt:
            case R.id.permission_call_re:
                NotificationTool.goToNotification(context);
                if (callBack != null) callBack.click();
                dismiss();
                break;
            case R.id.permission_close:
                dismiss();
                if (callBack != null) callBack.cancel();
                break;
        }
    }

    public interface OnCallBack {
        void click();

        void cancel();
    }
}

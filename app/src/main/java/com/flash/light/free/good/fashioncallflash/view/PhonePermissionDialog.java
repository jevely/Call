package com.flash.light.free.good.fashioncallflash.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flash.light.free.good.fashioncallflash.R;
import com.flash.light.free.good.fashioncallflash.tool.NotificationTool;
import com.flash.light.free.good.fashioncallflash.util.DeviceUtils;
import com.flash.light.free.good.fashioncallflash.util.UtilsKt;

/**
 * 权限申请弹框
 */
public class PhonePermissionDialog extends Dialog implements View.OnClickListener {

    private Activity context;

    public static int FLOAT = 1;
    public static int NOTIFY = 2;
    public static int ALL = 3;
    private int state;

    private LinearLayout permission_float_ll, permission_notify_ll;
    private TextView permission_float_bt, permission_notify_bt;
    private ImageView permission_close;

    public PhonePermissionDialog(Activity context, int state) {
        super(context, R.style.dialog_screen);
        this.context = context;
        this.state = state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_permission_dialog_layout);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = UtilsKt.getScreen().x;
        lp.height = UtilsKt.getScreen().y;
        getWindow().setAttributes(lp);

        setCanceledOnTouchOutside(false);

        permission_close = findViewById(R.id.permission_close);
        permission_float_bt = findViewById(R.id.permission_float_bt);
        permission_notify_bt = findViewById(R.id.permission_notify_bt);
        permission_float_ll = findViewById(R.id.permission_float_ll);
        permission_notify_ll = findViewById(R.id.permission_notify_ll);

        permission_float_bt.setOnClickListener(this);
        permission_notify_bt.setOnClickListener(this);
        permission_close.setOnClickListener(this);

        if (state == FLOAT) {
            permission_float_ll.setVisibility(View.VISIBLE);
            permission_float_ll.setBackgroundResource(R.drawable.permisstion_tip_bottom);
            permission_notify_ll.setVisibility(View.GONE);
        } else if (state == NOTIFY) {
            permission_float_ll.setVisibility(View.GONE);
            permission_notify_ll.setVisibility(View.VISIBLE);
        } else {
            permission_float_ll.setVisibility(View.VISIBLE);
            permission_notify_ll.setVisibility(View.VISIBLE);
        }

    }

    public void setBtUnclick(int state) {
        if (FLOAT == state) {
            permission_float_bt.setBackgroundResource(R.drawable.permission_tip_bt_unback);
            permission_float_bt.setClickable(false);
        } else if (NOTIFY == state) {
            permission_notify_bt.setBackgroundResource(R.drawable.permission_tip_bt_unback);
            permission_notify_bt.setClickable(false);
        } else {
            permission_float_bt.setBackgroundResource(R.drawable.permission_tip_bt_unback);
            permission_notify_bt.setBackgroundResource(R.drawable.permission_tip_bt_unback);
            permission_float_bt.setClickable(false);
            permission_notify_bt.setClickable(false);
        }
    }

    public void setBtClick(int state) {
        if (FLOAT == state) {
            permission_float_bt.setBackgroundResource(R.drawable.permission_tip_bt_back);
            permission_float_bt.setClickable(true);
        } else if (NOTIFY == state) {
            permission_notify_bt.setBackgroundResource(R.drawable.permission_tip_bt_back);
            permission_notify_bt.setClickable(true);
        } else {
            permission_float_bt.setBackgroundResource(R.drawable.permission_tip_bt_back);
            permission_notify_bt.setBackgroundResource(R.drawable.permission_tip_bt_back);
            permission_float_bt.setClickable(true);
            permission_notify_bt.setClickable(true);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (callBack != null) callBack.cancel();
    }

    private OnCallBack callBack;

    public PhonePermissionDialog setCallBack(OnCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.permission_float_bt:
                DeviceUtils.openFloatWindowPermission(context);
                if (callBack != null) callBack.click();
                dismiss();
                break;
            case R.id.permission_notify_bt:
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

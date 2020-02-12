package com.flash.light.free.good.fashioncallflash.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flash.light.free.good.fashioncallflash.R;
import com.flash.light.free.good.fashioncallflash.util.UtilsKt;

/**
 *
 */
public class PermissionDialog extends Dialog {

    private TextView white_yes;
    private RelativeLayout permission_record_re, permission_call_re, permission_contacts_re;
    private boolean call, contacts;
//    private View permission_record_line;

    public PermissionDialog(Context context, boolean call, boolean contacts) {
        super(context, R.style.dialog_screen);
        this.call = call;
        this.contacts = contacts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_dialog_layout);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = UtilsKt.getScreen().x;
        lp.height = UtilsKt.getScreen().y;
        getWindow().setAttributes(lp);

        setCanceledOnTouchOutside(false);

        white_yes = findViewById(R.id.white_yes);
        permission_record_re = findViewById(R.id.permission_record_re);
        permission_call_re = findViewById(R.id.permission_call_re);
        permission_contacts_re = findViewById(R.id.permission_contacts_re);
//        permission_record_line = findViewById(R.id.permission_record_line);

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            permission_record_re.setVisibility(View.GONE);
//            permission_record_line.setVisibility(View.GONE);
//        }

        if (call) {
            permission_call_re.setVisibility(View.GONE);
        }

        if (contacts) {
            permission_contacts_re.setVisibility(View.GONE);
        }

        white_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.click();
                }
                dismiss();
            }
        });
    }

    private ClickCallBack clickCallBack;

    public void setCallBack(ClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ClickCallBack {
        void click();
    }

}

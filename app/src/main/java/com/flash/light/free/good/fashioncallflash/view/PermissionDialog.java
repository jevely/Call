package com.flash.light.free.good.fashioncallflash.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.flash.light.free.good.fashioncallflash.R;
import com.flash.light.free.good.fashioncallflash.util.UtilsKt;

/**
 *
 */
public class PermissionDialog extends Dialog {

    private Button white_yes;
    private RelativeLayout permission_record_re;
//    private View permission_record_line;

    public PermissionDialog(Context context) {
        super(context, R.style.dialog_screen);
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
//        permission_record_line = findViewById(R.id.permission_record_line);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            permission_record_re.setVisibility(View.GONE);
//            permission_record_line.setVisibility(View.GONE);
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

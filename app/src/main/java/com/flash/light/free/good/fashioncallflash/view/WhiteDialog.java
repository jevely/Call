package com.flash.light.free.good.fashioncallflash.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.flash.light.free.good.fashioncallflash.R;
import com.flash.light.free.good.fashioncallflash.util.UtilsKt;

/**
 * 设置省电模式
 */
public class WhiteDialog extends Dialog {

    private Button white_yes;

    public WhiteDialog(Context context) {
        super(context, R.style.dialog_screen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_dialog_layout);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = UtilsKt.getScreen().x;
        lp.height = UtilsKt.getScreen().y;
        getWindow().setAttributes(lp);

        setCanceledOnTouchOutside(false);

        white_yes = findViewById(R.id.white_yes);

        white_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsKt.goToWhite();
                dismiss();
            }
        });
    }

}

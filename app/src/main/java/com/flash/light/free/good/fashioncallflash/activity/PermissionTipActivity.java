package com.flash.light.free.good.fashioncallflash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.flash.light.free.good.fashioncallflash.R;

/**
 * 开启权限提示界面
 * 2018-3-29
 */
public class PermissionTipActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_tip_click);
        init();
    }

    private void init() {

        RelativeLayout access_ll = findViewById(R.id.access_ll);
        access_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionTipActivity.this.finish();
            }
        });
    }
}

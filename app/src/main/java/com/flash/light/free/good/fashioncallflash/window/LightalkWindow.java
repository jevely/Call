package com.flash.light.free.good.fashioncallflash.window;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.flash.light.free.good.fashioncallflash.R;
import com.flash.light.free.good.fashioncallflash.content.ContactContent;
import com.flash.light.free.good.fashioncallflash.db.DataBaseTool;
import com.flash.light.free.good.fashioncallflash.db.ThemeContent;
import com.flash.light.free.good.fashioncallflash.tool.CallTool;
import com.flash.light.free.good.fashioncallflash.tool.ScreenTool;
import com.flash.light.free.good.fashioncallflash.util.SharedPreTool;
import com.flash.light.free.good.fashioncallflash.view.CallThemeBackView;

public class LightalkWindow {

    private static LightalkWindow lightalkWindow;

    //窗口管理器
    private WindowManager wm;
    //view
    private View mView;
    //布局参数
    private WindowManager.LayoutParams layoutParams;

    private Context context;

    private Boolean isShow = false;

    private CallThemeBackView call_theme_back;
    private ImageView call_icon;
    private TextView call_name, call_num;
    private ImageView call_close, call_open, call_back;
    private String filePath;

    private LightalkWindow() {
    }

    public static LightalkWindow getInstence() {
        if (lightalkWindow == null) {
            synchronized (LightalkWindow.class) {
                if (lightalkWindow == null) {
                    lightalkWindow = new LightalkWindow();
                }
            }
        }
        return lightalkWindow;
    }

    public static void destroy() {
        lightalkWindow = null;
    }

    public void initView(final Context context, boolean init) {
        if (wm != null && !init) return;
        this.context = context.getApplicationContext();
        //窗口管理器
        wm = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        //布局参数
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View
                    .SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View
                    .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        } else {
            layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN |
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN | WindowManager
//                    .LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR | WindowManager.LayoutParams
//                    .FLAG_TRANSLUCENT_NAVIGATION;
//        } else {
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
//        }


        layoutParams.flags = 21497344;
        if (Build.VERSION.SDK_INT >= 19) {
            layoutParams.flags |= 134217728;
            layoutParams.flags |= 67108864;
        }


        //类型（系统锁屏之上）

        if (Build.VERSION.SDK_INT > 25) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }

        layoutParams.format = PixelFormat.TRANSPARENT;

        //////////////////////////////////////////////////////////////////////
        mView = View.inflate(this.context, R.layout.light_talk_layout, null);

        call_theme_back = mView.findViewById(R.id.call_theme_back);
        call_icon = mView.findViewById(R.id.call_icon);
        call_name = mView.findViewById(R.id.call_name);
        call_num = mView.findViewById(R.id.call_num);
        call_close = mView.findViewById(R.id.call_close);
        call_open = mView.findViewById(R.id.call_open);
        call_back = mView.findViewById(R.id.call_back);

        call_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeScreenLock();
                CallTool.rejectCall();
            }
        });
        call_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeScreenLock();
                CallTool.answerPhone(context);
            }
        });
    }

    private boolean getShowBackPath() {
        //获取展示文件地址
//        String url = SharedPref.getString(context, SharedPref.CALL_THEME);
//        if (TextUtils.isEmpty(url)) return false;
//        if (TextUtils.equals("defaultvideourl", url)) {
//            filePath = DownloadTool.VIDEO_PATH + "defaultvideo.mp4";
//        } else if (TextUtils.equals("defaultimgurl", url)) {
//            filePath = DownloadTool.IMG_PATH + "defaultimg.jpg";
//        } else {
//            ThemeDBHelper dbHelper = new ThemeDBHelper(context);
//            ThemeContent oneTheme = dbHelper.getOneTheme(url);
//            dbHelper.close();
//            filePath = oneTheme.getDownloadPath();
//        }
        return true;
    }

    /**
     * 设置数据
     */
    public void setData(ContactContent contactContent, String phone, int type) {

        if (contactContent != null) {
            call_name.setText(contactContent.getName());
            if (contactContent.getIcon() != null)
                call_icon.setImageBitmap(contactContent.getIcon());
        } else {
            call_name.setText("Unknown caller");
        }
        call_num.setText(phone);
        if (type == 1) {
//            Picasso.with(context).load(R.mipmap.call_theme2).config(Bitmap.Config.RGB_565).into(call_back);
        } else if (type == 2) {
//            Picasso.with(context).load(R.mipmap.call_theme3).config(Bitmap.Config.RGB_565).into(call_back);
        } else {
            call_back.setImageBitmap(null);
        }
    }

    /**
     * 关闭锁屏
     */
    private void closeScreenLock() {
        hidelockScreen();
    }

    /**
     * 显示锁屏
     */
    public void showlockScreen() {

        String videoPath = SharedPreTool.Companion.getInstance().getString(SharedPreTool.SELECT_THEME);
        if (TextUtils.isEmpty(videoPath)) {
            return;
        }

        if (!isShow) {
            if (!getShowBackPath()) return;
            if (call_theme_back == null) return;
            isShow = true;
            call_theme_back.setViewSize(ScreenTool.Companion.getInstance().getAllScreen().x, ScreenTool.Companion.getInstance().getAllScreen().y);

            ThemeContent content = DataBaseTool.Companion.getInstance().find(SharedPreTool.Companion.getInstance().getString(SharedPreTool.SELECT_THEME));
            if (content == null || TextUtils.isEmpty(content.getPath())) {
                return;
            }
            call_theme_back.show(content.getPath(), ScreenTool.Companion.getInstance().getAllScreen());
            wm.addView(mView, layoutParams);
//            MainApplication.getInstance().isCallShow = true;
            showAnim = true;
            callAnim(call_open);
        }
    }

    /**
     * 取消锁屏
     */
    public void hidelockScreen() {
        try {
            String videoPath = SharedPreTool.Companion.getInstance().getString(SharedPreTool.SELECT_THEME);
            if (TextUtils.isEmpty(videoPath)) {
                return;
            }

            if (isShow) {
                isShow = false;
                showAnim = false;
                if (call_theme_back != null)
                    call_theme_back.stop();
                if (wm != null && mView != null) {
                    wm.removeView(mView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowScreenLock() {
        return isShow;
    }

    private boolean showAnim = false;

    public void callAnim(View view01) {
        //动作合集
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view01, "translationX", 0, 10, 0, -10, 0)
        );
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (showAnim) {
                    set.setDuration(1000).start();
                }
            }
        });
        set.setDuration(1000).start();
    }

}

package com.flash.light.free.good.fashioncallflash.util;

import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flash.light.free.good.fashioncallflash.CallApplication;

/**
 * Toast工具类
 */
public class ToastUtil {

    private static Toast toast;

    public static void show(int res) {
        show(CallApplication.getContext().getString(res), Toast.LENGTH_SHORT);
    }

    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(int res, int duration) {
        show(CallApplication.getContext().getString(res), duration);
    }

    public static void show(CharSequence text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(CallApplication.getContext(), text, duration);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 带图片的toast
     */
    public static void showImageToast(String content, int imageID) {
        Toast toast = Toast.makeText(CallApplication.getContext(), content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout layout = (LinearLayout) toast.getView();
        ImageView image = new ImageView(CallApplication.getContext());
        image.setImageResource(imageID);
        layout.addView(image, 0);
        toast.show();
    }

}

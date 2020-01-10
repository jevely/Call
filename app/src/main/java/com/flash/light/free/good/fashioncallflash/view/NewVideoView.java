package com.flash.light.free.good.fashioncallflash.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.flash.light.free.good.fashioncallflash.tool.ScreenTool;
import com.flash.light.free.good.fashioncallflash.util.UtilsKt;

public class NewVideoView extends VideoView {
    public NewVideoView(Context context) {
        this(context, null);
    }

    public NewVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = getDefaultSize(getWidth(), widthMeasureSpec);
//        int height = getDefaultSize(getHeight(), heightMeasureSpec);


        setMeasuredDimension(ScreenTool.Companion.getInstance().getAllScreen().x, ScreenTool.Companion.getInstance().getAllScreen().y);
    }
}

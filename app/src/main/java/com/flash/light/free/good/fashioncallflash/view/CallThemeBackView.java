package com.flash.light.free.good.fashioncallflash.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.flash.light.free.good.fashioncallflash.R;
import com.flash.light.free.good.fashioncallflash.util.ResourceUtil;

import java.io.File;


/**
 * 来电背景
 */
public class CallThemeBackView extends LinearLayout {

    private ImageView call_theme_back_img;
    private NewVideoView call_theme_back_video;

    private String showType;

    private Context context;

    public CallThemeBackView(Context context) {
        this(context, null);
    }

    public CallThemeBackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CallThemeBackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.callthemebacklayout, null);
        addView(view);
        init(view);
    }

    /**
     * 初始化
     */
    private void init(View view) {
        call_theme_back_img = view.findViewById(R.id.call_theme_back_img);
        call_theme_back_video = view.findViewById(R.id.call_theme_back_video);
    }

    //图片展示
    private void imageShow(String imagePath, Point point) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        call_theme_back_img.setImageBitmap(bitmap);
        call_theme_back_img.setVisibility(VISIBLE);
        ViewGroup.LayoutParams layoutParams = call_theme_back_img.getLayoutParams();
        layoutParams.width = point.x;
        layoutParams.height = point.y;
        call_theme_back_img.setLayoutParams(layoutParams);
    }

    //video展示
    private void videoShow(String videoName, final Point point) {
        call_theme_back_video.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" +
                ResourceUtil.INSTANCE.getRaw(getContext(),videoName)));
//        call_theme_back_video.setVideoPath(videoPath);
        call_theme_back_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                call_theme_back_video.start();
            }
        });
        call_theme_back_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
                call_theme_back_video.start();
            }
        });
        call_theme_back_video.setVisibility(VISIBLE);
        ViewGroup.LayoutParams layoutParams2 = call_theme_back_video.getLayoutParams();
        layoutParams2.width = point.x;
        layoutParams2.height = point.y;
        call_theme_back_video.setLayoutParams(layoutParams2);
    }

    public void show(String resoucePath, Point point) {
//        File file = new File(resoucePath);
//        String[] names = file.getName().split("\\.");
//        String type = names[names.length - 1];
//        showType = type;
//        if (TextUtils.equals(type, "mp4") || TextUtils.equals(type, "MP4")) {
            videoShow(resoucePath, point);
//        } else {
//            imageShow(resoucePath, point);
//        }
    }

    public void stop() {
        if (TextUtils.equals(showType, "mp4") || TextUtils.equals(showType, "MP4")) {
            call_theme_back_video.setVisibility(GONE);
            call_theme_back_video.stopPlayback();
        } else {
            call_theme_back_img.setVisibility(GONE);
        }
    }

    public void setViewSize(int x, int y) {
        ViewGroup.LayoutParams layoutParams3 = getLayoutParams();
        layoutParams3.width = x;
        layoutParams3.height = y;
        setLayoutParams(layoutParams3);
    }

}

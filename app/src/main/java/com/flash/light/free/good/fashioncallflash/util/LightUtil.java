package com.flash.light.free.good.fashioncallflash.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.animation.LinearInterpolator;

import com.flash.light.free.good.fashioncallflash.CallApplication;

/**
 * Created by A03 on 2018/4/24.
 */

public class LightUtil {

    private CameraManager mCameraManager;
    private Camera mCamera;
    private static boolean isOpenLoad = false;
    public static boolean isOn = true;
    public static String select_mode = "sos";

    private ValueAnimator valueAnim, valueAnim2;
    private AnimatorSet animSet;

    private static LightUtil lightUtil;

    public static LightUtil getInstance() {
        if (null == lightUtil) {
            lightUtil = new LightUtil();
        }
        return lightUtil;
    }

    public void on() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCameraManager = (CameraManager) CallApplication.getContext().getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraManager.setTorchMode("0", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                mCamera = Camera.open();
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isOpenLoad = true;
    }

    public void off() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCameraManager = (CameraManager) CallApplication.getContext().getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraManager.setTorchMode("0", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
        isOpenLoad = false;
    }

    public void sos() {
//        cancel();

        ValueAnimator valueAnim = ValueAnimator.ofInt(0);
        valueAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                flash();
            }
        });
        valueAnim.setInterpolator(new LinearInterpolator());
        valueAnim.setRepeatCount(3);
        valueAnim.setDuration(1000 / 3);

        ValueAnimator valueAnim2 = ValueAnimator.ofInt(0);
        valueAnim2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                flash();
            }
        });
        valueAnim2.setInterpolator(new LinearInterpolator());
        valueAnim2.setRepeatCount(3);
        valueAnim2.setDuration(1000);

        ValueAnimator valueAnim3 = ValueAnimator.ofInt(0);
        valueAnim3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                flash();
            }
        });
        valueAnim3.setInterpolator(new LinearInterpolator());
        valueAnim3.setRepeatCount(3);
        valueAnim3.setDuration(1000 / 3);

        //一组
        animSet = new AnimatorSet();
        animSet.playSequentially(valueAnim, valueAnim2, valueAnim3);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                off();
//            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                animation.setStartDelay(2000);
//                animation.start();
            }
        });
    }

    ValueAnimator valueAnim9;

    //闪烁
    private void flash() {
        valueAnim9 = ValueAnimator.ofInt(0);
        valueAnim9.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                if (isOpenLoad) {
                    off();
                } else {
                    on();
                }
            }
        });
        valueAnim9.setRepeatCount(2);
        valueAnim9.setDuration(200).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 99) {
                //Log.e("zzz","cancel");
                handler.removeMessages(1);
                handler.removeMessages(11);
                handler.removeMessages(12);
            } else if (msg.what == 11 && isParty) {
                //Log.e("zzz","party");
                if (isOpenLoad) {
                    off();
                } else {
                    on();
                }
                handler.sendEmptyMessageDelayed(11, 500);
            } else if (msg.what == 1 && isFm) {
                //Log.e("zzz","fm");
                flash();
                handler.sendEmptyMessageDelayed(1, fm_time);
            } else if (msg.what == 12 && isSOS) {
                //Log.e("zzz","sos");
                sos();
                handler.sendEmptyMessageDelayed(12, 8000);
            }
        }
    };

    private int fm_time = 1000;
    private boolean isParty = false;
    private boolean isFm = false;
    private boolean isSOS = false;


    public void party2() {
        resetState();
        if (!isOpenLoad) {
            on();
        }
        //Log.e("zzz","party2");
        isParty = true;
        handler.sendEmptyMessageDelayed(11, 500);
    }

    public void fm2(int mode) {
        resetState();
        if (!isOpenLoad) {
            on();
        }
        isFm = true;
        fm_time = 1000 / mode;
        handler.sendEmptyMessageDelayed(1, fm_time);
    }

    public void sos2() {
        resetState();

        if (!isOpenLoad) {
            on();
        }

        isSOS = true;
        sos();

        handler.sendEmptyMessageDelayed(12, 8000);
    }

    public void open2() {
        resetState();
        on();
    }

    public void cancel2() {
        if (SharedPreTool.Companion.getInstance().getBoolean(SharedPreTool.CALL_FLASH)) {
            resetState();
            off();
        }
    }

    public void resetState() {
        isParty = false;
        isFm = false;
        isSOS = false;
        handler.removeMessages(1);
        handler.removeMessages(11);
        handler.removeMessages(12);
        if (null != animSet) {
            animSet.cancel();
            animSet.end();
        }
        if (null != valueAnim9) {
            valueAnim9.cancel();
            valueAnim9.end();
        }
    }


    public void switchMode(String select_mode) {

        if (!SharedPreTool.Companion.getInstance().getBoolean(SharedPreTool.CALL_FLASH)) {
            return;
        }

        if (TextUtils.isEmpty(select_mode)) {
            return;
        }

        this.select_mode = select_mode;
        if (isOn) {
            switch (select_mode) {
                case "0":
                    open2();
                    break;
                case "Party":
                    party2();
                    break;
                case "SOS":
                    sos2();
                    break;
                default:
                    int time = Integer.valueOf(select_mode);
                    fm2(time);
                    break;

            }
        } else {
            cancel2();
        }
    }

}

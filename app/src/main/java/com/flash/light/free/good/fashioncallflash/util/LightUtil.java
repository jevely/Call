package com.flash.light.free.good.fashioncallflash.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.flash.light.free.good.fashioncallflash.CallApplication;

import org.json.JSONObject;


/**
 * Created by A03 on 2018/4/24.
 */

public class LightUtil {

    private CameraManager mCameraManager;
    private Camera mCamera;
    private static boolean isOpenLoad = false;
//    public static boolean isOn = MainApplication.getInstance().isOn;
//    public static String select_mode = MainApplication.getInstance().select_mode;
    public static boolean isOn =true;
    public static String select_mode = "";

    private ValueAnimator valueAnim,valueAnim2;
    private AnimatorSet animSet;
    private ImageView flash_view;

    private static LightUtil lightUtil;
    public static LightUtil getInstance(){
        if(null==lightUtil) {
            lightUtil = new LightUtil();
        }
        return lightUtil;
    }


    public void setView(ImageView view){
        flash_view = view;
    }

    public static void toggle(Context context, boolean state) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                //获取CameraManager
                CameraManager mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                //获取当前手机所有摄像头设备ID
                String[] ids = new String[0];
                ids = mCameraManager.getCameraIdList();
                for (String id : ids) {
                    CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
                    //查询该摄像头组件是否包含闪光灯
                    Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                    /*
                    * 获取相机面对的方向
                    * CameraCharacteristics.LENS_FACING_FRONT 前置摄像头
                    * CameraCharacteristics.LENS_FACING_BACK 后只摄像头
                    * CameraCharacteristics.LENS_FACING_EXTERNAL 外部的摄像头
                    */
                    Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                    if (flashAvailable != null && flashAvailable
                            && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        //打开或关闭手电筒
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Log.e("zzz", id);
                            mCameraManager.setTorchMode(id, state);
                        }
                    }
                }

            } catch (CameraAccessException e) {
                Log.e("zzzz", e.getMessage());
                e.printStackTrace();
            }
        }
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
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        isOpenLoad = true;
//        if(null!=flash_view)
//            flash_view.setImageDrawable(CallApplication.getContext().getResources().getDrawable(R.drawable.small_light_on));
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
//        if(null!=flash_view)
//            flash_view.setImageDrawable(CallApplication.getContext().getResources().getDrawable(R.drawable.small_light_un));
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingPermission")
    public static boolean isOn(Context context) {
        CameraManager mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                mCameraManager.openCamera("0", new CameraDevice.StateCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onOpened(@NonNull CameraDevice camera) {
                        Log.e("zzz", "zzz");
                        try {
                            CaptureRequest.Builder builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                            CaptureRequest request = builder.build();
                            Log.e("zzzddd",CaptureRequest.FLASH_MODE.getName());
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice camera) {
                        Log.e("zzz", "zzz1");
                    }

                    @Override
                    public void onError(@NonNull CameraDevice camera, int error) {
                        Log.e("zzz", error + "");
                    }
                }, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
//        CaptureRequest.Builder builder = mDevice.createCaptureRequest(
//                CameraDevice.TEMPLATE_STILL_CAPTURE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            CaptureRequest request = builder.build();
//        }

//        boolean isFlag = false;
//        try {
//            Camera camera = Camera.open();
//            Camera.Parameters parameters = camera.getParameters();
//            String flashMode = parameters.getFlashMode();
//            Log.e("zzzmode",flashMode);
//            if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
//                isFlag = true;
//            }
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//            return isFlag;
//        } catch (Exception e) {
//            Log.e("zzz123",e.getMessage());
//            return false;
//        }
//    }


    public void sos(){
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
        valueAnim.setDuration(1000/3);

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
        valueAnim3.setDuration(1000/3);

        //一组
        animSet = new AnimatorSet();
        animSet.playSequentially(valueAnim,valueAnim2,valueAnim3);
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

    public void fm(int mode){
        cancel();
        valueAnim2 = ValueAnimator.ofInt(0);
        valueAnim2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                flash();
            }
        });
        valueAnim2.setRepeatCount(-1);
        valueAnim2.setDuration(mode*1000).start();
    }


    //闪烁
    public void party(){
        cancel();
        valueAnim = ValueAnimator.ofInt(0);
        valueAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                if(isOpenLoad){
                    off();
                }else{
                    on();
                }
            }
        });
        valueAnim.setRepeatCount(-1);
        valueAnim.setDuration(500).start();
    }

    ValueAnimator valueAnim9;
    //闪烁
    private void flash(){
        valueAnim9 = ValueAnimator.ofInt(0);
        valueAnim9.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                if(isOpenLoad){
                    off();
                }else{
                    on();
                }
            }
        });
        valueAnim9.setRepeatCount(2);
        valueAnim9.setDuration(200).start();
    }

    public void cancel(){
        if(null!=valueAnim){
            valueAnim.cancel();
            valueAnim.end();
            valueAnim = null;
        }
        if(null!=valueAnim2){
            valueAnim2.cancel();
            valueAnim2.end();
            valueAnim2 = null;
        }
        if(null!=animSet){
            animSet.cancel();
            animSet.end();
            animSet = null;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==99){
                Log.e("zzz","cancel");
                handler.removeMessages(1);
                handler.removeMessages(11);
                handler.removeMessages(12);
            }else if(msg.what==11&&isParty) {
                Log.e("zzz","party");
                if(isOpenLoad){
                    off();
                }else{
                    on();
                }
                handler.sendEmptyMessageDelayed(11, 500);
            }else if(msg.what==1&&isFm){
                Log.e("zzz","fm");
                flash();
                handler.sendEmptyMessageDelayed(1,fm_time);
            }else if(msg.what==12&&isSOS){
                Log.e("zzz","sos");
                sos();
                handler.sendEmptyMessageDelayed(12,8000);
            }
        }
    };

    private int fm_time = 1000;
    private boolean isParty = false;
    private boolean isFm = false;
    private boolean isSOS = false;


    public void party2(){
        resetState();
        if(!isOpenLoad) {
            on();
        }
        Log.e("zzz","party2");
        isParty = true;
        handler.sendEmptyMessageDelayed(11,500);
    }

    public void fm2(int mode){
        resetState();
        if(!isOpenLoad) {
            on();
        }
        isFm = true;
        fm_time = 1000/mode;
        handler.sendEmptyMessageDelayed(1,fm_time);
    }

    public void sos2(){
        resetState();
        if(!isOpenLoad) {
            on();
        }
        isSOS = true;
        sos();
        handler.sendEmptyMessageDelayed(12,8000);
    }

    public void open2(){
        resetState();
        on();
    }

    public void cancel2(){
        resetState();
        off();
//        handler.sendEmptyMessage(99);
    }

    public void resetState(){
        isParty = false;
        isFm = false;
        isSOS = false;
        handler.removeMessages(1);
        handler.removeMessages(11);
        handler.removeMessages(12);
        if(null!=animSet){
            animSet.cancel();
            animSet.end();
        }
        if(null!=valueAnim9){
            valueAnim9.cancel();
            valueAnim9.end();
        }
    }


    public void switchMode(String select_mode){
        if(TextUtils.isEmpty(select_mode)){
            return;
        }
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("mode",select_mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.select_mode = select_mode;
        if(isOn){
            switch (select_mode){
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
        }else{
            cancel2();
        }
    }

    //短信闪
    public void smsFlash(Context context){
//        if(SharedPref.getBoolean(context,SharedPref.SMS_ALERT,false)) {
//            flash();
//        }
//        switchMode(select_mode);
    }
    //来电闪
    public void callFlash(Context context){
//        if(SharedPref.getBoolean(context,SharedPref.CALL_ALERT,false)) {
//            party2();
//        }
    }

    public void stopCallFlash(Context context){
//        cancel2();
//        if(SharedPref.getBoolean(context,SharedPref.CALL_ALERT,false)) {
//            switchMode(select_mode);
//        }
    }


}

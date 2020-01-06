package com.flash.light.free.good.fashioncallflash.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.flash.light.free.good.fashioncallflash.R

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //打印hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.e("KeyHash->", KeyHash);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Handler().postDelayed(Runnable {
            startActivity(
                Intent(
                    this@SplashActivity,
                    MainActivity::class.java
                )
            )
            finish()
        }, 1500)
    }
}

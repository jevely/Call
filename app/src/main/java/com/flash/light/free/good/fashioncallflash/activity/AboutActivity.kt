package com.flash.light.free.good.fashioncallflash.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.flash.light.free.good.fashioncallflash.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val textView = findViewById<TextView>(R.id.textView)
        try {
            textView.text = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
            textView.visibility = View.GONE
        }
        findViewById<ImageView>(R.id.setting_back).setOnClickListener {
            finish()
        }
    }
}

package com.flash.light.free.good.fashioncallflash.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.flash.light.free.good.fashioncallflash.R
import com.flash.light.free.good.fashioncallflash.adapter.MainAdapter
import com.flash.light.free.good.fashioncallflash.tool.ScreenTool
import com.flash.light.free.good.fashioncallflash.util.SharedPreTool
import com.flash.light.free.good.fashioncallflash.util.setWhite

class OldMainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var main_setting: ImageView
    private lateinit var adapter: MainAdapter
    private lateinit var viewpager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_main)
        //获取屏幕宽度，展示来电秀的时候用，这样可以使它全屏展示
        ScreenTool.getInstance().setScreen(this)
        init()

        val count = SharedPreTool.getInstance().getInt(SharedPreTool.INAPP_COUNT)
        if (count >= 1) {
            setWhite(this)
        } else {
            Thread(Thr()).start()
        }
        SharedPreTool.getInstance().putInt(SharedPreTool.INAPP_COUNT, count + 1)

    }

    private fun init() {
        main_setting = findViewById(R.id.main_setting)
        viewpager = findViewById(R.id.viewpager)

        main_setting.setOnClickListener(this)

        adapter = MainAdapter()
        viewpager.adapter = adapter
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.main_setting -> startActivity(
                Intent(
                    this@OldMainActivity,
                    SettingActivity::class.java
                )
            )
        }
    }

    private inner class Thr : Runnable {
        override fun run() {
            try {
                Thread.sleep(1000)
                handler.sendEmptyMessage(0)
                Thread.sleep(1000)
                handler.sendEmptyMessage(1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                viewpager.setCurrentItem(1, true)
            } else {
                viewpager.setCurrentItem(0, true)
            }
        }
    }

}

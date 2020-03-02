package com.flash.light.free.good.fashioncallflash.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.flash.light.free.good.fashioncallflash.R
import com.flash.light.free.good.fashioncallflash.adapter.MainAdapter
import com.flash.light.free.good.fashioncallflash.tool.ScreenTool
import com.flash.light.free.good.fashioncallflash.util.SharedPreTool
import com.flash.light.free.good.fashioncallflash.util.setWhite
import com.flash.light.free.good.fashioncallflash.view.NewGridManager

class OldMainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var main_setting: ImageView
    private lateinit var main_recyclerview: RecyclerView
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_main)
        //获取屏幕宽度，展示来电秀的时候用，这样可以使它全屏展示
        ScreenTool.getInstance().setScreen(this)
        init()

        val count = SharedPreTool.getInstance().getInt(SharedPreTool.START_COUNT)
        if (count >= 1) {
            setWhite(this)
        }
        SharedPreTool.getInstance().putInt(SharedPreTool.START_COUNT, count + 1)
    }

    private fun init() {
        main_setting = findViewById(R.id.main_setting)
        main_recyclerview = findViewById(R.id.main_recyclerview)

        main_setting.setOnClickListener(this)

        main_recyclerview.layoutManager = NewGridManager(this, 2)
        adapter = MainAdapter()
        main_recyclerview.adapter = adapter
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
}

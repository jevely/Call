package com.flash.light.free.good.flash.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.flash.light.free.good.flash.CallApplication
import com.flash.light.free.good.flash.R
import com.flash.light.free.good.flash.tool.ContactTool
import com.flash.light.free.good.flash.util.SharedPreTool

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var setting_back: ImageView
    private lateinit var setting_call_switch: SwitchCompat
    private lateinit var setting_flash_switch: SwitchCompat
    private lateinit var setting_contact_re: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setting_back = findViewById(R.id.setting_back)
        setting_call_switch = findViewById(R.id.setting_call_switch)
        setting_flash_switch = findViewById(R.id.setting_flash_switch)
        setting_contact_re = findViewById(R.id.setting_contact_re)

        val call_theme_switch =
            SharedPreTool.getInstance().getBoolean(SharedPreTool.CALL_THEME_SWITCH)
        setting_call_switch.isChecked = call_theme_switch

        val call_flash_switch =
            SharedPreTool.getInstance().getBoolean(SharedPreTool.CALL_FLASH)
        setting_flash_switch.isChecked = call_flash_switch

        setting_back.setOnClickListener(this)
        setting_contact_re.setOnClickListener(this)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.setting_back -> finish()
            R.id.setting_contact_re -> {
                ContactTool.getInstence().getAllContact()
                Toast.makeText(CallApplication.getContext(), "Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        setting_call_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPreTool.getInstance().putBoolean(SharedPreTool.CALL_THEME_SWITCH, isChecked)
        }

        setting_flash_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            SharedPreTool.getInstance().putBoolean(SharedPreTool.CALL_FLASH, isChecked)
        }
    }
}

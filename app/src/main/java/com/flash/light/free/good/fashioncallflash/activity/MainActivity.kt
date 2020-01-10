package com.flash.light.free.good.fashioncallflash.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.flash.light.free.good.fashioncallflash.R
import com.flash.light.free.good.fashioncallflash.adapter.MainAdapter
import com.flash.light.free.good.fashioncallflash.tool.ContactTool
import com.flash.light.free.good.fashioncallflash.tool.ScreenTool
import com.flash.light.free.good.fashioncallflash.util.Permission
import com.flash.light.free.good.fashioncallflash.util.SharedPreTool
import com.flash.light.free.good.fashioncallflash.util.setWhite
import com.flash.light.free.good.fashioncallflash.view.NewGridManager
import com.flash.light.free.good.fashioncallflash.view.PermissionDialog
import com.flash.light.free.good.fashioncallflash.view.WhiteDialog

class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var main_recyclerview: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var main_setting: ImageView

    private var SET_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenTool.getInstance().setScreen(this)
        init()

        val count = SharedPreTool.getInstance().getInt(SharedPreTool.START_COUNT)
        if (count == 0) {
            val permissionDialog = PermissionDialog(this)
            permissionDialog.setCallBack(object : PermissionDialog.ClickCallBack {
                override fun click() {
                    requestPermission(
                        this@MainActivity,
                        arrayOf(
//                            Permission.WRITE,
//                            Permission.READ,
                            Permission.CALL,
                            Permission.CALL_NUM,
                            Permission.CONTACTS
                        ),
                        SET_REQUEST
                    )
                }
            })
            permissionDialog.show()

        } else if (count >= 1) {
            setWhite(this)
        }
        SharedPreTool.getInstance().putInt(SharedPreTool.START_COUNT, count + 1)
    }

    private fun init() {
        main_setting = findViewById(R.id.main_setting)
        main_recyclerview = findViewById(R.id.main_recyclerview)
        main_recyclerview.layoutManager = NewGridManager(this, 2)

        adapter = MainAdapter(this)

        main_recyclerview.adapter = adapter

        main_setting.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.main_setting -> startActivity(
                Intent(
                    this@MainActivity,
                    SettingActivity::class.java
                )
            )
        }
    }

//    override fun requestSuccess(requestCode: Int, permission: List<String>) {
//        if (permission.contains(Permission.CONTACTS)) {
//            ContactTool.getInstence().getAllContact()
//        }
//    }
//
//    override fun requestError(requestCode: Int, permission: List<String>) {
//        super.requestError(requestCode, permission)
//    }

}

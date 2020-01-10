package com.flash.light.free.good.fashioncallflash.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.flash.light.free.good.fashioncallflash.CallApplication
import com.flash.light.free.good.fashioncallflash.R
import com.flash.light.free.good.fashioncallflash.db.ThemeContent
import com.flash.light.free.good.fashioncallflash.net.DownloadCallBack
import com.flash.light.free.good.fashioncallflash.net.NetTool
import com.flash.light.free.good.fashioncallflash.tool.ContactTool
import com.flash.light.free.good.fashioncallflash.tool.DataTool
import com.flash.light.free.good.fashioncallflash.tool.NotificationTool
import com.flash.light.free.good.fashioncallflash.util.*
import com.flash.light.free.good.fashioncallflash.view.CallThemeBackView
import com.flash.light.free.good.fashioncallflash.view.PhonePermissionDialog
import com.flash.light.free.good.fashioncallflash.window.LightalkWindow

class ShowActivity : BaseActivity() {

    private lateinit var calltheme: CallThemeBackView
    private lateinit var show_iv: ImageView
    private lateinit var call_bt: Button
    private lateinit var download_bt: Button
    private var SET_REQUEST = 1
    private lateinit var call_load: RelativeLayout
    private lateinit var downloadThread: Thread

    private lateinit var themeContent: ThemeContent

    private var isRequestWindowPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        init()
    }

    private fun init() {
        val intent = intent
        val position = intent.getIntExtra("position", 0)
        themeContent = DataTool.getInstance().themeList[position]

        call_load = findViewById(R.id.call_load)
        calltheme = findViewById(R.id.calltheme)
        show_iv = findViewById(R.id.show_iv)
        call_bt = findViewById(R.id.call_bt)
        download_bt = findViewById(R.id.download_bt)

        Glide
            .with(this)
            .load(themeContent.image_url)
            .placeholder(R.mipmap.ic_launcher)
            .override(getScreen().x, getScreen().y)
            .centerCrop()
            .into(show_iv)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission(
                this@ShowActivity,
                arrayOf(
//                    Permission.WRITE,
//                    Permission.READ,
                    Permission.CALL,
                    Permission.CALL_NUM,
                    Permission.CONTACTS
                ),
                SET_REQUEST
            )
        } else {
            startDownload()
        }


        val url = SharedPreTool.getInstance().getString(SharedPreTool.SELECT_THEME)
        if (TextUtils.equals(url, themeContent.video_url)) {
            call_bt.text = resources.getString(R.string.theme_select)
        }

        call_bt.setOnClickListener {
            permissionRequest()
        }

        download_bt.setOnClickListener {
            startDownload()
        }
    }

    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                call_bt.visibility = View.VISIBLE
                calltheme.show(msg.obj as String?, getScreen())
            } else if (msg.what == 1) {
                Toast.makeText(
                    CallApplication.getContext(),
                    "Download error",
                    Toast.LENGTH_SHORT
                ).show()
                download_bt.visibility = View.VISIBLE
            }
            call_load.visibility = View.GONE
        }
    }

    override fun requestSuccess(requestCode: Int, permission: List<String>) {
        super.requestSuccess(requestCode, permission)
        if (
//            permission.contains(Permission.WRITE)
//            && permission.contains(Permission.READ)
//            &&
            permission.contains(Permission.CALL)
            && permission.contains(Permission.CALL_NUM)
            && permission.contains(Permission.CONTACTS)

        ) {
            if (requestCode == SET_REQUEST) {
                ContactTool.getInstence().getAllContact()
                startDownload()
            }
        }
    }

    override fun requestError(requestCode: Int, permission: List<String>) {
        super.requestError(requestCode, permission)
        if (
//            permission.contains(Permission.WRITE)
//            || permission.contains(Permission.READ)
//            ||
            permission.contains(Permission.CALL)
            || permission.contains(Permission.CALL_NUM)
            || permission.contains(Permission.CONTACTS)
        ) {
            Toast.makeText(
                CallApplication.getContext(),
                "Please allow the permissions",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startDownload() {
        download_bt.visibility = View.GONE
        call_load.visibility = View.VISIBLE
        downloadThread = Thread(Runnable {
            try {
                NetTool.downloadWallPaper(
                    themeContent.video_url,
                    object : DownloadCallBack {
                        override fun downloadSuccess(path: String) {
                            Logger.d("下载主题完成")
                            val msg = handler.obtainMessage()
                            msg.what = 0
                            msg.obj = path
                            handler.sendMessage(msg)
                        }

                        override fun downloadError() {
                            Logger.d("下载主题出错")
                            handler.sendEmptyMessage(1)
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                Logger.d("现在线程终止")
                handler.sendEmptyMessage(1)
            }
        })
        downloadThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(null)

        try {
            if (downloadThread != null) {
                downloadThread.interrupt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        if (requestCode == 1) {
            Logger.d("权限开启成功")
//            Handler().postDelayed(Runnable { permissionRequest() }, 2000)
        }
    }

    private fun permissionRequest() {
        if (!checkFloatWindowPermission(this) && !checkAlertWindowsPermission(this)) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && NotificationTool.checkNotificationPermission(
                    this
                )
            ) {
                val permissionDialog =
                    PhonePermissionDialog(
                        this,
                        PhonePermissionDialog.FLOAT
                    )
                permissionDialog.setCallBack(object : PhonePermissionDialog.OnCallBack {

                    override fun click() {
                        isRequestWindowPermission = true
                    }

                    override fun cancel() {
                        ToastUtil.show(R.string.dialog_permission_fail)
                    }
                })
                permissionDialog.show()
                permissionDialog.setBtClick(PhonePermissionDialog.FLOAT)
                permissionDialog.setBtUnclick(PhonePermissionDialog.NOTIFY)
            } else {
                val permissionDialog =
                    PhonePermissionDialog(
                        this,
                        PhonePermissionDialog.ALL
                    )
                permissionDialog.setCallBack(object : PhonePermissionDialog.OnCallBack {

                    override fun click() {
                        isRequestWindowPermission = true
                    }

                    override fun cancel() {
                        ToastUtil.show(R.string.dialog_permission_fail)
                    }
                })
                permissionDialog.show()
                permissionDialog.setBtClick(PhonePermissionDialog.FLOAT)
                permissionDialog.setBtUnclick(PhonePermissionDialog.NOTIFY)
            }
            return
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && !NotificationTool.checkNotificationPermission(
                this
            )
        ) {
            val permissionDialog =
                PhonePermissionDialog(
                    this,
                    PhonePermissionDialog.NOTIFY
                )
            permissionDialog.setCallBack(object : PhonePermissionDialog.OnCallBack {

                override fun click() {
                    isRequestWindowPermission = true
                }

                override fun cancel() {
                    ToastUtil.show(R.string.dialog_permission_fail)
                }
            })
            permissionDialog.show()
            permissionDialog.setBtClick(PhonePermissionDialog.NOTIFY)
            permissionDialog.setBtUnclick(PhonePermissionDialog.FLOAT)
            return
        }

        SharedPreTool.getInstance()
            .putString(SharedPreTool.SELECT_THEME, themeContent.video_url!!)
        call_bt.text = resources.getString(R.string.theme_select)
        Toast.makeText(this@ShowActivity, "success", Toast.LENGTH_SHORT).show()
        SharedPreTool.getInstance().putBoolean(SharedPreTool.CALL_THEME_SWITCH, true)

//        LightalkWindow.getInstence().initView(this, true)
//                LightalkWindow.getInstence().setData(
//                    null,
//                    "123",
//                    1
//                )
//                LightalkWindow.getInstence().showlockScreen()
    }

    override fun onResume() {
        super.onResume()
        if (isRequestWindowPermission) {
            isRequestWindowPermission = false
            Handler().postDelayed(Runnable { permissionRequest() }, 500)
        }
    }

}

package com.flash.light.free.good.flash.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.flash.light.free.good.flash.CallApplication
import com.flash.light.free.good.flash.R
import com.flash.light.free.good.flash.db.ThemeContent
import com.flash.light.free.good.flash.tool.ContactTool
import com.flash.light.free.good.flash.tool.DataTool
import com.flash.light.free.good.flash.tool.NotificationTool
import com.flash.light.free.good.flash.util.*
import com.flash.light.free.good.flash.view.CallThemeBackView
import com.flash.light.free.good.flash.view.OutPermissionDialog
import com.flash.light.free.good.flash.view.PermissionDialog
import java.nio.file.Path

class ContentActivity : BaseActivity() {

    private lateinit var calltheme: CallThemeBackView
    private lateinit var show_iv: ImageView
    private lateinit var call_bt: Button
    private var SET_REQUEST = 1
    private lateinit var call_load: RelativeLayout
    private lateinit var downloadThread: Thread

    private lateinit var themeContent: ThemeContent

    private var isRequestWindowPermission = false

    private var path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        init()
    }

    private fun init() {
        val intent = intent
        val position = intent.getIntExtra("position", -1)
        path = intent.getStringExtra("path")

        call_load = findViewById(R.id.call_load)
        calltheme = findViewById(R.id.calltheme)
        show_iv = findViewById(R.id.show_iv)
        call_bt = findViewById(R.id.call_bt)

        val url = SharedPreTool.getInstance().getString(SharedPreTool.SELECT_THEME)

        if (TextUtils.isEmpty(path)) {
            themeContent = DataTool.getInstance().themeList[position]

            if (TextUtils.equals(url, themeContent.video_name)) {
                call_bt.text = resources.getString(R.string.theme_select)
            }

            Glide
                .with(this)
                .load(themeContent.image_url)
                .placeholder(R.mipmap.defult_img)
                .override(getScreen().x, getScreen().y)
                .centerCrop()
                .into(show_iv)
        } else {
            if (TextUtils.equals(url, path)) {
                call_bt.text = resources.getString(R.string.theme_select)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission(this, Permission.CALL) || !checkPermission(
                    this,
                    Permission.CONTACTS
                )
            ) {
                val permissionDialog = PermissionDialog(
                    this,
                    checkPermission(this, Permission.CALL),
                    checkPermission(this, Permission.CONTACTS)
                )
                permissionDialog.setCallBack(object : PermissionDialog.ClickCallBack {
                    override fun click() {
                        if (checkPermission(this@ContentActivity, Permission.CALL)) {
                            requestPermission(
                                this@ContentActivity,
                                arrayOf(Permission.CONTACTS),
                                SET_REQUEST
                            )
                            return
                        }

                        if (checkPermission(this@ContentActivity, Permission.CONTACTS)) {
                            requestPermission(
                                this@ContentActivity,
                                arrayOf(Permission.CALL),
                                SET_REQUEST
                            )
                            return
                        }
                        requestPermission(
                            this@ContentActivity,
                            arrayOf(
//                            Permission.WRITE,
//                            Permission.READ,
                                Permission.CALL,
//                                Permission.CALL_NUM,
                                Permission.CONTACTS
                            ),
                            SET_REQUEST
                        )
                    }
                })
                permissionDialog.show()
            } else {
                requestPermission(
                    this@ContentActivity,
                    arrayOf(
//                    Permission.WRITE,
//                    Permission.READ,
                        Permission.CALL,
//                    Permission.CALL_NUM,
                        Permission.CONTACTS
                    ),
                    SET_REQUEST
                )
            }
//            requestPermission(
//                this@ShowActivity,
//                arrayOf(
////                    Permission.WRITE,
////                    Permission.READ,
//                    Permission.CALL,
//                    Permission.CALL_NUM,
//                    Permission.CONTACTS
//                ),
//                SET_REQUEST
//            )
        } else {
            handler.sendEmptyMessage(0)
        }

        call_bt.setOnClickListener {
            permissionRequest()
        }
    }

    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                call_bt.visibility = View.VISIBLE
                if (TextUtils.isEmpty(path)) {
                    calltheme.show(themeContent.video_name, getScreen())
                } else {
                    calltheme.show(path, getScreen())
                }
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
//            && permission.contains(Permission.CALL_NUM)
            && permission.contains(Permission.CONTACTS)

        ) {
            if (requestCode == SET_REQUEST) {
                ContactTool.getInstence().getAllContact()
                handler.sendEmptyMessage(0)
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
//            || permission.contains(Permission.CALL_NUM)
            || permission.contains(Permission.CONTACTS)
        ) {
            Toast.makeText(
                CallApplication.getContext(),
                "Please allow the permissions",
                Toast.LENGTH_SHORT
            ).show()
        }
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
                    OutPermissionDialog(
                        this,
                        OutPermissionDialog.FLOAT
                    )
                permissionDialog.setCallBack(object : OutPermissionDialog.OnCallBack {

                    override fun click() {
                        isRequestWindowPermission = true
                    }

                    override fun cancel() {
                        Toast.makeText(
                            this@ContentActivity,
                            resources.getString(R.string.dialog_permission_fail),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                permissionDialog.show()
                permissionDialog.setBtClick(OutPermissionDialog.FLOAT)
                permissionDialog.setBtUnclick(OutPermissionDialog.NOTIFY)
            } else {
                if (!isFinishing) {
                    val permissionDialog =
                        OutPermissionDialog(
                            this,
                            OutPermissionDialog.ALL
                        )
                    permissionDialog.setCallBack(object : OutPermissionDialog.OnCallBack {

                        override fun click() {
                            isRequestWindowPermission = true
                        }

                        override fun cancel() {
                            Toast.makeText(
                                this@ContentActivity,
                                resources.getString(R.string.dialog_permission_fail),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                    permissionDialog.show()
                    permissionDialog.setBtClick(OutPermissionDialog.FLOAT)
                    permissionDialog.setBtUnclick(OutPermissionDialog.NOTIFY)
                }
            }
            return
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && !NotificationTool.checkNotificationPermission(
                this
            )
        ) {
            val permissionDialog =
                OutPermissionDialog(
                    this,
                    OutPermissionDialog.NOTIFY
                )
            permissionDialog.setCallBack(object : OutPermissionDialog.OnCallBack {

                override fun click() {
                    isRequestWindowPermission = true
                }

                override fun cancel() {
                    Toast.makeText(
                        this@ContentActivity,
                        resources.getString(R.string.dialog_permission_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            permissionDialog.show()
            permissionDialog.setBtClick(OutPermissionDialog.NOTIFY)
            permissionDialog.setBtUnclick(OutPermissionDialog.FLOAT)
            return
        }
        if (path == null || TextUtils.isEmpty(path)) {
            SharedPreTool.getInstance()
                .putString(SharedPreTool.SELECT_THEME, themeContent.video_name!!)
        } else {
            SharedPreTool.getInstance()
                .putString(SharedPreTool.SELECT_THEME, path!!)
        }

        call_bt.text = resources.getString(R.string.theme_select)
        Toast.makeText(this@ContentActivity, "success", Toast.LENGTH_SHORT).show()
        SharedPreTool.getInstance().putBoolean(SharedPreTool.CALL_THEME_SWITCH, true)
    }

    override fun onResume() {
        super.onResume()
        if (isRequestWindowPermission) {
            isRequestWindowPermission = false
            Handler().postDelayed(Runnable { permissionRequest() }, 500)
        }
    }

}

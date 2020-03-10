package com.flash.light.free.good.flash.activity

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.flash.light.free.good.flash.CallApplication
import com.flash.light.free.good.flash.R
import com.flash.light.free.good.flash.adapter.MainAdapter
import com.flash.light.free.good.flash.db.DataBaseTool
import com.flash.light.free.good.flash.db.ThemeContent
import com.flash.light.free.good.flash.other.ScaleInTransformer
import com.flash.light.free.good.flash.tool.ContactTool
import com.flash.light.free.good.flash.tool.ScreenTool
import com.flash.light.free.good.flash.util.Logger
import com.flash.light.free.good.flash.util.Permission
import com.flash.light.free.good.flash.util.SharedPreTool
import com.flash.light.free.good.flash.util.setWhite

class MainActivity : BaseActivity(), View.OnClickListener, MainAdapter.AddClickCallBack {

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
        }
        SharedPreTool.getInstance().putInt(SharedPreTool.INAPP_COUNT, count + 1)

    }

    private fun init() {
        main_setting = findViewById(R.id.main_setting)
        viewpager = findViewById(R.id.viewpager)

        main_setting.setOnClickListener(this)

        adapter = MainAdapter(this, this)
        viewpager.adapter = adapter

        viewpager.apply {
            offscreenPageLimit = 1
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                val padding = resources.getDimensionPixelOffset(R.dimen.dp_10) +
                        resources.getDimensionPixelOffset(R.dimen.dp_10)
                // setting padding on inner RecyclerView puts overscroll effect in the right place
                setPadding(padding, 0, padding, 0)
                clipToPadding = false
            }
        }
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(ScaleInTransformer())
        compositePageTransformer.addTransformer(MarginPageTransformer(resources.getDimension(R.dimen.dp_10).toInt()))
        viewpager.setPageTransformer(compositePageTransformer)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val selectedVideo: Uri? = data?.getData()
            val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
            val cursor: Cursor? = contentResolver.query(
                selectedVideo!!,
                filePathColumn, null, null, null
            )
            if (cursor != null) {
                cursor.moveToFirst()
                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                val VIDEOPATH = cursor.getString(columnIndex)
                Logger.d("path = $VIDEOPATH")
                cursor.close()

                val intent = Intent(CallApplication.getContext(), ContentActivity::class.java)
                intent.putExtra("path", VIDEOPATH)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                CallApplication.getContext().startActivity(intent)

                val theme = ThemeContent()
                theme.video_name = VIDEOPATH
                DataBaseTool.getInstance().insertWords(theme)
            }
        }
    }

    override fun addClickCallBack() {
        checkPermission()
    }

    private fun checkPermission() {
        requestPermission(
            this@MainActivity,
            arrayOf(
                Permission.WRITE,
                Permission.READ
            ),
            3
        )
    }

    override fun requestSuccess(requestCode: Int, permission: List<String>) {
        super.requestSuccess(requestCode, permission)
        if (permission.contains(Permission.WRITE) && permission.contains(Permission.READ)) {
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, 2)
        }
    }

    override fun requestError(requestCode: Int, permission: List<String>) {
        super.requestError(requestCode, permission)
    }

}

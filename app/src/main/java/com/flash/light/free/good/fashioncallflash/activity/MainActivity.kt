package com.flash.light.free.good.fashioncallflash.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.flash.light.free.good.fashioncallflash.R
import com.flash.light.free.good.fashioncallflash.adapter.MainAdapter
import com.flash.light.free.good.fashioncallflash.adapter.MainViewpagerAdapter
import com.flash.light.free.good.fashioncallflash.tool.ContactTool
import com.flash.light.free.good.fashioncallflash.tool.DataTool
import com.flash.light.free.good.fashioncallflash.tool.ScreenTool
import com.flash.light.free.good.fashioncallflash.util.Permission
import com.flash.light.free.good.fashioncallflash.util.SharedPreTool
import com.flash.light.free.good.fashioncallflash.util.setWhite
import com.flash.light.free.good.fashioncallflash.view.NewGridManager
import com.flash.light.free.good.fashioncallflash.view.PermissionDialog
import com.flash.light.free.good.fashioncallflash.view.WhiteDialog
import com.google.android.material.tabs.TabLayout

class MainActivity : BaseActivity(), View.OnClickListener {

//    private lateinit var main_recyclerview: RecyclerView
//    private lateinit var adapter: MainAdapter
    private lateinit var main_setting: ImageView

    private lateinit var main_viewpager: ViewPager2
    private lateinit var main_tab: TabLayout
    private lateinit var adapter: MainViewpagerAdapter
    private var oldPagerPosition = -1

    private var SET_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //获取屏幕宽度，展示来电秀的时候用，这样可以使它全屏展示
        ScreenTool.getInstance().setScreen(this)
        init()

        val count = SharedPreTool.getInstance().getInt(SharedPreTool.START_COUNT)
        if (count == 0) {
//            val permissionDialog = PermissionDialog(this)
//            permissionDialog.setCallBack(object : PermissionDialog.ClickCallBack {
//                override fun click() {
//                    requestPermission(
//                        this@MainActivity,
//                        arrayOf(
////                            Permission.WRITE,
////                            Permission.READ,
//                            Permission.CALL,
//                            Permission.CALL_NUM,
//                            Permission.CONTACTS
//                        ),
//                        SET_REQUEST
//                    )
//                }
//            })
//            permissionDialog.show()

        } else if (count >= 1) {
            setWhite(this)
        }
        SharedPreTool.getInstance().putInt(SharedPreTool.START_COUNT, count + 1)
    }

    private fun init() {
        main_setting = findViewById(R.id.main_setting)
        main_viewpager = findViewById(R.id.main_viewpager)
        main_tab = findViewById(R.id.main_tab)

        adapter = MainViewpagerAdapter(this)
        main_viewpager.adapter = adapter

        main_setting.setOnClickListener(this)

        main_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab) {
                Log.d("LJW", "onTabReselected")
                main_viewpager.setCurrentItem(p0.position)
            }

            override fun onTabUnselected(p0: TabLayout.Tab) {
                Log.d("LJW", "onTabUnselected")
                val item_view = p0.customView
                val textView = item_view?.findViewById<TextView>(R.id.teb_textview)
                textView?.setTextColor(resources.getColor(R.color.tab_unselect))
            }

            override fun onTabSelected(p0: TabLayout.Tab) {
                Log.d("LJW", "onTabSelected:${p0.position}")
                main_viewpager.setCurrentItem(p0.position)

                val item_view = p0.customView
                val textView = item_view?.findViewById<TextView>(R.id.teb_textview)
                textView?.setTextColor(resources.getColor(R.color.tab_select))

            }
        })

        main_viewpager.isUserInputEnabled = false

        main_viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                main_tab.setScrollPosition(position, 0F, true)

                if (oldPagerPosition != -1) {
                    val textView = main_tab.getTabAt(oldPagerPosition)
                        ?.customView?.findViewById<TextView>(R.id.teb_textview)
                    textView?.setTextColor(resources.getColor(R.color.tab_unselect))
                }

                val textView = main_tab.getTabAt(position)
                    ?.customView?.findViewById<TextView>(R.id.teb_textview)
                textView?.setTextColor(resources.getColor(R.color.tab_select))

                oldPagerPosition = position
            }
        })

        val inflater = LayoutInflater.from(this)
        DataTool.getInstance().nameList.forEach {

            val tab_view = inflater.inflate(R.layout.tab_view_layout, null)
            val textView = tab_view.findViewById<TextView>(R.id.teb_textview)
            textView.text = it

            val newTab = main_tab.newTab()
            newTab.setCustomView(tab_view)
            main_tab.addTab(newTab)
//            main_tab.addTab(main_tab.newTab().setText(it.name))
        }
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

package com.flash.light.free.good.fashioncallflash.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flash.light.free.good.fashioncallflash.fragment.ThemeFragment
import com.flash.light.free.good.fashioncallflash.tool.DataTool

class MainViewpagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return DataTool.getInstance().nameList.size
    }

    override fun createFragment(position: Int): Fragment {
        return ThemeFragment.newInstance(position)
    }

}
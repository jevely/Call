package com.flash.light.free.good.flash.tool

import com.flash.light.free.good.flash.R
import com.flash.light.free.good.flash.db.DataBaseTool
import com.flash.light.free.good.flash.db.ThemeContent
import java.util.*

class DataTool {
    companion object {

        private var dataTool: DataTool? = null

        fun getInstance(): DataTool {
            if (dataTool == null) {
                @Synchronized
                if (dataTool == null) {
                    dataTool =
                        DataTool()
                }
            }
            return dataTool as DataTool
        }

        fun destroy() {
            dataTool = null
        }
    }


    val themeList = mutableListOf<ThemeContent>()
    fun initData() {
        themeList.clear()

        val list = mutableListOf<ThemeContent>()

        val theme1 = ThemeContent()
        theme1.image_url = R.mipmap.hs_1
        theme1.video_url = R.raw.hs_1
        theme1.video_name = "hs_1"
        DataBaseTool.getInstance().insertWords(theme1)
        list.add(theme1)

        val theme2 = ThemeContent()
        theme2.image_url = R.mipmap.hs_2
        theme2.video_url = R.raw.hs_2
        theme2.video_name = "hs_2"
        DataBaseTool.getInstance().insertWords(theme2)
        list.add(theme2)

        val theme3 = ThemeContent()
        theme3.image_url = R.mipmap.hs_3
        theme3.video_url = R.raw.hs_3
        theme3.video_name = "hs_3"
        DataBaseTool.getInstance().insertWords(theme3)
        list.add(theme3)

        val theme4 = ThemeContent()
        theme4.image_url = R.mipmap.hs_4
        theme4.video_url = R.raw.hs_4
        theme4.video_name = "hs_4"
        DataBaseTool.getInstance().insertWords(theme4)
        list.add(theme4)

        val theme5 = ThemeContent()
        theme5.image_url = R.mipmap.hs_5
        theme5.video_url = R.raw.hs_5
        theme5.video_name = "hs_5"
        DataBaseTool.getInstance().insertWords(theme5)
        list.add(theme5)

        val theme6 = ThemeContent()
        theme6.image_url = R.mipmap.hs_6
        theme6.video_url = R.raw.hs_6
        theme6.video_name = "hs_6"
        DataBaseTool.getInstance().insertWords(theme6)
        list.add(theme6)

        val theme7 = ThemeContent()
        theme7.image_url = R.mipmap.hs_7
        theme7.video_url = R.raw.hs_7
        theme7.video_name = "hs_7"
        DataBaseTool.getInstance().insertWords(theme7)
        list.add(theme7)

        val theme8 = ThemeContent()
        theme8.image_url = R.mipmap.hs_8
        theme8.video_url = R.raw.hs_8
        theme8.video_name = "hs_8"
        DataBaseTool.getInstance().insertWords(theme8)
        list.add(theme8)

        val theme9 = ThemeContent()
        theme9.image_url = R.mipmap.hs_9
        theme9.video_url = R.raw.hs_9
        theme9.video_name = "hs_9"
        DataBaseTool.getInstance().insertWords(theme9)
        list.add(theme9)

        val theme10 = ThemeContent()
        theme10.image_url = R.mipmap.hs_10
        theme10.video_url = R.raw.hs_10
        theme10.video_name = "hs_10"
        DataBaseTool.getInstance().insertWords(theme10)
        list.add(theme10)

        while (list.isNotEmpty()) {
            val index = Random().nextInt(list.size)
            val content = list[index]
            list.remove(content)
            themeList.add(content)
        }
    }

}
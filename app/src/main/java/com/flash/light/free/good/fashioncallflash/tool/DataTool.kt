package com.flash.light.free.good.fashioncallflash.tool

import com.flash.light.free.good.fashioncallflash.db.DataBaseTool
import com.flash.light.free.good.fashioncallflash.db.ThemeContent
import com.flash.light.free.good.fashioncallflash.net.NetUrl
import com.flash.light.free.good.fashioncallflash.util.Logger
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
        for (i in 0 until 94) {
            val theme = ThemeContent()
            theme.image_url = "${NetUrl.THEME}hs_${i + 1}.png"
            theme.video_url = "${NetUrl.THEME}hs_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
//            themeList.add(theme)
            list.add(theme)
        }

        while (list.isNotEmpty()) {
            val index = Random().nextInt(list.size)
            val content = list[index]
            list.remove(content)
            themeList.add(content)
        }

        Logger.d("list.size = ${list.size}")
        Logger.d("themeList.size = ${themeList.size}")

    }
}
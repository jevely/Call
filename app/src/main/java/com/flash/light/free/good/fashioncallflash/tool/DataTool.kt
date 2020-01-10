package com.flash.light.free.good.fashioncallflash.tool

import com.flash.light.free.good.fashioncallflash.db.DataBaseTool
import com.flash.light.free.good.fashioncallflash.db.ThemeContent
import com.flash.light.free.good.fashioncallflash.net.NetUrl

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
        for (i in 0 until 50) {
            val theme = ThemeContent()
            theme.image_url = "${NetUrl.THEME}hs_${i + 1}.png"
            theme.video_url = "${NetUrl.THEME}hs_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            themeList.add(theme)
        }

    }
}
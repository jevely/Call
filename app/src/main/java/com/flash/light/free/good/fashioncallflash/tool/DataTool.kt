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

        for (i in 0 until 20) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/scenery/scenery_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/scenery/scenery_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            list.add(theme)
        }

        for (i in 0 until 7) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/animal/animal_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/animal/animal_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            list.add(theme)
        }

        for (i in 23 until 27) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/dance/dance_${i + 1}.png"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/dance/dance_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            list.add(theme)
        }

        for (i in 0 until 16) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/electric/electric_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/electric/electric_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            list.add(theme)
        }

        for (i in 0 until 19) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/heart/heart_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/heart/heart_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            list.add(theme)
        }

        for (i in 0 until 25) {
            if (i < 22) {
                val theme = ThemeContent()
                theme.image_url =
                    "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/other/other_${i + 1}.webp"
                theme.video_url =
                    "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/other/other_${i + 1}.mp4"
                DataBaseTool.getInstance().insertWords(theme)
                list.add(theme)
            } else {
                val theme = ThemeContent()
                theme.image_url =
                    "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/other/other_${i + 1}.png"
                theme.video_url =
                    "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/other/other_${i + 1}.mp4"
                DataBaseTool.getInstance().insertWords(theme)
                list.add(theme)
            }
        }

        for (i in 0 until 13) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/sport/sport_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/sport/sport_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
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

    val animalTheme = mutableListOf<ThemeContent>()
    val danceTheme = mutableListOf<ThemeContent>()
    val electricTheme = mutableListOf<ThemeContent>()
    val heartTheme = mutableListOf<ThemeContent>()
    val otherTheme = mutableListOf<ThemeContent>()
    val sceneryTheme = mutableListOf<ThemeContent>()
    val sportTheme = mutableListOf<ThemeContent>()
    val allTheme = mutableListOf<MutableList<ThemeContent>>()
    fun initDataNew() {
        //https://s3.amazonaws.com/download.filterisq.com/jar/newcall/scenery/scenery_1.mp4
        //scenery
        for (i in 0 until 20) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/scenery/scenery_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/scenery/scenery_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            sceneryTheme.add(theme)
        }
        // https://s3.amazonaws.com/download.filterisq.com/jar/newcall/animal/animal_1.mp4
        //animal
        for (i in 0 until 7) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/animal/animal_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/animal/animal_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            animalTheme.add(theme)
        }
        //https://s3.amazonaws.com/download.filterisq.com/jar/newcall/dance/dance_1.mp4
        //dance
        for (i in 0 until 27) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/dance/dance_${i + 1}.png"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/dance/dance_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            danceTheme.add(theme)
        }
        //https://s3.amazonaws.com/download.filterisq.com/jar/newcall/electric/electric_1.mp4
        //electric
        for (i in 0 until 16) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/electric/electric_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/electric/electric_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            electricTheme.add(theme)
        }
        //https://s3.amazonaws.com/download.filterisq.com/jar/newcall/heart/heart_1.mp4
        //heart
        for (i in 0 until 19) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/heart/heart_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/heart/heart_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            heartTheme.add(theme)
        }
        //https://s3.amazonaws.com/download.filterisq.com/jar/newcall/other/other_1.mp4
        //other
        for (i in 0 until 25) {
            if (i < 22) {
                val theme = ThemeContent()
                theme.image_url =
                    "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/other/other_${i + 1}.webp"
                theme.video_url =
                    "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/other/other_${i + 1}.mp4"
                DataBaseTool.getInstance().insertWords(theme)
                otherTheme.add(theme)
            } else {
                val theme = ThemeContent()
                theme.image_url =
                    "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/other/other_${i + 1}.png"
                theme.video_url =
                    "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/other/other_${i + 1}.mp4"
                DataBaseTool.getInstance().insertWords(theme)
                otherTheme.add(theme)
            }
        }
        //https://s3.amazonaws.com/download.filterisq.com/jar/newcall/sport/sport_1.mp4
        //sport
        for (i in 0 until 13) {
            val theme = ThemeContent()
            theme.image_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/sport/sport_${i + 1}.webp"
            theme.video_url =
                "https://s3.amazonaws.com/download.filterisq.com/jar/newcall/sport/sport_${i + 1}.mp4"
            DataBaseTool.getInstance().insertWords(theme)
            sportTheme.add(theme)
        }

        sceneryTheme.reverse()
        animalTheme.reverse()
        danceTheme.reverse()
        electricTheme.reverse()
        heartTheme.reverse()
        otherTheme.reverse()
        sportTheme.reverse()

        allTheme.add(sceneryTheme)
        allTheme.add(animalTheme)
        allTheme.add(danceTheme)
        allTheme.add(electricTheme)
        allTheme.add(heartTheme)
        allTheme.add(otherTheme)
        allTheme.add(sportTheme)
    }

    val nameList =
        arrayListOf("Scenery", "Animal", "Dance", "Electric", "Heart", "Other", "Sport")
}
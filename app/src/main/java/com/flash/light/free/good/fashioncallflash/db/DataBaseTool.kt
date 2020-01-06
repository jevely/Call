package com.flash.light.free.good.fashioncallflash.db

import android.content.Context
import androidx.room.Room

class DataBaseTool private constructor() {

    companion object {

        private var dataBaseTool: DataBaseTool? = null

        fun getInstance(): DataBaseTool {
            if (dataBaseTool == null) {
                @Synchronized
                if (dataBaseTool == null) {
                    dataBaseTool = DataBaseTool()
                }
            }
            return dataBaseTool!!
        }

        fun destroy() {
            dataBaseTool = null
        }

    }

    private var themeDataBase: ThemeDataBase? = null

    fun initDataBase(context: Context) {
        themeDataBase = Room.databaseBuilder(context, ThemeDataBase::class.java, "calltheme")
            .allowMainThreadQueries().build()
    }

//    fun insertWords(vararg themeContent: ThemeContent) {
//        themeDataBase?.themeDao()?.insert(*themeContent)
//    }
//
//    fun updateWords(vararg themeContent: ThemeContent) {
//        themeDataBase?.themeDao()?.upData(*themeContent)
//    }

    fun insertWords(themeContent: ThemeContent) {
        val theme = find(themeContent.video_url!!)
        if (theme == null) {
            themeDataBase?.themeDao()?.insert(themeContent)
        } else {
            updateWords(themeContent)
        }
    }

    fun updateWords(themeContent: ThemeContent) {
        val theme = find(themeContent.video_url!!)
        if (theme == null) {
            insertWords(themeContent)
        } else {
            themeDataBase?.themeDao()?.upData(themeContent)
        }
    }

    fun delete(vararg themeContent: ThemeContent) {
        themeDataBase?.themeDao()?.delete(*themeContent)
    }

    fun getAll(): MutableList<ThemeContent>? {
        return themeDataBase?.themeDao()?.getAll()
    }

    fun find(input_content: String): ThemeContent? {
        return themeDataBase?.themeDao()?.find(input_content)
    }

    fun findLike(input_content: String): MutableList<ThemeContent>? {
        return themeDataBase?.themeDao()?.findLike(input_content)
    }

}
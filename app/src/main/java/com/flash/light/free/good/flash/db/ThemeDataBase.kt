package com.flash.light.free.good.flash.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ThemeContent::class], version = 1)
abstract class ThemeDataBase : RoomDatabase() {

    abstract fun themeDao(): ThemeDao

}
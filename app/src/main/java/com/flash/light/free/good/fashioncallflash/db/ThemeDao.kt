package com.flash.light.free.good.fashioncallflash.db

import androidx.room.*

@Dao
interface ThemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg themeContent: ThemeContent)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun upData(vararg themeContent: ThemeContent)

    @Delete
    fun delete(vararg themeContent: ThemeContent)

    @Query("SELECT * FROM calltheme")
    fun getAll(): MutableList<ThemeContent>

    @Query("SELECT * FROM calltheme WHERE video_url == :input_content")
    fun find(input_content: String): ThemeContent

    @Query("SELECT * FROM calltheme WHERE video_url LIKE :input_content")
    fun findLike(input_content: String): MutableList<ThemeContent>
}
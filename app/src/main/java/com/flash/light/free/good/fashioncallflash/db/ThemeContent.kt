package com.flash.light.free.good.fashioncallflash.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.facebook.ads.NativeAd

@Entity(tableName = "calltheme")
class ThemeContent {
    @PrimaryKey
    var id: Int? = null

    var image_url: String? = null

    var video_url: String? = null

    var path: String = ""

    var isOpen: Boolean = false

    var isDownload: Boolean = false

    var type: Int = 1

    @Ignore
    var fbNativeAd : NativeAd?= null
}
package com.flash.light.free.good.fashioncallflash.net

interface DownloadCallBack{
    fun downloadSuccess(path : String)
    fun downloadError()
}
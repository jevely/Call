package com.flash.light.free.good.fashioncallflash.util

import android.content.Context

object ResourceUtil {

    fun getDrawable(context: Context, key: String): Int {
        return getResource(context, "drawable", key)
    }

    fun getMipmap(context: Context, key: String): Int {
        return getResource(context, "mipmap", key)
    }

    fun getId(context: Context, key: String): Int {
        return getResource(context, "id", key)
    }

    fun getString(context: Context, key: String): Int {
        return getResource(context, "string", key)
    }

    fun getColor(context: Context, key: String): Int {
        return getResource(context, "color", key)
    }

    fun getLayout(context: Context, key: String): Int {
        return getResource(context, "layout", key)
    }

    fun getDimen(context: Context, key: String): Int {
        return getResource(context, "dimen", key)
    }

    fun getStyle(context: Context, key: String): Int {
        return getResource(context, "style", key)
    }

    fun getStyleable(context: Context, key: String): Int {
        return getResource(context, "styleable", key)
    }

    fun getAnim(context: Context, key: String): Int {
        return getResource(context, "anim", key)
    }

    fun getArray(context: Context, key: String): Int {
        return getResource(context, "array", key)
    }

    fun getRaw(context: Context, key: String): Int {
        return getResource(context, "raw", key)
    }

    private fun getResource(context: Context, type: String, key: String): Int {
        val resource = context.resources
        val pkgName = context.packageName
        val id = resource.getIdentifier(key, type, pkgName)
        return id
    }
}
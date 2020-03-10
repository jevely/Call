package com.flash.light.free.good.flash.tool

import android.app.Activity
import android.graphics.Point
import com.flash.light.free.good.flash.util.getSceenHeight
import com.flash.light.free.good.flash.util.getScreen
import com.flash.light.free.good.flash.util.isNavigationBarShow

class ScreenTool {

    companion object {

        private var screenTool: ScreenTool? = null

        fun getInstance(): ScreenTool {
            if (screenTool == null) {
                @Synchronized
                if (screenTool == null) {
                    screenTool = ScreenTool()
                }

            }
            return screenTool as ScreenTool
        }

        fun destroy() {
            screenTool = null
        }
    }

    private lateinit var screenPoint: Point

    fun setScreen(activity: Activity) {
        val screenWidth = getScreen().x

        if (isNavigationBarShow(activity)) {
            val screenHeight = getSceenHeight(activity)
            screenPoint = Point(screenWidth, screenHeight)
        } else {
            val screenHeight = getScreen().y
            screenPoint = Point(screenWidth, screenHeight)
        }
    }

    fun getAllScreen(): Point {
        return screenPoint
    }


}
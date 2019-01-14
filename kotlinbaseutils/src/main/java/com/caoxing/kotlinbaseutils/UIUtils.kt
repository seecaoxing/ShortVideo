package com.caoxing.kotlinbaseutils

import android.content.Context

object UIUtils {

    fun dip2px(context: Context, dpValue: Double): Int {
        var density = context.resources.displayMetrics.density
        return (dpValue * density + 0.5).toInt()

    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }


}
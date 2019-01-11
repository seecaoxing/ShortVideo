package com.utils.baseutils;

import android.util.Log;

/**
 * Created by li_bin on 2016/11/2.
 * Log打印工具类
 */
public class LogUtil {

    //是否输出Log
    public static boolean mLogPrint = BuildConfig.DEBUG;

    public static void v(String tag, String message) {
        if (mLogPrint) {
            Log.v(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (mLogPrint) {
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (mLogPrint) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (mLogPrint) {
            Log.e(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (mLogPrint) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message, Exception e){
        if (mLogPrint) {
            Log.e(tag, message,e);
        }
    }
}

package com.utils.baseutils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by fanweiguo on 16/12/3.
 */

public class CommonUtil {

    public static String TAG = "CommonUtil";
    public static String SP_LOGIN_USERID = "sp_login_userid";
    public static String SP_LOGIN_USER_TOKEN = "sp_login_user_token";
    public static String TAG_USER_INFO = "user_info";
    public static String SP_LOGIN_TYPE = "sp_login_type";
    public static String PHONE_LOGIN_TYPE = "phone";


    public static void convertActivityToTranslucent(Activity activity) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                    break;
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
                        translucentConversionListenerClazz, ActivityOptions.class);
                method.setAccessible(true);
                method.invoke(activity, new Object[]{null, null});
            } else {
                Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
                        translucentConversionListenerClazz);
                method.setAccessible(true);
                method.invoke(activity, new Object[]{null});
            }

        } catch (Throwable t) {
        }
    }


    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    private static long lastClickTime;


    /**
     * 根据输入的String，判断是否在截断范围内，在截断内，+...返回并显示
     *
     * @param originalStr
     * @param num
     * @return
     */
    public static String clipString(String originalStr, int num) {
        if (TextUtils.isEmpty(originalStr) || num <= 0) {
            return "";
        }

        int length = originalStr.length();
        if (length <= num) {
            return originalStr;
        }

        String clipStr = originalStr.substring(0, num) + "...";

        return clipStr;
    }

    /**
     * 根据输入的String，判断是否在截断范围内，英文算一个，中文算两个,
     * 在截断内，+...返回并显示
     *
     * @param originalStr
     * @param maxLen
     * @return
     */
    public static String clipString1(String originalStr, int maxLen) {
        if (TextUtils.isEmpty(originalStr) || maxLen <= 0) {
            return originalStr;
        }

        int count = 0;
        int endIndex = 0;
        for (int i = 0; i < originalStr.length(); i++) {
            char item = originalStr.charAt(i);
            if (item < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
            if (maxLen == count || (item >= 128 && maxLen + 1 == count)) {
                endIndex = i;
            }
        }
        if (count <= maxLen) {
            return originalStr;
        } else {
            return originalStr.substring(0, endIndex) + "...";
        }
    }


    public static String getOddsFormat(float oddFloat) {

        String oddFormatStr = "0.00";

        if (oddFloat >= 10 && oddFloat < 100) {
            oddFormatStr = String.format(Locale.getDefault(), "%.1f", oddFloat);
        } else if (oddFloat >= 0 && oddFloat < 10) {
            oddFormatStr = String.format(Locale.getDefault(), "%.2f", oddFloat);
        } else if (oddFloat >= 100) {
            oddFormatStr = String.valueOf((int) oddFloat);
        } else {
            oddFormatStr = String.valueOf(oddFloat);
        }
        return oddFormatStr;
    }


    public static int getDemCount(String oddStr) {

        int index = oddStr.indexOf(".");
        if (index != -1) {
            String lastStr = oddStr.substring(index + 1, oddStr.length());
            return lastStr.length();
        }
        return -1;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isActivityFinishing(Fragment fragment) {
        if (fragment == null || fragment.getActivity() == null) return true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return fragment.getActivity().isDestroyed();
        } else {
            return fragment.getActivity().isFinishing();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isActivityFinishing(Activity activity) {
        if (activity == null) return true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity.isDestroyed();
        } else {
            return activity.isFinishing();
        }
    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }


    public static String formatFloat(float moneyOrDiscount) {
        String temp = String.valueOf(moneyOrDiscount);
        return formatFloat(temp);
    }

    public static String formatFloat(String moneyOrDiscount) {
        if (moneyOrDiscount.endsWith(".0")) {
            moneyOrDiscount = moneyOrDiscount.substring(0, moneyOrDiscount.length() - 2);
        }
        return moneyOrDiscount;
    }

    public static String formatTime(long time) {

        long hours = time / DateTimeUtil.ONE_HOUR;
        long minitue = (time % DateTimeUtil.ONE_HOUR) / DateTimeUtil.ONE_MIN;
        long second = (time % DateTimeUtil.ONE_MIN) / DateTimeUtil.ONE_SEC;
        StringBuffer sb = new StringBuffer();
        if (hours < 10) {
            sb.append(0);
        }
        sb.append(hours);
        sb.append(":");

        if (minitue < 10) {
            sb.append(0);
        }
        sb.append(minitue);
        sb.append(":");

        if (second < 10) {
            sb.append(0);
        }
        sb.append(second);

        return sb.toString();
    }

}

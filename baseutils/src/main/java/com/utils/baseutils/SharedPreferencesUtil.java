package com.utils.baseutils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by li_bin on 2016/10/28.
 * SharedPreferences存储帮助类
 */
public class SharedPreferencesUtil {

    private final static String LOTTERY_PREF = "lottery_pref";

    public final static String PUSH_OPEN = "push_open";
    public final static String PUSH_CROSS_TRADE_OPEN = "cross_trade_open";
    public final static String PUSH_RELOTTERY_INDEX_OPEN = "relottery_index_open";

    public final static String PUSH_FOLLOW_EXPERT_OPEN = "follow_expert_open";
    public final static String PUSH_FIVE_START_OPEN = "push_five_open_v2";
    public final static String PUSH_DONT_TROUBLE = "push_dont_trouble";
    public final static String LOGIN_TYPE = "login_type";
    public final static String PUSH_ODDS_CHANGE_OPEN = "push_odds_change_open";
    public final static String MY_COUPON_RED_POINT = "my_coupon_red_point";
    public final static String PUSH_TRADE_ODDS_CHANGE_OPEN = "push_trade_odds_change_open";
    public final static String REALTIME_EXPIRATION_DATE = "RealTimeExpirationDate";
    public final static String POINTCARD_EXPIRATION_DATE = "PointCardExpirationDate";
    public final static String IS_POPUP_TCM_CREDIT = "is_popup_tcm_credit";

    public final static String SHOW_MODEL_DEMO = "show_model_demo";

    public final static String SHOW_OPNE_SERVICE = "show_open_service";

    public final static String SHOW_GUIDE_DIALOG = "show_guide_dialog";

    private static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences setting = context.getSharedPreferences(LOTTERY_PREF, Context.MODE_PRIVATE);
        return setting;
    }

    /**
     * 保存String类型值
     *
     * @param key
     * @param value
     */
    public static void setStringValue(Context context, String key, String value) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取String类型的Value值
     *
     * @param key          Map->key
     * @param defaultValue 若没值的情况下的默认值
     * @return
     */
    public static String getStringValue(Context context, String key, String defaultValue) {
        SharedPreferences settings = getSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    /**
     * 保存Int类型的值
     *
     * @param key
     * @param value
     */
    public static void setIntValue(Context context, String key, int value) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 获取Int类型的值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntValue(Context context, String key, int defaultValue) {
        SharedPreferences settings = getSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    /**
     * 保存Boolean类型值
     *
     * @param key
     * @param value
     */
    public static void setBooleanValue(Context context, String key, boolean value) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取Boolean类型值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = getSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    public static void setLongValue(Context context, String key, long value) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLongValue(Context context, String key, long defaultValue) {
        SharedPreferences settings = getSharedPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    /**
     * 清除sp key
     *
     * @param key
     */
    public static void clearSpValue(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }
}

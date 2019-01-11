package com.utils.baseutils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by li_bin on 2016/11/2.
 * 时间和日期的工具类
 */
public class DateTimeUtil {


    /*格式11/24*/
    public static final String MONTH_DAY_HOT_FORMAT = "MM/dd";
    /*格式几月几日*/
    public static final String MONTH_DAY_FORMAT = "MM月dd日";
    /* 格式：月－日*/
    public static final String SHORT_DATE_FORMAT = "MM-dd";
    /* 格式：年－月－日*/
    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";
    /* 格式：年-月-日 小时-分钟*/
    public static final String LONG_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String LONG_DATE_TIME_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";

    public static final long ONE_SEC = 1000L;
    public static final long ONE_MIN = 60 * ONE_SEC;
    public static final long ONE_HOUR = 60 * ONE_MIN;
    public static final long ONE_DAY = 24 * ONE_HOUR;


    private static GregorianCalendar calendar = new GregorianCalendar();


    public static String getDateFormat(String formatStr) {

        SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.getDefault());
        Date date = new Date();
        String dateStr = format.format(date);
        return dateStr;
    }

    /**
     * 根据时间戳返回
     * 今日，明天
     * 几月几号
     * 星期几
     */
    public static String getDateFromeTime(long time) throws ParseException {
        String str = "";
        str += format(time) + " ";
        str += getDateFromLong(time, MONTH_DAY_FORMAT) + " ";
        str += getWeek(time);
        return str;
    }


    public static String getHotItemDateFromeTime(long time) {
        String str = "";
        str += getDateFromLong(time, MONTH_DAY_HOT_FORMAT) + " ";
        return str;
    }



    /**
     * 根据毫秒时间戳来格式化字符串
     * 今天显示今天、昨天显示昨天、前天显示前天.
     * 早于前天的显示具体年-月-日，如2017-06-12；
     *
     * @param timeStamp 毫秒值
     * @return 今天 昨天 前天 或者 yyyy-MM-dd HH:mm:ss类型字符串
     */
    public static String format(long timeStamp) throws ParseException {
        long curTimeMillis = System.currentTimeMillis();

        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");

        long todayStartMillis = sdfOne.parse(sdfOne.format(curTimeMillis)).getTime();

        long oneDayMillis = 24 * 60 * 60 * 1000;

        long dayAfterAfterTomorrow = todayStartMillis + (oneDayMillis * 3);

        long dayAfterTomorrow = todayStartMillis + (oneDayMillis * 2);

        long tomorrowStartMilis = todayStartMillis + oneDayMillis;

        long yesterdayStartMilis = todayStartMillis - oneDayMillis;

        long yesterdayBeforeStartMilis = yesterdayStartMilis - oneDayMillis;

        if (dayAfterTomorrow <= timeStamp && timeStamp < dayAfterAfterTomorrow) {
            return "后天";
        } else if (tomorrowStartMilis <= timeStamp && timeStamp < dayAfterTomorrow) {
            return "明天";
        } else if (todayStartMillis <= timeStamp && timeStamp < tomorrowStartMilis) {
            return "今天";
        } else if (yesterdayStartMilis <= timeStamp && timeStamp < todayStartMillis) {
            return "昨天";
        } else if (yesterdayBeforeStartMilis <= timeStamp && timeStamp < yesterdayStartMilis) {
            return "前天";
        }
        return "";
    }

    /**
     * 根据日期计算周几
     *
     * @param millis
     * @return
     */
    //获取指定毫秒数的对应星期
    public static String getWeek(long millis) {
        calendar.setTimeInMillis(millis);
        String week = "星期";
        int cweek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (cweek) {
            case 1:
                week += "日";
                break;
            case 2:
                week += "一";
                break;
            case 3:
                week += "二";
                break;
            case 4:
                week += "三";
                break;
            case 5:
                week += "四";
                break;
            case 6:
                week += "五";
                break;
            case 7:
                week += "六";
                break;
        }
        return week;

    }


    /**
     * 根据时间戳返回年月日
     *
     * @param time
     * @param formatStr
     * @return
     */
    public static String getDateFromLong(long time, String formatStr) {
        try {
            if (TextUtils.isEmpty(formatStr)) {
                formatStr = LONG_DATE_FORMAT;
            }
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            return format.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据时间戳返回年月日 小时分钟
     *
     * @param time
     * @return
     */
    public static String getDateTimeFromLong(long time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(LONG_DATE_TIME_FORMAT);
            return format.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 时间<1min，则显示：结束于刚刚；
     * 1h>时间>1min，则显示：结束于XX分钟前
     * 24h>时间>1h，则显示：结束于XX小时前
     * 3天>=时间>1天，则显示：结束语X天前；
     * 时间>3天，则显示：年-月-日
     *
     * @param t
     * @return
     */
    public static String getDateTimeState(long t) {
        StringBuffer sb = new StringBuffer();
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            if (day <= 3) {
                sb.append(day + "天前");
            } else {
                sb.append(getDateFromLong(t, "yyyy-MM-dd"));
            }
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天前");
            } else {
                sb.append(hour + "小时前");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时前");
            } else {
                sb.append(minute + "分钟前");
            }
        } else if (mill == 60) {
            sb.append("1分钟前");
        } else {
            sb.append("刚刚");
        }

        return sb.toString();
    }

    /**
     * 详细如下：
     * interval < 1分钟，显示：“刚刚”
     * 1分钟 <= interval < 1小时，显示：“xx分钟前”
     * 1小时 <= interval < 1天，显示：“xx小时前”
     * interval >= 1天，显示：刷新时的具体日期
     *
     * @param t
     * @return
     */
    public static String getDateTimeState1(long t) {
        StringBuffer sb = new StringBuffer();
        long time = System.currentTimeMillis() - t;
        if (time < 60 * 1000) {
            sb.append("刚刚");
        } else if (time >= 60 * 1000 && time < 60 * 60 * 1000) {
            long str = time / (60 * 1000);
            sb.append(str + "分钟前");
        } else if (time >= 60 * 60 * 1000 && time < 24 * 60 * 60 * 1000) {
            long str = time / (60 * 60 * 1000);
            sb.append(str + "小时前");
        } else if (time >= 24 * 60 * 60 * 1000) {
            sb.append(getDateFromLong(t, "yyyy-MM-dd"));
        }
        return sb.toString();
    }

    // 2017-01-13 10:00:00
    public static long getDurationFromString(String duration) {
        try {
            if (!TextUtils.isEmpty(duration)) {
                SimpleDateFormat df = new SimpleDateFormat(LONG_DATE_TIME_FORMAT_SECOND, Locale.getDefault());
                df.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                return df.parse(duration).getTime();
            }
        } catch (Exception e) {
        }
        return -1;
    }

    // 2017-01-13 10:00:00
    public static long getDurationFromString(String pattern, String duration) {
        try {
            if (!TextUtils.isEmpty(duration)) {
                SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
                df.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                return df.parse(duration).getTime();
            }
        } catch (Exception e) {
        }
        return -1;
    }


    public static boolean isSameDay(long timeA, long timeB) {
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(timeA);
        Calendar cb = Calendar.getInstance();
        cb.setTimeInMillis(timeB);
        if (ca.get(Calendar.YEAR) != cb.get(Calendar.YEAR)) return false;
        if (ca.get(Calendar.MONTH) != cb.get(Calendar.MONTH)) return false;
        if (ca.get(Calendar.DAY_OF_MONTH) != cb.get(Calendar.DAY_OF_MONTH)) return false;
        return true;
    }

    /**
     * 判断在哪个时间段内
     *
     * @return
     */
    public static boolean isOverTime(int start, int end) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = calendar.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分到目前为止的分钟数
        start *= 60;//开始的分钟
        end *= 60;//结束的分钟
        if (minuteOfDay >= start && minuteOfDay <= end) {
            return true;
        }
        return false;
    }

}

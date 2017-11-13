package com.haiqiu.miaohi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 格式化日期
 * Created by miaohi on 2016/4/27.
 */
public class TimeFormatUtils {
    private static final String TAG = "TimeFormatUtils";
    /**
     * 日期转字符串
     */
    public static String dateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    /**
     * 字符串转日期
     */
    public static Date strToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            MHLogUtil.e(TAG,e);
        }
        return date;
    }

    /**
     * 格式日期
     */
    public static String formatDate(Date data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
        String dateStr = "";
        try {
            dateStr = format.format(data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
        return dateStr;
    }

    /**
     * 将时间秒数转换为 3"23'类似的格式
     */
    public static String getMyTimeFormat(int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        return d + "'" + s + "\"";
    }

    /**
     * 将时间秒数转换为 00:05:23类似的格式
     */
    public static String getCountDownFormat(int millis) {
        int h = 0;
        int d = 0;
        int s = 0;
        int second = millis / 1000;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            h = 00;
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        String hourResult;
        String minResult;
        String secondResult;
        if (h <= 9) {
            hourResult = "0" + h;
        } else {
            hourResult = h + "";
        }

        if (d <= 9) {
            minResult = "0" + d;
        } else {
            minResult = d + "";
        }

        if (s <= 9) {
            secondResult = "0" + s;
        } else {
            secondResult = s + "";
        }


        return hourResult + ":" + minResult + ":" + secondResult;
    }

    /**
     * 获取当前时间戳
     *
     * @return 时间戳
     */
    public static long getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    /**
     * 格式化12小时制<br>
     * 格式：yyyy-MM-dd hh-MM-ss
     *
     * @param time 时间
     * @return
     */
    public static String format12Time(long time) {
        return format(time, "yyyy-MM-dd hh:MM:ss");
    }


    public static String formatTime(long time) {
        return format(time, "HH:mm MM-dd");
    }

    /**
     * /**
     * 格式化24小时制<br>
     * 格式：yyyy-MM-dd HH-MM-ss
     *
     * @param time 时间
     * @return
     */
    public static String format24Time(long time) {
        return format(time, "yyyy-MM-dd HH:MM:ss");
    }

    /**
     * 格式化年/月/日
     *
     * @param time
     * @return
     */
    public static String formatYMD(long time) {
        return format(time, "yyyy/MM/dd");
    }

    /**
     * 格式化24小时制<br>
     * 格式：yyyy/MM/dd
     *
     * @param time 时间
     * @return
     */
    public static String format24Time1(long time) {
        return format(time, "yyyy/MM/dd");
    }

    public static String format24TimeHM(long time) {
        return format(time, "HH:MM");
    }

    public static String formatMD(long time) {
        return format(time, "HH:MM");
    }

    /**
     * 格式化时间,自定义标签
     *
     * @param time    时间
     * @param pattern 格式化时间用的标签
     * @return
     */
    public static String format(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(time));
    }

    /**
     * 获取当前天
     *
     * @return 天
     */
    @SuppressWarnings("static-access")
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前月
     *
     * @return 月
     */
    @SuppressWarnings("static-access")
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前年
     *
     * @return 年
     */
    @SuppressWarnings("static-access")
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前年月日
     *
     * @return 格式化的当前时间
     */
    public static String getYMD() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date());
    }

    /**
     * 获取当前年
     */
    public static String getY() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return df.format(new Date());
    }

    /**
     * 获取中国地区当前时间
     *
     * @return
     */
    public static long getCurrentTimeMillis_CH() {
        Date date = Calendar.getInstance().getTime();
        TimeZone oldZone = TimeZone.getDefault();
        TimeZone newZone = TimeZone.getTimeZone("GMT-8");
        int timeOffset = oldZone.getRawOffset() + newZone.getRawOffset();
        Date date_CH = new Date(date.getTime() - timeOffset);
        return date_CH.getTime();
    }


}

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haiqiu.miaohi.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.bean.ObserveAttentionResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;


public class MHStringUtils {

    private static final String TAG="MHStringUtils";
    /**
     * 严格判断是否是null
     */
    public static boolean isEmpty(String value) {
        return value == null || "".equalsIgnoreCase(value.trim()) || "null".equalsIgnoreCase(value.trim());
    }

    /**
     * SHA-256+MD5
     *
     * @param str
     * @return
     */
    public static String getPassword(String str) {
        byte[] shaRes = getMessageDigest(str.getBytes(), "SHA-256");
        byte[] md5Res = getMessageDigest(shaRes, "MD5");

        StringBuffer result = new StringBuffer();
        for (byte byt : md5Res)
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));

        return result.toString();
    }


    /**
     * 刚刚
     *
     * @param time
     * @return
     */
    public static String getTimeCH(String time) {
        if (MHStringUtils.isEmpty(time))
            return "";
        try {
            Date todayDate = TimeFormatUtils.strToDate(TimeFormatUtils.getYMD() + " " + "00:00:00");
            Date oldDate = null;
            if (time.contains(".")) {
                oldDate = TimeFormatUtils.strToDate(time.split("\\.")[0]);
            } else {
                oldDate = new Date(Long.valueOf(time));
            }
            long intervalMilli = oldDate.getTime() - todayDate.getTime();
            int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
            long dValue = System.currentTimeMillis() - oldDate.getTime();
            if (dValue < 60 * 1000) {
                return "刚刚";
            } else if (intervalMilli > 0) {//今天
                if (dValue < 60 * 60 * 1000) {
                    return dValue / (60 * 1000) + "分钟前";
                } else {
                    return dValue / (60 * 60 * 1000) + "小时前";
                }
            } else if (intervalMilli < 0 && xcts == 0) {//昨天
                return "昨天";
            } else if (intervalMilli < 0 && xcts == -1) {//前天
                return "前天";
            } else {
                return TimeFormatUtils.formatDate(oldDate);
            }
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
        return "";
    }

    /**
     * 获取时间长度
     *
     * @param time
     */
    public static String getTimeLength(String time) {
        if (!TextUtils.isEmpty(time) && !TextUtils.equals("null", time)) {
            Date oldDate = TimeFormatUtils.strToDate(time.split("\\.")[0]);
            long intervalMilli = System.currentTimeMillis() - oldDate.getTime();
            if (intervalMilli / 1000 <= 60) {
                return "刚刚";
            } else if (intervalMilli / (1000 * 60) <= 60) {
                return intervalMilli / (1000 * 60) + "分钟前";
            } else if (intervalMilli / (1000 * 60 * 60) <= 24) {
                return intervalMilli / (1000 * 60 * 60) + "小时前";
            } else {
                return MHStringUtils.timeFormatNoMS(time);
            }
        }
        return time;
    }

    public static byte[] getMessageDigest(byte[] data, String type) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(type);
            md.update(data);
        } catch (NoSuchAlgorithmException e) {
            MHLogUtil.e(TAG,e);
        }
        return md.digest();
    }

    /**
     * 时间格式化
     *
     * @param seconds
     * @return 0'33''
     */
    public static String timeFormat(String seconds) {
        if (MHStringUtils.isEmpty(seconds))
            return "00'00''";
        int second = Integer.valueOf(seconds);
        if (second < 0) {
            return "00'00''";
        }
        long hh = (long) (second / 3600 + 0.5);
        long mm = (long) (second % 3600 / 60 + 0.5);
        long ss = second % 60;
        String strTemp;
        if (0 != hh) {
            strTemp = "59'59''";
        } else {
            strTemp = String.format("%02d'%02d''", mm, ss);
        }
        return strTemp;
    }

    /**
     * 时间格式化
     *
     * @param time 2016-06-17 20:21:34.0
     * @return 2016-06-17 20:21
     */
    public static String timeFormatNoMS(String time) {
        if (!TextUtils.isEmpty(time) && !TextUtils.equals("null", time)) {
            String[] str = time.split(" ");
            String date = str[0];
            String[] times = str[1].split(":");
            return date + " " + times[0] + ":" + times[1];
        }

        return time;
    }

    /**
     * 计数格式化
     *
     * @param count 数量
     * @return 10.2万
     */
    public static String countFormat(String count) {
        if (MHStringUtils.isEmpty(count)) return "0";
        return CommonUtil.formatCount(Long.valueOf(count));
    }

    /**
     * 去掉双空格
     *
     * @param str
     * @return
     */
    public static String replaceDoubleSpace(String str) {
        if (str != null) {
            StringBuilder sb = new StringBuilder(str);
            for (int i = 0; i < sb.length(); i++) {
                if (" ".equals(sb.substring(i, i + 1))) {
                    if (i - 1 >= 0 && i + 1 < sb.length() && " ".equals(sb.substring(i - 1, i)) && !" ".equals(sb.substring(i + 1, i + 2))) {
                        if (i - 2 < 0 || !" ".equals(sb.substring(i - 2, i - 1))) {
                            sb.replace(i - 1, i, "");
                            sb.replace(i - 1, i, "");
                        }
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 计算剩余时间（东八区）
     *
     * @param startTime
     * @return
     */
    public static String calcuSurplusTime(String startTime, int length) {
        if (isEmpty(startTime)) return "";
        Date oldDate = TimeFormatUtils.strToDate(startTime.split("\\.")[0]);
        if (null == oldDate) return "";
        double intervalMilli = TimeFormatUtils.getCurrentTimeMillis_CH() - oldDate.getTime();
        long maxLenth;
        if (length == -1) maxLenth = 48 * 60 * 60 * 1000;
        else maxLenth = length * 60 * 60 * 1000;
        if (intervalMilli < 0) intervalMilli = 0;
        if (intervalMilli > maxLenth) return "";
        else if (maxLenth - intervalMilli >= 60 * 60 * 1000)
            return (int) Math.ceil((maxLenth - intervalMilli) / (60 * 60 * 1000)) + "小时";
        else if (maxLenth - intervalMilli < 60 * 60 * 1000)
            return (int) Math.ceil((maxLenth - intervalMilli) / (60 * 1000)) + "分钟";
        return "";
    }

    /**
     * @param endTime   毫秒值
     * @param startTime 毫秒值
     * @return 返回相差的时间值, 小于30秒是刚刚
     */
    public static String getDifferenceTime(long endTime, long startTime) {
        double between = (endTime - startTime) / 1000;

        long day = (long) (between / (24 * 3600));
        long hour = (long) (between % (24 * 3600) / 3600);
        long minute = (long) (between % 3600 / 60);
        long second = (long) (between % 60);

        String result = "";
        if (day > 0) {
            result += day + "天";
        }
        if (hour > 0) {
            result += hour + "小时";
        }
        if (minute > 0) {
            result += minute + "分钟";
        }
        if (second > 0) {
            result += second + "秒";
        }
        if (day == 0 && hour == 0 && minute == 0 && second < 60) {
            result = "刚刚";
        }
        return result;
    }

    /**
     * @param endTime   毫秒值
     * @param startTime 毫秒值
     * @return 返回相差的时间值
     */
    public static String getDifferenceTime2(long endTime, long startTime) {
        double between = (endTime - startTime) / 1000;

        long day = (long) (between / (24 * 3600));
        long hour = (long) (between % (24 * 3600) / 3600);
        long minute = (long) (between % 3600 / 60);
        long second = (long) (between % 60);

        String result = "";
        if (day > 0) {
            result += day + "天";
        }
        if (hour > 0) {
            result += hour + "小时";
        }
        if (minute > 0) {
            result += minute + "分钟";
        }
        if (second > 0) {
            result += second + "秒";
        }
        return result;
    }

    /**
     * @param endTime   毫秒值
     * @param startTime 毫秒值
     * @return 返回相差的时间值, 以小时为单位
     */
    public static String getDifferenceTime3(long endTime, long startTime) {
        double between = (endTime - startTime) / 1000;

        long hour = (long) (between / 3600);
        long minute = (long) (between % 3600 / 60);
        long second = (long) (between % 60);

        String result = "";

        if (hour > 0) {
            result += hour + "小时";
        }
        if (minute > 0) {
            result += minute + "分钟";
        }
        if (second > 0) {
            result += second + "秒";
        }
        return result;
    }


    public static String calcuSurplusTime(String startTime) {
        return calcuSurplusTime(startTime, -1);
    }

    public static String getFormatPhoneNumber(String number) {
        String s1 = number.substring(0, 3);
        String s2 = number.substring(3, 7);
        String s3 = number.substring(7);
        String formatNumber = s1 + " " + s2 + " " + s3;
        return formatNumber;
    }

    /**
     * 设置字符串的颜色和点击
     *
     * @return
     */
    public static SpannableString setColorAndClick(Context context, List<ObserveAttentionResult> observeAttentionResults) {
        String result = null;
        SpannableString spanText = null;
        if (null != observeAttentionResults) {
            int lastIndex = 0;
            int currIndex = 0;
            String targetStr = "";
            for (int i = 0; i < observeAttentionResults.size(); i++) {
                if (i == observeAttentionResults.size() - 1) {
                    targetStr += observeAttentionResults.get(i).getObserve_user_name();
                } else {
                    //FIXME null
                    targetStr += observeAttentionResults.get(i).getObserve_user_name() + ", ";
                }
            }
            spanText = new SpannableString(targetStr);
            for (int i = 0; i < observeAttentionResults.size(); i++) {
                String subStr = null;
                if (i == observeAttentionResults.size() - 1) {
                    subStr = observeAttentionResults.get(i).getObserve_user_name();
                } else {
                    subStr = observeAttentionResults.get(i).getObserve_user_name() + ", ";
                }
                int subLenth = subStr.length();
                lastIndex = currIndex;
                currIndex = lastIndex + subLenth;
                spanText.setSpan(new ForegroundColorSpan(Color.parseColor("#00a0e9")), lastIndex, currIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanText.setSpan(new TextClickSpan(context, observeAttentionResults.get(i)), lastIndex, currIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastIndex = currIndex;
            }
        }
        return spanText;
    }

    private static class TextClickSpan extends ClickableSpan {

        private ObserveAttentionResult observeAttentionResult;
        private Context context;

        public TextClickSpan(Context context, ObserveAttentionResult observeAttentionResult) {
            this.observeAttentionResult = observeAttentionResult;
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, PersonalHomeActivity.class);
            intent.putExtra("userId", observeAttentionResult.getObserve_user_id());
            if (TextUtils.equals(UserInfoUtil.getUserId(context), observeAttentionResult.getObserve_user_id())) {
                intent.putExtra("isSelf", true);
            } else {
                intent.putExtra("isSelf", false);
            }
            intent.putExtra("activityType", 0);
            context.startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(Color.parseColor("#00a0e9"));
        }
    }

}
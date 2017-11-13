package com.haiqiu.miaohi.utils;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.Constellation;

/**
 * 星座工具
 * Created by ningl on 16/9/5.
 */
public class ConstellationUtil {

    /**
     * 根据日期获取星座
     *
     * @param date 日期字符串 2016
     */
    public static Constellation getConstellationByDate(String date) {
        String[] split = null;
        int resourceID = 0;
        int yueint = 0;
        int riint = 0;
        String result = null;
        try {
            split = date.split("-");
            if (split.length != 3) return null;
            yueint = Integer.parseInt(split[1]);
            riint = Integer.parseInt(split[2]);
        } catch (Exception e) {

            MHLogUtil.e("getConstellationByDate", e);
            return null;
        }
        switch (yueint) {
            case 1:
                if (riint <= 19) {
                    result = "摩羯座";
                    resourceID = R.drawable.svg_mojiezuo;
                } else {
                    resourceID = R.drawable.svg_shuipingzuo;
                    result = "水瓶座";
                }
                break;
            case 2:
                if (riint <= 18) {
                    result = "水瓶座";
                    resourceID = R.drawable.svg_shuipingzuo;
                } else {
                    result = "双鱼座";
                    resourceID = R.drawable.svg_shuangyuzuo;
                }
                break;
            case 3:
                if (riint <= 20) {
                    result = "双鱼座";
                    resourceID = R.drawable.svg_shuangyuzuo;
                } else {
                    resourceID = R.drawable.svg_baiyangzuo;
                    result = "白羊座";
                }
                break;
            case 4:
                if (riint <= 19) {
                    result = "白羊座";
                    resourceID = R.drawable.svg_baiyangzuo;
                } else {
                    result = "金牛座";
                    resourceID = R.drawable.svg_jinniuzuo;
                }
                break;
            case 5:
                if (riint <= 20) {
                    result = "金牛座";
                    resourceID = R.drawable.svg_jinniuzuo;
                } else {
                    resourceID = R.drawable.svg_shuangyuzuo;
                    result = "双子座";
                }
                break;
            case 6:
                if (riint <= 21) {
                    resourceID = R.drawable.svg_shuangyuzuo;
                    result = "双子座";
                } else {
                    resourceID = R.drawable.svg_juxiezuo;
                    result = "巨蟹座";
                }
                break;
            case 7:
                if (riint <= 22) {
                    resourceID = R.drawable.svg_juxiezuo;
                    result = "巨蟹座";
                } else {
                    resourceID = R.drawable.svg_shizizuo;
                    result = "狮子座";
                }
                break;
            case 8:
                if (riint <= 22) {
                    resourceID = R.drawable.svg_shizizuo;
                    result = "狮子座";
                } else {
                    resourceID = R.drawable.svg_chunvzuo;
                    result = "处女座";
                }
                break;
            case 9:
                if (riint <= 22) {
                    resourceID = R.drawable.svg_chunvzuo;
                    result = "处女座";
                } else {
                    resourceID = R.drawable.svg_tianchengzuo;
                    result = "天秤座";
                }
                break;
            case 10:
                if (riint <= 23) {
                    resourceID = R.drawable.svg_tianchengzuo;
                    result = "天秤座";
                } else {
                    resourceID = R.drawable.svg_tianxiezuo;
                    result = "天蝎座";
                }
                break;
            case 11:
                if (riint <= 21) {
                    resourceID = R.drawable.svg_tianxiezuo;
                    result = "天蝎座";
                } else {
                    resourceID = R.drawable.svg_sheshouzuo;
                    result = "射手座";
                }
                break;
            case 12:
                if (riint <= 21) {
                    resourceID = R.drawable.svg_sheshouzuo;
                    result = "射手座";
                } else {
                    resourceID = R.drawable.svg_mojiezuo;
                    result = "摩羯座";
                }
                break;
        }
        Constellation constellation = new Constellation();
        constellation.setConstellationId(resourceID);
        constellation.setConstellationName(result);
        return constellation;
    }
}

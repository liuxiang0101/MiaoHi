package com.haiqiu.miaohi.utils;

import android.content.Context;

/**
 * Created by zhandalin on 2016-10-26 11:13.
 * 说明:获取工程中所用到的key
 */
public class MiaoHiKeyUtils {
    static {
        System.loadLibrary("miaohi");
    }

    public static native void init(Context context);

    /**
     * @return 融云正式key
     */
    public static native String getRongIMKey();

    public static native String getWeiXinAppid();

    public static native String getWeiXinAppsecret();

    public static native String getQQAppid();

    public static native String getQQAppkey();

    public static native String getSinaAppkey();

    public static native String getSinaAppsecret();


    public static native String getTDAppid();

    public static native String getUmengAppkey();


}

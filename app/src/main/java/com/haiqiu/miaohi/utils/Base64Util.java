package com.haiqiu.miaohi.utils;

/**
 * Base64工具
 * Created by ningl on 2016/5/20.
 */
public class Base64Util {

    /**
     * base64加密
     *
     * @param str
     * @return
     */
    public static String getBase64Str(String str) {
        return Base64.encode(str.getBytes());
    }

    /**
     * 解码
     *
     * @param s
     * @return
     */
    public static String getFromBase64(String s) {
        return  new String(Base64.decode(s));
    }
}

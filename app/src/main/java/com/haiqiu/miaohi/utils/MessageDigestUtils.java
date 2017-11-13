package com.haiqiu.miaohi.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 获取MD5值
 * Created by miaohi on 2016/5/4.
 */
public class MessageDigestUtils {
    private static final String TAG = "MessageDigestUtils";
    /**
     * 将字符串转成MD5值
     */
    public static String getStringMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            MHLogUtil.e(TAG,e);
            return null;
        } catch (UnsupportedEncodingException e) {
            MHLogUtil.e(TAG,e);
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     * SHA-256加密
     */
    private static byte[] getHash(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        if (null != digest && null != password) {
            digest.reset();
            return digest.digest(password.getBytes());
        }
        return new byte[0];
    }

    public static String getStringSHA256(String strForEncrypt) {
        byte[] data = getHash(strForEncrypt);
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }
}

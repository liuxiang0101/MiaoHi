package com.haiqiu.miaohi.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LiuXiang on 2016/12/28.
 */
public class InputVerificationUtil {
    /**
     * 判断字符串中是否只存在数字和字母
     *
     * @param s
     * @return
     */
    public static boolean pwdIsNumAndLetter(Context context, String s) {
        if (s.length()<=0)return false;
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            return true;
        } else {
            Toast.makeText(context, "密码格式是数字或字母", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

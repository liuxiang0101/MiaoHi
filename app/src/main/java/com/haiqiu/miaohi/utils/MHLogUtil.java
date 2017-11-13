package com.haiqiu.miaohi.utils;

import android.util.Log;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.umeng.analytics.MobclickAgent;


public class MHLogUtil {
    public static boolean showLog = ConstantsValue.isDeveloperMode(null);
    public static final String TAG = "MiaoHi";

    public static void v(String logText) {
        if (showLog) {
            Log.v(TAG, String.valueOf(logText));
        }
    }

    public static void d(String logText) {
        if (showLog) {
            Log.d(TAG, String.valueOf(logText));
        }
    }

    public static void i(String logText) {
        if (showLog) {
            Log.i(TAG, String.valueOf(logText));
        }
    }

    public static void w(String logText) {
        if (showLog) {
            Log.w(TAG, String.valueOf(logText));
        }
    }


    public static void v(String tag, String logText) {
        if (showLog) {
            Log.v(tag, logText);
        }
    }

    public static void d(String tag, String logText) {
        if (showLog) {
            if (null != logText && logText.length() > 3000) {
                int index = 0;
                String tempText;
                do {
                    tempText = logText.substring(3000 * index, 3000 * (index + 1));
                    if (index != 0)
                        Log.d(tag, String.valueOf("------去掉该行-----\n" + tempText));
                    else
                        Log.d(tag, String.valueOf(tempText));

                    tempText = logText.substring(3000 * (index + 1), logText.length());
                    index++;
                } while (tempText.length() > 3000);

                Log.d(tag, String.valueOf("------去掉该行------\n" + tempText));
            } else {
                Log.d(tag, logText);
            }
        }
    }

    public static void i(String tag, String logText) {
        if (showLog) {
            Log.i(tag, logText);
        }
    }

    public static void w(String tag, String logText) {
        if (showLog) {
            Log.w(tag, logText);
        }
    }

    public static void e(String logText) {
        if (showLog) {
            Log.e(TAG, String.valueOf(logText));
        }
    }

    public static void e(String tag, String logText) {
        if (showLog) {
            Log.e(tag, logText);
        }
    }

    public static void e(Throwable e) {
        e(TAG, e);
    }

    /**
     * catch 的错误上传到服务器上
     */
    public static void e(String tag, Throwable e) {
        String msg = "TAG=" + tag;
        if (showLog) {
            if (null != e) {
                String temp = e.getMessage() + "\n" + ArraysToString(e.getStackTrace());
                msg += "--info=" + temp;
                Log.e(tag, temp);
            } else {
                Log.e(tag, msg);
            }
        }
        MobclickAgent.reportError(MHApplication.applicationContext, msg);
    }

    public static void d(Class<?> c, String logText) {
        if (showLog) {
            Log.d(TAG, "[" + c.getSimpleName() + "]" + logText);
        }
    }

    public static void d(Object o, String logText) {
        if (showLog) {
            Log.d(TAG, "[" + o.getClass().getSimpleName() + "]" + logText);
        }
    }

    private static String ArraysToString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax < 0)
            return "null";
        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString();
            b.append("\n");
        }
    }
}

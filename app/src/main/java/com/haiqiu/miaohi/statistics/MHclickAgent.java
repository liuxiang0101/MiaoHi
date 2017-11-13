package com.haiqiu.miaohi.statistics;

import android.content.Context;
import android.text.TextUtils;

import com.haiqiu.miaohi.utils.MHLogUtil;

/**
 * Created by LiuXiang on 2016/11/16.
 * 事件统计-方法唤起
 */
public class MHclickAgent {
    private static final String TAG = "MHclickAgent";
    private static final EventRecord eventRecord = new EventRecord();

    //统计时长
    //Activity之间有继承或者控制关系请不要同时在父和子Activity中重复添加onPause和onResume方法，否则会造成重复统计，导致启动次数异常增高
    public static void onPause(Context context) {
        eventRecord.pauseContext(context);
    }

    public static void onResume(Context context) {
        if (context == null) {
            MHLogUtil.e(TAG, "unexpected null context in onResume");
        } else {
            eventRecord.resumeContext(context);
        }
    }

    //统计页面
    public static void onPageStart(Context context,String pageName) {
        if (!TextUtils.isEmpty(pageName)) {
            eventRecord.saveStartPageName(context,pageName);
        } else {
            MHLogUtil.e(TAG, "pageName is null or empty");
        }

    }

    public static void onPageEnd(Context context,String pageName) {
        if (!TextUtils.isEmpty(pageName)) {
            eventRecord.saveEndPageName(context,pageName);
        } else {
            MHLogUtil.e(TAG, "pageName is null or empty");
        }

    }

    //点击事件统计
    public static void onEvent(Context context, String eventName) {
        eventRecord.recordEvent(context, eventName, null, -1L, 1);
    }

    public static void onEvent(Context context, String eventName, String eventLabel) {
        if (TextUtils.isEmpty(eventLabel)) {
            MHLogUtil.e(TAG, "label is null or empty");
        } else {
            eventRecord.recordEvent(context, eventName, eventLabel, -1L, 1);
        }
    }

    //账号统计
    public static void onProfileSignIn(String id) {
        onProfileSignIn("local", id);
    }

    public static void onProfileSignIn(String idType, String uid) {
        if (TextUtils.isEmpty(uid)) {
            MHLogUtil.e(TAG, "uid is null");
        } else if (uid.length() > 64) {
            MHLogUtil.e(TAG, "uid is Illegal(length bigger then  legitimate length).");
        } else {
            if (TextUtils.isEmpty(idType)) {
                eventRecord.saveAccound("localId", uid);
            } else {
                if (idType.length() > 32) {
                    MHLogUtil.e(TAG, "provider is Illegal(length bigger then  legitimate length).");
                    return;
                }

                eventRecord.saveAccound(idType, uid);
            }

        }
    }
}

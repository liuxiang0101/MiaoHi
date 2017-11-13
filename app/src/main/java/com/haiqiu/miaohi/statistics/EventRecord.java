package com.haiqiu.miaohi.statistics;

import android.content.Context;

import com.haiqiu.miaohi.utils.MHLogUtil;


/**
 * Created by LiuXiang on 2016/11/16.
 */
public class EventRecord {
    private final String TAG = "EventRecord";
    private ActivityStatistics activityStatistics = new ActivityStatistics();
    private EventStatistics eventStatistics = new EventStatistics();

    //界面执行onPause时传入context处理方法
    void pauseContext(final Context context) {
        if (context == null) {
            MHLogUtil.e(TAG, "unexpected null context in onPause");
        } else {
            if (GetStatisticsConfig.ACTIVITY_DURATION_OPEN) {
                this.activityStatistics.saveOnPausePageName(context, context.getClass().getName());
            }
        }
    }

    //界面执行onResume时传入context处理方法
    void resumeContext(final Context context) {
        if (context == null) {
            MHLogUtil.e(TAG, "unexpected null context in onResume");
        } else {
            if (GetStatisticsConfig.ACTIVITY_DURATION_OPEN) {
                this.activityStatistics.saveOnResumePageName(context, context.getClass().getName());
            }
        }
    }

    void saveEndPageName(Context context, String pageName) {
        if (!GetStatisticsConfig.ACTIVITY_DURATION_OPEN) {
            try {
                this.activityStatistics.saveOnPausePageName(context, pageName);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }

    void saveStartPageName(Context context, String pageName) {
        if (!GetStatisticsConfig.ACTIVITY_DURATION_OPEN) {
            try {
                this.activityStatistics.saveOnResumePageName(context, pageName);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }

    public void recordEvent(Context context, String eventName, String eventLabel, long time, int num) {
        try {
            this.eventStatistics.saveEvent(eventName, eventLabel, time, num);
        } catch (Exception exception) {
            MHLogUtil.e(TAG, exception.toString());
        }

    }

    void saveAccound(String idType, String uid) {
        try {

        } catch (Exception exception) {
            MHLogUtil.e(TAG, " Excepthon  in  onProfileSignIn" + exception.getMessage());
        }

    }
}

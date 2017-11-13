package com.haiqiu.miaohi.statistics;

import com.haiqiu.miaohi.statistics.bean.EventRecordBean;
import com.haiqiu.miaohi.statistics.bean.StatisticalDataStorageBean;
import com.haiqiu.miaohi.utils.MHLogUtil;

import java.util.HashMap;

/**
 * Created by LiuXiang on 2016/11/17.
 * 自定义事件统计-存储
 */
public class EventStatistics {
    private final String TAG = "EventStatistics";

    public void saveEvent(String eventName, String eventLabel, long time, int number) {
        try {
            if (!this.judgeEventName(eventName) || !this.judgeEventLabel(eventLabel)) {
                return;
            }
            MHLogUtil.i(TAG, "onEvent being called! eventName:" + eventName + ",  eventLabel" + eventLabel);
            HashMap hashMap = new HashMap();
            hashMap.put(eventName, eventLabel == null ? "" : eventLabel);

            EventRecordBean bean = new EventRecordBean();
            bean.setEventName(eventName);
            bean.setEventLabel(eventLabel == null ? "" : eventLabel);
            bean.setTime(System.currentTimeMillis());
            bean.setCount(number);
            StatisticalDataStorageBean.onEventList.add(bean);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    //判断自定义事件Name
    private boolean judgeEventName(String string) {
        try {
            if (string != null) {
                int length = string.trim().getBytes().length;
                if (length > 0 && length <= 128) {
                    return true;
                }
            }

            MHLogUtil.e(TAG, "Event id is empty or too long in tracking Event");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }
    //判断自定义事件Label
    private boolean judgeEventLabel(String string) {
        try {
            if (string == null) {
                return true;
            }

            if (string.trim().getBytes().length <= 256) {
                return true;
            }

            MHLogUtil.e(TAG, "Event label or value is empty or too long in tracking Event");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }
}

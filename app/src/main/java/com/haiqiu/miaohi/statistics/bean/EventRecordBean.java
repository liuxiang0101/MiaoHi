package com.haiqiu.miaohi.statistics.bean;

/**
 * Created by LiuXiang on 2016/11/18.
 */
public class EventRecordBean {
    private String eventName;
    private String eventLabel;
    private long time;
    private int count;

    public String getEventName() {
        return eventName;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    public long getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventLabel(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

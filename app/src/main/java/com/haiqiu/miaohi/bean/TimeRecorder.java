package com.haiqiu.miaohi.bean;

/**
 * Created by zhandalin on 2016-05-23 09:22.
 * 说明:用于记录录制时间信息
 */
public class TimeRecorder {
    private long startTime;
    private long endTime;
    private long originalEndTime;
    private boolean isEnable = true;

    public TimeRecorder(long startTime, long endTime, long originalEndTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.originalEndTime = originalEndTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getOriginalEndTime() {
        return originalEndTime;
    }

    public void setOriginalEndTime(long originalEndTime) {
        this.originalEndTime = originalEndTime;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
}

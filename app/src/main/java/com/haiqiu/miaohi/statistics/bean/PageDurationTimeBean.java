package com.haiqiu.miaohi.statistics.bean;

/**
 * Created by LiuXiang on 2016/11/18.
 */
public class PageDurationTimeBean {
    private String pageName;
    private long durationTime;

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setDurationTime(long durationTime) {
        this.durationTime = durationTime;
    }

    public String getPageName() {
        return pageName;
    }

    public long getDurationTime() {
        return durationTime;
    }
}

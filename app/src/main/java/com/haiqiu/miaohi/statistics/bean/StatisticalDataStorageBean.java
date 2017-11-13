package com.haiqiu.miaohi.statistics.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuXiang on 2016/11/23.
 * 存储一次的统计数据
 */
public class StatisticalDataStorageBean {
    public static List<PageDurationTimeBean> pageStopList=new ArrayList<>();
    public static List<EventRecordBean> onEventList=new ArrayList<>();

    public static List getPageStopList() {
        return pageStopList;
    }

    public static List getOnEventList() {
        return onEventList;
    }

}

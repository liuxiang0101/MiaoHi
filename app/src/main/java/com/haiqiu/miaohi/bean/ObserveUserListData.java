package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by LiuXiang on 2016/9/2.
 */
public class ObserveUserListData {
    private int observe_count;
    private int praise_count;
    private List<ObserveUserListObj> page_result;

    public int getObserve_count() {
        return observe_count;
    }

    public int getPraise_count() {
        return praise_count;
    }

    public List<ObserveUserListObj> getPage_result() {
        return page_result;
    }
}

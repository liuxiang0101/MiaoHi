package com.haiqiu.miaohi.bean;

import java.util.ArrayList;

/**
 * 发现--大咖榜
 * Created by miaohi on 2016/5/4.
 */
public class TopVipObj {
    private String page_index;
    private String page_size;
    private String video_count;
    private ArrayList<PageResultArrayObj> page_result;

    @Override
    public String toString() {
        return "TopVipObj{" +
                "page_index='" + page_index + '\'' +
                ", page_size='" + page_size + '\'' +
                ", page_result=" + page_result +
                '}';
    }

    public String getVideo_count() {
        return video_count;
    }

    public String getPage_index() {
        return page_index;
    }

    public String getPage_size() {
        return page_size;
    }

    public ArrayList<PageResultArrayObj> getPage_result() {
        return page_result;
    }

    public void setPage_index(String page_index) {
        this.page_index = page_index;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public void setPage_result(ArrayList<PageResultArrayObj> page_result) {
        this.page_result = page_result;
    }
}

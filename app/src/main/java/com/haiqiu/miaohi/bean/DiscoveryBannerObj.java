package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/12/12.
 */
public class DiscoveryBannerObj {
    private String banner_id;
    private String banner_uri;
    private String target_name;
    private String target_content;
    private String target_content_extra;
    private int banner_state;
    private int banner_width;
    private int banner_height;
    private int recommend;


    public String getBanner_uri() {
        return banner_uri;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public String getTarget_name() {
        return target_name;
    }

    public String getTarget_content() {
        return target_content;
    }

    public String getTarget_content_extra() {
        return target_content_extra;
    }

    public int getBanner_state() {
        return banner_state;
    }

    public int getBanner_width() {
        return banner_width;
    }

    public int getBanner_height() {
        return banner_height;
    }

    public int getRecommend() {
        return recommend;
    }
}

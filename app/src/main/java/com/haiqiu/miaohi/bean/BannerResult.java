package com.haiqiu.miaohi.bean;

/**
 * Created by zhandalin on 2016-06-16 17:37.
 * 说明:
 */
public class BannerResult {
    private String banner_id;
    private int banner_type;
    private String banner_note2;
    private String banner_note1;
    private String recommend;
    private String banner_state;
    private String banner_uri;

    public String getBanner_id() {
        return banner_id;
    }


    public int getBanner_type() {
        return banner_type;
    }


    public String getBanner_note2() {
        return banner_note2;
    }


    public String getBanner_note1() {
        return banner_note1;
    }



    public String getBanner_uri() {
        return banner_uri;
    }

    public void setBanner_uri(String banner_uri) {
        this.banner_uri = banner_uri;
    }
}

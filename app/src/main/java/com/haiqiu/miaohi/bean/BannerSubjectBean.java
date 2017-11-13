package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2017/1/4.
 * H5底部分享所需bean
 */
public class BannerSubjectBean {
    //专题H5
    private String activity_name;
    private String activity_id;
    private String activity_note;
    //普通H5
    private String html_note;
    private String share_icon;
    private String html_name;

    public String getActivity_note() {
        return activity_note;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getHtml_note() {
        return html_note;
    }

    public String getShare_icon() {
        return share_icon;
    }

    public String getHtml_name() {
        return html_name;
    }
}

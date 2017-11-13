package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * 大咖榜--条目
 * Created by miaohi on 2016/5/4.
 */
public class PageResultArrayObj {
    //大咖榜
    private String user_id;
    private String user_name;
    private String portrait_uri;
    private String portrait_state;
    private String fans_count;
    private String user_type;

    //热榜
    private String hot_count;

    //最迷恋/惊叹/喜欢榜
    private String auto_id;
    private String video_id;
    private String video_uri;
    private String video_state;
    private String duration_second;
    private String cover_uri;
    private String cover_state;
    private String face_count;


    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPortrait_uri() {
        return portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public String getPortrait_state() {
        return portrait_state;
    }

    public String getFans_count() {
        return fans_count;
    }

    public String getHot_count() {
        return hot_count;
    }

    public String getAuto_id() {
        return auto_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public String getVideo_state() {
        return video_state;
    }

    public String getDuration_second() {
        return duration_second;
    }

    public String getCover_uri() {
        if (null != cover_uri && cover_uri.contains("?")) {
            return cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public String getCover_state() {
        return cover_state;
    }

    public String getFace_count() {
        return face_count;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }

    public void setPortrait_state(String portrait_state) {
        this.portrait_state = portrait_state;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }

    public void setHot_count(String hot_count) {
        this.hot_count = hot_count;
    }

    public void setAuto_id(String auto_id) {
        this.auto_id = auto_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public void setVideo_state(String video_state) {
        this.video_state = video_state;
    }

    public void setDuration_second(String duration_second) {
        this.duration_second = duration_second;
    }

    public void setCover_uri(String cover_uri) {
        this.cover_uri = cover_uri;
    }

    public void setCover_state(String cover_state) {
        this.cover_state = cover_state;
    }

    public void setFace_count(String face_count) {
        this.face_count = face_count;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}

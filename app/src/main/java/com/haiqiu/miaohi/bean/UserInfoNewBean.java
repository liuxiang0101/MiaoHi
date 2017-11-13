package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * 用户信息
 * Created by ningl on 2016/6/22.
 */
public class UserInfoNewBean {


    /**
     * portrait_state : 10
     * achievement_video_count : 3
     * user_name : 您
     * attention_count : 1
     * user_gender : 1
     * fans_new_count : 2
     * gift_received_count : 0
     * praise_sent_count : 0
     * user_state : 10
     * user_type : 10
     * gift_received_new_count : 0
     * user_note : 用户说明测试
     * portrait_uri : http://7xt99c.com1.z0.glb.clouddn.com/myIcon_20160531160338
     */

    private String portrait_state;
    private String achievement_video_count;
    private String user_name;
    private String attention_count;
    private String user_gender;
    private String fans_new_count;
    private String gift_received_count;
    private String praise_sent_count;
    private String user_state;
    private String user_type;
    private String gift_received_new_count;
    private String user_note;
    private String portrait_uri;

    public String getPortrait_state() {
        return portrait_state;
    }

    public void setPortrait_state(String portrait_state) {
        this.portrait_state = portrait_state;
    }

    public String getAchievement_video_count() {
        return achievement_video_count;
    }

    public void setAchievement_video_count(String achievement_video_count) {
        this.achievement_video_count = achievement_video_count;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAttention_count() {
        return attention_count;
    }

    public void setAttention_count(String attention_count) {
        this.attention_count = attention_count;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getFans_new_count() {
        return fans_new_count;
    }

    public void setFans_new_count(String fans_new_count) {
        this.fans_new_count = fans_new_count;
    }

    public String getGift_received_count() {
        return gift_received_count;
    }

    public void setGift_received_count(String gift_received_count) {
        this.gift_received_count = gift_received_count;
    }

    public String getPraise_sent_count() {
        return praise_sent_count;
    }

    public void setPraise_sent_count(String praise_sent_count) {
        this.praise_sent_count = praise_sent_count;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getGift_received_new_count() {
        return gift_received_new_count;
    }

    public void setGift_received_new_count(String gift_received_new_count) {
        this.gift_received_new_count = gift_received_new_count;
    }

    public String getUser_note() {
        return user_note;
    }

    public void setUser_note(String user_note) {
        this.user_note = user_note;
    }

    public String getPortrait_uri() {
        return portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }
}

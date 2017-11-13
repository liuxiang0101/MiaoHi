package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/9/20.
 */
public class RecommendUserResult {
    private String user_id;
    private String user_name;
    private String portrait_uri;
    private String vip_note;
    private int user_type;
    private int user_gender;
    private int attention_state;
    private int portrait_state;

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public int getUser_type() {
        return user_type;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public int getAttention_state() {
        return attention_state;
    }

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }

    public int getPortrait_state() {
        return portrait_state;
    }

    public String getVip_note() {
        return vip_note;
    }
}

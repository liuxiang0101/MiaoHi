package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2017/1/3.
 */
public class AttentionSingleBean {
    private String action_mark;
    private String user_id;

    public AttentionSingleBean(String action_mark, String user_id) {
        this.action_mark = action_mark;
        this.user_id = user_id;
    }

    public String getAction_mark() {
        return action_mark;
    }

    public String getUser_id() {
        return user_id;
    }
}

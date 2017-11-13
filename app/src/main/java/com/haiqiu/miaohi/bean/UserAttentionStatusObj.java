package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/12/6.
 */
public class UserAttentionStatusObj {
    private String user_id;
    private int attention_state;

    public String getUser_id() {
        return user_id;
    }

    public boolean isAttention_state() {
        return attention_state == 1;
    }
}

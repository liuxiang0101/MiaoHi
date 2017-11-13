package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/12/16.
 */
public class SearchUserList {
    private int portait_state;
    private int user_type;
    private int user_gender;
    private int attention_state;
    private int answer_auth;
    private String user_id;
    private String user_name;
    private String portrait_uri;
    private String user_authentic;

    public int getUser_gender() {
        return user_gender;
    }

    public boolean isAnswer_auth() {
        return answer_auth==1;
    }

    public int getPortait_state() {
        return portait_state;
    }

    public int getUser_type() {
        return user_type;
    }

    public boolean isAttention_state() {
        return attention_state == 1;
    }

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state ? 1 : 0;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public String getUser_authentic() {
        return user_authentic;
    }
}

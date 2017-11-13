package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/12/12.
 */
public class DiscoveryUserObj {
    private String user_id;
    private String user_name;
    private String portrait_uri;
    private String user_note;
    private int portait_state;
    private int user_type;
    private int attention_state;

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public String getUser_note() {
        return user_note;
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
}

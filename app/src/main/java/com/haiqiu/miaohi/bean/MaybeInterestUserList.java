package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by LiuXiang on 2016/12/13.
 */
public class MaybeInterestUserList {
    private String user_id;
    private String user_name;
    private String portrait_uri;
    private String user_note;
    private int portait_state;
    private int user_type;
    private int attention_state;
    private int user_gender;
    private int answer_auth ;
    List<MaybeInterestObjList> object_list;

    public int getAnswer_auth() {
        return answer_auth;
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

    public String getUser_note() {
        return user_note;
    }

    public int getAttention_state() {
        return attention_state;
    }

    public int getPortait_state() {
        return portait_state;
    }

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state?1:0;
    }

    public int getUser_type() {
        return user_type;
    }

    public boolean isAttention_state() {
        return attention_state == 1;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public List<MaybeInterestObjList> getObject_list() {
        return object_list;
    }
}

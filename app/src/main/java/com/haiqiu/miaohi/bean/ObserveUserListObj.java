package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/9/2.
 */
public class ObserveUserListObj  {

    private String user_id;//用户id
    private int attention_state;//关注状态

    private String nick_name;
    private String user_name;
    private int answer_auth;
    private int user_type;
    private int user_gender;
    private int free_observer;
    private String portrait_uri;
    private String user_portrait_uri;
    private String user_authentic;
    private int user_portrait_state;

    public String getUser_name() {
        return user_name;
    }

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public int getFree_observer() {
        return free_observer;
    }

    public String getUser_authentic() {
        return user_authentic;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public int getAnswer_auth() {
        return answer_auth;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getUser_portrait_uri() {
        return user_portrait_uri;
    }

    public int getUser_portrait_state() {
        return user_portrait_state;
    }

    public boolean isAttention_state() {
        return attention_state == 1;
    }

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state ? 1 : 0;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getAttention_state() {
        return attention_state;
    }

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setAnswer_auth(int answer_auth) {
        this.answer_auth = answer_auth;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }



    public void setUser_authentic(String user_authentic) {
        this.user_authentic = user_authentic;
    }

}

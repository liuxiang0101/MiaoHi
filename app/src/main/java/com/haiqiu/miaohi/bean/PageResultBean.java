package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * Created by hackest on 16/7/27.
 */
public class PageResultBean {
    private String portrait_state;
    private String answer_total;
    private String vip_note;
    private String user_type;
    private String user_id;
    private String user_name;
    private String user_note;
    private String question_total;
    private String play_total;
    private String user_state;
    private String portrait_uri;

    private String answer_authentic;
    private String answer_note;

    public String getPortrait_state() {
        return portrait_state;
    }

    public void setPortrait_state(String portrait_state) {
        this.portrait_state = portrait_state;
    }

    public String getAnswer_total() {
        return answer_total;
    }

    public void setAnswer_total(String answer_total) {
        this.answer_total = answer_total;
    }

    public String getVip_note() {
        return vip_note;
    }

    public void setVip_note(String vip_note) {
        this.vip_note = vip_note;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_note() {
        return user_note;
    }

    public void setUser_note(String user_note) {
        this.user_note = user_note;
    }

    public String getQuestion_total() {
        return question_total;
    }

    public void setQuestion_total(String question_total) {
        this.question_total = question_total;
    }

    public String getPlay_total() {
        return play_total;
    }

    public void setPlay_total(String play_total) {
        this.play_total = play_total;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public String getPortrait_uri() {
        return portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }

    public String getAnswer_authentic() {
        return answer_authentic;
    }

    public void setAnswer_authentic(String answer_authentic) {
        this.answer_authentic = answer_authentic;
    }

    public String getAnswer_note() {
        return answer_note;
    }

    public void setAnswer_note(String answer_note) {
        this.answer_note = answer_note;
    }
}

package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * 问我的实体类
 * Created by ningl on 2016/7/20.
 */
public class AskMeBean {


    /**
     * question_text : 不不不不不不不不不聚聚不不不不不不不不哭噜噜噜女复读不啊不哭剧毒考虑噜噜噜
     * question_time : 2016-07-26 10:23:57.0
     * portrait_state : 10
     * question_state : 0
     * user_type : USER-03c97163-3464-11e6-83a3-44a8424640fa
     * user_id : USER-8571b2c4-265e-11e6-83a3-44a8424640fa
     * user_name : 狼牙棒
     * question_private : 0
     * question_id : EXPR-018630ab-52d8-11e6-8f77-44a8424640fa
     * user_state : 16
     * portrait_uri : http://icon.dev.miaohi.com/iconImageE4A9FFE9D67F11230113EB01F637E41F_2016_07_09_03_58_45_10
     */

    private String question_text;
    private String question_time;
    private String portrait_state;
    private String question_state;
    private String user_type;
    private String user_id;
    private String user_name;
    private int question_private;
    private String question_id;
    private String user_state;
    private String portrait_uri;
    private String video_time;

    public String getVideo_time() {
        return video_time;
    }


    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getQuestion_time() {
        return question_time;
    }


    public String getPortrait_state() {
        return portrait_state;
    }

    public void setPortrait_state(String portrait_state) {
        this.portrait_state = portrait_state;
    }

    public String getQuestion_state() {
        return question_state;
    }

    public void setQuestion_state(String question_state) {
        this.question_state = question_state;
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

    public int getQuestion_private() {
        return question_private;
    }



    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }



    public String getPortrait_uri() {
        return portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }
}

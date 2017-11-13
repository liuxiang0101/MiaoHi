package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * Created by ningl on 2016/7/20.
 */
public class HaveReadBean {


    /**
     * video_time : 2016-07-26 16:51:44.0
     * answer_user_name : vip1
     * cover_uri : http://image.dev.miaohi.com/coverrEpdBDD3dfd3j8XjbteJ54QByBhib4mh_2016_7_25_10_23_47
     * answer_portrait_state : 10
     * answer_user_state : 10
     * question_id : EXPR-2aca23ae-525c-11e6-8f77-44a8424640fa
     * answer_user_type : 20
     * answer_portrait_uri : http://7xt99c.com2.z0.glb.qiniucdn.com/portrait_demo_woman_2.png
     * question_text : 5p2l5ZKv6ICD6JmR5oiR6Zi/54u4
     * cover_state : 10
     * question_state : 10
     * answer_user_id : USER-vipcd9a2-062a-11e6-9003-44a8424640fc
     * video_id : ANVI-be89ce50-52d6-11e6-8f77-44a8424640fa
     */

    private String video_time;
    private String answer_user_name;
    private String cover_uri;
    private String answer_portrait_state;
    private String answer_user_state;
    private String question_id;
    private String answer_user_type;
    private String answer_portrait_uri;
    private String question_text;
    private String cover_state;
    private String question_state;
    private String answer_user_id;
    private String video_id;

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public String getAnswer_user_name() {
        return answer_user_name;
    }

    public void setAnswer_user_name(String answer_user_name) {
        this.answer_user_name = answer_user_name;
    }

    public String getCover_uri() {
        if (null != cover_uri && cover_uri.contains("?")) {
            return cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return cover_uri+ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public void setCover_uri(String cover_uri) {
        this.cover_uri = cover_uri;
    }

    public String getAnswer_portrait_state() {
        return answer_portrait_state;
    }

    public void setAnswer_portrait_state(String answer_portrait_state) {
        this.answer_portrait_state = answer_portrait_state;
    }

    public String getAnswer_user_state() {
        return answer_user_state;
    }

    public void setAnswer_user_state(String answer_user_state) {
        this.answer_user_state = answer_user_state;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_user_type() {
        return answer_user_type;
    }

    public void setAnswer_user_type(String answer_user_type) {
        this.answer_user_type = answer_user_type;
    }

    public String getAnswer_portrait_uri() {
        return answer_portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setAnswer_portrait_uri(String answer_portrait_uri) {
        this.answer_portrait_uri = answer_portrait_uri;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getCover_state() {
        return cover_state;
    }

    public void setCover_state(String cover_state) {
        this.cover_state = cover_state;
    }

    public String getQuestion_state() {
        return question_state;
    }

    public void setQuestion_state(String question_state) {
        this.question_state = question_state;
    }

    public String getAnswer_user_id() {
        return answer_user_id;
    }

    public void setAnswer_user_id(String answer_user_id) {
        this.answer_user_id = answer_user_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}

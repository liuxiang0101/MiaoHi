package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/12/13.
 */
public class ChoiceQaPageResult {
    private String answer_time_text;
    private String question_content;
    private long observe_count;
    private long observe_price;
    private String observe_price_text;
    private String answer_vip_note;
    private boolean temporary_free;
    private long time_remain;
    private boolean is_question_owner;
    private String question_id;
    private String answer_user_id;
    private int answer_user_type;
    private String answer_user_name;
    private int answer_user_state;
    private String answer_user_portrait;
    private String answer_user_note;
    private String answer_user_remarks;

    public String getAnswer_vip_note() {
        return answer_vip_note;
    }

    public String getAnswer_time_text() {
        return answer_time_text;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public long getObserve_count() {
        return observe_count;
    }

    public String getObserve_price_text() {
        return observe_price_text;
    }

    public boolean isTemporary_free() {
        return temporary_free;
    }

    public long getTime_remain() {
        return time_remain;
    }


    public String getQuestion_id() {
        return question_id;
    }

    public String getAnswer_user_id() {
        return answer_user_id;
    }

    public int getAnswer_user_type() {
        return answer_user_type;
    }

    public String getAnswer_user_name() {
        return answer_user_name;
    }

    public long getObserve_price() {
        return observe_price;
    }

    public String getAnswer_user_portrait() {
        return answer_user_portrait;
    }


}

package com.haiqiu.miaohi.bean;

/**
 * Created by ningl on 16/12/9.
 */
public class OthterQAInfo {


    /**
     * answer_time_text : xxxx
     * question_content : xxxx
     * observe_count : xxxx
     * observe_price_text : xxxx
     * temporary_free : xxxx
     * time_remain : xxxx
     * is_question_owner : xxxx
     */

    private String answer_time_text;
    private String question_content;
    private int observe_count;
    private String observe_price_text;
    private boolean temporary_free;
    private long time_remain;
    private boolean is_question_owner;
    private String answer_user_name;
    private int answer_user_type;
    private String answer_user_portrait;
    private String answer_user_id;
    private String question_id;
    private long observe_price;

    public boolean isTemporary_free() {
        return temporary_free;
    }

    public long getObserve_price() {
        return observe_price;
    }

    public void setObserve_price(long observe_price) {
        this.observe_price = observe_price;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_user_id() {
        return answer_user_id;
    }

    public void setAnswer_user_id(String answer_user_id) {
        this.answer_user_id = answer_user_id;
    }

    public String getAnswer_user_portrait() {
        return answer_user_portrait;
    }

    public void setAnswer_user_portrait(String answer_user_portrait) {
        this.answer_user_portrait = answer_user_portrait;
    }

    public int getAnswer_user_type() {
        return answer_user_type;
    }

    public void setAnswer_user_type(int answer_user_type) {
        this.answer_user_type = answer_user_type;
    }

    public String getAnswer_user_name() {
        return answer_user_name;
    }

    public void setAnswer_user_name(String answer_user_name) {
        this.answer_user_name = answer_user_name;
    }

    public boolean is_question_owner() {
        return is_question_owner;
    }

    public String getAnswer_time_text() {
        return answer_time_text;
    }

    public void setAnswer_time_text(String answer_time_text) {
        this.answer_time_text = answer_time_text;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public int getObserve_count() {
        return observe_count;
    }

    public void setObserve_count(int observe_count) {
        this.observe_count = observe_count;
    }

    public String getObserve_price_text() {
        return observe_price_text;
    }

    public void setObserve_price_text(String observe_price_text) {
        this.observe_price_text = observe_price_text;
    }

    public boolean getTemporary_free() {
        return temporary_free;
    }

    public void setTemporary_free(boolean temporary_free) {
        this.temporary_free = temporary_free;
    }

    public long getTime_remain() {
        return time_remain;
    }

    public void setTime_remain(long time_remain) {
        this.time_remain = time_remain;
    }

    public boolean isIs_question_owner() {
        return is_question_owner;
    }

    public void setIs_question_owner(boolean is_question_owner) {
        this.is_question_owner = is_question_owner;
    }
}

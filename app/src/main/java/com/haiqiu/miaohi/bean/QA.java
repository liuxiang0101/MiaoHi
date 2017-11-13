package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by ningl on 16/12/7.
 */
public class QA {

    List<ObserverQA> observer_list;
    /**
     * is_private : false
     * answer_user_name : While_true_
     * observe_count : 0
     * question_content : jdjdjd
     * answer_time_text : 还未回答
     * time_remain : 30243134
     * question_id : QUST-10d9f229-bc70-11e6-8e6b-000c296cfbbb
     * answer_user_id : USER-f985bdb1-a0ce-11e6-80a3-44a8424640fa
     * answer_user_portrait : http://tva3.sinaimg.cn/default/images/default_avatar_male_50.gif
     * answer_user_type : 20
     */

    private boolean is_private;
    private String answer_user_name;
    private String observe_count;
    private String question_content;
    private String answer_time_text;
    private long time_remain;
    private String question_id;
    private String answer_user_id;
    private String answer_user_portrait;
    private int answer_user_type;
    private int question_state;

    private double progress;
    private int uploadType;
    private int uploadState;
    private boolean is_question_owner;
    private boolean temporary_free;
    private String observe_price_text;
    private long observe_price;

    public long getObserve_price() {
        return observe_price;
    }

    public void setObserve_price(long observe_price) {
        this.observe_price = observe_price;
    }

    public String getObserve_price_text() {
        return observe_price_text;
    }

    public void setObserve_price_text(String observe_price_text) {
        this.observe_price_text = observe_price_text;
    }

    public boolean is_question_owner() {
        return is_question_owner;
    }

    public void setIs_question_owner(boolean is_question_owner) {
        this.is_question_owner = is_question_owner;
    }

    public boolean isTemporary_free() {
        return temporary_free;
    }

    public void setTemporary_free(boolean temporary_free) {
        this.temporary_free = temporary_free;
    }

    private boolean isUploadding;//是否正在上传

    public boolean isUploadding() {
        return isUploadding;
    }

    public void setUploadding(boolean uploadding) {
        isUploadding = uploadding;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public int getUploadType() {
        return uploadType;
    }

    public void setUploadType(int uploadType) {
        this.uploadType = uploadType;
    }

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }

    public boolean is_private() {
        return is_private;
    }

    public int getQuestion_state() {
        return question_state;
    }

    public void setQuestion_state(int question_state) {
        this.question_state = question_state;
    }

    public List<ObserverQA> getObserver_list() {
        return observer_list;
    }

    public void setObserver_list(List<ObserverQA> observer_list) {
        this.observer_list = observer_list;
    }

    public boolean isIs_private() {
        return is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    public String getAnswer_user_name() {
        return answer_user_name;
    }

    public void setAnswer_user_name(String answer_user_name) {
        this.answer_user_name = answer_user_name;
    }

    public String getObserve_count() {
        return observe_count;
    }

    public void setObserve_count(String observe_count) {
        this.observe_count = observe_count;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public String getAnswer_time_text() {
        return answer_time_text;
    }

    public void setAnswer_time_text(String answer_time_text) {
        this.answer_time_text = answer_time_text;
    }

    public long getTime_remain() {
        return time_remain;
    }

    public void setTime_remain(long time_remain) {
        this.time_remain = time_remain;
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
}

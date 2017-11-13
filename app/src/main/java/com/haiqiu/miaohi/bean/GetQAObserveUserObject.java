package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/9/22.
 */
public class GetQAObserveUserObject {
    private String observe_user_id;
    private String observe_portrait_uri;
    private int observe_user_type;
    private int observe_portrait_state;

    public String getObserve_user_id() {
        return observe_user_id;
    }

    public String getObserve_portrait_uri() {
        return observe_portrait_uri;
    }

    public int getObserve_user_type() {
        return observe_user_type;
    }

    public int getObserve_portrait_state() {
        return observe_portrait_state;
    }

    public void setObserve_user_id(String observe_user_id) {
        this.observe_user_id = observe_user_id;
    }

    public void setObserve_portrait_uri(String observe_portrait_uri) {
        this.observe_portrait_uri = observe_portrait_uri;
    }

    public void setObserve_user_type(int observe_user_type) {
        this.observe_user_type = observe_user_type;
    }

    public void setObserve_portrait_state(int observe_portrait_state) {
        this.observe_portrait_state = observe_portrait_state;
    }
}

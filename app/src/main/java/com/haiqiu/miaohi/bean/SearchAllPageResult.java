package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by miaohi on 2016/5/31.
 */
public class SearchAllPageResult {
    //搜索用户字段
    private int portrait_state;
    private int user_type;
    private String user_id;
    private String user_name;
    private String attention_state;
    private String portrait_uri;
    //搜索专题字段
    private int auto_id;
    private int icon_state;
    private String activity_name;
    private String activity_id;
    private String icon_uri;

    //标识是否选中
    private boolean isSelected;
    //标识是否选中
    private boolean isEnable=true;

    //2.0版本后的新搜索，借用原有bean类
    private List<SearchObjectList>object_list;
    private List<SearchUserList>user_list;
    private List<SearchQuestionList>question_list;

    public List<SearchObjectList> getObject_list() {
        return object_list;
    }

    public List<SearchUserList> getUser_list() {
        return user_list;
    }

    public List<SearchQuestionList> getQuestion_list() {
        return question_list;
    }

    public int getAuto_id() {
        return auto_id;
    }

    public int getIcon_state() {
        return icon_state;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getIcon_uri() {
        return icon_uri;
    }

    public int getPortrait_state() {
        return portrait_state;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getAttention_state() {
        return attention_state;
    }

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public void setAttention_state(String attention_state) {
        this.attention_state = attention_state;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}

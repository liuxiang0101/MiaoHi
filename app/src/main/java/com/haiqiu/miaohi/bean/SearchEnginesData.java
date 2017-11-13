package com.haiqiu.miaohi.bean;

/**
 * Created by miaohi on 2016/5/31.
 */
public class SearchEnginesData {
    private long object_list_count;
    private long user_list_count;
    private long question_list_count;
    private SearchAllPageResult page_result;

    public long getObject_list_count() {
        return object_list_count;
    }

    public long getUser_list_count() {
        return user_list_count;
    }

    public long getQuestion_list_count() {
        return question_list_count;
    }

    public SearchAllPageResult getPage_result() {
        return page_result;
    }
}

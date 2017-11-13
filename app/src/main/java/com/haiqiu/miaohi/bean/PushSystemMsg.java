package com.haiqiu.miaohi.bean;

/**
 * Created by miaohi on 2016/6/21.
 */
public class PushSystemMsg {
    private String title;
    private String content;
    private String time;
    private String push_id;
    private int id;

    public String getPush_id() {
        return push_id;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

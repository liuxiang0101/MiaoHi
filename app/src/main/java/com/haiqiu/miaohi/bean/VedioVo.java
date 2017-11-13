package com.haiqiu.miaohi.bean;

public class VedioVo {
    int id;
    int duration;
    String title;
    String displayname;
    String data;
    String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VedioVo(int id, int duration, String title, String displayname,
                   String data, String type) {
        super();
        this.id = id;
        this.duration = duration;
        this.title = title;
        this.displayname = displayname;
        this.data = data;
        this.type = type;
    }

    public VedioVo() {
    }
}

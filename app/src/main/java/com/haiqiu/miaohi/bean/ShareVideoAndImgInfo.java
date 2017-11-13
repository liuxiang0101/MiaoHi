package com.haiqiu.miaohi.bean;

/**
 * Created by ningl on 16/12/24.
 */

public class ShareVideoAndImgInfo {
    private String name;
    private String joinTime;
    private String imgUrl;
    private int type;
    private String qaCode_str;
    private double width;
    private double height;
    private String note;
    private long answerTime;
    private String headerUrl;

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(long answerTime) {
        this.answerTime = answerTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQaCode_str() {
        return qaCode_str;
    }

    public void setQaCode_str(String qaCode_str) {
        this.qaCode_str = qaCode_str;
    }
}

package com.haiqiu.miaohi.activity;

/**
 * Created by ningl on 16/12/1.
 */
public class CommonPersonInfo {
    private String name;
    private String describe;
    private String time;
    private String name_nodescribe;
    private int userType;
    private String headUri;
    private String userId;
    private boolean isShownQA;
    private boolean isShownGender;
    private int gender;
    private int contentType;//1-视频  2-图片  3-映答

    public int getContentType() {
        return contentType;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isShownQA() {
        return isShownQA;
    }

    public void setShownQA(boolean shownQA) {
        isShownQA = shownQA;
    }

    public boolean isShownGender() {
        return isShownGender;
    }

    public void setShownGender(boolean shownGender) {
        isShownGender = shownGender;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName_nodescribe() {
        return name_nodescribe;
    }

    public void setName_nodescribe(String name_nodescribe) {
        this.name_nodescribe = name_nodescribe;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadUri() {
        return headUri;
    }

    public void setHeadUri(String headUri) {
        this.headUri = headUri;
    }

    public boolean isVip(){
        return getUserType()>10;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}

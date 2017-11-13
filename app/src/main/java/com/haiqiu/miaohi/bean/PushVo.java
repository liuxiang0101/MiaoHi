package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/8/1.
 */
public class PushVo {
    private String res;
    private String content_id;
    private String msg_id;
    private String mac;
    private String command;
    private String objectId;
    private String objectType;

    private String systemContent;
    //系统消息-专题
    private String activity_name;
    private String objectNote;
    private String activity_picture;
    private String activity_uri;

    public String getActivity_name() {
        return activity_name;
    }

    public String getActivity_picture() {
        return activity_picture;
    }

    public String getActivity_uri() {
        return activity_uri;
    }

    public String getObjectNote() {
        return objectNote;
    }

    public String getSystemContent() {
        return systemContent;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getRes() {
        return res;
    }

    public String getContent_id() {
        return content_id;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public String getMac() {
        return mac;
    }

    public String getCommand() {
        return command;
    }
}

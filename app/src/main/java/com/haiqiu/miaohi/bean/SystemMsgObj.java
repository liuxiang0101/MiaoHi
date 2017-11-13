package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/12/1.
 */
public class SystemMsgObj {
    private String objectType;
    private String objectId;
    //系统消息-专题
    private String activity_name;
    private String objectNote;
    private String activity_picture;
    private String activity_uri;

    public SystemMsgObj(String objectType, String objectId, String activity_name, String objectNote, String activity_picture, String activity_uri) {
        this.objectType = objectType;
        this.objectId = objectId;
        this.activity_name = activity_name;
        this.objectNote = objectNote;
        this.activity_picture = activity_picture;
        this.activity_uri = activity_uri;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public String getObjectNote() {
        return objectNote;
    }

    public String getActivity_picture() {
        return activity_picture;
    }

    public String getActivity_uri() {
        return activity_uri;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getObjectId() {
        return objectId;
    }
}

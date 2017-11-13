package com.haiqiu.miaohi.bean;

/**
 * 全部表情
 * Created by miaohi on 2016/4/27.
 */
public class FaceResultArrayObj {
    private String icon_uri;
    private String icon_role;
    private String icon_state;
    private String face_id;
    private String face_name;
    private String face_type;


    @Override
    public String toString() {
        return "FaceResultArrayObj{" +
                "icon_uri='" + icon_uri + '\'' +
                ", icon_role='" + icon_role + '\'' +
                ", icon_state='" + icon_state + '\'' +
                ", face_id='" + face_id + '\'' +
                ", face_name='" + face_name + '\'' +
                ", face_type='" + face_type + '\'' +
                '}';
    }

    public String getIcon_state() {
        return icon_state;
    }

    public String getFace_type() {
        return face_type;
    }

    public String getFace_id() {
        return face_id;
    }

    public String getIcon_uri() {
        return icon_uri;
    }

    public String getIcon_role() {
        return icon_role;
    }

    public String getFace_name() {
        return face_name;
    }
}

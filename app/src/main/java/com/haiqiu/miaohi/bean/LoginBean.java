package com.haiqiu.miaohi.bean;

import java.io.Serializable;

/**
 * Created by ningl on 2016/5/20.
 */
public class LoginBean implements Serializable {


    /**
     * user_type : 20
     * user_name : vip1
     * getui_alias : 93b0373be1bd2f0a1b7acac7bd0711ab
     * qiniu_upload_image_token : koAR5ikhBhYKGkGz-NpP7wsiSD6DtoegPzxypsID:YBE364RJZpjoroZ7Pq0KPmhz6zQ=:eyJzY29wZSI6ImhhaXFpdS1taWFvaGktbWh2LWltYWdlIiwiZGVhZGxpbmUiOjE0NzkyMTYxMjZ9
     * miaohi_token : TOKE-vipcd9a3-062a-11e6-9003-44a8424640tc
     * qiniu_upload_video_token : koAR5ikhBhYKGkGz-NpP7wsiSD6DtoegPzxypsID:5Y3t3MJZRYFF_-fZYFgxgXjq_sw=:eyJzY29wZSI6ImhhaXFpdS1taWFvaGktbWh2LXZpZGVvIiwiZGVhZGxpbmUiOjE0NzkyMTY0MTd9
     * qiniu_web_icon_base : o7fapsitv.bkt.clouddn.com
     * qiniu_web_image_base : o7fawhrna.bkt.clouddn.com
     * qiniu_web_video_base : o7fa02tzz.bkt.clouddn.com
     * qiniu_upload_icon_token : koAR5ikhBhYKGkGz-NpP7wsiSD6DtoegPzxypsID:5CGV8QnXqiLfdM7FWkqhMt2i0fA=:eyJzY29wZSI6ImhhaXFpdS1taWFvaGktbWh2LWljb24iLCJkZWFkbGluZSI6MTQ3OTIxNTU5NH0=
     */

    private String user_type;
    private String user_id;
    private String user_name;
    private String getui_alias;
    private String qiniu_upload_image_token;
    private String miaohi_token;
    private String qiniu_upload_video_token;
    private String qiniu_web_icon_base;
    private String qiniu_web_image_base;
    private String qiniu_web_video_base;
    private String qiniu_upload_icon_token;
    private String rong_token;
    private String portrait_uri;
    private int answer_auth;
    private int label_selected;

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public boolean isAnswer_auth() {
        return answer_auth==1;
    }

    public boolean isLabel_selected() {
        return label_selected==1;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getGetui_alias() {
        return getui_alias;
    }

    public void setGetui_alias(String getui_alias) {
        this.getui_alias = getui_alias;
    }

    public String getQiniu_upload_image_token() {
        return qiniu_upload_image_token;
    }

    public void setQiniu_upload_image_token(String qiniu_upload_image_token) {
        this.qiniu_upload_image_token = qiniu_upload_image_token;
    }

    public String getMiaohi_token() {
        return miaohi_token;
    }


    public String getQiniu_upload_video_token() {
        return qiniu_upload_video_token;
    }



    public String getQiniu_web_icon_base() {
        return qiniu_web_icon_base;
    }



    public String getQiniu_web_image_base() {
        return qiniu_web_image_base;
    }



    public String getQiniu_web_video_base() {
        return qiniu_web_video_base;
    }


    public String getQiniu_upload_icon_token() {
        return qiniu_upload_icon_token;
    }


    public String getRong_token() {
        return rong_token;
    }

    public void setRong_token(String rong_token) {
        this.rong_token = rong_token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * 送出礼物实体类
 * Created by ningl on 2016/6/30.
 */
public class SendPraiseBean {


    /**
     * video_uri : http://video.dev.miaohi.com/video_e698b866f5fc54dced4ac52bfa8c8530_2016_06_29_17_35_06_413
     * portrait_state : 10
     * send_time : 2016-06-29 17:49:42.0
     * user_type : 10
     * user_id : USER-f46ba0d8-36aa-11e6-83a3-44a8424640fa
     * user_name : 二二二
     * video_state : 10
     * video_cover_state : 10
     * hls_state : 8
     * video_id : VIDE-cc1554da-3ddc-11e6-ba13-44a8424640fa
     * video_cover_uri : http://image.dev.miaohi.com/img_e698b866f5fc54dced4ac52bfa8c8530_2016_06_29_17_35_06_406
     * portrait_uri : http://icon.dev.miaohi.com/myIcon_20160628121739
     */

    private String video_uri;
    private String portrait_state;
    private String send_time;
    private String user_type;
    private String user_id;
    private String user_name;
    private String video_state;
    private String video_cover_state;
    private String hls_state;
    private String video_id;
    private String video_cover_uri;
    private String portrait_uri;
    private String send_time_text;

    public String getSend_time_text() {
        return send_time_text;
    }

    public void setSend_time_text(String send_time_text) {
        this.send_time_text = send_time_text;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public String getPortrait_state() {
        return portrait_state;
    }

    public void setPortrait_state(String portrait_state) {
        this.portrait_state = portrait_state;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getVideo_state() {
        return video_state;
    }

    public void setVideo_state(String video_state) {
        this.video_state = video_state;
    }

    public String getVideo_cover_state() {
        return video_cover_state;
    }

    public void setVideo_cover_state(String video_cover_state) {
        this.video_cover_state = video_cover_state;
    }

    public String getHls_state() {
        return hls_state;
    }

    public void setHls_state(String hls_state) {
        this.hls_state = hls_state;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_cover_uri() {
        if (null != video_cover_uri && video_cover_uri.contains("?")) {
            return video_cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return video_cover_uri+ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public void setVideo_cover_uri(String video_cover_uri) {
        this.video_cover_uri = video_cover_uri;
    }

    public String getPortrait_uri() {
        return portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }
}

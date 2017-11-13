package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * Created by miaohi on 2016/6/20.
 */
public class PushMsgNewFriendData extends PushMsgData {
    // 新的好友&&@我的&&受邀专题
    private String send_attention_time;
    private String send_atme_time;
    private String send_invitation_time;
    private String sender_portrait_uri;
    private String sender_name;
    private String sender_id;
    private String activity_id;
    private String comment_id;
    private String video_cover_uri;
    private String video_id;
    private String activity_name;
    private String comment_text;
    private String notify_identify;
    private String photo_thumb_uri;
    private String photo_uri;
    private String photo_id;
    private int sender_portrait_state;
    private int sender_type;

    public String getPhoto_thumb_uri() {
        return photo_thumb_uri;
    }

    public String getPhoto_uri() {
        return photo_uri;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public String getNotify_identify() {
        return notify_identify;
    }

    public String getComment_text() {
        return comment_text;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public String getVideo_cover_uri() {
        if (null != video_cover_uri && video_cover_uri.contains("?")) {
            return video_cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return video_cover_uri+ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getSend_atme_time() {
        return send_atme_time;
    }

    public String getSend_invitation_time() {
        return send_invitation_time;
    }

    public String getSend_attention_time() {
        return send_attention_time;
    }

    public String getSender_portrait_uri() {
        return sender_portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public int getSender_portrait_state() {
        return sender_portrait_state;
    }

    public int getSender_type() {
        return sender_type;
    }

    public void setSend_attention_time(String send_attention_time) {
        this.send_attention_time = send_attention_time;
    }

    public void setSend_atme_time(String send_atme_time) {
        this.send_atme_time = send_atme_time;
    }

    public void setSend_invitation_time(String send_invitation_time) {
        this.send_invitation_time = send_invitation_time;
    }

    public void setSender_portrait_uri(String sender_portrait_uri) {
        this.sender_portrait_uri = sender_portrait_uri;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public void setSender_portrait_state(int sender_portrait_state) {
        this.sender_portrait_state = sender_portrait_state;
    }

    public void setSender_type(int sender_type) {
        this.sender_type = sender_type;
    }

}

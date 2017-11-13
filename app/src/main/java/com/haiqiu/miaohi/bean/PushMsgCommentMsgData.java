package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * Created by miaohi on 2016/6/20.
 * 评论消息 -- 某人评论了你的视频
 */
public class PushMsgCommentMsgData extends PushMsgData{
    private String root_id;
    private String root_uri;
    private String root_cover_uri;
    private String root_hls_uri;
    private String root_user_id;
    private String root_user_name;
    private String root_user_portrait_uri;
    private String commented_id;
    private String commented_user_id;
    private String commented_user_name;
    private String commented_user_portrait_uri;
    private String comment_id;
    private String comment_time;
    private String comment_text;
    private String comment_user_id;
    private String comment_user_name;
    private String comment_user_portrait_uri;
    private int root_type;
    private int root_uri_state;
    private int root_cover_state;
    private int root_hls_state;
    private int root_user_type;
    private int root_user_portrait_state;
    private int commented_type;
    private int commented_user_type;
    private int commented_user_portrait_state;
    private int comment_state;
    private int comment_user_type;
    private int comment_user_portrait_state;

    public String getRoot_id() {
        return root_id;
    }

    public String getRoot_uri() {
        return root_uri;
    }

    public String getRoot_cover_uri() {
        if (null != root_cover_uri && root_cover_uri.contains("?")) {
            return root_cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return root_cover_uri+ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public String getRoot_hls_uri() {
        return root_hls_uri;
    }

    public String getRoot_user_id() {
        return root_user_id;
    }

    public String getRoot_user_name() {
        return root_user_name;
    }

    public String getRoot_user_portrait_uri() {
        return root_user_portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public String getCommented_id() {
        return commented_id;
    }

    public String getCommented_user_id() {
        return commented_user_id;
    }

    public String getCommented_user_name() {
        return commented_user_name;
    }

    public String getCommented_user_portrait_uri() {
        return commented_user_portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public String getComment_id() {
        return comment_id;
    }

    public String getComment_time() {
        return comment_time;
    }

    public String getComment_text() {
        return comment_text;
    }

    public String getComment_user_id() {
        return comment_user_id;
    }

    public String getComment_user_name() {
        return comment_user_name;
    }

    public String getComment_user_portrait_uri() {
        return comment_user_portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public int getRoot_type() {
        return root_type;
    }

    public int getRoot_uri_state() {
        return root_uri_state;
    }

    public int getRoot_cover_state() {
        return root_cover_state;
    }

    public int getRoot_hls_state() {
        return root_hls_state;
    }

    public int getRoot_user_type() {
        return root_user_type;
    }

    public int getRoot_user_portrait_state() {
        return root_user_portrait_state;
    }

    public int getCommented_type() {
        return commented_type;
    }

    public int getCommented_user_type() {
        return commented_user_type;
    }

    public int getCommented_user_portrait_state() {
        return commented_user_portrait_state;
    }

    public int getComment_state() {
        return comment_state;
    }

    public int getComment_user_type() {
        return comment_user_type;
    }

    public int getComment_user_portrait_state() {
        return comment_user_portrait_state;
    }
}

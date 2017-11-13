package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * Created by miaohi on 2016/6/2.
 */
public class HomeFoundPageResult {
    private int portrait_state;
    private int duration_second;
    private int auto_id;
    private int cover_state;
    private int user_type;
    private int face_count;
    private int video_state;
    private int hls_state;
    private String cover_uri;
    private String user_name;
    private String video_uri;
    private String upload_time;
    private String user_id;
    private String portrait_uri;
    private String video_id;

    public int getPortrait_state() {
        return portrait_state;
    }

    public int getDuration_second() {
        return duration_second;
    }

    public int getAuto_id() {
        return auto_id;
    }

    public int getCover_state() {
        return cover_state;
    }

    public int getUser_type() {
        return user_type;
    }

    public int getFace_count() {
        return face_count;
    }

    public int getVideo_state() {
        return video_state;
    }

    public int getHls_state() {
        return hls_state;
    }

    public String getCover_uri() {
        if (null != cover_uri && cover_uri.contains("?")) {
            return cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return cover_uri+ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPortrait_uri() {
        return portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public String getVideo_id() {
        return video_id;
    }
}

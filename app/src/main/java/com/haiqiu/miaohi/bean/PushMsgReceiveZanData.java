package com.haiqiu.miaohi.bean;

/**
 * Created by miaohi on 2016/6/20.
 * 收到的赞
 */
public class PushMsgReceiveZanData extends PushMsgData{
    private String sender_id;
    private String sender_name;
    private String sender_portrait_uri;
    private String face_id;
    private String face_name;
    private String face_icon_uri;
    private String face_icon_role;
    private String video_id;
    private String video_uri;
    private String video_cover_uri;
    private String video_hls_uri;
//    private String praise_time;
    private int sender_type;
    private int sender_portrait_state;
    private int face_icon_state;
    private int video_state;
    private int video_cover_state;
    private int string;
    private int video_hls_state;

    private String photo_id;
    private String photo_thumb_uri;
    private String photo_uri;

    public String getSender_id() {
        return sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getSender_portrait_uri() {
        return sender_portrait_uri;
    }

    public String getFace_id() {
        return face_id;
    }

    public String getFace_name() {
        return face_name;
    }

    public String getFace_icon_uri() {
        return face_icon_uri;
    }

    public String getFace_icon_role() {
        return face_icon_role;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public String getVideo_cover_uri() {
        return video_cover_uri;
    }

    public String getVideo_hls_uri() {
        return video_hls_uri;
    }

//    public String getPraise_time() {
//        return praise_time;
//    }

    public int getSender_type() {
        return sender_type;
    }

    public int getSender_portrait_state() {
        return sender_portrait_state;
    }

    public int getFace_icon_state() {
        return face_icon_state;
    }

    public int getVideo_state() {
        return video_state;
    }

    public int getVideo_cover_state() {
        return video_cover_state;
    }

    public int getString() {
        return string;
    }

    public int getVideo_hls_state() {
        return video_hls_state;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public String getPhoto_thumb_uri() {
        return photo_thumb_uri;
    }

    public String getPhoto_uri() {
        return photo_uri;
    }
}

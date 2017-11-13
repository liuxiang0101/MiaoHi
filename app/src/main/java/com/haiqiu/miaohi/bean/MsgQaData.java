package com.haiqiu.miaohi.bean;

/**
 * Created by LiuXiang on 2016/12/21.
 */
public class MsgQaData {
    private String question_text;
    private String question_time;
    private int sender_state;
    private int sender_portrait_state;
    private int sender_type;
    private String sender_portrait_uri;
    private int question_private;
    private String sender_name;
    private String question_id;
    private String sender_id;
    private String video_cover_uri;

    public String getVideo_cover_uri() {
        return video_cover_uri;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public String getQuestion_time() {
        return question_time;
    }

    public int getSender_state() {
        return sender_state;
    }

    public int getSender_portrait_state() {
        return sender_portrait_state;
    }

    public int getSender_type() {
        return sender_type;
    }

    public String getSender_portrait_uri() {
        return sender_portrait_uri;
    }

    public int getQuestion_private() {
        return question_private;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getSender_id() {
        return sender_id;
    }
}

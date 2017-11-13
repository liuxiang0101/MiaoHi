package com.haiqiu.miaohi.bean;

/**
 * Created by miaohi on 2016/6/21.
 */
public class PushMsgData {
    private String push_id;
    private int msg_state;
    private long send_time;
    private String msge_content;
    private long get_time;

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public void setMsg_state(int msg_state) {
        this.msg_state = msg_state;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public void setMsge_content(String msge_content) {
        this.msge_content = msge_content;
    }

    public void setGet_time(long get_time) {
        this.get_time = get_time;
    }

    public String getPush_id() {
        return push_id;
    }

    public int getMsg_state() {
        return msg_state;
    }

    public long getSend_time() {
        return send_time;
    }

    public String getMsge_content() {
        return msge_content;
    }

    public long getGet_time() {
        return get_time;
    }
}

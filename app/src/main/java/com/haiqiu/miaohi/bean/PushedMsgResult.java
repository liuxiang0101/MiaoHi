package com.haiqiu.miaohi.bean;

/**
 * Created by miaohi on 2016/6/20.
 */
public class PushedMsgResult {
    private String push_id;
    private String msg_id;
    private String send_time;
    private String send_time_text;
    private String msg_content;
    private String get_time;
    private int msg_state;

    public String getSend_time_text() {
        return send_time_text;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public String getPush_id() {
        return push_id;
    }

    public String getSend_time() {
        return send_time;
    }


    public String getGet_time() {
        return get_time;
    }

    public int getMsg_state() {
        return msg_state;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setGet_time(String get_time) {
        this.get_time = get_time;
    }

    public void setMsg_state(int msg_state) {
        this.msg_state = msg_state;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    @Override
    public String toString() {
        return "PushedMsgResult{" +
                "push_id='" + push_id + '\'' +
                ", send_time='" + send_time + '\'' +
                ", msg_content='" + msg_content + '\'' +
                ", get_time='" + get_time + '\'' +
                ", msg_state=" + msg_state +
                '}';
    }
}

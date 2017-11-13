package com.haiqiu.miaohi.bean;

/**
 * Created by miaohi on 2016/6/20.
 */
public class PushedMsgBean {
    private String push_id;
    private String msg_state;

    public String getMsg_state() {
        return msg_state;
    }

    public void setMsg_state(String msg_state) {
        this.msg_state = msg_state;
    }

    public String getPush_id() {
        return push_id;
    }


    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    @Override
    public String toString() {
        return "{" + '"' + "push_id" + '"' + ":" + '"' + push_id + '"' + ", " + '"' + "msg_state=" + '"' + "" + msg_state + '"' + '}';
    }
}

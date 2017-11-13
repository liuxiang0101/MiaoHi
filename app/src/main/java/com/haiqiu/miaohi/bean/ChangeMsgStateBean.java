package com.haiqiu.miaohi.bean;

/**
 * Created by miaohi on 2016/7/4.
 */
public class ChangeMsgStateBean {
    private String msg_id;
    private String msg_state;

    public String getMsg_id() {
        return msg_id;
    }

    public String getMsg_state() {
        return msg_state;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public void setMsg_state(String msg_state) {
        this.msg_state = msg_state;
    }

    public ChangeMsgStateBean(String msg_id, String msg_state) {
        this.msg_id = msg_id;
        this.msg_state = msg_state;
    }
}

package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by miaohi on 2016/6/20.
 */
public class PushedMsgData {
    private List<PushedMsgResult> msg_result;

    public List<PushedMsgResult> getMsg_result() {
        return msg_result;
    }

    public void setMsg_result(List<PushedMsgResult> msg_result) {
        this.msg_result = msg_result;
    }
}

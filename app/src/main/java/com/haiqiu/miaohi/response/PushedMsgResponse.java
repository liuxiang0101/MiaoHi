package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.PushedMsgData;

/**
 * Created by miaohi on 2016/6/20.
 */
public class PushedMsgResponse extends BaseResponse {
    private PushedMsgData data;

    public PushedMsgData getData() {
        return data;
    }

    public void setData(PushedMsgData data) {
        this.data = data;
    }
}

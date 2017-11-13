package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * Created by ningl on 2016/6/30.
 */
public class SendPraiseResponse extends BaseResponse {

    private SendPraiseResult data;

    public SendPraiseResult getData() {
        return data;
    }

    public void setData(SendPraiseResult data) {
        this.data = data;
    }
}

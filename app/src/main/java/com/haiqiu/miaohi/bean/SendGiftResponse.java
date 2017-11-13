package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * 获取大咖可发送的礼物响应实体类
 * Created by ningl on 2016/6/24.
 */
public class SendGiftResponse extends BaseResponse {
    private SendGiftResult data;

    public SendGiftResult getData() {
        return data;
    }

    public void setData(SendGiftResult data) {
        this.data = data;
    }
}

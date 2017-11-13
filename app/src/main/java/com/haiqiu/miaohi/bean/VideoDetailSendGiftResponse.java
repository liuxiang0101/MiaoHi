package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * 发送礼物响应实体类
 * Created by ningl on 2016/6/24.
 */
public class VideoDetailSendGiftResponse extends BaseResponse {

    private VideoDetailSendGiftBean data;

    public VideoDetailSendGiftBean getData() {
        return data;
    }

    public void setData(VideoDetailSendGiftBean data) {
        this.data = data;
    }
}

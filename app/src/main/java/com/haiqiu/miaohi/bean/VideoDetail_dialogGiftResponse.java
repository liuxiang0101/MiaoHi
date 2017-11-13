package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * 视频详情礼物对话框响应实体类
 * Created by ningl on 2016/6/23.
 */
public class VideoDetail_dialogGiftResponse extends BaseResponse {

    private VideoDetail_dialogGiftResult data;

    public VideoDetail_dialogGiftResult getData() {
        return data;
    }

    public void setData(VideoDetail_dialogGiftResult data) {
        this.data = data;
    }
}

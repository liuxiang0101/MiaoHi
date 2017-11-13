package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * 视频详情响应实体类
 * Created by ningl on 2016/6/22.
 */
public class VideoDetailResponse extends BaseResponse {

    private VideoDetailBean data;

    public VideoDetailBean getData() {
        return data;
    }

    public void setData(VideoDetailBean data) {
        this.data = data;
    }
}

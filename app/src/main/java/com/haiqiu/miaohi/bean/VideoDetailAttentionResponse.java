package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * 视频详情关注响应实体类
 * Created by ningl on 2016/6/22.
 */
public class VideoDetailAttentionResponse extends BaseResponse {

    private VideoDetailAttentionBean data;

    public VideoDetailAttentionBean getData() {
        return data;
    }

    public void setData(VideoDetailAttentionBean data) {
        this.data = data;
    }
}

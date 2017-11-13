package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * 视频详情点赞返回响应实体类
 * Created by ningl on 2016/6/23.
 */
public class VideoDetailPraiseResponse extends BaseResponse{
    private VideoDetailPraiseBean data;

    public VideoDetailPraiseBean getData() {
        return data;
    }

    public void setData(VideoDetailPraiseBean data) {
        this.data = data;
    }
}

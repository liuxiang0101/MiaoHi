package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * 视频详情评论响应实体类
 * Created by ningl on 2016/6/22.
 */
public class VideoDetailUserCommentResponse extends BaseResponse {

    private VideoDetailUserCommentResult data;

    public VideoDetailUserCommentResult getData() {
        return data;
    }

    public void setData(VideoDetailUserCommentResult data) {
        this.data = data;
    }
}

package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * 发送评论返回响应实体类
 * Created by ningl on 2016/6/27.
 */
public class SendCommentResponse extends BaseResponse {

    private VideoDetailUserCommentBean data;

    public VideoDetailUserCommentBean getData() {
        return data;
    }

    public void setData(VideoDetailUserCommentBean data) {
        this.data = data;
    }
}

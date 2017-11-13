package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.VideoDetailReceiveGiftPageResult;

/**
 * 视频详情获得的礼物
 * Created by ningl on 16/9/5.
 */
public class VideoDetailReceiveGiftResponse extends BaseResponse {

    private VideoDetailReceiveGiftPageResult data;

    public VideoDetailReceiveGiftPageResult getData() {
        return data;
    }

    public void setData(VideoDetailReceiveGiftPageResult data) {
        this.data = data;
    }
}

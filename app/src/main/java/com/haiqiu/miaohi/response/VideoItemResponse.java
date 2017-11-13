package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.VideoItemData;

/**
 * Created by zhandalin on 2016-05-28 15:36.
 * 说明:
 */
public class VideoItemResponse extends BaseResponse{
    private VideoItemData data;

    public VideoItemData getData() {
        return data;
    }

    public void setData(VideoItemData data) {
        this.data = data;
    }
}

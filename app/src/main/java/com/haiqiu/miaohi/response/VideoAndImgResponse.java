package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.ImgDetail;

/**
 * Created by ningl on 16/12/17.
 */
public class VideoAndImgResponse extends BaseResponse {

    private ImgDetail data;

    public ImgDetail getData() {
        return data;
    }

    public void setData(ImgDetail data) {
        this.data = data;
    }
}

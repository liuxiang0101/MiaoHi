package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.UploadPicture;

/**
 * Created by ningl on 16/12/17.
 */
public class UploadPictureResponse extends BaseResponse {

    private UploadPicture data;

    public UploadPicture getData() {
        return data;
    }

    public void setData(UploadPicture data) {
        this.data = data;
    }
}

package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.DraftsUploadBean;

/**
 * Created by ningl on 2016/7/27.
 */
public class DraftsUploadResponse extends BaseResponse{

    private DraftsUploadBean data;

    public DraftsUploadBean getData() {
        return data;
    }

    public void setData(DraftsUploadBean data) {
        this.data = data;
    }
}

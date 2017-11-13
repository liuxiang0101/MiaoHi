package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.OtherQAData;

/**
 * Created by ningl on 16/12/9.
 */
public class OtherQAInfoResponse extends BaseResponse {

    private OtherQAData data;

    public OtherQAData getData() {
        return data;
    }

    public void setData(OtherQAData data) {
        this.data = data;
    }
}

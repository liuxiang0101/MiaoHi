package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.HaveReadData;

/**
 * Created by ningl on 2016/7/20.
 */
public class HaveResponse extends BaseResponse {

    private HaveReadData data;

    public HaveReadData getData() {
        return data;
}

    public void setData(HaveReadData data) {
        this.data = data;
    }
}

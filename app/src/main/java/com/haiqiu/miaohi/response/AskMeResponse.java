package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.AskMeData;

/**
 * Created by ningl on 2016/7/20.
 */
public class AskMeResponse extends BaseResponse {

    private AskMeData data;

    public AskMeData getData() {
        return data;
    }

    public void setData(AskMeData data) {
        this.data = data;
    }
}

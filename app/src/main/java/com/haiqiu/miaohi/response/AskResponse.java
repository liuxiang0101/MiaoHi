package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.AskData;

/**
 * Created by ningl on 2016/7/20.
 */
public class AskResponse extends BaseResponse {

    private AskData data;

    public AskData getData() {
        return data;
    }

    public void setData(AskData data) {
        this.data = data;
    }
}

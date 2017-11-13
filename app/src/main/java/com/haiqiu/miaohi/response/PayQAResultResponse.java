package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.PayQAResultData;

/**
 * Created by ningl on 17/1/12.
 */

public class PayQAResultResponse extends BaseResponse {

    private PayQAResultData data;

    public PayQAResultData getData() {
        return data;
    }

    public void setData(PayQAResultData data) {
        this.data = data;
    }
}

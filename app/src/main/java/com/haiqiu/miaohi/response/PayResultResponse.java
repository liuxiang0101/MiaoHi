package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.PayResultData;

/**
 * Created by zhandalin on 2016-08-04 11:05.
 * 说明:
 */
public class PayResultResponse extends BaseResponse {
    private PayResultData data;

    public PayResultData getData() {
        return data;
    }
}

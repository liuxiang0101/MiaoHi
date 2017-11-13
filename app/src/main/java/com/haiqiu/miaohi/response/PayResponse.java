package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.PayInfoData;

/**
 * Created by zhandalin on 2016-07-25 15:28.
 * 说明:
 */
public class PayResponse extends BaseResponse{
    public PayInfoData data;

    public PayInfoData getData() {
        return data;
    }
}

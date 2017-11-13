package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.UpdateData;

/**
 * Created by zhandalin on 2016-07-04 21:25.
 * 说明:检测更新
 */
public class UpdateResponse extends BaseResponse {
    private UpdateData data;

    public UpdateData getData() {
        return data;
    }
}

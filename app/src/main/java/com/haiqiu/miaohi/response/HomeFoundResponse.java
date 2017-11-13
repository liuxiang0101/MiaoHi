package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.HomeFoundData;

/**
 * Created by zhandalin on 2016-05-23 20:46.
 * 说明:
 */
public class HomeFoundResponse extends BaseResponse {
    private HomeFoundData data;

    public HomeFoundData getData() {
        return data;
    }

    public void setData(HomeFoundData data) {
        this.data = data;
    }
}

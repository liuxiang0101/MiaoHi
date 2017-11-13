package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.HomeActivityData;

/**
 * 首页-专题
 * Created by miaohi on 2016/6/2.
 */
public class HomeActivityResponse extends BaseResponse{
    private HomeActivityData data;

    public HomeActivityData getData() {
        return data;
    }
}

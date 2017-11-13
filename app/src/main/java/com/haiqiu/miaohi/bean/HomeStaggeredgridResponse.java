package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * Created by ninel on 2016/6/17.
 */
public class HomeStaggeredgridResponse extends BaseResponse{
    private HomeStaggeredgridData data;

    public HomeStaggeredgridData getData() {
        return data;
    }

    public void setData(HomeStaggeredgridData data) {
        this.data = data;
    }
}

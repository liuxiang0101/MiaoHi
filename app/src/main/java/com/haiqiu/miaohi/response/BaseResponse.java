package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.ServerResponseBaseInfo;

/**
 * Created by zhandalin on 2016-05-20 15:44.
 * 说明:服务端返回数据的基类,所有response类必须继承这个类
 */
public class BaseResponse {
    private ServerResponseBaseInfo base;

    public ServerResponseBaseInfo getBase() {
        return base;
    }

    public void setBase(ServerResponseBaseInfo base) {
        this.base = base;
    }
}

package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.CheckBindWeChatBean;

/**
 * Created by ningl on 2016/7/27.
 */
public class CheckBindWeChatResponse extends BaseResponse {

    private CheckBindWeChatBean data;

    public CheckBindWeChatBean getData() {
        return data;
    }

    public void setData(CheckBindWeChatBean data) {
        this.data = data;
    }
}

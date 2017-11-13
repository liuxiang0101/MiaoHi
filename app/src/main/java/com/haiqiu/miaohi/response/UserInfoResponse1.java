package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.UserInfo;

/**
 * Created by ningl on 16/12/5.
 */
public class UserInfoResponse1 extends BaseResponse {

    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}

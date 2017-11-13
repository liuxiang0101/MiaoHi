package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.UserWorkItem;

/**
 * Created by ningl on 16/12/6.
 */
public class UserWorksResponse extends BaseResponse {

    private UserWorkItem data;

    public UserWorkItem getData() {
        return data;
    }

    public void setData(UserWorkItem data) {
        this.data = data;
    }
}

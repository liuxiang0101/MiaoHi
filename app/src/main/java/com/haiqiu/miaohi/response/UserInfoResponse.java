package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.UserInfoBean;
import com.haiqiu.miaohi.response.BaseResponse;

import java.io.Serializable;

/**
 * Created by ninglei on 2016/5/25.
 */
public class UserInfoResponse extends BaseResponse implements Serializable {

    private UserInfoBean data;

    public UserInfoBean getData() {
        return data;
    }

    public void setData(UserInfoBean data) {
        this.data = data;
    }
}

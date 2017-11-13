package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.LoginBean;
import com.haiqiu.miaohi.response.BaseResponse;

import java.io.Serializable;

/**
 * 登陆
 * Created by ningl on 2016/5/20.
 */
public class LoginResponse extends BaseResponse implements Serializable{

    private LoginBean data;

    public LoginBean getData() {
        return data;
    }

    public void setData(LoginBean data) {
        this.data = data;
    }
}

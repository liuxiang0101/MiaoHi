package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.SaveUserInfo;
import com.haiqiu.miaohi.response.BaseResponse;

import java.io.Serializable;

/**
 * Created by ningl on 2016/5/26.
 */
public class SaveUserInfoResponse extends BaseResponse implements Serializable{

    private SaveUserInfo data;

    public SaveUserInfo getData() {
        return data;
    }

    public void setData(SaveUserInfo data) {
        this.data = data;
    }
}

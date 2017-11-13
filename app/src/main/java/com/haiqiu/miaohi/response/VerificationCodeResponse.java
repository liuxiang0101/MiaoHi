package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.VerifactionCodeBean;
import com.haiqiu.miaohi.response.BaseResponse;

import java.io.Serializable;

/**
 * Created by ningl on 2016/5/25.
 */
public class VerificationCodeResponse extends BaseResponse implements Serializable{

    private VerifactionCodeBean data;

    public VerifactionCodeBean getData() {
        return data;
    }

    public void setData(VerifactionCodeBean data) {
        this.data = data;
    }
}

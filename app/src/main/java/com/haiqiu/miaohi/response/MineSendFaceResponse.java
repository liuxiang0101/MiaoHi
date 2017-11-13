package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.MineSendFaceBeans;

import java.io.Serializable;

/**
 * Created by ningl on 2016/5/27.
 */
public class MineSendFaceResponse extends BaseResponse implements Serializable {

    private MineSendFaceBeans data;

    public MineSendFaceBeans getData() {
        return data;
    }

    public void setData(MineSendFaceBeans data) {
        this.data = data;
    }
}

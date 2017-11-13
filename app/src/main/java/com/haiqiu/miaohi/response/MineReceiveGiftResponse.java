package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.MineReceiveGiftsBean;
import com.haiqiu.miaohi.response.BaseResponse;

import java.io.Serializable;

/**
 * Created by ningl on 2016/5/27.
 */
public class MineReceiveGiftResponse extends BaseResponse implements Serializable{

    private MineReceiveGiftsBean data;


    public MineReceiveGiftsBean getData() {
        return data;
    }

    public void setData(MineReceiveGiftsBean data) {
        this.data = data;
    }
}

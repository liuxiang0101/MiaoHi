package com.haiqiu.miaohi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ningl on 2016/5/27.
 */
public class MineReceiveGiftsBean implements Serializable {

    private List<MineReceiveGiftBean> page_result;

    public List<MineReceiveGiftBean> getPage_result() {
        return page_result;
    }

    public void setPage_result(List<MineReceiveGiftBean> page_result) {
        this.page_result = page_result;
    }
}

package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * 大咖可发送的礼物集合实体类
 * Created by ningl on 2016/6/24.
 */
public class SendGiftResult {
    private List<SendGiftBean> gift_result;

    public List<SendGiftBean> getGift_result() {
        return gift_result;
    }

    public void setGift_result(List<SendGiftBean> gift_result) {
        this.gift_result = gift_result;
    }
}

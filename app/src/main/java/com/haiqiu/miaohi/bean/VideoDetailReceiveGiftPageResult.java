package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * 视频详情 获得的礼物
 * Created by ningl on 16/9/5.
 */
public class VideoDetailReceiveGiftPageResult {

    private List<VideoDetailReceiveGiftBean> page_result;
    private String gift_count;

    public List<VideoDetailReceiveGiftBean> getPage_result() {
        return page_result;
    }

    public void setPage_result(List<VideoDetailReceiveGiftBean> page_result) {
        this.page_result = page_result;
    }

    public String getGift_count() {
        return gift_count;
    }

    public void setGift_count(String gift_count) {
        this.gift_count = gift_count;
    }
}

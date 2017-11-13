package com.haiqiu.miaohi.receiver;

/**
 * 刷新礼物数量
 * Created by hackest on 2016/9/6.
 */
public class RefreshGiftCountEvent {

    int currentCount;
    String videoid;

    public RefreshGiftCountEvent(int currentCount, String videoid) {
        this.currentCount = currentCount;
        this.videoid = videoid;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
}

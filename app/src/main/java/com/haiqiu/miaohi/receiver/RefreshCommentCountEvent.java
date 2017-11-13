package com.haiqiu.miaohi.receiver;

/**
 * Created by ningl on 16/10/25.
 */
public class RefreshCommentCountEvent {

    int currentCount;
    String videoid;

    public RefreshCommentCountEvent(int currentCount, String videoid) {
        this.currentCount = currentCount;
        this.videoid = videoid;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }
}

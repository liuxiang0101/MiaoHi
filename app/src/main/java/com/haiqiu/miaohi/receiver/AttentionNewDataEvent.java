package com.haiqiu.miaohi.receiver;

/**
 * 关注页新数据
 * Created by ningl on 17/2/7.
 */

public class AttentionNewDataEvent {

    private String notify_message;

    public AttentionNewDataEvent(String notify_message) {
        this.notify_message = notify_message;
    }

    public void setNotify_message(String notify_message) {
        this.notify_message = notify_message;
    }

    public String getNotify_message() {

        return notify_message;
    }
}

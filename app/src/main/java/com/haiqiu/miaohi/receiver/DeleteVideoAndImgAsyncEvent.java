package com.haiqiu.miaohi.receiver;


/**
 * Created by ningl on 17/1/13.
 */

public class DeleteVideoAndImgAsyncEvent {

    private int contentType;
    private String targetId;
    private int fromType;

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }
}

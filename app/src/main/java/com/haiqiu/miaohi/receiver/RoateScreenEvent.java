package com.haiqiu.miaohi.receiver;

/**
 * Created by ningl on 16/11/10.
 */
public class RoateScreenEvent {
    private int requestedOrientation;

    public RoateScreenEvent(int requestedOrientation) {
        this.requestedOrientation = requestedOrientation;
    }

    public int getRequestedOrientation() {
        return requestedOrientation;
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.requestedOrientation = requestedOrientation;
    }
}

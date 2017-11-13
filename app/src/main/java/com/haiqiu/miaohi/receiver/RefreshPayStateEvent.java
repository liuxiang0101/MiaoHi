package com.haiqiu.miaohi.receiver;

/**
 * Created by hackest on 16/8/19.
 */
public class RefreshPayStateEvent {
    int position;

    public RefreshPayStateEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

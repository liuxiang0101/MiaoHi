package com.haiqiu.miaohi.receiver;

/**
 * Created by hackest on 2016/9/6.
 */
public class RefreshBalanceEvent {
    long price;

    public RefreshBalanceEvent(long price) {
        this.price = price;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}

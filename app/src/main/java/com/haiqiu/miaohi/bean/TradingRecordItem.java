package com.haiqiu.miaohi.bean;

/**
 * Created by hackest on 16/8/18.
 */
public class TradingRecordItem {

    private String transaction_title;
    private String transaction_content;
    private long transaction_time;
    private int transaction_type;
    private String object_id;
    private String hi_coin_view;

    public String getTransaction_title() {
        return transaction_title;
    }

    public void setTransaction_title(String transaction_title) {
        this.transaction_title = transaction_title;
    }

    public String getTransaction_content() {
        return transaction_content;
    }

    public void setTransaction_content(String transaction_content) {
        this.transaction_content = transaction_content;
    }

    public long getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(long transaction_time) {
        this.transaction_time = transaction_time;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getHi_coin_view() {
        return hi_coin_view;
    }

    public void setHi_coin_view(String hi_coin_view) {
        this.hi_coin_view = hi_coin_view;
    }
}

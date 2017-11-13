package com.haiqiu.miaohi.bean;

/**
 * Created by hackest on 16/8/18.
 */
public class RechargingRecordItem {
    private String deposit_title;
    private int deposit_price;
    private long deposit_time;
    private String deposit_id;
    private String deposit_status_view;
    private String additional_hi_coin_text;

    public String getDeposit_title() {
        return deposit_title;
    }


    public int getDeposit_price() {
        return deposit_price;
    }


    public long getDeposit_time() {
        return deposit_time;
    }


    public String getDeposit_status_view() {
        return deposit_status_view;
    }


    public String getAdditional_hi_coin_text() {
        return additional_hi_coin_text;
    }
}

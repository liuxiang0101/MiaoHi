package com.haiqiu.miaohi.bean;

/**
 * Created by hackest on 16/8/16.
 */
public class RechargingItem {
    private long hi_coin;
    private long goods_price;
    private String goods_id;
    private String additional_hi_coin_text;
    public boolean isChecked; //选中状态;


    public RechargingItem(long hi_coin, long goods_price, String goods_id, boolean isChecked) {
        this.hi_coin = hi_coin;
        this.goods_price = goods_price;
        this.goods_id = goods_id;
        this.isChecked = isChecked;
    }

    public long getHi_coin() {
        return hi_coin;
    }

    public void setHi_coin(long hi_coin) {
        this.hi_coin = hi_coin;
    }

    public long getGoods_price() {
        return goods_price;
    }

    public String getAdditional_hi_coin_text() {
        return additional_hi_coin_text;
    }

    public void setGoods_price(long goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setAdditional_hi_coin_text(String additional_hi_coin_text) {
        this.additional_hi_coin_text = additional_hi_coin_text;
    }


    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}

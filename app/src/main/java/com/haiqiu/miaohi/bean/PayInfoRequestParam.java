package com.haiqiu.miaohi.bean;

/**
 * Created by zhandalin on 2016-08-16 20:30.
 * 说明:支付请求参数
 */
public class PayInfoRequestParam {
    private int deposit_type;
    private String goods_id;
    private String deposit_price;

    public int getDeposit_type() {
        return deposit_type;
    }

    public void setDeposit_type(int deposit_type) {
        this.deposit_type = deposit_type;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getDeposit_price() {
        return deposit_price;
    }

    public void setDeposit_price(String deposit_price) {
        this.deposit_price = deposit_price;
    }
}

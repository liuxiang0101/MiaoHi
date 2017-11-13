package com.haiqiu.miaohi.bean;

/**
 * Created by hackest on 2016/8/31.
 */
public class GiftResultBean {

    private int hi_coin;
    private String icon_role;
    private String icon_state;
    private String gift_id;
    private String remain_count;
    private String gift_name;
    private String gift_type;
    private String icon_uri;
    int select = 0;
    private boolean gift_has_sent;


    public GiftResultBean(int hi_coin, String icon_role, String icon_state, String gift_id, String remain_count, String gift_name, String gift_type, String icon_uri) {
        this.hi_coin = hi_coin;
        this.icon_role = icon_role;
        this.icon_state = icon_state;
        this.gift_id = gift_id;
        this.remain_count = remain_count;
        this.gift_name = gift_name;
        this.gift_type = gift_type;
        this.icon_uri = icon_uri;
    }

    public boolean isGift_has_sent() {
        return gift_has_sent;
    }

    public void setGift_has_sent(boolean gift_has_sent) {
        this.gift_has_sent = gift_has_sent;
    }

    public int getHi_coin() {
        return hi_coin;
    }

    public void setHi_coin(int hi_coin) {
        this.hi_coin = hi_coin;
    }

    public String getIcon_role() {
        return icon_role;
    }

    public void setIcon_role(String icon_role) {
        this.icon_role = icon_role;
    }

    public String getIcon_state() {
        return icon_state;
    }

    public void setIcon_state(String icon_state) {
        this.icon_state = icon_state;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getRemain_count() {
        return remain_count;
    }

    public void setRemain_count(String remain_count) {
        this.remain_count = remain_count;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getGift_type() {
        return gift_type;
    }

    public void setGift_type(String gift_type) {
        this.gift_type = gift_type;
    }

    public String getIcon_uri() {
        return icon_uri;
    }

    public void setIcon_uri(String icon_uri) {
        this.icon_uri = icon_uri;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }
}

package com.haiqiu.miaohi.bean;

/**
 * 大咖可送礼物的实体类
 * Created by ningl on 2016/6/24.
 */
public class SendGiftBean {


    /**
     * icon_role : default
     * icon_state : 10
     * gift_id : GIFT-b65d091a-047b-11e6-aa2d-002522d24c4a
     * remain_count : -1
     * gift_name : 背心
     * gift_type : 20
     * gift_state : 10
     * icon_uri : http://o71qpn3yk.bkt.clouddn.com/gift_normal_vest_default.png
     */

    private String icon_role;
    private String icon_state;
    private String gift_id;
    private String remain_count;
    private String gift_name;
    private int gift_type;
    private String gift_state;
    private String icon_uri;

    public int getGift_type() {
        return gift_type;
    }

    public void setGift_type(int gift_type) {
        this.gift_type = gift_type;
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

    public String getGift_state() {
        return gift_state;
    }

    public void setGift_state(String gift_state) {
        this.gift_state = gift_state;
    }

    public String getIcon_uri() {
        return icon_uri;
    }

    public void setIcon_uri(String icon_uri) {
        this.icon_uri = icon_uri;
    }
}

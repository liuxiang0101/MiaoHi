package com.haiqiu.miaohi.bean;

import java.io.Serializable;

/**
 * 全部礼物
 * Created by miaohi on 2016/4/27.
 */
public class GiftResultArrayObj implements Serializable {
    private String gift_id;
    private String gift_name;
    private String gift_type;
    private String icon_uri;
    private String icon_role;

    public GiftResultArrayObj(String gift_id, String gift_name, String gift_type, String icon_uri, String icon_role) {
        this.gift_id = gift_id;
        this.gift_name = gift_name;
        this.gift_type = gift_type;
        this.icon_uri = icon_uri;
        this.icon_role = icon_role;
    }

    @Override
    public String toString() {
        return "GiftResultArrayObj{" +
                "gift_id='" + gift_id + '\'' +
                ", gift_name='" + gift_name + '\'' +
                ", gift_type='" + gift_type + '\'' +
                ", icon_uri='" + icon_uri + '\'' +
                ", icon_role='" + icon_role + '\'' +
                '}';
    }

    public String getGift_id() {
        return gift_id;
    }

    public String getGift_name() {
        return gift_name;
    }

    public String getGift_type() {
        return gift_type;
    }

    public String getIcon_uri() {
        return icon_uri;
    }

    public String getIcon_role() {
        return icon_role;
    }
}

package com.haiqiu.miaohi.bean;

/**
 * Created by zhandalin on 2016-12-05 16:49.
 * 说明:贴纸信息描述
 */
public class DecalInfo {
    public final static int FROM_NETWORK = 0;
    public final static int FROM_LOCAL = 1;


    private String sticker_id;
    private String sticker_name;
    private int type;
    private String sticker_uri;
    private int sticker_width;
    private int sticker_height;
    private boolean selected;

    public String getSticker_id() {
        return sticker_id;
    }

    public void setSticker_id(String sticker_id) {
        this.sticker_id = sticker_id;
    }

    public String getSticker_name() {
        return sticker_name;
    }

    public void setSticker_name(String sticker_name) {
        this.sticker_name = sticker_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSticker_uri() {
        return sticker_uri;
    }

    public void setSticker_uri(String sticker_uri) {
        this.sticker_uri = sticker_uri;
    }

    public int getSticker_width() {
        return sticker_width;
    }

    public void setSticker_width(int sticker_width) {
        this.sticker_width = sticker_width;
    }

    public int getSticker_height() {
        return sticker_height;
    }

    public void setSticker_height(int sticker_height) {
        this.sticker_height = sticker_height;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}

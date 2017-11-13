package com.haiqiu.miaohi.bean;

import android.graphics.Bitmap;

/**
 * Created by zhandalin on 2016-09-04 21:03.
 * 说明:滤镜的参数
 */
public class FilterInfo {
    private String filter_name;
    private String filter_param;
    private float intensity = 1;
    private String icon_name;

    private transient Bitmap icon_bitmap;

    private boolean isSelected;

    public String getFilter_name() {
        return filter_name;
    }

    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }

    public String getFilter_param() {
        if (null == filter_param) filter_param = "";
        return filter_param;
    }

    public void setFilter_param(String filter_param) {
        this.filter_param = filter_param;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }

    public Bitmap getIcon_bitmap() {
        return icon_bitmap;
    }

    public void setIcon_bitmap(Bitmap icon_bitmap) {
        this.icon_bitmap = icon_bitmap;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}

package com.haiqiu.miaohi.bean;

/**
 * 分享item实体类
 * Created by ningl on 2016/6/25.
 */
public class SharedItemBean {

    private String name;
    private int drawable;
    private int sharedType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getSharedType() {
        return sharedType;
    }

    public void setSharedType(int sharedType) {
        this.sharedType = sharedType;
    }
}

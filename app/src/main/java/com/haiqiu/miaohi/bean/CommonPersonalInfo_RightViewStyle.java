package com.haiqiu.miaohi.bean;

/**
 * 公共用户信息ViewGroup右侧view
 * Created by ningl on 16/12/1.
 */
public class CommonPersonalInfo_RightViewStyle {

    private int with;
    private int height;
    private int strokeWidth;
    private int strokeColor;
    private int solidColor;
    private int textSize;
    private int radius;
    private boolean isDefaultStyle = true;
    private String text;
    private int strokeColor_default;
    private int solidColor_default;

    public int getWith() {
        return with;
    }

    public void setWith(int with) {
        this.with = with;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }



    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}

package com.haiqiu.miaohi.base;

/**
 * Created by ningl on 16/12/2.
 */
public class FadeOutFromBottomItem {

    private String color;
    private String text;

    public FadeOutFromBottomItem(String color, String text) {
        this.color = color;
        this.text = text;
    }

    public FadeOutFromBottomItem() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

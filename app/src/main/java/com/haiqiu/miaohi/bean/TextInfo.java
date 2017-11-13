package com.haiqiu.miaohi.bean;

/**
 * 字体信息类
 * Created by ningl on 16/10/9.
 */
public class TextInfo {

    private String originalStr;
    private int color;
    private int start;
    private int end;
    private String target;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOriginalStr() {
        return originalStr;
    }

    public void setOriginalStr(String originalStr) {
        this.originalStr = originalStr;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}

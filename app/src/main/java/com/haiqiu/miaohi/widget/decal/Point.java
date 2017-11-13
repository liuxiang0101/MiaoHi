package com.haiqiu.miaohi.widget.decal;

/**
 * Created by zhandalin on 2016-12-26 16:52.
 * 说明: 系统的x,y是int型,不爽
 */
public class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x: " + x + ",y: " + y;
    }
}
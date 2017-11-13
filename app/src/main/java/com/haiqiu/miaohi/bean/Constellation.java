package com.haiqiu.miaohi.bean;

/**
 * 星座
 * Created by ningl on 16/9/5.
 */
public class Constellation {

    private int constellationId;// 星座图片id
    private String constellationName;//星座名称


    public int getConstellationId() {
        return constellationId;
    }

    public void setConstellationId(int constellationId) {
        this.constellationId = constellationId;
    }

    public String getConstellationName() {
        return constellationName;
    }

    public void setConstellationName(String constellationName) {
        this.constellationName = constellationName;
    }
}

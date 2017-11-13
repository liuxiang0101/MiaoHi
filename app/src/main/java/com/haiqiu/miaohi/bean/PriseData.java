package com.haiqiu.miaohi.bean;

/**
 * Created by miaohi on 2016/5/6.
 */
public class PriseData {
    //  给评论点赞返回值
    private String click_praise;
    private String praise_count;
    private Object reserved;

    public void setReserved(Object reserved) {
        this.reserved = reserved;
    }

    public Object getReserved() {

        return reserved;
    }

    @Override
    public String toString() {
        return "PriseData{" +
                "click_praise='" + click_praise + '\'' +
                ", praise_count='" + praise_count + '\'' +
                '}';
    }

    public String getClick_praise() {
        return click_praise;
    }

    public void setClick_praise(String click_praise) {
        this.click_praise = click_praise;
    }

    public String getPraise_count() {
        return praise_count;
    }
}

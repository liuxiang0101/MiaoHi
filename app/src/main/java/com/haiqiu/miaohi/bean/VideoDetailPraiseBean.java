package com.haiqiu.miaohi.bean;

/**
 * 视频详情点赞实体类
 * Created by ningl on 2016/6/23.
 */
public class VideoDetailPraiseBean {

    private String praise_time;
    private String praise_count;

    public String getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public String getPraise_time() {
        return praise_time;
    }

    public void setPraise_time(String praise_time) {
        this.praise_time = praise_time;
    }
}

package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * Created by zhandalin on 2016-06-24 11:45.
 * 说明:视频基本信息,video_id,video_uri,hls_uri是必传字段,其他可以选传
 */
public class VideoInfo {
    private int video_state;
    private int hls_uri_state = 10;
    private long lastPlayDuration;//上一次播放的时长

    private String video_id;
    private String video_uri;
    private String hls_uri;
    private String play_count = "1";//服务端必须要传String,这个字段不能删除
    private int localPlayCount = 1;


    public int getVideo_state() {
        //防止没有初始化影响结果
        if (0 == video_state) video_state = ConstantsValue.VideoState.STATE_TYPE_NORMAL;
        return video_state;
    }

    public void setVideo_state(int video_state) {
        this.video_state = video_state;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public String getHls_uri() {
        return hls_uri;
    }

    public void setHls_uri(String hls_uri) {
        this.hls_uri = hls_uri;
    }

    public String getPlay_count() {
        return localPlayCount + "";
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public int getLocalPlayCount() {
        return localPlayCount;
    }

    public void setLocalPlayCount(int localPlayCount) {
        play_count = localPlayCount + "";
        this.localPlayCount = localPlayCount;
    }

    public long getLastPlayDuration() {
        return lastPlayDuration;
    }

    public void setLastPlayDuration(long lastPlayDuration) {
        this.lastPlayDuration = lastPlayDuration;
    }

    public int getHls_uri_state() {
        return hls_uri_state;
    }

    public void setHls_uri_state(int hls_uri_state) {
        this.hls_uri_state = hls_uri_state;
    }
}

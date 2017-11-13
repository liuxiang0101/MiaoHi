package com.haiqiu.miaohi.bean;

/**
 * Created by zhandalin on 2017-01-03 10:51.
 * 说明:视频播放额外的信息
 */
public class VideoExtraInfo {
    public String videoId;
    private VideoType videoType = VideoType.VIDEO_TYPE_COMMON;

    /**
     * 专题名字
     */
    public String subjectName;

    /**
     * 专题uri
     */
    public String subjectUri;


    /**
     * 推荐的标签
     */
    public String recommendStr;


    /**
     * 视频时长
     */
    public long videoDuration;

    /**
     * 播放数
     */
    public long playNum;

    /**
     * 当前item的位置
     */
    public int position;

    public void setVideoType(VideoType videoType) {
        this.videoType = videoType;
    }

    public VideoType getVideoType() {
        return videoType;
    }

    public enum VideoType {
        VIDEO_TYPE_COMMON, VIDEO_TYPE_YD
    }
}

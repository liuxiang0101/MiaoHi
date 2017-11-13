package com.haiqiu.miaohi.receiver;

import com.haiqiu.miaohi.bean.VideoUploadInfo;

/**
 * 上传成功
 * Created by hackest on 2016/9/5.
 */
public class UploadSuccessEvent {

    VideoUploadInfo videoUploadInfo;

    public UploadSuccessEvent(VideoUploadInfo videoUploadInfo) {
        this.videoUploadInfo = videoUploadInfo;
    }


    public VideoUploadInfo getVideoUploadInfo() {
        return videoUploadInfo;
    }

    public void setVideoUploadInfo(VideoUploadInfo videoUploadInfo) {
        this.videoUploadInfo = videoUploadInfo;
    }
}

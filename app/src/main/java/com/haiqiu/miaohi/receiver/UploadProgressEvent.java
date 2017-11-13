package com.haiqiu.miaohi.receiver;

import com.haiqiu.miaohi.bean.VideoUploadInfo;

/**
 * 上传进度
 * Created by hackest on 2016/9/5.
 */
public class UploadProgressEvent {

    int progress;
    double percent;
    int uploadTag;
    public static final int UPLOAD_PRE = 1;
    public static final int UPLOAD_SUCCESS = 2;
    public static final int UPLOAD_FAILE = 3;
    public static final int UPLOAD_PROGRESS = 4;
    VideoUploadInfo uploadInfo;
    int uplodType;//上传类型
    int uploadState;

    public UploadProgressEvent(int progress) {
        this.progress = progress;
    }

    public UploadProgressEvent(double percent, VideoUploadInfo uploadInfo) {
        this.percent = percent;
        this.uploadInfo = uploadInfo;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getUploadTag() {
        return uploadTag;
    }

    public void setUploadTag(int uploadTag) {
        this.uploadTag = uploadTag;
    }

    public VideoUploadInfo getUploadInfo() {
        return uploadInfo;
    }

    public void setUploadInfo(VideoUploadInfo uploadInfo) {
        this.uploadInfo = uploadInfo;
    }

    public int getUplodType() {
        return uplodType;
    }

    public void setUplodType(int uplodType) {
        this.uplodType = uplodType;
    }

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }
}

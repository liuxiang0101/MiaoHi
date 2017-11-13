package com.haiqiu.miaohi.bean;

import android.net.Uri;

import com.haiqiu.miaohi.utils.MHLogUtil;

import java.io.File;

/**
 * Created by zhandalin on 2016-06-27 14:48.
 * 说明:
 */
public class ChooseMediaBean {
    private String mediaPath;
    private int mediaType;
    private int mediaWidth;
    private int mediaHeight;
    private long mediaSize;
    private long duration;

    private String videoThumbnailUri;
    private String failMessage;
    private boolean isFail;

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getVideoThumbnailUri() {
        try {
            if (null == videoThumbnailUri) {
                videoThumbnailUri = Uri.fromFile(new File(mediaPath)).toString();
            }
        } catch (Exception e) {
            MHLogUtil.e("getVideoThumbnailUri",e);
        }
        return videoThumbnailUri;
    }

    public void setVideoThumbnailUri(String videoThumbnailUri) {
        this.videoThumbnailUri = videoThumbnailUri;
    }

    public boolean isFail() {
        return isFail;
    }

    public void setFail(boolean fail) {
        isFail = fail;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public int getMediaWidth() {
        return mediaWidth;
    }

    public void setMediaWidth(int mediaWidth) {
        this.mediaWidth = mediaWidth;
    }

    public int getMediaHeight() {
        return mediaHeight;
    }

    public void setMediaHeight(int mediaHeight) {
        this.mediaHeight = mediaHeight;
    }

    public long getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(long mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }
}

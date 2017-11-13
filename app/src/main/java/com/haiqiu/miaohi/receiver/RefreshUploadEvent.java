package com.haiqiu.miaohi.receiver;

import com.haiqiu.miaohi.bean.VideoUploadInfo;

/**
 * 上传映答
 * Created by ningl on 16/12/19.
 */

public class RefreshUploadEvent {

    VideoUploadInfo task;

    public RefreshUploadEvent(VideoUploadInfo task) {
        this.task = task;
    }

    public VideoUploadInfo getTask() {
        return task;
    }

    public void setTask(VideoUploadInfo task) {
        this.task = task;
    }
}

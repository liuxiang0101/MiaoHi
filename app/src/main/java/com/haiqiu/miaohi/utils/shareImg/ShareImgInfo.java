package com.haiqiu.miaohi.utils.shareImg;

/**
 * 分享图片 内容图片加载状态
 * Created by ningl on 16/12/10.
 */
public class ShareImgInfo {

    public static final int UNLOAD = -1;    //未加载
    public static final int LOADFAIL = 0;   //加载失败
    public static final int LOADSUCCESS = 1;//加载成功

    private int uploadState = -1;
    private boolean isChecked;//是否检查过


    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

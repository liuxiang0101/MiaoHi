package com.haiqiu.miaohi.okhttp;


/**
 * Created by zhandalin on 2016-12-17 18:37.
 * 说明:
 */
public abstract class MHHttpProgressHandler implements MHHttpBaseHandler {

    public abstract void onSuccess(String content);


    @Override
    public void onFailure(String content) {

    }

    public abstract void onProgress(float progress);
}

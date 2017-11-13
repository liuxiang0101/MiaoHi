package com.haiqiu.miaohi.okhttp;

import com.haiqiu.miaohi.response.BaseResponse;

public abstract class MHHttpHandler<T extends BaseResponse> implements MHHttpBaseHandler {
    @Override
    public void onSuccess(String content) {

    }

    public abstract void onSuccess(T response);


    /**
     * 和服务端交互成功,但是返回的状态码不对
     *
     * @param message 服务端返回来的错误信息
     */
    public void onStatusIsError(String message) {

    }

}

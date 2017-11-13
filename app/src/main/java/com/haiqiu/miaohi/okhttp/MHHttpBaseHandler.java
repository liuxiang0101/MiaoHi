package com.haiqiu.miaohi.okhttp;

public interface MHHttpBaseHandler {

    /**
     * 网络请求成功
     *
     * @param content 服务端返回的类容
     */
    void onSuccess(String content);

    /**
     * 网络请求失败,失败的类型:无网络,网络不好连接失败,服务端挂了
     *
     * @param content 失败的信息
     */
    void onFailure(String content);
}
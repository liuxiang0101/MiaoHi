package com.haiqiu.miaohi.okhttp;

import com.haiqiu.miaohi.utils.MHStringUtils;

import java.util.TreeMap;

/**
 * Created by bookzhan on 2015/8/7.
 * 说明:请求参数的封装
 */
public class MHRequestParams {
    //用TreeMap为以后MD5做扩展用
    private TreeMap<String, String> paramMap = new TreeMap<>();

    /**
     * 要注意的是这里面添加的只是 info里面的字段
     */
    public void addParams(String key, String param) {
        if (MHStringUtils.isEmpty(key) || MHStringUtils.isEmpty(param)) {
            return;
        }
        paramMap.put(key, param);
    }

    public TreeMap<String, String> getParamMap() {
        return paramMap;
    }
}

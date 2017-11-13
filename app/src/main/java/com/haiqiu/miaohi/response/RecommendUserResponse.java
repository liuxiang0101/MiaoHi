package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.RecommendUserData;

/**
 * Created by LiuXiang on 2016/9/20.
 */
public class RecommendUserResponse extends BaseResponse{
    private RecommendUserData data;

    public RecommendUserData getData() {
        return data;
    }
}

package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.UserInfoData;

/**
 * 用户粉丝&关注
 * Created by miaohi on 2016/6/2.
 */
public class CommonUserInfoResponse extends BaseResponse{
    UserInfoData data;

    public UserInfoData getData() {
        return data;
    }
}

package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.UserAttentionStatusData;

/**
 * Created by LiuXiang on 2016/12/6.
 */
public class UserAttentionStatusResponse extends BaseResponse{
    UserAttentionStatusData data;

    public UserAttentionStatusData getData() {
        return data;
    }
}

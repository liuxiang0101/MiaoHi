package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.DiscoveryUsersData;

/**
 * Created by LiuXiang on 2016/12/13.
 */
public class DiscoveryUsersResponse extends BaseResponse{
    DiscoveryUsersData data;

    public DiscoveryUsersData getData() {
        return data;
    }
}

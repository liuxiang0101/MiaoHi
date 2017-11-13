package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.UserMoneyBalanceData;

/**
 * Created by LiuXiang on 2016/7/19.
 */
public class UserMoneyBalanceResponse extends BaseResponse {
    private UserMoneyBalanceData data;

    public UserMoneyBalanceData getData() {
        return data;
    }
}

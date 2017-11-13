package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.AccountBindPageResult;

/**
 * 账户绑定
 * Created by ningl on 16-8-31.
 */
public class AccountBindResponse extends BaseResponse {

    private AccountBindPageResult data;

    public AccountBindPageResult getData() {
        return data;
    }

    public void setData(AccountBindPageResult data) {
        this.data = data;
    }
}

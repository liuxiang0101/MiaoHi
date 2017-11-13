package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * 账户绑定
 * Created by ningl on 16-8-31.
 */
public class AccountBindPageResult {

    private List<AccountBindBean> page_result;

    public List<AccountBindBean> getPage_result() {
        return page_result;
    }

    public void setPage_result(List<AccountBindBean> page_result) {
        this.page_result = page_result;
    }
}

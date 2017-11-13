package com.haiqiu.miaohi.bean;

/**
 * 账户绑定
 * Created by ningl on 16-8-31.
 */
public class AccountBindBean {


    /**
     * account_num : xxxxx
     * account_type : xxxx
     */

    private String account_num;
    private int account_type;

    public AccountBindBean(String account_num, int account_type) {
        this.account_num = account_num;
        this.account_type = account_type;
    }



    public String getAccount_num() {
        return account_num;
    }

    public int getAccount_type() {
        return account_type;
    }
}

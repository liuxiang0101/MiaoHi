package com.haiqiu.miaohi.bean;

import java.util.ArrayList;

/**
 * Created by miaohi on 2016/6/2.
 */
public class UserInfoData {
    private ArrayList<UserInfoPageResult> page_result;
    private String rong_token;

    public String getRong_token() {
        return rong_token;
    }

    public ArrayList<UserInfoPageResult> getPage_result() {
        return page_result;
    }
}

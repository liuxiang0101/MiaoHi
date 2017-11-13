package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * 我的页面
 * Created by Guoy on 2016/4/23.
 */
public class FragMineBean {
    private String portrait_uri;
    private String nick_name;
    private String user_gender;
    private String user_area;
    private String user_birthday;

    public String getPortrait_uri() {
        return portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public String getUser_area() {
        return user_area;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    @Override
    public String toString() {
        return "FragMineBean{" +
                "portrait_uri" + portrait_uri + '\'' +
                "nick_name" + nick_name + '\'' +
                "user_gender" + user_gender + '\'' +
                "user_area" + user_area + '\'' +
                "user_birthday" + user_birthday + '\'' + '}';
    }
}

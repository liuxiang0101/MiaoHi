package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.utils.MHStringUtils;

/**
 * Created by zhandalin on 2016-12-20 14:51.
 * 说明:
 */
public class UserGroupInfo {
    private String groupName;//分组的数量
    private int groupNum;//分组的数量

    public UserGroupInfo(String groupName, int groupNum) {
        this.groupName = groupName;
        this.groupNum = groupNum;
    }

    public String getGroupName() {
        if (MHStringUtils.isEmpty(groupName)) groupName = "#";
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    @Override
    public String toString() {
        return "UserGroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupNum=" + groupNum +
                '}';
    }
}

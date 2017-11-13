package com.haiqiu.miaohi.widget;

import android.widget.SectionIndexer;

import com.haiqiu.miaohi.bean.UserGroupInfo;
import com.haiqiu.miaohi.utils.MHLogUtil;

import java.util.Arrays;
import java.util.List;

public class MySectionIndexer implements SectionIndexer {
    private String[] mSections = new String[0];//分组数据
    private int[] mPositions = new int[0];//每一个分组数据在整个列表中的位置
    private int mCount;
    private int currentBottomIndex;
    private int currentTopIndex;

    public void setUserGroupInfo(List<UserGroupInfo> userGroupInfo) {
        if (null == userGroupInfo || userGroupInfo.size() == 0) return;
        mSections = new String[userGroupInfo.size()];
        mPositions = new int[userGroupInfo.size()];

        int position = 0;
        for (int i = 0; i < userGroupInfo.size(); i++) {
            UserGroupInfo groupInfo = userGroupInfo.get(i);
            mSections[i] = groupInfo.getGroupName();
            mPositions[i] = position;
            position += groupInfo.getGroupNum();
        }
        mCount = position;
    }

    @Override
    public Object[] getSections() {
        return mSections;
    }

    public String getSectionTitle(int position) {
        if (mSections.length > position)
            return mSections[position];
        return "#";
    }

    public void setCurrentBottomIndex(int currentBottomIndex) {
        this.currentBottomIndex = currentBottomIndex;
    }

    public void setCurrentTopIndex(int currentTopIndex) {
        this.currentTopIndex = currentTopIndex;
    }

    @Override
    public int getPositionForSection(int section) {
        //change by lcq 2012-10-12 section > mSections.length以为>=
        if (section < 0 || section >= mSections.length) {
            return -1;
        }
        return mPositions[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position < 0 || position >= mCount) {
            return -1;
        }
        MHLogUtil.d("kkkk", "src--position=" + position);
        position = currentTopIndex + position;

        //注意这个方法的返回值，它就是index<0时，返回-index-2的原因
        //解释Arrays.binarySearch，如果搜索结果在数组中，刚返回它在数组中的索引，如果不在，刚返回第一个比它大的索引的负数-1
        //如果没弄明白，请自己想查看api
        int index = Arrays.binarySearch(mPositions, position);
        MHLogUtil.d("kkkk", "index=" + index + "---currentTopIndex=" + currentTopIndex + "--position=" + position);
        return index >= 0 ? index : -index - 2; //当index小于0时，返回-index-2，

    }

}
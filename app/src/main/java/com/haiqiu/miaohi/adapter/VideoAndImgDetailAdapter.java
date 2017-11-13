package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.fragment.VideoAndImgDetailFragment;

import java.util.List;

/**
 * 视频和图片详情适配器
 * Created by ningl on 16/12/14.
 */
public class VideoAndImgDetailAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<VideoAndImg> data;
    private boolean isFromCommentList;
    private boolean isNeedBack;

    public VideoAndImgDetailAdapter(FragmentManager fm, Context context, List<VideoAndImg> data, boolean isFromCommentList,boolean isNeedBack) {
        super(fm);
        this.context = context;
        this.data = data;
        this.isFromCommentList = isFromCommentList;
        this.isNeedBack = isNeedBack;
    }

    @Override
    public Fragment getItem(int position) {
        VideoAndImgDetailFragment fragment = new VideoAndImgDetailFragment();
        Bundle arg = new Bundle();
        arg.putParcelable("videoandimg", data.get(position));
        arg.putBoolean("isFromCommentList", isFromCommentList);
        arg.putBoolean("isNeedBack", isNeedBack);
        arg.putBoolean("isDelete", data.get(position).isDelete());
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }

}

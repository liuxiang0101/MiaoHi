package com.haiqiu.miaohi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.DiscoveryLabelObj;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.widget.tagview.Tag;
import com.haiqiu.miaohi.widget.tagview.TagListView;
import com.haiqiu.miaohi.widget.tagview.TagView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuXiang on 2016/11/29.
 */
public class SortDialogFragment extends DialogFragment {
    private TagListView mTagListView;
    private OnGetChildViewNum onGetChildViewNum;
    private final List<Tag> mTags = new ArrayList<>();
    private ArrayList<DiscoveryLabelObj> list;
    private String defaultKindTag;

    public void setGetChildViewNum(OnGetChildViewNum onGetChildViewNum) {
        this.onGetChildViewNum = onGetChildViewNum;
    }

    public SortDialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = getArguments().getParcelableArrayList("list");
        defaultKindTag = getArguments().getString("kind_tag");
        setUpData();
    }

    public void refreshData() {
        if (((SortDialog) getParentFragment()).getPageMap().get(getArguments().getInt("pageIndex"))) {
            resetTagView();
        }
    }

    private void resetTagView() {
        List<Tag> tags = mTagListView.getTags();
        for (int i = 0; i < tags.size(); i++) {
            tags.get(i).setChecked(false);
            ((TagView) mTagListView.getViewByTag(tags.get(i))).setChecked(false);
            ((TagView) mTagListView.getViewByTag(tags.get(i))).setTextColor(getResources().getColor(R.color.color_c4));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.header_history_record, null);
        mTagListView = (TagListView) view.findViewById(R.id.tagview);
        view.findViewById(R.id.ll_tag_search).setVisibility(View.VISIBLE);
        mTagListView.setVisibility(View.VISIBLE);
        mTagListView.setTags(mTags, true);

        List<Tag> tags = mTagListView.getTags();
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getKindTag().equals(defaultKindTag)) {
                ((TagView) mTagListView.getViewByTag(tags.get(i))).setChecked(true);
                ((TagView) mTagListView.getViewByTag(tags.get(i))).setTextColor(getResources().getColor(R.color.color_1d));
            }
        }

        mTagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                MHLogUtil.e(tag.getTitle() + "----");
                MHLogUtil.e(tag.isChecked() + "----");
                resetTagView();
                tagView.setChecked(true);

                tagView.setTextColor(getResources().getColor(R.color.color_1d));
                for (int i = 0; i < ((SortDialog) getParentFragment()).getPageMap().size(); i++) {
                    ((SortDialog) getParentFragment()).getPageMap().put(i, true);
                }
                ((SortDialog) getParentFragment()).getPageMap().put(getArguments().getInt("pageIndex"), false);

                //切换label标签，列表接口刷新
                ((SortDialog) getParentFragment()).setCurrentKindTag(tag.getKindTag());
                ((SortDialog) getParentFragment()).setCurrentCommend(tag.getCommend());
            }

            @Override
            public void onGetViewNum(int num) {
                ArrayList<DiscoveryLabelObj> li = new ArrayList<>();
                for (int i = num; i < list.size(); i++) {
                    li.add(list.get(i));
                }
                if (onGetChildViewNum != null) {
                    onGetChildViewNum.onGetViewNum(num, li);
                }
            }
        });

        return view;
    }

    private void setUpData() {
        for (int i = 0; i < list.size(); i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(false);
            DiscoveryLabelObj obj = list.get(i);
            tag.setKindTag(obj.getKind_tag());
            tag.setCommend(obj.getKind_Command());
            tag.setTitle(obj.getKind_name());
            mTags.add(tag);
        }
    }

    public interface OnGetChildViewNum {
        void onGetViewNum(int num, ArrayList<DiscoveryLabelObj> list);
    }
}

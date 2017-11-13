package com.haiqiu.miaohi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.DiscoveryLabelObj;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.widget.tagview.Tag;
import com.haiqiu.miaohi.widget.tagview.TagListView;
import com.haiqiu.miaohi.widget.tagview.TagView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LiuXiang on 2016/10/28.
 */
public class SortDialog1 extends DialogFragment {

    private String currentKindTag;
    private String currentCommend;
    private ArrayList<DiscoveryLabelObj> list;
    private final List<Tag> mTags = new ArrayList<>();
    private HashMap<Integer, Boolean> pageMap = new HashMap<>();

    private Button bt_ok;
    private ImageView iv_close;
    private TagListView mTagListView;

    public HashMap<Integer, Boolean> getPageMap() {
        return pageMap;
    }

    public void setCurrentCommend(String currentCommend) {
        this.currentCommend = currentCommend;
    }

    public void setCurrentKindTag(String currentKindTag) {
        this.currentKindTag = currentKindTag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收数据
        list = getArguments().getParcelableArrayList("list");
        currentKindTag = getArguments().getString("kind_tag");
        //设置要显示的分类数据
        setUpData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        View view = inflater.inflate(R.layout.sort_dialog1, container);
        currentCommend = getArguments().getString("commend");
        initView(view);
        initData();

        mTagListView.setVisibility(View.VISIBLE);
        mTagListView.setTags(mTags, true);

        List<Tag> tags = mTagListView.getTags();
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getKindTag().equals(currentKindTag)) {
                ((TagView) mTagListView.getViewByTag(tags.get(i))).setChecked(true);
                ((TagView) mTagListView.getViewByTag(tags.get(i))).setTextColor(getResources().getColor(R.color.color_1d));
            }
        }

        mTagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                //重置状态为未选中状态
                resetTagView();
                //设置当前点击控件置为选中状态
                tagView.setChecked(true);
                tagView.setTextColor(getResources().getColor(R.color.color_1d));

                for (int i = 0; i < getPageMap().size(); i++) {
                    getPageMap().put(i, true);
                }
                getPageMap().put(getArguments().getInt("pageIndex"), false);

                //切换label标签，列表接口刷新
                setCurrentKindTag(tag.getKindTag());
                setCurrentCommend(tag.getCommend());
            }

            @Override
            public void onGetViewNum(int num) {

            }
        });
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        mTagListView = (TagListView) view.findViewById(R.id.tagview);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //点击OK按钮
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MHStringUtils.isEmpty(currentKindTag)) {
                    Toast.makeText(getActivity(), "未选择标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                //点击按钮时切换发现页的分类内容
                ((FoundSquareFragment) getParentFragment()).changeLabel(currentKindTag, currentCommend, true);
                SortDialog1.this.dismiss();
            }
        });
        //点击"X"关闭
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortDialog1.this.dismiss();
            }
        });
    }

    /**
     * 重置状态
     */
    private void resetTagView() {
        List<Tag> tags = mTagListView.getTags();
        for (int i = 0; i < tags.size(); i++) {
            tags.get(i).setChecked(false);
            ((TagView) mTagListView.getViewByTag(tags.get(i))).setChecked(false);
            ((TagView) mTagListView.getViewByTag(tags.get(i))).setTextColor(getResources().getColor(R.color.color_c4));
        }
    }

    /**
     * 设置显示分类的数据
     */
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

    @Override
    public void onDestroyView() {
        currentKindTag = null;
        currentCommend = null;
        super.onDestroyView();
    }
}

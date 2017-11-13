package com.haiqiu.miaohi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.DiscoveryLabelObj;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.widget.HomePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by LiuXiang on 2016/10/28.
 */
public class SortDialog extends DialogFragment {
    private ViewPager mViewPager;
    private Button bt_ok;
    private ImageView iv_close;
    private ArrayList<DiscoveryLabelObj> list;
    private HomePageIndicator mIndicator;
    private ArrayList<Fragment> mImageViewList;    // imageView集合
    private GuideAdapter adapter;
    private SortDialogFragment fragment;
    private int total;
    private int count;
    private int pageIndex = 0;
    private boolean isFirst = true;
    private HashMap<Integer, Boolean> pageMap = new HashMap<>();
    private String currentKindTag;
    private String currentCommend;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        View view = inflater.inflate(R.layout.sort_dialog, container);
        list = getArguments().getParcelableArrayList("list");
        currentKindTag = getArguments().getString("kind_tag");
        currentCommend = getArguments().getString("commend");
        total = list.size();
        initView(view);
        initData();
        initVp(getActivity());
        return view;
    }

    private void initView(View view) {
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_guide);
        mIndicator = (HomePageIndicator) view.findViewById(R.id.indicator);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MHStringUtils.isEmpty(currentKindTag)) {
                    Toast.makeText(getActivity(), "未选择标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((FoundSquareFragment) getParentFragment()).changeLabel(currentKindTag, currentCommend, true);
                SortDialog.this.dismiss();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortDialog.this.dismiss();
            }
        });
        mImageViewList = new ArrayList<>();
        adapter = new GuideAdapter(getChildFragmentManager());
        addPage(list);

    }

    private void addPage(ArrayList<DiscoveryLabelObj> list) {
        fragment = new SortDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", list);
        bundle.putString("kind_tag", currentKindTag);
        bundle.putInt("pageIndex", pageIndex);
        pageMap.put(pageIndex, false);
        fragment.setArguments(bundle);
        mImageViewList.add(fragment);
        mIndicator.setPageCount(mImageViewList.size(), 1);
        adapter.notifyDataSetChanged();
        fragment.setGetChildViewNum(new SortDialogFragment.OnGetChildViewNum() {
            @Override
            public void onGetViewNum(int num, ArrayList<DiscoveryLabelObj> li) {
                if (count < total) {
                    if (isFirst) {
                        isFirst = false;
                        total = total - num;
                    }
                    count = count + num;
                    MHLogUtil.e("----------" + num);
                    addPage(li);
                }
            }
        });
        pageIndex++;
    }

    private void initVp(Context context) {
        mViewPager.setAdapter(adapter);// 设置数据
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.addOnPageChangeListener(mIndicator);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((SortDialogFragment) mImageViewList.get(position)).refreshData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class GuideAdapter extends FragmentPagerAdapter {
        public GuideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mImageViewList.get(position);
        }

        @Override
        public int getCount() {
            return mImageViewList.size();
        }
    }

    @Override
    public void onDestroyView() {
        currentKindTag = null;
        currentCommend = null;
        super.onDestroyView();
    }
}

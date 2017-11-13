package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.fragment.ChoiceQaFragment;
import com.haiqiu.miaohi.fragment.HotResponderFragment;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.widget.tablayout.SlidingTabLayout;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by LiuXiang on 2016/12/6.
 * 映答广场界面
 */
public class QaSquareActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private ViewPager viewpager;
    private SlidingTabLayout tabLayout;
    //    public static boolean isVisible;
    private UMShareAPI umShareAPI = null;

    private final String titles[] = {"精选映答", "热门答主"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_square);
        showLoading();
        initView();
        initViewPager();
    }

    private void initView() {
//        isVisible = true;
        umShareAPI = UMShareAPI.get(context);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(findViewById(R.id.iv_back));
        SetClickStateUtil.getInstance().setStateListener(findViewById(R.id.iv_search));
    }

    public void initViewPager() {
        //fragment嵌套
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewpager.setAdapter(new ItemPageAdapter(fragmentManager));
        viewpager.setOffscreenPageLimit(2);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        TCAgent.onEvent(context, "映答-精选映答" + ConstantsValue.android);
                        break;
                    case 1:
                        TCAgent.onEvent(context, "映答-热门答主" + ConstantsValue.android);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setViewPager(viewpager); //初始化的绑定
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.iv_search:
                startActivity(new Intent(QaSquareActivity.this, SearchHistoryRecordActivity.class)
                        .putExtra("searchType", 2));
                break;
        }
    }

    /**
     * viewPager适配器
     */
    class ItemPageAdapter extends FragmentStatePagerAdapter {
        public ItemPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }
    }

    private BaseFragment getFragment(int position) {
        BaseFragment baseFragment = null;

        if (position == 0) {
            //精选映答
            baseFragment = new ChoiceQaFragment();
        } else if (position == 1) {
            //热门答主
            baseFragment = new HotResponderFragment();
        }
        return baseFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) umShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}

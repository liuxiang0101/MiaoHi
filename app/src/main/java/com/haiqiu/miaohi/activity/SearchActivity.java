package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.fragment.SearchUserFragment;
import com.haiqiu.miaohi.fragment.SearchContentFragment;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.widget.tablayout.SlidingTabLayout;

/**
 * 搜索界面--搜索用户/搜索用户
 * Created by zhandalin on 2016/6/23.
 */
public class SearchActivity extends BaseActivity {

    private EditText et_search_user;
    private ViewPager view_pager;
    private int currentPage;
    private SearchContentFragment searchUserFragment;
    private SearchUserFragment searchActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        et_search_user = (EditText) findViewById(R.id.et_search_user);
        et_search_user.setHint("搜索用户");
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
        SetClickStateUtil.getInstance().setStateListener(findViewById(R.id.iv_close));

        et_search_user.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch(true);
                    return true;
                }
                return false;
            }

        });
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (0 == position) {
                    et_search_user.setHint("搜索用户");
                } else {
                    et_search_user.setHint("搜索专题");
                }
                startSearch(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        searchUserFragment = new SearchContentFragment();
        searchUserFragment.startSearch(null);
        searchActivityFragment = new SearchUserFragment();
        searchActivityFragment.startSearch(null);
        view_pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (0 == position) {
                    return searchUserFragment;
                } else {
                    return searchActivityFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (0 == position) {
                    return "用户";
                } else {
                    return "专题";
                }
            }
        });
        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        tabLayout.setViewPager(view_pager);
    }

    private void startSearch(boolean showTip) {
        String keyWords = et_search_user.getText().toString().trim();
        if (MHStringUtils.isEmpty(keyWords)) {
            if (showTip)
                showToastAtCenter("请输入搜索内容");
            return;
        }
        if (currentPage == 0) {
            searchUserFragment.startSearch(keyWords);
        } else {
            searchActivityFragment.startSearch(keyWords);
        }
    }


}

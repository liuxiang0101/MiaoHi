package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.SearchEnginesData;
import com.haiqiu.miaohi.fragment.SearchContentFragment;
import com.haiqiu.miaohi.fragment.SearchQaFragment;
import com.haiqiu.miaohi.fragment.SearchUserFragment;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.SearchEnginesResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.widget.tablayout.SlidingTabLayout;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索界面--搜索用户/搜索用户
 * Created by zhandalin on 2016/6/23.
 */
public class SearchBodyActivity extends BaseActivity implements View.OnClickListener {
    private int currentPage;
    private int searchType;

    private ViewPager view_pager;
    private EditText et_search_user;
    private SlidingTabLayout tabLayout;
    private LinearLayout ll_clear_et_text;

    private SearchQaFragment searchQaFragment;
    private SearchUserFragment searchUserFragment;
    private SearchContentFragment searchContentFragment;

    private UMShareAPI umShareAPI = null;
    private Gson gson = new Gson();
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_body);
        umShareAPI = UMShareAPI.get(context);
        searchType = getIntent().getIntExtra("searchType", 0);
        //收起键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //获取本地存储的历史搜索记录，此页面搜索时予以保存
        String historyRecord = SpUtils.getString("history_record");
        if (MHStringUtils.isEmpty(historyRecord)) {
            list = new ArrayList<>();
        } else {
            list = gson.fromJson(historyRecord, list.getClass());
        }

        initView();
        initData();
    }

    private void initView() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        et_search_user = (EditText) findViewById(R.id.et_search_user);
        ll_clear_et_text = (LinearLayout) findViewById(R.id.ll_clear_et_text);

        addClickEvent();
        initEditView();
        initViewPager();
    }

    private void initData() {
        String keyWords = getIntent().getExtras().getString("keyWords", "");
        if (MHStringUtils.isEmpty(keyWords)) {
            showToastAtBottom("请输入搜索关键字");
            return;
        }
        et_search_user.setText(keyWords);
        et_search_user.setSelection(keyWords.length());
        startSearch(true);
        final String[] titles = new String[3];
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("search_key_word", Base64Util.getBase64Str(keyWords));
        requestParams.addParams("default_type", "1");
        requestParams.addParams("page_size", "10");
        requestParams.addParams("page_index", "0");
        MHHttpClient.getInstance().post(SearchEnginesResponse.class, context, ConstantsValue.Url.SEARCH_ENGINES, requestParams, new MHHttpHandler<SearchEnginesResponse>() {
            @Override
            public void onSuccess(SearchEnginesResponse response) {
                tabLayout.setVisibility(View.VISIBLE);
                if (null == response) {
                    fillTabTitleWhenError(titles);
                } else {
                    fillTabTitle(response.getData());
                }
                view_pager.setCurrentItem(searchType);
            }

            @Override
            public void onFailure(String content) {
                fillTabTitleWhenError(titles);
            }

        });
    }

    public void fillTabTitle(SearchEnginesData data) {
        String[] titles = new String[3];
        titles[0] = "内容(" + getShowNumber(data.getObject_list_count()) + ")";
        titles[1] = "用户(" + getShowNumber(data.getUser_list_count()) + ")";
        titles[2] = "映答(" + getShowNumber(data.getQuestion_list_count()) + ")";
        tabLayout.setViewPager(view_pager, titles);
    }

    private void fillTabTitleWhenError(String[] titles) {
        titles[0] = "内容(0)";
        titles[1] = "用户(0)";
        titles[2] = "映答(0)";
        tabLayout.setViewPager(view_pager, titles);
    }

    /**
     * 添加点击事件
     */
    private void addClickEvent() {
        et_search_user.setOnClickListener(this);
        ll_clear_et_text.setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(findViewById(R.id.iv_close));
        ll_clear_et_text.setVisibility(et_search_user.getText().length() > 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * EditView的设置
     */
    private void initEditView() {
        et_search_user.setHint("搜索用户");
        //编辑框输入字符变化监听
        et_search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    ll_clear_et_text.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(SearchBodyActivity.this, SearchHistoryRecordActivity.class)
                            .putExtra("searchType", searchType);
                    startActivityNoAnimation(intent);
                    finish();
                }
            }
        });
        //点击软键盘上的搜索时的监听
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
    }

    private String getShowNumber(long num) {
        String str;
        if (num < 100)
            str = num + "";
        else
            str = "99+";
        return str;
    }

    /**
     * ViewPager的设置
     */
    private void initViewPager() {
        //三种类型的搜索内容
        searchUserFragment = new SearchUserFragment();
//        searchUserFragment.startSearch(null);
        searchContentFragment = new SearchContentFragment();
//        searchActivityFragment.startSearch(null);
        searchQaFragment = new SearchQaFragment();
//        searchQaFragment.startSearch(null);
        //viewPager页面改变监听
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                startSearch(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置viewpager适配器
        view_pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return searchContentFragment;
                    case 1:
                        return searchUserFragment;
                    case 2:
                        return searchQaFragment;
                    default:
                        return searchUserFragment;
                }

            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        view_pager.setOffscreenPageLimit(3);
    }

    /**
     * 对关键字进行检索的方法
     *
     * @param showTip
     */
    private void startSearch(boolean showTip) {
        String keyWords = et_search_user.getText().toString().trim();
        if (MHStringUtils.isEmpty(keyWords)) {
            if (showTip)
                showToastAtCenter("请输入搜索内容");
            return;
        }
        switch (currentPage) {
            case 0:
                searchContentFragment.startSearch(keyWords);
                break;
            case 1:
                searchUserFragment.startSearch(keyWords);
                break;
            case 2:
                searchQaFragment.startSearch(keyWords);
                break;
            default:
                searchUserFragment.startSearch(keyWords);
                break;
        }
        saveHistoryRecord(keyWords);
    }

    //将历史记录保存至本地逻辑
    private void saveHistoryRecord(String keyWords) {
        if (list.contains(keyWords)) return;
        if (list.size() >= 10) {
            for (int i = 0; i < 10; i++) {
                if (i < 9)
                    list.set(i, list.get(i + 1));
                else list.set(9, keyWords);
            }
        } else {
            list.add(list.size(), keyWords);
        }
        SpUtils.put("history_record", gson.toJson(list));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_clear_et_text:
                et_search_user.setText("");
                break;
            case R.id.iv_close:
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.et_search_user:
                Intent intent = new Intent(SearchBodyActivity.this, SearchHistoryRecordActivity.class)
                        .putExtra("searchType", searchType);
                intent.putExtra("keyWords", et_search_user.getText().toString().trim());
                startActivityNoAnimation(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && event.getRepeatCount() > 0)
            return true;

        if (KeyEvent.KEYCODE_BACK == keyCode) {
            finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

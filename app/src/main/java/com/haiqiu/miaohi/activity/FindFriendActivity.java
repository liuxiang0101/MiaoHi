package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.UserListAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.UserData;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.UserDataResponse;
import com.haiqiu.miaohi.utils.Base64;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;

import java.util.List;

/**
 * 我的-找好友
 * Created by zhandalin on 2016/6/23.
 */
public class FindFriendActivity extends BaseActivity implements View.OnClickListener {
    private static final int pageSize = 20;

    private EditText et_search_user;
    private PullToRefreshListView pull_to_refresh_listview;
    private int pageIndex;
    private UserListAdapter adapter;
    private ListView listView;
    private String keyWords;
    private View ll_clear_et_text;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        type = getIntent().getIntExtra("type", UserListActivity.USER_LIST_TYPE_ATTENTION);
        initView();
    }

    private void initView() {
        et_search_user = (EditText) findViewById(R.id.et_search_user);
        findViewById(R.id.iv_close).setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(findViewById(R.id.iv_close));
        ll_clear_et_text = findViewById(R.id.ll_clear_et_text);
        ll_clear_et_text.setOnClickListener(this);

        pull_to_refresh_listview = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        pull_to_refresh_listview.setPullLoadEnabled(true);
        pull_to_refresh_listview.setPullRefreshEnabled(false);
        et_search_user.setHint("搜索好友");
        et_search_user.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch();
                    return true;
                }
                return false;
            }

        });
        et_search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ll_clear_et_text.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listView = pull_to_refresh_listview.getRefreshableView();
        listView.setDividerHeight(0);
        listView.setDivider(null);
        pull_to_refresh_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
        View tv_conform = findViewById(R.id.tv_conform);
        tv_conform.setOnClickListener(this);
        if (type == UserListActivity.USER_LIST_TYPE_AT_FRIENDS)
            tv_conform.setVisibility(View.VISIBLE);
        else
            tv_conform.setVisibility(View.GONE);
    }

    private void startSearch() {
        keyWords = et_search_user.getText().toString().trim();
        if (MHStringUtils.isEmpty(keyWords)) {
            showToastAtCenter("请输入搜索内容");
            return;
        }
        hiddenKeyboard();
        pageIndex = 0;
        initData();
    }

    private void initData() {
        if (MHStringUtils.isEmpty(keyWords)) {
            showToastAtCenter("请输入搜索内容");
            return;
        }

        MHRequestParams params = new MHRequestParams();
        params.addParams("search_keyword", Base64.encode(keyWords.getBytes()));
        params.addParams("page_size", pageSize + "");
        params.addParams("page_index", pageIndex + "");
        if (type == UserListActivity.USER_LIST_TYPE_FANS)
            params.addParams("search_type", "2");

        MHHttpClient.getInstance().post(UserDataResponse.class, context, ConstantsValue.Url.SEARCH_FRIEND, params, new MHHttpHandler<UserDataResponse>() {
            @Override
            public void onSuccess(UserDataResponse response) {
                List<UserData> page_result = response.data.page_result;
                if ((null == page_result || page_result.size() == 0) && pageIndex != 0) {
                    pull_to_refresh_listview.onLoadComplete(false);
                    return;
                } else {
                    pull_to_refresh_listview.onLoadComplete(true);
                }
                if (pageIndex == 0) {
                    if (null != page_result && page_result.size() > 0) {
                        if (null == adapter) {
                            adapter = new UserListAdapter(context, page_result, type == UserListActivity.USER_LIST_TYPE_AT_FRIENDS);
                            listView.setAdapter(adapter);
                        } else {
                            adapter.changeList(page_result);
                            listView.setSelection(0);
                        }
                        pull_to_refresh_listview.hideAllTipView();
                    } else {
                        pull_to_refresh_listview.showBlankView();
                    }
                } else {
                    adapter.addData(page_result);
                }
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != adapter) adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_conform:
            case R.id.iv_close:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.ll_clear_et_text:
                et_search_user.setText(null);
                break;
        }
    }
}

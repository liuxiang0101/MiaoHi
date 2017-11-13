package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.SearchSubjectAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.SearchAllPageResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.SearchAllResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请参加专题页面
 * Created by zhandalin on 2016/6/28.
 */
public class JoinInSubjectActivity extends BaseActivity {
    private String activityId = "";
    private String activityName = "";
    private int pageIndex = 0;
    private PullToRefreshListView pulltorefresh_listview;
    private ListView listView;
    private EditText et_search_user;
    private SearchSubjectAdapter adapter;
    private List<SearchAllPageResult> dataList = new ArrayList<>();
    private int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_join_activity);
        initView();
        initData(null);
    }

    private void initView() {
        pulltorefresh_listview = (PullToRefreshListView) findViewById(R.id.pulltorefresh_listview);
        listView = pulltorefresh_listview.getRefreshableView();
        pulltorefresh_listview.setPullRefreshEnabled(false);
        pulltorefresh_listview.setPullLoadEnabled(true);

        et_search_user = (EditText) findViewById(R.id.et_search_user);
        et_search_user.setHint("搜索专题");
        et_search_user.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    submit();
                    return true;
                }
                return false;
            }

        });
        CommonNavigation navigation = (CommonNavigation) findViewById(R.id.navigation);
        navigation.setOnLeftLayoutClickListener(new CommonNavigation.OnLeftLayoutClick() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("activityId", activityId);
                intent.putExtra("activityName", activityName);
                setResult(101, intent);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });

        pulltorefresh_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initData(et_search_user.getText().toString().trim());
            }

            @Override
            public void onLoadMore() {
                initData(et_search_user.getText().toString().trim());
            }

        });
        adapter = new SearchSubjectAdapter(context, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                SearchAllPageResult pageResult = dataList.get(position);
                activityId = pageResult.getActivity_id();
                activityName = pageResult.getActivity_name();
                Intent intent = new Intent();
                intent.putExtra("activityId", activityId);
                intent.putExtra("activityName", activityName);
                setResult(101, intent);
                finish();
            }
        });
    }


    private void initData(String keyWords) {
        MHRequestParams requestParams = new MHRequestParams();
        if (null != keyWords)
            requestParams.addParams("search_keyword", Base64Util.getBase64Str(keyWords));
        requestParams.addParams("page_size", pageSize + "");
        requestParams.addParams("page_index", pageIndex + "");
        requestParams.addParams("type", "1");
        MHHttpClient.getInstance().post(SearchAllResponse.class, context, ConstantsValue.Url.SEARCH_ACTIVITY, requestParams, new MHHttpHandler<SearchAllResponse>() {
            @Override
            public void onSuccess(SearchAllResponse response) {
                ArrayList<SearchAllPageResult> page_result = response.getData().getPage_result();
                if (pageIndex == 0) {
                    dataList.clear();
                    if (null == page_result || page_result.size() == 0) {
                        showToastAtCenter("无搜索结果");
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
                if (null != page_result)
                    dataList.addAll(page_result);
                adapter.notifyDataSetChanged();

                if (null == page_result || page_result.size() == 0)
                    pulltorefresh_listview.onLoadComplete(false);
                else {
                    pulltorefresh_listview.onLoadComplete(true);
                }
                pulltorefresh_listview.hideAllTipView();
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                showToastAtCenter("无搜索结果");
                pulltorefresh_listview.onLoadComplete(true);
                pulltorefresh_listview.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                pulltorefresh_listview.showErrorView();
            }
        });
    }

    @Override
    protected void reTry() {
        super.reTry();
        showMHLoading();
        initData(et_search_user.getText().toString().trim());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        //不允许多选,只会有一个
        ArrayList<String> userIds = data.getStringArrayListExtra("userIds");
        if (null != userIds && userIds.size() > 0) {
            intent.putExtra("inviteUserId", userIds.get(0));
        }
        intent.putExtra("inviteUserName", data.getStringExtra("userNames"));
        intent.putExtra("activityId", activityId);
        intent.putExtra("activityName", activityName);
        setResult(103, intent);
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }


    private void submit() {
        String user = et_search_user.getText().toString().trim();
        if (MHStringUtils.isEmpty(user)) {
            showToastAtCenter("请输入搜索内容");
            return;
        }
        pageIndex = 0;
        initData(user);
    }
}

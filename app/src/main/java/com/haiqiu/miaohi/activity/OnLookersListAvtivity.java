package com.haiqiu.miaohi.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.OvserveUserListAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.ObserveUserListData;
import com.haiqiu.miaohi.bean.ObserveUserListObj;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.ObserveUserListResponse;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * 点赞-围观共用列表页
 * Created by LiuXiang on 2016/9/2.
 */
public class OnLookersListAvtivity extends BaseActivity {
    private final String TAG = "OnLookersListAvtivity";
    private int pageIndex = 0;
    private String question_id;
    private String commend;
    private ListView lv_onlookers;
    private CommonNavigation navigation;
    private List<ObserveUserListObj> list;
    private OvserveUserListAdapter adapter;
    private BroadcastReceiver broadcastReceiver;
    private PullToRefreshListView pulltorefreshs_wipmenulistview;
    private boolean isObserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlookers_list);
        commend = ConstantsValue.Url.GET_PRAISED_USER_LIST;
        isObserve = getIntent().getBooleanExtra("isObserve", false);
        if (isObserve)
            commend = ConstantsValue.Url.QUESTION_OBSERVE_USER_LIST;
        question_id = getIntent().getStringExtra("question_id");
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        list = new ArrayList();
        navigation = (CommonNavigation) findViewById(R.id.navigation);
        pulltorefreshs_wipmenulistview = (PullToRefreshListView) findViewById(R.id.pulltorefreshs_wipmenulistview);
        pulltorefreshs_wipmenulistview.setPullLoadEnabled(true);
        lv_onlookers = pulltorefreshs_wipmenulistview.getRefreshableView();
        initLv();
    }

    private void initLv() {
        pulltorefreshs_wipmenulistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 0;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                getNetData();

            }
        });
        initData();
    }

    private void initData() {
        showMHLoading(true, false);
        getNetData();
    }

    private void getNetData() {
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", "20");
        requestParams.addParams("page_index", "" + pageIndex);
        if (isObserve) {
            requestParams.addParams("question_id", question_id);
        } else {
            requestParams.addParams("type", getIntent().getStringExtra("type"));
            requestParams.addParams("video_id", getIntent().getStringExtra("video_id"));
            requestParams.addParams("photo_id", getIntent().getStringExtra("photo_id"));
        }

        MHHttpClient.getInstance().post(ObserveUserListResponse.class, context, commend, requestParams, new MHHttpHandler<ObserveUserListResponse>() {


            @Override
            public void onSuccess(ObserveUserListResponse response) {
                hiddenLoadingView();
                if (response.getData() == null) return;
                ObserveUserListData data = response.getData();
                if (isObserve) {
                    navigation.setTitle(CommonUtil.formatCount(data.getObserve_count()) + "人围观");
                } else {
                    navigation.setTitle(CommonUtil.formatCount(data.getPraise_count()) + "人点赞");
                }

                //此次加载新一页的数据添加到总集合中
                List<ObserveUserListObj> li = response.getData().getPage_result();
                if (pageIndex == 0 && (null == response.getData() || null == response.getData().getPage_result() || response.getData().getPage_result().size() == 0)) {
                    pulltorefreshs_wipmenulistview.showBlankView();
                } else {
                    pulltorefreshs_wipmenulistview.hideAllTipView();
                }
                if (pageIndex == 0) {
                    list.clear();
                }
                if (null == li || li.size() == 0)
                    pulltorefreshs_wipmenulistview.onLoadComplete(false);
                else {
                    pulltorefreshs_wipmenulistview.onLoadComplete(true);
                }
                if (null != li)
                    list.addAll(li);


                //数据正常，进行显示
                if (adapter == null) {
                    adapter = new OvserveUserListAdapter(OnLookersListAvtivity.this, list, isObserve);
                    lv_onlookers.setAdapter(adapter);
                }
                lv_onlookers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(!isLogin(false))return;
                        Intent intent = new Intent(OnLookersListAvtivity.this, PersonalHomeActivity.class);
                        intent.putExtra("userId", list.get(position).getUser_id());
                        startActivityForResult(intent, 100);
                    }
                });
                adapter.notifyDataSetChanged();
                pageIndex++;
//                SpUtils.put("last_into_time", System.currentTimeMillis() + "");
            }

            @Override
            public void onFailure(String content) {
                pulltorefreshs_wipmenulistview.onLoadComplete(true);
                if (pageIndex == 0) pulltorefreshs_wipmenulistview.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                if (pageIndex == 0) pulltorefreshs_wipmenulistview.showErrorView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != adapter) adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

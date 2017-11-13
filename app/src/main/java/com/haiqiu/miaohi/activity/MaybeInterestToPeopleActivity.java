package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.MaybeInterestToPeopleAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.MaybeInterestUserList;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.MaybeInterestResponse;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.RecyclerViewHeader;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshRecyclerHeaderView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * 可能感兴趣的人
 * Created by ningl on 16/11/29.
 */
public class MaybeInterestToPeopleActivity extends BaseActivity {
    private RecyclerViewHeader rv;
    private PullToRefreshRecyclerHeaderView ptrr;
    private MaybeInterestToPeopleAdapter adapter;

    private int pageIndex = 0;
    private String stringUrl;
    private UMShareAPI mShareAPI = null;
    private List<MaybeInterestUserList> listPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareAPI = UMShareAPI.get(context);
        setContentView(R.layout.activity_maybeinteresttopeople);
        initView();

        if (getIntent().getBooleanExtra("isFromDiscover", false)) {
            stringUrl = ConstantsValue.Url.GET_DISCOVER_INTEREST_USERS;
        } else {
            stringUrl = ConstantsValue.Url.GET_ATTENTION_INTEREST_USERS;
        }
        showLoading();
        getNetData();
    }

    private void initView() {
        listPages = new ArrayList<>();
        ptrr = (PullToRefreshRecyclerHeaderView) findViewById(R.id.ptrr);
        CommonNavigation nav = (CommonNavigation) findViewById(R.id.nav_maybeinterest);
        rv = ptrr.getRefreshableView();
        rv.setItemAnimator(new DefaultItemAnimator());
        //导航栏色彩搭配改变
        nav.setBackgroundResource(R.color.navigation_bg);
        ImageView iv_search = new ImageView(context);
        iv_search.setImageResource(R.drawable.icon_qa_square_search);
        nav.setRightLayoutView(iv_search);
        nav.hideBottomLine();
        nav.setPadding(0, 0, DensityUtil.dip2px(context, 8), 0);

        ptrr.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerViewHeader>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerViewHeader> refreshView) {
                pageIndex = 0;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                getNetData();
            }

        });
        nav.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaybeInterestToPeopleActivity.this, SearchHistoryRecordActivity.class)
                        .putExtra("searchType", 1));
                TCAgent.onEvent(context, "推人页搜索" + ConstantsValue.android);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(context));
    }

    private void getNetData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_index", "" + pageIndex);
        requestParams.addParams("page_size", "20");
        MHHttpClient.getInstance().post(MaybeInterestResponse.class, context, stringUrl, requestParams, new MHHttpHandler<MaybeInterestResponse>() {
            @Override
            public void onSuccess(MaybeInterestResponse response) {
                TCAgent.onEvent(context, "人物展示" + ConstantsValue.android);
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
                ptrr.onLoadComplete(true);

            }

        });
    }

    //分页逻辑
    private void pageLoadLogic(MaybeInterestResponse response) {
        if (pageIndex == 0 && (response == null || null == response.getData().getPage_result().getInterest_user_list() || response.getData().getPage_result().getInterest_user_list().size() == 0)) {
            ptrr.showBlankView();
        } else {
            ptrr.hideAllTipView();
        }


        List<MaybeInterestUserList> listPage;
        if (null != response)
            listPage = response.getData().getPage_result().getInterest_user_list();
        else
            listPage = new ArrayList<>();
        if (null == listPage || listPage.size() == 0)
            ptrr.onLoadComplete(false);
        else {
            ptrr.onLoadComplete(true);
        }

        if (pageIndex == 0) {
            listPages.clear();
        }
        if (null != listPage)
            listPages.addAll(listPage);

        //数据正常，进行显示
        if (adapter == null) {
            adapter = new MaybeInterestToPeopleAdapter(listPages, context);
            rv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        pageIndex++;
    }

    @Override
    protected void onResume() {
        if (null != adapter) adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mShareAPI.onActivityResult(requestCode, resultCode, data);
        }
    }
}

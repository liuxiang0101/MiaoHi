package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.CommonRechargingRecordAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.RechargingRecordItem;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.RechargingRecordResponse;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值记录
 * Created by hackest on 16/8/17.
 */
public class RechargingRecordActivity extends BaseActivity {


    private PullToRefreshListView pull_to_refresh_listview;
    private ListView listView;
    CommonRechargingRecordAdapter adapter;
    private int pageIndex = 0;
    int pageSize = 20;
    List<RechargingRecordItem> recordItemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharging_record);
        initView();
        initData();
    }

    private void initData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", "20");
        requestParams.addParams("page_index", pageIndex + "");
        MHHttpClient.getInstance().post(RechargingRecordResponse.class, ConstantsValue.Url.GET_DEPOSITRECORD, requestParams, new MHHttpHandler<RechargingRecordResponse>() {
            @Override
            public void onSuccess(RechargingRecordResponse response) {
                recordItemList = response.getData().getPage_result();

                if (pageIndex == 0) {
                    if (recordItemList != null && recordItemList.size() > 0) {
                        adapter.appendData(recordItemList, true);
                        pull_to_refresh_listview.hideAllTipView();
                    } else {
                        pull_to_refresh_listview.showBlankView("好尴尬，连根毛都没有...");
                    }
                } else {
                    adapter.appendData(recordItemList, false);
                }
                adapter.update();

                if (null == recordItemList || recordItemList.size() == 0)
                    pull_to_refresh_listview.onLoadComplete(false);
                else {
                    pull_to_refresh_listview.onLoadComplete(true);
                }
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                pull_to_refresh_listview.onLoadComplete(true);
                if (pageIndex == 0)
                    pull_to_refresh_listview.showErrorView("获取充值记录失败了，请重试");

            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                pull_to_refresh_listview.onLoadComplete(true);

                if (pageIndex == 0)
                    pull_to_refresh_listview.showErrorView("获取充值记录失败了，请重试");
            }
        });


    }

    private void initView() {
        pull_to_refresh_listview = (PullToRefreshListView) findViewById(R.id.ptr_recharging_listview);


        listView = pull_to_refresh_listview.getRefreshableView();
        pull_to_refresh_listview.setPullLoadEnabled(true);
        adapter = new CommonRechargingRecordAdapter(context);
        listView.setAdapter(adapter);

        pull_to_refresh_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 0;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }

        });
        pull_to_refresh_listview.showMHLoading();
    }
}

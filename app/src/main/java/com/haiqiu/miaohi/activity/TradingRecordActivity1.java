package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.TradingRecordAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.TradingRecordItem;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.TradingRecordResponse;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 交易记录
 * Created by hackest on 16/8/15.
 */
public class TradingRecordActivity1 extends BaseActivity {
    private final String TAG = "TradingRecordActivity1";
    private int pageIndex = 0;
    private CommonNavigation title_bar;
    private PullToRefreshListView pulltorefreshs_wipmenulistview;
    private ListView lv_trading_record;
    private List<TradingRecordItem> list;
    private TradingRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_record1);
        initView();
        showLoading("加载中...", true, false);
        getNetData();
    }

    private void initView() {
        title_bar = (CommonNavigation) findViewById(R.id.title_bar);
        pulltorefreshs_wipmenulistview = (PullToRefreshListView) findViewById(R.id.pulltorefreshs_wipmenulistview);
        lv_trading_record = pulltorefreshs_wipmenulistview.getRefreshableView();
        list = new ArrayList<>();

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
    }

    private void getNetData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", "20");
        requestParams.addParams("page_index", "" + pageIndex);
        requestParams.addParams("transaction_type", "0");
        MHHttpClient.getInstance().post(TradingRecordResponse.class, context, ConstantsValue.Url.GETTRANSACTIONRECORD, requestParams, new MHHttpHandler<TradingRecordResponse>() {
            @Override
            public void onSuccess(TradingRecordResponse response) {
                //此次加载新一页的数据添加到总集合中
                List<TradingRecordItem> li = response.getData().getPage_result();
                if (pageIndex == 0 && (null == response.getData() || null == response.getData().getPage_result() || response.getData().getPage_result().size() == 0)) {
                    pulltorefreshs_wipmenulistview.showBlankView();
                } else {
                    pulltorefreshs_wipmenulistview.hideAllTipView();
                }
                if (pageIndex == 0) {
                    list.clear();
                }
                if (null != li)
                    list.addAll(li);
                if (null == li || li.size() == 0)
                    pulltorefreshs_wipmenulistview.onLoadComplete(false);
                else {
                    pulltorefreshs_wipmenulistview.onLoadComplete(true);
                }

                //数据正常，进行显示
                if (adapter == null) {
                    adapter = new TradingRecordAdapter(TradingRecordActivity1.this, list);
                    lv_trading_record.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                pulltorefreshs_wipmenulistview.onLoadComplete(true);
                if (pageIndex == 0) pulltorefreshs_wipmenulistview.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                pulltorefreshs_wipmenulistview.onLoadComplete(true);
                if (pageIndex == 0) pulltorefreshs_wipmenulistview.showErrorView();
            }
        });
    }
}

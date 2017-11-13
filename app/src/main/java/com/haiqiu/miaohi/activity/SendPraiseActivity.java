package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.SendPraiseAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.SendPraiseBean;
import com.haiqiu.miaohi.bean.SendPraiseResponse;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 送出赞
 * Created by ningl on 2016/6/30.
 */
public class SendPraiseActivity extends BaseActivity {
    private PullToRefreshListView ptrlv_sendpraise;
    private ListView lv;
    private List<SendPraiseBean> page_result;
    private SendPraiseAdapter adapter;
    private int pageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendpraise);
        initView();
        page_result = new ArrayList<>();
        adapter = new SendPraiseAdapter(this, page_result);
        lv.setAdapter(adapter);
        showLoading();
        getNetData();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra("video_id", page_result.get(position).getVideo_id());
                context.startActivity(intent);
            }
        });
        ptrlv_sendpraise.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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

    private void initView() {
        ptrlv_sendpraise = (PullToRefreshListView) findViewById(R.id.ptrlv_sendpraise);
        lv = ptrlv_sendpraise.getRefreshableView();
        ptrlv_sendpraise.setPullLoadEnabled(true);
    }

    private void getNetData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", "20");
        requestParams.addParams("page_index", String.valueOf(pageIndex));
        MHHttpClient.getInstance().post(SendPraiseResponse.class, ConstantsValue.Url.PRAISESENT, requestParams, new MHHttpHandler<SendPraiseResponse>() {
            @Override
            public void onSuccess(SendPraiseResponse response) {
                hiddenLoadingView();
                List<SendPraiseBean> sendPraiseBeans = response.getData().getPage_result();
                if (sendPraiseBeans == null) return;
                if (pageIndex == 0) {
                    if (sendPraiseBeans.isEmpty()) {
                        ptrlv_sendpraise.showBlankView("还没有送出过赞，快去发现精彩内容吧(￣3￣)");
                    } else {
                        ptrlv_sendpraise.hideAllTipView();
                    }
                    page_result.clear();
                }
                if (null == page_result || page_result.size() == 0)
                    ptrlv_sendpraise.onLoadComplete(false);
                else {
                    ptrlv_sendpraise.onLoadComplete(true);
                }
                page_result.addAll(sendPraiseBeans);
                adapter.notifyDataSetChanged();
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                hiddenLoadingView();
                ptrlv_sendpraise.onLoadComplete(true);
                if (pageIndex == 0) ptrlv_sendpraise.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                hiddenLoadingView();
                ptrlv_sendpraise.onLoadComplete(true);
                if (pageIndex == 0) ptrlv_sendpraise.showErrorView();
            }
        });
    }
}

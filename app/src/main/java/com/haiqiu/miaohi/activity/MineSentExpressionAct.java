package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.MineSentExpressionAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.MineSendFaceBean;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.MineSendFaceResponse;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的-所送表情
 * Created by Guoy on 2016/4/11.
 */
public class MineSentExpressionAct extends BaseActivity {
    private int pageIndex = 0;
    private String user_id = "";
    private List<MineSendFaceBean> list;
    private MineSentExpressionAdapter adapter;
    private PullToRefreshRecyclerView pulltorefresh_recyclerview;
    private RecyclerView xrv_sentexpression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_sentexpression);
        user_id = getIntent().getStringExtra("user_id");
        list = new ArrayList<>();
        adapter = new MineSentExpressionAdapter(context, list);
        pulltorefresh_recyclerview = (PullToRefreshRecyclerView) findViewById(R.id.pulltorefresh_recyclerview);
        xrv_sentexpression = pulltorefresh_recyclerview.getRefreshableView();
        pulltorefresh_recyclerview.setPullLoadEnabled(true);
        xrv_sentexpression.setLayoutManager(new LinearLayoutManager(MineSentExpressionAct.this));
        xrv_sentexpression.setAdapter(adapter);

        pulltorefresh_recyclerview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageIndex = 0;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                getNetData();
            }
        });
    }

    @Override
    protected void onResume() {
        pageIndex = 0;
        showMHLoading();
        getNetData();
        super.onResume();
    }

    /**
     * 请求网络数据
     */
    private void getNetData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", "20");
        requestParams.addParams("user_id", user_id);
        requestParams.addParams("page_index", String.valueOf(pageIndex));
        MHHttpClient.getInstance().post(MineSendFaceResponse.class, context, ConstantsValue.Url.FACESENT, requestParams, new MHHttpHandler<MineSendFaceResponse>() {
            @Override
            public void onSuccess(MineSendFaceResponse response) {
                List<MineSendFaceBean> page_result = response.getData().getPage_result();

                if(pageIndex==0&&(null==response.getData()||null==response.getData().getPage_result())){
                    pulltorefresh_recyclerview.showBlankView();
                }else {
                    pulltorefresh_recyclerview.hideAllTipView();
                }
                if (pageIndex == 0) {
                    list.clear();
                }
                if (null != page_result)
                    list.addAll(page_result);

                adapter.notifyDataSetChanged();
                if (null == page_result || page_result.size() == 0)
                    pulltorefresh_recyclerview.onLoadComplete(false);
                else {
                    pulltorefresh_recyclerview.onLoadComplete(true);
                }
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                pulltorefresh_recyclerview.onLoadComplete(true);
                if(pageIndex==0)pulltorefresh_recyclerview.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                if(pageIndex==0)pulltorefresh_recyclerview.showErrorView();
            }
        });
    }

}

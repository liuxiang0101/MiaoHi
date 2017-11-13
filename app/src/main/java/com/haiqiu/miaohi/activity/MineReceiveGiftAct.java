package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.MineReceiveGiftAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.MineReceiveGiftBean;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.MineReceiveGiftResponse;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 我的-所收礼物
 * Created by Guoy on 2016/4/11.
 */
public class MineReceiveGiftAct extends BaseActivity {
    public static final String TAG = "MineReceiveGiftAct";
    private int pageIndex = 0;
    private String user_id = "";

    private List<MineReceiveGiftBean> list;
    private MineReceiveGiftAdapter adapter;
    private PullToRefreshRecyclerView pulltorefresh_recyclerview;
    private RecyclerView xrv_receivegift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_receivegift);
        user_id = getIntent().getStringExtra("user_id");

        initView();
        clearUnreadMsg();
    }

    private void initView() {
        pulltorefresh_recyclerview = (PullToRefreshRecyclerView) findViewById(R.id.pulltorefresh_recyclerview);
        xrv_receivegift = pulltorefresh_recyclerview.getRefreshableView();
        pulltorefresh_recyclerview.setPullLoadEnabled(true);

        list = new ArrayList<>();
        xrv_receivegift.setLayoutManager(new LinearLayoutManager(this));
        xrv_receivegift.setHasFixedSize(true);
        adapter = new MineReceiveGiftAdapter(context, list);
        xrv_receivegift.setAdapter(adapter);
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

    private void clearUnreadMsg() {
        RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, ConstantsValue.MessageCommend.MSG_RECEIVE_GIFT, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                MHLogUtil.e(TAG, "清除收到礼物的未读消息");
//                RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
//                    @Override
//                    public void onSuccess(Integer integer) {
//                        int totalUnreadCount = integer;
//                        BadgeUtil.setBadgeCount(getApplicationContext(), totalUnreadCount);
//                    }
//
//                    @Override
//                    public void onVideoError(RongIMClient.ErrorCode errorCode) {
//                    }
//                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
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


    private void getNetData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", "20");
        requestParams.addParams("user_id", user_id);
        requestParams.addParams("page_index", String.valueOf(pageIndex));
        MHHttpClient.getInstance().post(MineReceiveGiftResponse.class, ConstantsValue.Url.GIFT_RECEIVED, requestParams, new MHHttpHandler<MineReceiveGiftResponse>() {
            @Override
            public void onSuccess(MineReceiveGiftResponse response) {
                hiddenLoadingView();
                if (pageIndex == 0
                        && null != response.getData()
                        && null != response.getData().getPage_result()
                        && response.getData().getPage_result().isEmpty()) {
                    pulltorefresh_recyclerview.showBlankView("还没有收到礼物，快去发布精彩内容啦(￣3￣)");
                } else {
                    pulltorefresh_recyclerview.hideAllTipView();
                }
                if(null == response.getData()
                        || null == response.getData().getPage_result()) {
                    return;
                }

                SpUtils.put(ConstantsValue.Sp.LAST_GIFT, System.currentTimeMillis() + "");
                List<MineReceiveGiftBean> page_result = response.getData().getPage_result();
                if (pageIndex == 0) {
                    list.clear();
                }
                if (null != page_result)
                    list.addAll(page_result);

                adapter.notifyDataSetChanged();

                if (null == page_result || page_result.size() ==0) {
                    pulltorefresh_recyclerview.onLoadComplete(false);
                }else {
                    pulltorefresh_recyclerview.onLoadComplete(true);
                }
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                hiddenLoadingView();
                pulltorefresh_recyclerview.onLoadComplete(true);
                if (pageIndex == 0) pulltorefresh_recyclerview.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                hiddenLoadingView();
                super.onStatusIsError(message);
                pulltorefresh_recyclerview.onLoadComplete(true);
                if (pageIndex == 0) pulltorefresh_recyclerview.showErrorView();
            }
        });
    }

}

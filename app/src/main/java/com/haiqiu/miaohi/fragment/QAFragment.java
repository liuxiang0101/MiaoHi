package com.haiqiu.miaohi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.WebActivity;
import com.haiqiu.miaohi.adapter.MyQAAdapter;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.QA;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.OpenQAEvent;
import com.haiqiu.miaohi.response.QAResponse;
import com.haiqiu.miaohi.utils.CountDownUtil;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 映答
 * Created by ningl on 16/12/7.
 */
public class QAFragment extends BaseFragment implements CountDownUtil.OnCountDown{

    private PullToRefreshRecyclerView ptrrv_qa;
    private RecyclerView rv;
    private int pageIndex;
    private int qaType;
    private static final int MYANSWER = 0;      //我的回答
    private static final int MYQUESTION = 1;    //我的提问
    private static final int MYCIRCUSEE = 2;    //我的围观
    private TextView tv_qaemptytip;
    private TextView tv_dredgeqa;
    private RelativeLayout rl_nodredgeqa;
    private List<QA> data;
    private MyQAAdapter adapter;
    private CountDownUtil countDownUtil;

    public QAFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        qaType = getArguments().getInt("qaType");
        if(qaType == MYANSWER) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        View rootView = View.inflate(context, R.layout.fragment_qa, null);
        ptrrv_qa = (PullToRefreshRecyclerView) rootView.findViewWithTag("ptrrv_qa");
        rv = ptrrv_qa.getRefreshableView();
        ptrrv_qa.setPullLoadEnabled(true);

        rv.setLayoutManager(new LinearLayoutManager(context));
        data = new ArrayList<>();
        initView(rootView);
        context.showLoading();
        getQaData();
        tv_dredgeqa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开通映答
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("uri", getResources().getString(R.string.open_qa_url));
                intent.putExtra("title", "开通映答");
                startActivity(intent);
            }
        });
        ptrrv_qa.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pageIndex = 0;
                getQaData();
            }

            @Override
            public void onLoadMore() {
                getQaData();
            }
        });
        return rootView;
    }

    /**
     * 获取我的回答数据
     */
    private void getQaData() {
        String qaUrl = null;
        if (qaType == MYANSWER) {
            qaUrl = ConstantsValue.Url.GETALLMYANSEREDQUESTION;
        } else if (qaType == MYQUESTION) {
            qaUrl = ConstantsValue.Url.GETALLMYASKQUESTION;
        } else if (qaType == MYCIRCUSEE) {
            qaUrl = ConstantsValue.Url.GETALLMYOBSERVEDQUESTION;
        }
        MHRequestParams params = new MHRequestParams();
        params.addParams("page_index", String.valueOf(pageIndex));
        params.addParams("page_size", ConstantsValue.Other.PAGESIZE);
        MHHttpClient.getInstance().post(QAResponse.class, context, qaUrl, params, new MHHttpHandler<QAResponse>() {
            @Override
            public void onSuccess(QAResponse response) {
//                pulltorefresh_Recyclerview.hideAllTipView();

                List<QA> pageData = response.getData().getPage_result();
                setData(pageData);
                if (null == pageData || pageData.size() == 0)
                    ptrrv_qa.onLoadComplete(false);
                else {
                    ptrrv_qa.onLoadComplete(true);
                }
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                ptrrv_qa.onLoadComplete(true);
                if (pageIndex == 0)
                    ptrrv_qa.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                ptrrv_qa.onLoadComplete(true);
                if (pageIndex == 0)
                    ptrrv_qa.showErrorView();
            }
        });
    }


    private void setData(List<QA> pageData) {
        if (pageIndex == 0) {
            if (null == pageData || pageData.size() == 0) {
                if (qaType == MYANSWER) {
                    //我的回答
                    if (UserInfoUtil.isAnswerDoit(context)) {
                        //有映答权限
                        ptrrv_qa.showBlankView("还没有回答过问题诶~");
                        rl_nodredgeqa.setVisibility(View.GONE);
                    } else {
                        //无映答权限
                        rl_nodredgeqa.setVisibility(View.VISIBLE);
                    }
                } else {
                    ptrrv_qa.showBlankView();
                }
                return;
            }
            ptrrv_qa.hideAllTipView();
            data.clear();
            data.addAll(pageData);
            if (null == adapter) {
                adapter = new MyQAAdapter(context, data);
                adapter.setQaType(qaType);
                rv.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            ptrrv_qa.hideAllTipView();
            data.addAll(pageData);
            adapter.notifyDataSetChanged();
        }
        startCountDown();
    }

    private void initView(View rootView) {
        tv_qaemptytip = (TextView) rootView.findViewById(R.id.tv_qaemptytip);
        tv_dredgeqa = (TextView) rootView.findViewById(R.id.tv_dredgeqa);
        rl_nodredgeqa = (RelativeLayout) rootView.findViewById(R.id.rl_nodredgeqa);
    }

    /**
     * 倒计时刷新
     */
    public void countDown() {
        if (adapter == null) return;
        adapter.refreshData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != countDownUtil){
            countDownUtil.unRegiste(this);
        }
        if(null == adapter) return;
        adapter.unRegistEvent();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
//            adapter.refreshDraft();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 接收开通映答消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateCountEvent(OpenQAEvent event) {
        if(qaType == MYANSWER){
            rl_nodredgeqa.setVisibility(View.GONE);
            ptrrv_qa.showBlankView("还没有回答过问题诶~");
        }
    }

    @Override
    public void onTick() {
        countDown();
    }

    /**
     * 开始倒计时
     */
    private void startCountDown(){
        if(null == data) return;
        countDownUtil = CountDownUtil.getInstance();
        if(countDownUtil.isContain(this)) return;
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).getTime_remain()<=1000){
                countDownUtil.registe(this);
                countDownUtil.setOnCountDownListener(this);
                return;
            }
        }
        countDownUtil.unRegiste(this);
    }
}

package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.VideoDetailReceiveGiftAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.VideoDetailReceiveGiftBean;
import com.haiqiu.miaohi.bean.VideoDetailReceiveGiftPageResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.VideoDetailReceiveGiftResponse;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频详情所收到的礼物
 * Created by ningl on 16/9/5.
 */
public class VideoDetailReceiveGiftActivity extends BaseActivity {

    private PullToRefreshRecyclerView ptl_videodetailreceivegift;
    private RecyclerView rv;
    private TextView tv_vdreceivegift_count;
    private RelativeLayout rl_vdreceivegift_count;
    private String videoId;
    private int pageIndex;
    private List<VideoDetailReceiveGiftBean> pageResult;
    private VideoDetailReceiveGiftAdapter adapter;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_videodetailreceivegift, null);
        setContentView(view);
        videoId = getIntent().getStringExtra("videoId");
        initView();
        pageResult = new ArrayList<>();
        showLoading();
        getNetData();
        ptl_videodetailreceivegift.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
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

    private void initView() {
        ptl_videodetailreceivegift = (PullToRefreshRecyclerView) view.findViewById(R.id.ptl_videodetailreceivegift);
        tv_vdreceivegift_count = (TextView) findViewById(R.id.tv_vdreceivegift_count);
        rl_vdreceivegift_count = (RelativeLayout) findViewById(R.id.rl_vdreceivegift_count);
        rv = ptl_videodetailreceivegift.getRefreshableView();
        ptl_videodetailreceivegift.setPullLoadEnabled(true);
        ptl_videodetailreceivegift.setAutoLoadMoreIsEnable(true);
        rv.setLayoutManager(new LinearLayoutManager(context));
    }

    /**
     * 获取收到的礼物数据
     */
    private void getNetData(){
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("video_id", videoId);
        requestParams.addParams("page_size", "10");
        requestParams.addParams("page_index", String.valueOf(pageIndex));
        MHHttpClient.getInstance().post(VideoDetailReceiveGiftResponse.class, context, ConstantsValue.Url.GIFTRECEIVEBYVIDEO, requestParams, new MHHttpHandler<VideoDetailReceiveGiftResponse>() {
            @Override
            public void onSuccess(VideoDetailReceiveGiftResponse response) {
                List<VideoDetailReceiveGiftBean> pageResult = response.getData().getPage_result();
                VideoDetailReceiveGiftPageResult videoDetailReceiveGiftPageResult = response.getData();

                if (videoDetailReceiveGiftPageResult == null || videoDetailReceiveGiftPageResult.getPage_result() == null) {
                    if(pageResult.isEmpty()&&pageIndex == 0){
                        ptl_videodetailreceivegift.showBlankView();
                    }
                    return;
                }else {
                    ptl_videodetailreceivegift.hideAllTipView();
                }
                rl_vdreceivegift_count.setVisibility(View.VISIBLE);
                tv_vdreceivegift_count.setText("此视频收到 "+videoDetailReceiveGiftPageResult.getGift_count()+" 个礼物");
                setData(pageResult);

                if (null == pageResult || pageResult.size() == 0)
                    ptl_videodetailreceivegift.onLoadComplete(false);
                else {
                    ptl_videodetailreceivegift.onLoadComplete(true);
                }
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                ptl_videodetailreceivegift.onLoadComplete(true);
                if(pageResult.isEmpty()){
                    ptl_videodetailreceivegift.showErrorView();
                    rl_vdreceivegift_count.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                ptl_videodetailreceivegift.onLoadComplete(true);
                if(pageResult.isEmpty()){
                    ptl_videodetailreceivegift.showErrorView();
                    rl_vdreceivegift_count.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 设置数据
     * @param pageResult
     */
    private void setData(List<VideoDetailReceiveGiftBean> pageResult) {
        if (null == pageResult)
            return;
        if (pageIndex == 0) {
            this.pageResult.clear();
            this.pageResult.addAll(pageResult);
            if (null == adapter) {
                adapter =  new VideoDetailReceiveGiftAdapter(context, this.pageResult);
                rv.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            this.pageResult.addAll(pageResult);
            adapter.notifyDataSetChanged();
        }
    }
}

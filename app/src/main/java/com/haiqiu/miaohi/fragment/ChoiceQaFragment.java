package com.haiqiu.miaohi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.ChoiceQaAdapter;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.ChoiceQaData;
import com.haiqiu.miaohi.bean.ChoiceQaPageResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.ChoiceQaResponse;
import com.haiqiu.miaohi.widget.CarouselImageView;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuXiang on 2016/12/6.
 * 精选映答tab页
 */
public class ChoiceQaFragment extends BaseFragment {
    private int pageIndex = 0;
    private ListView listView;
    private ChoiceQaAdapter adapter;
    private List<ChoiceQaPageResult> listPage;
    private CarouselImageView carouselImageView;
    private PullToRefreshListView pulltorefresh_listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choice_qa, container, false);
        initView(view);
        initListView();
        getNetData();
        return view;
    }

    private void initView(View view) {
        listPage = new ArrayList<>();
        pulltorefresh_listview = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_listview);
        listView = pulltorefresh_listview.getRefreshableView();
        listView.setDividerHeight(0);
        pulltorefresh_listview.setPullRefreshEnabled(true);
        pulltorefresh_listview.setPullLoadEnabled(true);
        carouselImageView = new CarouselImageView(getActivity());
    }

    private void initListView() {
        pulltorefresh_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
        requestParams.addParams("page_index", "" + pageIndex);
        requestParams.addParams("page_size", "20");
        MHHttpClient.getInstance().post(ChoiceQaResponse.class, context, ConstantsValue.Url.TOP_QUESTION_RECOMMEND, requestParams, new MHHttpHandler<ChoiceQaResponse>() {
            @Override
            public void onSuccess(ChoiceQaResponse response) {
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
                pulltorefresh_listview.onLoadComplete(true);
                if (pageIndex == 0) pulltorefresh_listview.showErrorView();
            }

        });
    }

    private void pageLoadLogic(ChoiceQaResponse response) {
        ChoiceQaData data = response.getData();
        if (pageIndex == 0 && (data == null || null == data.getPage_result() || data.getPage_result().size() == 0)) {
            pulltorefresh_listview.showBlankView();
        } else {
            pulltorefresh_listview.hideAllTipView();
        }
        //轮播图
        if (data.getBanner_result() == null) {
            if (listView.getHeaderViewsCount() > 0)
                listView.removeHeaderView(carouselImageView);
        } else if (data.getBanner_result().size() == 0) {
            if (listView.getHeaderViewsCount() > 0)
                listView.removeHeaderView(carouselImageView);
        } else {
            if (listView.getHeaderViewsCount() < 1)
                listView.addHeaderView(carouselImageView);
            carouselImageView.initData(data.getBanner_result());
        }

        List<ChoiceQaPageResult> li = data.getPage_result();
        if (pageIndex == 0) {
            listPage.clear();
        }
        if (null != li)
            listPage.addAll(li);
        if (null == li || li.size() == 0)
            pulltorefresh_listview.onLoadComplete(false);
        else
            pulltorefresh_listview.onLoadComplete(true);


        //数据正常，进行显示
        if (adapter == null && pageIndex == 0) {
            adapter = new ChoiceQaAdapter(listPage, getActivity());
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        pageIndex++;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if (null != carouselImageView) carouselImageView.onResume();
        } else {
            //相当于Fragment的onPause
            if (null != carouselImageView) carouselImageView.onPause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != carouselImageView) carouselImageView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != carouselImageView) carouselImageView.onResume();
    }
}

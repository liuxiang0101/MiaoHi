package com.haiqiu.miaohi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.activity.UserListActivity;
import com.haiqiu.miaohi.adapter.UserListAdapter;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.UserData;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.UserDataResponse;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;

import java.util.List;

/**
 * Created by zhandalin on 2016-12-22 18:39.
 * 说明:一般展示粉丝的Fragment
 */
public class CommonUserFragment extends BaseFragment {
    private final static int PAGE_SIZE = 40;
    private int pageIndex = 0;
    private UserListAdapter userListAdapter;
    private PullToRefreshListView pull_to_refresh_listview;
    private ListView listview;
    private int type;
    private String userId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pull_to_refresh_listview = new PullToRefreshListView(context);
        listview = pull_to_refresh_listview.getRefreshableView();
        listview.setDividerHeight(0);
        listview.setDivider(null);
        pull_to_refresh_listview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        pull_to_refresh_listview.setPullLoadEnabled(true);
        pull_to_refresh_listview.setAutoLoadMoreBackwardPosition(10);
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
        return pull_to_refresh_listview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pull_to_refresh_listview.showMHLoading();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != userListAdapter) userListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onMyResume() {
        super.onMyResume();
        if (null != userListAdapter) userListAdapter.notifyDataSetChanged();
    }

    /**
     * @param type   UserListActivity.USER_LIST_TYPE_FANS 与UserListActivity.USER_LIST_TYPE_ATTENTION
     * @param userId 如果当前用户是自己那就传null,切记
     */
    public void setInitParam(int type, String userId) {
        this.type = type;
        this.userId = userId;
    }

    private void initData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("other_user", userId);
        requestParams.addParams("page_size", PAGE_SIZE + "");
        requestParams.addParams("page_index", pageIndex + "");

        String url = ConstantsValue.Url.USER_ATTENTION_OWN;
        if (UserListActivity.USER_LIST_TYPE_FANS == type) {
            url = ConstantsValue.Url.USER_FANS_OWN;
        }
        MHHttpClient.getInstance().post(UserDataResponse.class, url, requestParams, new MHHttpHandler<UserDataResponse>() {
            @Override
            public void onSuccess(UserDataResponse response) {
                if (pageIndex == 0 && (null == response.data || null == response.data.page_result || response.data.page_result.size() == 0)) {
                    if (type == UserListActivity.USER_LIST_TYPE_FANS) {
                        context.showBlankView("还没有粉丝…");
                    } else {
                        context.showBlankView("还没关注人诶～");
                    }
                } else {
                    if (context instanceof UserListActivity) {
                        ((UserListActivity) context).setTabVisibility(View.VISIBLE);
                    }
                    pull_to_refresh_listview.hideAllTipView();
                }
                List<UserData> page_result = response.data.page_result;
                if (null == page_result || page_result.size() == 0)
                    pull_to_refresh_listview.onLoadComplete(false);
                else {
                    pull_to_refresh_listview.onLoadComplete(true);
                }

                if (pageIndex == 0) {
                    if (userListAdapter == null) {
                        userListAdapter = new UserListAdapter(context, page_result, type == UserListActivity.USER_LIST_TYPE_AT_FRIENDS);
                        listview.setAdapter(userListAdapter);
                    } else {
                        userListAdapter.changeList(page_result);
                    }
                    listview.setSelection(0);
                } else {
                    userListAdapter.addData(page_result);
                }

                hasInited = true;
                pageIndex++;
                context.hiddenLoadingView();
            }

            @Override
            public void onFailure(String content) {
                pull_to_refresh_listview.onLoadComplete(true);
                if (pageIndex == 0) {
                    pull_to_refresh_listview.showErrorView();
                }
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                pull_to_refresh_listview.onLoadComplete(true);
                if (pageIndex == 0) {
                    pull_to_refresh_listview.showErrorView();
                }
            }
        });
    }

}

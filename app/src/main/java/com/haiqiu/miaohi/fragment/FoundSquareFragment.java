package com.haiqiu.miaohi.fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.MaybeInterestToPeopleActivity;
import com.haiqiu.miaohi.activity.QaSquareActivity;
import com.haiqiu.miaohi.activity.SearchHistoryRecordActivity;
import com.haiqiu.miaohi.adapter.FoundSquareRecycleViewAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.DiscoveryLabelObj;
import com.haiqiu.miaohi.bean.DiscoveryObj;
import com.haiqiu.miaohi.bean.DiscoveryObjectObj;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.DiscoveryResponse;
import com.haiqiu.miaohi.response.GetObjByLabelResponse;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshRecyclerView;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 首页-发现广场
 * Created by LiuXiang on 2016/12/5.
 */
public class FoundSquareFragment extends BaseFragment implements View.OnClickListener {
    private ArrayList<DiscoveryLabelObj> listLabel;
    private List<DiscoveryObjectObj> listObj = new ArrayList<>();
    private HashMap<String, String> labelMap = new HashMap<>();

    private int pageIndex = 0;
    private String currentKindTag;                              //当前分类后台记录的tag值
    private String currentCommend;                              //当前分类后台记录的commend值

    private BaseActivity baseActivity;

    private RecyclerView recyclerView;
    private PullToRefreshRecyclerView pulltorefresh_rv;
    private RelativeLayout rl_suspension;                       //页面滑动时的悬浮分类条
    private LinearLayout ll_select_sort;                        //可点击的发现分类区域
    private TextView tv_sort_name;                              //当前被选中的分类名称

    private FoundSquareRecycleViewAdapter rvAdapter;            //网格布局适配器
    private GridLayoutManager gridLayoutManager;                //recycleView布局管理器
    private BroadcastReceiver broadcastReceiver;                //广播接收

    private boolean isHasRefresh = true;
    public static boolean isNeedRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播接收器
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null == pulltorefresh_rv || null == intent) return;
                if (ConstantsValue.IntentFilterAction.LOGIN_SUCCESS_ACTION.equals(intent.getAction())) {
                    recyclerView.smoothScrollToPosition(0);
                    pulltorefresh_rv.doPullRefreshing(true);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsValue.IntentFilterAction.LOGIN_SUCCESS_ACTION);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found_square, null);
        initView(view);
        pulltorefresh_rv.showMHLoading();
        getNetData();
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        baseActivity = (BaseActivity) getActivity();
        tv_sort_name = (TextView) view.findViewById(R.id.tv_sort_name);
        rl_suspension = (RelativeLayout) view.findViewById(R.id.rl_suspension);
        ll_select_sort = (LinearLayout) view.findViewById(R.id.ll_select_sort);
        pulltorefresh_rv = (PullToRefreshRecyclerView) view.findViewById(R.id.pulltorefresh_rv);
        recyclerView = pulltorefresh_rv.getRefreshableView();

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        //设置合并
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position > 3)
                    return 1;
                else if (position < 0)
                    return 0;
                else
                    return 3;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        ll_select_sort.setOnClickListener(this);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(view.findViewById(R.id.iv_close));
        view.findViewById(R.id.et_search_user).setOnClickListener(this);
        view.findViewById(R.id.et_search_user).setFocusable(false);

        addEvent();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void addEvent() {
        pulltorefresh_rv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //刷新时，请求默认的数据
                pageIndex = 0;
                getNetData();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "findingfeed_more");
                    jsonObject.put("description", "发现下拉更多");
                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                }
            }

            @Override
            public void onLoadMore() {
                    changeLabel(currentKindTag, currentCommend, false);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取最后一个可见view的位置
//                int lastItemPosition = gridLayoutManager.findLastVisibleItemPosition();
                //获取第一个可见view的位置
                int firstItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
                if (firstItemPosition < 3) {
                    rl_suspension.setVisibility(View.INVISIBLE);
                } else {
                    rl_suspension.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (null != rvAdapter)
                if (null != rvAdapter.getCarouselImageView())
                    rvAdapter.getCarouselImageView().onPause();
        } else {
            TCAgent.onPageStart(context, "发现");
            if (null != rvAdapter)
                if (null != rvAdapter.getCarouselImageView())
                    rvAdapter.getCarouselImageView().onResume();
            if (isNeedRefresh) {
                pageIndex = 0;
                getNetData();
                isNeedRefresh = false;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        TCAgent.onPageStart(context, "发现");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_select_sort:                   //点击分类
                clickSort();
                break;
            case R.id.et_search_user:                   //点击搜索框
                context.startActivity(new Intent(getActivity(), SearchHistoryRecordActivity.class));
                context.overridePendingTransition(0, 0);
                break;
            case R.id.iv_close:                         //点击映答图标
                context.startActivity(new Intent(getActivity(), QaSquareActivity.class));
                break;
            case R.id.tv_discover_more:                 //点击发现更多
                Intent intent = new Intent(getActivity(), MaybeInterestToPeopleActivity.class);
                intent.putExtra("isFromDiscover", true);
                context.startActivity(intent);
                TCAgent.onEvent(context, "发现推人发现更多" + ConstantsValue.android);
                break;
        }
    }

    public void clickSort() {
        if (listLabel != null) {
            if (listLabel.size() > 9) {
                SortDialog sortDialog = new SortDialog();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", listLabel);
                bundle.putString("kind_tag", currentKindTag);
                bundle.putString("commend", currentCommend);
                sortDialog.setArguments(bundle);
                sortDialog.show(getChildFragmentManager(), "SortDialog");
            } else {
                SortDialogNew sortDialog1 = new SortDialogNew();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", listLabel);
                bundle.putString("kind_tag", currentKindTag);
                bundle.putString("commend", currentCommend);
                sortDialog1.setArguments(bundle);
                sortDialog1.show(getChildFragmentManager(), "SortDialog1");
            }
        }
    }

    /**
     * 获取默认的数据
     */
    private void getNetData() {
        //这里首次调用时kind_tag和kind_command为空
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_index", "0");
        requestParams.addParams("page_size", "21");
        requestParams.addParams("kind_tag", currentKindTag == null ? "" : currentKindTag);
        requestParams.addParams("kind_command", currentCommend == null ? "" : currentCommend);
        MHHttpClient.getInstance().post(DiscoveryResponse.class, context, ConstantsValue.Url.DISCOVERY, requestParams, new MHHttpHandler<DiscoveryResponse>() {
            @Override
            public void onSuccess(DiscoveryResponse response) {
                //返回为空时显示空数据页
                if (response == null || response.getData() == null) {
                    pulltorefresh_rv.showBlankView();
                    return;
                } else {
                    pulltorefresh_rv.hideAllTipView();
                }
                DiscoveryObj object = response.getData().getPage_result();
                //获取分类标签列表数据
                ArrayList<DiscoveryLabelObj> list = (ArrayList) object.getLabel_list();
                listLabel = list;
                if (list.size() > 0 && isHasRefresh) {
                    currentKindTag = list.get(0).getKind_tag();
                    currentCommend = list.get(0).getKind_Command();
                    tv_sort_name.setText(list.get(0).getKind_name());
                    isHasRefresh = false;
                }
                for (DiscoveryLabelObj obj : list) {
                    labelMap.put(obj.getKind_tag(), obj.getKind_name());
                }
                ll_select_sort.setVisibility(View.VISIBLE);
                if (list == null || list.size() == 0) {
                    ll_select_sort.setVisibility(View.INVISIBLE);
                }

                //网格布局视图
                listObj.clear();
                listObj.addAll(response.getData().getPage_result().getObject_list());
                if (rvAdapter == null) {
                    rvAdapter = new FoundSquareRecycleViewAdapter(getActivity(), object.getBanner_list(), object.getInterest_user_list(), listObj);
                    recyclerView.setAdapter(rvAdapter);
                } else {
                    rvAdapter.notifyDataSetChanged();
                }
                rvAdapter.setVideoList(listObj);
                rvAdapter.setFragment(FoundSquareFragment.this);
                if (null != rvAdapter.getTv_sort_name())
                    rvAdapter.getTv_sort_name().setText(tv_sort_name.getText());
                pulltorefresh_rv.onLoadComplete(true);

                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                pulltorefresh_rv.onLoadComplete(true);
                pulltorefresh_rv.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                pulltorefresh_rv.onLoadComplete(true);
                pulltorefresh_rv.showErrorView();
            }
        });
    }

    /**
     * 根据label切换不同列表数据
     */
    public void changeLabel(String kindTag, String commend, boolean isReturnToZero) {
        if (isReturnToZero) {
            pulltorefresh_rv.showMHLoading();
            pageIndex = 0;
            recyclerView.scrollToPosition(3);
        }
        if (MHStringUtils.isEmpty(currentCommend) || MHStringUtils.isEmpty(currentKindTag)) {
            baseActivity.hiddenLoadingView();
            pulltorefresh_rv.onLoadComplete(false);
            return;
        }
        tv_sort_name.setText(labelMap.get(kindTag));
        if (null != rvAdapter)
            if (null != rvAdapter.getTv_sort_name())
                rvAdapter.getTv_sort_name().setText(labelMap.get(kindTag));
        currentCommend = commend;
        currentKindTag = kindTag;
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_index", "" + pageIndex);
        requestParams.addParams("page_size", "21");
        requestParams.addParams("kind_command", currentCommend);
        requestParams.addParams("kind_tag", currentKindTag);
        MHHttpClient.getInstance().post(GetObjByLabelResponse.class, context, ConstantsValue.Url.GET_OBJECT_LIST_BY_LABEL, requestParams, new MHHttpHandler<GetObjByLabelResponse>() {
            @Override
            public void onSuccess(GetObjByLabelResponse response) {
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
            }
        });
    }

    private void pageLoadLogic(GetObjByLabelResponse response) {
        if (pageIndex == 0 && (response.getData() == null || null == response.getData().getPage_result() || response.getData().getPage_result().getObject_list().size() == 0)) {

        } else {
            pulltorefresh_rv.hideAllTipView();
        }

        List<DiscoveryObjectObj> li = response.getData().getPage_result().getObject_list();
        if (pageIndex == 0) {
            listObj.clear();
        }
        if (null != li)
            listObj.addAll(li);
        if (null == li || li.size() == 0)
            pulltorefresh_rv.onLoadComplete(false);
        else {
            pulltorefresh_rv.onLoadComplete(true);
        }

        rvAdapter.notifyDataSetChanged();
        rvAdapter.setVideoList(listObj);
        pageIndex++;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != rvAdapter) {
            rvAdapter.notifyItemChanged(2);
            if (null != rvAdapter.getCarouselImageView())
                rvAdapter.getCarouselImageView().onResume();
        }
    }

    @Override
    protected void onMyResume() {
        super.onMyResume();
        if (null != rvAdapter)
            if (null != rvAdapter.getCarouselImageView())
                rvAdapter.getCarouselImageView().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != rvAdapter)
            if (null != rvAdapter.getCarouselImageView())
                rvAdapter.getCarouselImageView().onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //该fragment销毁时接触注册的广播
        if (null != broadcastReceiver)
            context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        if (null != recyclerView)
            recyclerView.smoothScrollToPosition(0);
        if (null != pulltorefresh_rv)
            pulltorefresh_rv.doPullRefreshing(true);
    }
}

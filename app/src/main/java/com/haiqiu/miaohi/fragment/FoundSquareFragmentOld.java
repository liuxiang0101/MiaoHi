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
import android.support.v7.widget.LinearLayoutManager;
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
import com.haiqiu.miaohi.adapter.FoundRvAdapter;
import com.haiqiu.miaohi.adapter.FoundSquareRvAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.DiscoveryLabelObj;
import com.haiqiu.miaohi.bean.DiscoveryObj;
import com.haiqiu.miaohi.bean.DiscoveryObjectObj;
import com.haiqiu.miaohi.bean.DiscoveryUserObj;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.DiscoveryResponse;
import com.haiqiu.miaohi.response.GetObjByLabelResponse;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.widget.CarouselImageView;
import com.haiqiu.miaohi.widget.RecyclerViewHeader;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshRecyclerHeaderView;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.haiqiu.miaohi.R.id.ll_select_sort1;
import static com.haiqiu.miaohi.R.id.ll_select_sort2;


/**
 * 首页-发现广场
 * Created by LiuXiang on 2016/12/5.
 */
public class FoundSquareFragmentOld extends BaseFragment implements View.OnClickListener {
    private ArrayList<DiscoveryLabelObj> listLabel;
    private int pageIndex = 0;
    private String currentKindTag;
    private String currentCommend;
    private View view;
    private RelativeLayout rl;
    private RelativeLayout rl_card;
    private RecyclerView recyclerview;
    private PullToRefreshRecyclerHeaderView pull_rv_header;
    private RecyclerViewHeader recyclerViewHeader;
    private CarouselImageView carouselImageView;
    private FoundRvAdapter foundRvAdapter;                                      //网格布局适配器
    private List<DiscoveryObjectObj> listObj;                                    //网格布局填充数据所用集合
    private FoundSquareRvAdapter rvAdapter;                                      //横向的卡片布局适配器
    private List<DiscoveryUserObj> listUser;                                     //横向的卡片布局填充数据所用集合
    private HashMap<String, String> labelMap = new HashMap<>();
    private BaseActivity baseActivity;
    private TextView tv_sort_name1;
    private TextView tv_sort_name2;
    private GridLayoutManager gridLayoutManager;
    private View headerViewCover;
    private boolean isHasRefresh = true;
    private boolean isHasAutoLoad = true;
    private BroadcastReceiver broadcastReceiver;
    private LinearLayout llSelectSort1;
    private LinearLayout llSelectSort2;
    public static boolean isNeedRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null == pull_rv_header || null == intent) return;
                if (ConstantsValue.IntentFilterAction.LOGIN_SUCCESS_ACTION.equals(intent.getAction())) {
                    recyclerview.smoothScrollToPosition(0);
                    pull_rv_header.doPullRefreshing(true);
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
        view = inflater.inflate(R.layout.fragment_square, null);
        initView(view);
        pull_rv_header.showMHLoading();
        getNetData();
        return view;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        listLabel = new ArrayList<>();
        listObj = new ArrayList<>();
        baseActivity = (BaseActivity) getActivity();
        rl = (RelativeLayout) view.findViewById(R.id.rl);
        llSelectSort1 = (LinearLayout) view.findViewById(R.id.ll_select_sort1);
        rl.setVisibility(View.INVISIBLE);
        tv_sort_name1 = (TextView) view.findViewById(R.id.tv_sort_name1);
        pull_rv_header = (PullToRefreshRecyclerHeaderView) view.findViewWithTag("pull_rv_header_tag");
        pull_rv_header.setPullLoadEnabled(true);
        recyclerViewHeader = pull_rv_header.getRefreshableView();

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewHeader.setLayoutManager(gridLayoutManager);

        View headerView = View.inflate(getActivity(), R.layout.header_square, null);
        headerViewCover = View.inflate(getActivity(), R.layout.header_square_cover, null);
        recyclerViewHeader.addHeaderView(headerView);
        recyclerViewHeader.addHeaderView(headerViewCover);
        recyclerview = (RecyclerView) headerView.findViewById(R.id.horizontal_recyclerview);
        rl_card = (RelativeLayout) headerView.findViewById(R.id.rl_card);
        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        carouselImageView = (CarouselImageView) headerView.findViewById(R.id.carousel_imageview);
        tv_sort_name2 = (TextView) headerViewCover.findViewById(R.id.tv_sort_name2);
        llSelectSort2 = (LinearLayout) headerViewCover.findViewById(R.id.ll_select_sort2);

        llSelectSort1.setOnClickListener(this);
        llSelectSort2.setOnClickListener(this);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(view.findViewById(R.id.iv_close));
        view.findViewById(R.id.et_search_user).setOnClickListener(this);
        view.findViewById(R.id.et_search_user).setFocusable(false);
        headerView.findViewById(R.id.tv_discover_more).setOnClickListener(this);

        addEvent();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void addEvent() {
        pull_rv_header.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerViewHeader>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerViewHeader> refreshView) {
                //刷新时，请求默认的数据
                pageIndex = 0;
                getNetData();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "findingfeed_more");
                    jsonObject.put("description", "发现下拉更多");
                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                } catch (Exception e) {

                    MHLogUtil.e(TAG, e);
                }
            }

            @Override
            public void onLoadMore() {
                changeLabel(currentKindTag, currentCommend, false);
            }
        });
        recyclerViewHeader.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取最后一个可见view的位置
                int lastItemPosition = gridLayoutManager.findLastVisibleItemPosition();
                //获取第一个可见view的位置
                int firstItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
                if (firstItemPosition < 1) {
                    rl.setVisibility(View.INVISIBLE);
                } else {
                    rl.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            TCAgent.onPageEnd(context, "发现");
            if (null != carouselImageView) carouselImageView.onPause();
        } else {
            TCAgent.onPageStart(context, "发现");
            if (null != carouselImageView) carouselImageView.onResume();
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
            case ll_select_sort1:               //点击分类
            case ll_select_sort2:
                if (listLabel != null)
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
                break;
            case R.id.et_search_user:               //点击搜索框
                context.startActivity(new Intent(getActivity(), SearchHistoryRecordActivity.class));
                context.overridePendingTransition(0, 0);
                break;
            case R.id.iv_close:                     //点击映答图标
                context.startActivity(new Intent(getActivity(), QaSquareActivity.class));
                break;
            case R.id.tv_discover_more:             //点击发现更多
                Intent intent = new Intent(getActivity(), MaybeInterestToPeopleActivity.class);
                intent.putExtra("isFromDiscover", true);
                context.startActivity(intent);
                TCAgent.onEvent(context, "发现推人发现更多" + ConstantsValue.android);
                break;
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
                    pull_rv_header.showBlankView();
                    return;
                } else {
                    pull_rv_header.hideAllTipView();
                }
                DiscoveryObj object = response.getData().getPage_result();
                //获取分类标签列表数据
                ArrayList<DiscoveryLabelObj> list = (ArrayList) object.getLabel_list();
                listLabel = list;
                if (list.size() > 0 && isHasRefresh) {
                    currentKindTag = list.get(0).getKind_tag();
                    currentCommend = list.get(0).getKind_Command();
                    tv_sort_name1.setText(list.get(0).getKind_name());
                    tv_sort_name2.setText(list.get(0).getKind_name());
                    isHasRefresh = false;
                }
                for (DiscoveryLabelObj obj : list) {
                    labelMap.put(obj.getKind_tag(), obj.getKind_name());
                }
                llSelectSort1.setVisibility(View.VISIBLE);
                llSelectSort2.setVisibility(View.VISIBLE);
                if (list == null || list.size() == 0) {
                    llSelectSort1.setVisibility(View.INVISIBLE);
                    llSelectSort2.setVisibility(View.INVISIBLE);
                }

                //banner轮播图
                if (object.getBanner_list() == null)
                    carouselImageView.setVisibility(View.GONE);
                else if (object.getBanner_list().size() == 0)
                    carouselImageView.setVisibility(View.GONE);
                else {
                    carouselImageView.setVisibility(View.VISIBLE);
                    carouselImageView.initData(object.getBanner_list());
                }

                //横向卡片布局视图
                rl_card.setVisibility(View.VISIBLE);
                listUser = object.getInterest_user_list();
                if (listUser.size() > 0) {
//                if (rvAdapter == null) {
                    rvAdapter = new FoundSquareRvAdapter(listUser, getActivity());
                    recyclerview.setAdapter(rvAdapter);
//                } else {
//                    rvAdapter.notifyDataSetChanged();
//                }
                    TCAgent.onEvent(context, "发现推人展示" + ConstantsValue.android);
                } else {
                    rl_card.setVisibility(View.GONE);
                }
                //网格布局视图
                listObj.clear();
                listObj.addAll(response.getData().getPage_result().getObject_list());
                if (foundRvAdapter == null) {
                    foundRvAdapter = new FoundRvAdapter(listObj, getActivity());
                    recyclerViewHeader.setAdapter(foundRvAdapter);
                } else {
                    foundRvAdapter.notifyDataSetChanged();
                }
                foundRvAdapter.setVideoList(listObj);
                pull_rv_header.onLoadComplete(true);

                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                pull_rv_header.onLoadComplete(true);
                pull_rv_header.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                pull_rv_header.onLoadComplete(true);
                pull_rv_header.showErrorView();
            }
        });
    }

    /**
     * 根据label切换不同列表数据
     *
     * @param kindTag
     */
    public void changeLabel(String kindTag, String commend, boolean isReturnToZero) {
        if (isReturnToZero) {
            pull_rv_header.showMHLoading();
            pageIndex = 0;
        }
        if (MHStringUtils.isEmpty(currentCommend) || MHStringUtils.isEmpty(currentKindTag)) {
            baseActivity.hiddenLoadingView();
            pull_rv_header.onLoadComplete(false);
            isHasAutoLoad = true;
            return;
        }
        tv_sort_name1.setText(labelMap.get(kindTag));
        tv_sort_name2.setText(labelMap.get(kindTag));
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
                isHasAutoLoad = true;
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
                isHasAutoLoad = true;
            }

            @Override
            public void onStatusIsError(String message) {
                isHasAutoLoad = true;
                super.onStatusIsError(message);
            }
        });
    }

    private void pageLoadLogic(GetObjByLabelResponse response) {
        if (pageIndex == 0 && (response.getData() == null || null == response.getData().getPage_result() || response.getData().getPage_result().getObject_list().size() == 0)) {

        } else {
            pull_rv_header.hideAllTipView();
        }

        List<DiscoveryObjectObj> li = response.getData().getPage_result().getObject_list();
        if (pageIndex == 0) {
            listObj.clear();
        }
        if (null != li)
            listObj.addAll(li);
        if (null == li || li.size() == 0)
            pull_rv_header.onLoadComplete(false);
        else {
            pull_rv_header.onLoadComplete(true);
        }


        //数据正常，进行显示
        if (foundRvAdapter == null) {
            foundRvAdapter = new FoundRvAdapter(listObj, getActivity());
            recyclerViewHeader.setAdapter(foundRvAdapter);
        } else
            foundRvAdapter.notifyDataSetChanged();
        foundRvAdapter.setVideoList(listObj);
        pageIndex++;
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (null != foundRvAdapter) foundRvAdapter.notifyDataSetChanged();
        if (null != rvAdapter) rvAdapter.notifyDataSetChanged();
        if (null != carouselImageView) carouselImageView.onResume();
    }

    @Override
    protected void onMyResume() {
        super.onMyResume();
//        if (null != foundRvAdapter) foundRvAdapter.notifyDataSetChanged();
        if (null != rvAdapter) rvAdapter.notifyDataSetChanged();
        if (null != carouselImageView) carouselImageView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != carouselImageView) carouselImageView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != broadcastReceiver)
            context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        if (null != recyclerview)
            recyclerview.smoothScrollToPosition(0);
        if (null != pull_rv_header)
            pull_rv_header.doPullRefreshing(true);
    }
}

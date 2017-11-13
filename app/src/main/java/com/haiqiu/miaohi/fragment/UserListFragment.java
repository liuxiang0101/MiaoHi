package com.haiqiu.miaohi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.UserListActivity;
import com.haiqiu.miaohi.adapter.UserStickyAdapter;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.UserData;
import com.haiqiu.miaohi.bean.UserGroupInfo;
import com.haiqiu.miaohi.db.UserInfoManager;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.widget.LetterSelectorView;
import com.haiqiu.miaohi.widget.MySectionIndexer;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshStickyHeaderListView;
import com.haiqiu.miaohi.widget.stickyheaderListView.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhandalin on 2016-12-19 16:27.
 * 说明:用户列表,有些临界点没有处理,比如1万条全是#分组,一般情况不影响使用
 */
public class UserListFragment extends BaseFragment {
    private final static int pageSize = 500;//分页大小
    private final static int maxDataSize = 10000;//最大的数据量如果大于这个数就采用分配加载

    private StickyListHeadersListView mListView;
    private LetterSelectorView mLetterListView;
    private Handler handler = new Handler();
    private UserStickyAdapter userListAdapter;
    private List<UserGroupInfo> allGroup;
    //默认是展示粉丝,会根据表名来区分是什么页面
    private String tableName = ConstantsValue.DB.TABLE_NAME_ATTENTION;
    private int currentBottomIndex;//用户索引,是指集合结尾的数据在整体数据中的位置
    private int currentTopIndex;//用户索引,是指集合开始的数据在整体数据中的位置
    private int currentSpecialGroupIndex;//#的索引,是指集合结尾的数据在整体数据中的位置

    private int autoLoadMoreBackwardPosition = pageSize / 3;
    private int lastNextPosition = -1;
    private int lastPrePosition = -1;
    private MySectionIndexer mIndexer;
    private int totalUserCount;
    private int specialGroupIndex;
    private List<UserData> userDataList = new ArrayList<>();
    private PullToRefreshStickyHeaderListView pull_to_refresh_stickyheader_listview;
    private int type;
    private BroadcastReceiver broadcastReceiver;
    private boolean hasLoad;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, null);
        pull_to_refresh_stickyheader_listview = (PullToRefreshStickyHeaderListView) view.findViewById(R.id.pull_to_refresh_stickyheader_listview);
        pull_to_refresh_stickyheader_listview.setAutoLoadMoreIsEnable(false);
        pull_to_refresh_stickyheader_listview.setPullRefreshEnabled(false);
        pull_to_refresh_stickyheader_listview.setPullLoadEnabled(false);
        mListView = pull_to_refresh_stickyheader_listview.getRefreshableView();
        mListView.setEmptyView(View.inflate(context, R.layout.list_group_item, null));
        mListView.setDrawingListUnderStickyHeader(true);
        mListView.setAreHeadersSticky(true);
        mListView.setDividerHeight(1);

        mLetterListView = (LetterSelectorView) view.findViewById(R.id.mLetterListView);
        mIndexer = new MySectionIndexer();
        return view;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userListAdapter = new UserStickyAdapter(context, userDataList, type == UserListActivity.USER_LIST_TYPE_AT_FRIENDS);
        mListView.setAdapter(userListAdapter);

        pull_to_refresh_stickyheader_listview.showMHLoading();
        initData();
        initListener();
        UserInfoManager.syncData(context.getApplicationContext());
    }

    private void initListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                mIndexer.setCurrentTopIndex(currentTopIndex);
//                MHLogUtil.d(TAG, "firstVisibleItem=" + firstVisibleItem + "---visibleItemCount=" + visibleItemCount + "---totalItemCount=" + totalItemCount + "--currentBottomIndex=" + currentBottomIndex);

                int tempPosition = firstVisibleItem + visibleItemCount + autoLoadMoreBackwardPosition;

                if (tempPosition == totalItemCount && tempPosition != lastNextPosition) {//保证只是执行一次
                    lastNextPosition = tempPosition;
                    getNextPageData();
                }

                tempPosition = firstVisibleItem - autoLoadMoreBackwardPosition;
                if (tempPosition == 0 && tempPosition != lastPrePosition) {
                    lastPrePosition = tempPosition;
                    getPrePageData();
                }
                if (tempPosition > autoLoadMoreBackwardPosition) {
                    lastPrePosition = tempPosition;
                }

            }
        });
        mLetterListView.setOnItemClickListener(new LetterSelectorView.OnItemClickListener() {
            @Override
            public void onItemClick(String content, int position) {
                if (content != null) {
                    int index = mIndexer.getPositionForSection(position);
                    MHLogUtil.d(TAG, "content=" + content + ",position=" + position + ",index=" + index);
                    if (position != -1) {
                        int count = userListAdapter.getCount();
                        if (count < totalUserCount) {//表示是由于分批加载数据没加载到所点击的位置,现在要做特殊处理
                            selectedItem(index);
                        } else {
                            mListView.setSelection(index);
                        }
                    }
                }
            }
        });
    }

    public void setInitParam(String tableName, int type) {
        if (null == tableName) return;
        this.type = type;
        this.tableName = tableName;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                totalUserCount = UserInfoManager.getUserCount(context.getApplication(), tableName);
                allGroup = UserInfoManager.getAllGroup(context.getApplicationContext(), tableName);
                mLetterListView.setAllGroup(allGroup);
                mIndexer.setUserGroupInfo(allGroup);

                if (allGroup.size() > 0) {
                    UserGroupInfo groupInfo = allGroup.get(allGroup.size() - 1);
                    if ("#".equals(groupInfo.getGroupName())) {
                        specialGroupIndex = mIndexer.getPositionForSection(allGroup.size() - 1);
                    }
                }

                if (totalUserCount <= maxDataSize) {//数据量小于分页大小的10倍就直接全部加载
                    MHLogUtil.d(TAG, "全部加载");
                    for (UserGroupInfo groupInfo : allGroup) {
                        List<UserData> userDataList = UserInfoManager.getUserInfos(tableName, context.getApplicationContext(), groupInfo.getGroupName(), null);
                        addData(userDataList, UserStickyAdapter.AddDataType.DEFAULT);
                    }
                    if (totalUserCount <= 0) {
                        pull_to_refresh_stickyheader_listview.showBlankView();
                    }
                } else {
                    getNextPageData();
                    //Test
//                    List<UserData> userDataList;
//                    do {
//                        userDataList = UserInfoManager.getUserInfos(tableName, context.getApplicationContext(), null, currentIndex + "," + pageSize);
//                        if (null == userDataList) break;
//                        MHLogUtil.d(TAG, "分批加载--currentIndex=" + currentIndex + "--userDataList.size=" + userDataList.size());
//                        currentIndex += userDataList.size();
//                        addData(userDataList,-1);
//                    } while (userDataList.size() >= pageSize);
//
//                    //把特殊的分组加到最后面
//                    userDataList = UserInfoManager.getUserInfos(tableName, context.getApplicationContext(), "#", null);
//                    addData(userDataList,-1);
                }
                if (null == broadcastReceiver) {//第一次初始化完成后再注册,防止加载错乱
                    broadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            MHLogUtil.d(TAG, "onReceive--action=" + intent.getAction());
                            if (userListAdapter.getCount() <= 0) {
                                MHLogUtil.d(TAG, "加载完成--初始化数据" + intent.getAction());
                                hasLoad = true;
                                initData();
                            }
                        }
                    };
                    String action = ConstantsValue.IntentFilterAction.SYNC_ATTENTION_DATA_SUCCESS;
                    if (ConstantsValue.DB.TABLE_NAME_FANS.equals(tableName)) {
                        action = ConstantsValue.IntentFilterAction.SYNC_FANS_DATA_SUCCESS;
                    }
                    context.registerReceiver(broadcastReceiver, new IntentFilter(action));
                }
            }
        }).start();
    }

    /**
     * 向下加载,并分页
     */
    private synchronized void getNextPageData() {
        if (totalUserCount <= maxDataSize) {
            MHLogUtil.d(TAG, "getNextPageData--已经全部加载--不再加载数据");
            return;
        }
        MHLogUtil.d(TAG, "getNextPageData");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<UserData> userDataList;
                if (specialGroupIndex != 0 && currentBottomIndex >= specialGroupIndex) {//这个表示是到了#组了,特殊处理
                    userDataList = UserInfoManager.getUserInfos(tableName, context.getApplicationContext(), "#", currentSpecialGroupIndex + "," + pageSize);
                    currentSpecialGroupIndex += userDataList.size();
                    addData(userDataList, UserStickyAdapter.AddDataType.DEFAULT);
                    MHLogUtil.d(TAG, "向下分批加载--currentIndex=" + currentBottomIndex + "--userDataList.size=" + userDataList.size() + "---currentSpecialGroupIndex=" + currentSpecialGroupIndex);
                } else {
                    userDataList = UserInfoManager.getUserInfos(tableName, context.getApplicationContext(), null, currentBottomIndex + "," + pageSize);
                    currentBottomIndex += userDataList.size();
                    addData(userDataList, UserStickyAdapter.AddDataType.DEFAULT);
                    MHLogUtil.d(TAG, "向下分批加载--currentIndex=" + currentBottomIndex + "--userDataList.size=" + userDataList.size());
                }
            }
        }).start();
    }

    /**
     * 向上加载,并分页
     */
    private synchronized void getPrePageData() {
        if (totalUserCount <= maxDataSize) {
            MHLogUtil.d(TAG, "getPrePageData--已经全部加载--不再加载数据");
            return;
        }
        MHLogUtil.d(TAG, "-----getPrePageData----");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //如果是#组会有问题
                currentTopIndex = currentBottomIndex - userListAdapter.getCount() - pageSize;
                int tempSize = pageSize;
                if (currentTopIndex < pageSize) {
                    tempSize = currentBottomIndex - userListAdapter.getCount();
                    currentTopIndex = 0;
                }
                MHLogUtil.d(TAG, "--向上--分批加载--currentTopIndex=" + currentTopIndex + "---tempSize=" + tempSize);
                if (tempSize <= 0) return;

                final List<UserData> userDataList = UserInfoManager.getUserInfos(tableName, context.getApplicationContext(), null, currentTopIndex + "," + tempSize);
                MHLogUtil.d(TAG, "--向上--分批加载--currentTopIndex=" + currentTopIndex + "--userDataList.size=" + userDataList.size());
                currentTopIndex -= userDataList.size();
                addData(userDataList, UserStickyAdapter.AddDataType.FIRST);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setSelection(userDataList.size() + autoLoadMoreBackwardPosition);
                    }
                });
            }
        }).start();
    }

    private void selectedItem(final int position) {
        if (totalUserCount <= maxDataSize) {
            MHLogUtil.d(TAG, "已经全部加载--不再加载数据");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int startIndex = position - pageSize;
                if (startIndex < 0) startIndex = 0;

                currentBottomIndex = position;
                List<UserData> userDataList;
                if (specialGroupIndex != 0 && position >= specialGroupIndex) {//这个表示是最后一组了,特殊处理
                    //拼接数据
                    currentSpecialGroupIndex = 0;
                    userDataList = UserInfoManager.getUserInfos(tableName, context.getApplicationContext(), null, startIndex + "," + pageSize);
                    addData(userDataList, UserStickyAdapter.AddDataType.CLEAR);
                    userDataList = UserInfoManager.getUserInfos(tableName, context.getApplicationContext(), "#", 0 + "," + pageSize);
                    addData(userDataList, UserStickyAdapter.AddDataType.DEFAULT);
                } else {
                    userDataList = UserInfoManager.getUserInfos(tableName, context.getApplicationContext(), null, startIndex + "," + (pageSize * 2));
                    addData(userDataList, UserStickyAdapter.AddDataType.CLEAR);
                    currentBottomIndex = position + pageSize;
                    currentTopIndex = currentBottomIndex - userListAdapter.getCount();
                    if (currentTopIndex < pageSize) {
                        currentTopIndex = 0;
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (position == 0)
                            mListView.setSelection(0);
                        else
                            mListView.setSelection(pageSize);
                    }
                });
            }
        }).start();
    }

    private synchronized void addData(final List<UserData> userDataList, final UserStickyAdapter.AddDataType addDataType) {
        if (null == userDataList || userDataList.size() == 0) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                userListAdapter.addData(userDataList, addDataType);
                userListAdapter.notifyDataSetChanged();
                pull_to_refresh_stickyheader_listview.hideAllTipView();
                currentTopIndex = currentBottomIndex - userListAdapter.getCount();
                if (currentTopIndex < pageSize) {
                    currentTopIndex = 0;
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(broadcastReceiver);
    }
}

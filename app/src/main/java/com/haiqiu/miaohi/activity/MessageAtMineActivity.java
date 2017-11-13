package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.MessageAtMineAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.ChangeMsgStateBean;
import com.haiqiu.miaohi.bean.PushMsgNewFriendObj;
import com.haiqiu.miaohi.bean.PushedMsgResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.PushedMsgResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 消息-@我的
 * Created by Guoy on 2016/4/11.
 */
public class MessageAtMineActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private int pageIndex = 0;
    private List<PushedMsgResult> list_page;
    private List<PushMsgNewFriendObj> list_msg;
    private MessageAtMineAdapter adapter;
    private ListView lv_message_at_mine;
    private PullToRefreshListView pulltorefreshs_listview;
    private CommonNavigation commonnavigation;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_common);
        setResult(4, new Intent());

        initView();
        initData();
        registerForContextMenu(lv_message_at_mine);
    }

    private void initView() {
        list_page = new ArrayList<>();
        list_msg = new ArrayList<>();
        pulltorefreshs_listview = (PullToRefreshListView) findViewById(R.id.pulltorefreshs_listview);
        commonnavigation = (CommonNavigation) findViewById(R.id.commonnavigation);
        commonnavigation.setTitle(getString(R.string.title_msg_at_me));
        pulltorefreshs_listview.setPullLoadEnabled(true);
        lv_message_at_mine = pulltorefreshs_listview.getRefreshableView();
        lv_message_at_mine.setDividerHeight(0);
        initLv();
    }

    private void initLv() {
        pulltorefreshs_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        pulltorefreshs_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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

    protected void initData() {
        pulltorefreshs_listview.showMHLoading();
        //获取服务端数据
        getNetData();
        //清除融云未读消息
        clearUnreadMsg();
        //改变该未读消息的状态
        changeMsgState(1, 0);
    }

    /**
     * 清除未读消息
     */
    private void clearUnreadMsg() {
        RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, "pmsgreceiveatme", new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        MHLogUtil.e(TAG, "----clear atme unread message----");
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        MHLogUtil.e(TAG, "----clear message error----");
                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    /**
     * 更改消息状态值
     */
    private void changeMsgState(int i, int position) {
        List<ChangeMsgStateBean> jArray = new ArrayList();
        MHRequestParams requestParams = new MHRequestParams();
        if (i == 0) {
            jArray.add(new ChangeMsgStateBean(list_page.get(position).getMsg_id(), "-1"));
            requestParams.addParams("push_msg", new Gson().toJson(jArray));
        } else {
            requestParams.addParams("msg_command", ConstantsValue.MessageCommend.MSG_AT_ME);
            requestParams.addParams("msg_state", "20");
//            requestParams.addParams("time_millis", ""+System.currentTimeMillis());
        }
        MHHttpClient.getInstance().post(PushedMsgResponse.class, context, i == 0 ? ConstantsValue.Url.SET_PUSHED_MSG_STSTE : ConstantsValue.Url.SET_PUSHED_MSG_STSTE_BY_TIME, requestParams, new MHHttpHandler<PushedMsgResponse>() {
            @Override
            public void onSuccess(PushedMsgResponse response) {
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }


    /**
     * 服务端获取数据
     */
    private void getNetData() {
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("msg_command", "pmsgreceiveatme");
        requestParams.addParams("page_index", "" + pageIndex);
        requestParams.addParams("page_size", "20");
        MHHttpClient.getInstance().post(PushedMsgResponse.class, ConstantsValue.Url.GET_PUSHED_MSG, requestParams, new MHHttpHandler<PushedMsgResponse>() {
            @Override
            public void onSuccess(PushedMsgResponse response) {
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
                pulltorefreshs_listview.onLoadComplete(true);
                if (pageIndex == 0) pulltorefreshs_listview.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                showToastAtCenter(message);
                pulltorefreshs_listview.onLoadComplete(true);
                if (pageIndex == 0) pulltorefreshs_listview.showErrorView();
            }
        });
    }

    /**
     * 分页处理逻辑
     *
     * @param response
     */
    private void pageLoadLogic(PushedMsgResponse response) {
        if (pageIndex == 0 && (response.getData() == null || null == response.getData().getMsg_result() || response.getData().getMsg_result().size() == 0)) {
            pulltorefreshs_listview.showBlankView();
        } else {
            pulltorefreshs_listview.hideAllTipView();
        }

        List<PushedMsgResult> li = response.getData().getMsg_result();
        //异步任务解析加密的历史消息
        AnalysisMsg analysisMsg = new AnalysisMsg();
        analysisMsg.execute(li);
        List<PushMsgNewFriendObj> li_msg = null;
        try {
            li_msg = (List<PushMsgNewFriendObj>) analysisMsg.get();
        } catch (InterruptedException e) {
            MHLogUtil.e(TAG,e);
        } catch (ExecutionException e) {
            MHLogUtil.e(TAG,e);
        }
        if (null == li || li.size() == 0)
            pulltorefreshs_listview.onLoadComplete(false);
        else {
            pulltorefreshs_listview.onLoadComplete(true);
        }

        if (pageIndex == 0) {
            list_page.clear();
            list_msg.clear();
        }
        if (li != null) {
            list_page.addAll(li);
            list_msg.addAll(li_msg);
        }

        //数据正常，进行显示
        if (adapter == null) {
            adapter = new MessageAtMineAdapter(this, list_page, list_msg);
            lv_message_at_mine.setAdapter(adapter);
        }
        lv_message_at_mine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.intoVideoDetail(list_msg.get(position).getData());
            }
        });
        adapter.notifyDataSetChanged();
        pageIndex++;
    }

    class AnalysisMsg extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            List<PushedMsgResult> li = (List<PushedMsgResult>) params[0];
            List<PushMsgNewFriendObj> li_msg = new ArrayList<>();
            for (int i = 0; i < li.size(); i++) {
                //解析加密字符串
                String content = li.get(i).getMsg_content();
                content = Base64Util.getFromBase64(content);
                //获取加密字符内的bean类
                PushMsgNewFriendObj response = gson.fromJson(content, PushMsgNewFriendObj.class);
                li_msg.add(response);
            }
            return li_msg;
        }

        @Override
        protected void onPostExecute(Object o) {
            hiddenLoadingView();
            super.onPostExecute(o);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.clearHeader();
        menu.add(0, 1, Menu.NONE, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 1:
                changeMsgState(0, menuInfo.position);
                list_page.remove(menuInfo.position);
                list_msg.remove(menuInfo.position);
                adapter.notifyDataSetChanged();
                if (list_page.size() <= 0)
                    pulltorefreshs_listview.showBlankView();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterForContextMenu(lv_message_at_mine);
        super.onDestroy();
    }
}

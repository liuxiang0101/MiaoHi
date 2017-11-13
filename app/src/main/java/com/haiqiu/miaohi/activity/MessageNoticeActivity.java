package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.MessageNoticeAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.ChangeMsgStateBean;
import com.haiqiu.miaohi.bean.PushMsgReceiveZanObj;
import com.haiqiu.miaohi.bean.PushedMsgResult;
import com.haiqiu.miaohi.bean.UserAttentionStatusObj;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.MsgQaResponse;
import com.haiqiu.miaohi.response.PushedMsgResponse;
import com.haiqiu.miaohi.response.UserAttentionStatusResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 消息-通知(收到的赞+新的好友)
 * Created by LiuXiang on 2016/12/01
 */
public class MessageNoticeActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private int pageIndex = 0;
    private boolean isFirst = true;
    private List userIdList = new ArrayList();
    private List<PushedMsgResult> list;
    private List<PushMsgReceiveZanObj> list_msg;
    private MessageNoticeAdapter adapter;
    private ListView lv_message_receive_expression;
    private PullToRefreshListView pulltorefreshs_listview;
    private CommonNavigation commonnavigation;
    private Gson gson = new Gson();
    private HashMap<String, Boolean> userStatusMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_common);

        setResult(5, new Intent());

        initView();
        initData();
        this.registerForContextMenu(lv_message_receive_expression);
    }

    @Override
    protected void onResume() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        super.onResume();
    }

    private void initView() {
        list = new ArrayList<>();
        list_msg = new ArrayList<>();
        pulltorefreshs_listview = (PullToRefreshListView) findViewById(R.id.pulltorefreshs_listview);
        pulltorefreshs_listview.setPullLoadEnabled(true);
        lv_message_receive_expression = pulltorefreshs_listview.getRefreshableView();
        lv_message_receive_expression.setDividerHeight(0);
        initLv();
        commonnavigation = (CommonNavigation) findViewById(R.id.commonnavigation);
        commonnavigation.setTitle(getString(R.string.title_notify));
    }

    private void initLv() {
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
        getNetData();
        clearUnreadMsg();
        changeMsgState(1, 0);
    }

    private void clearUnreadMsg() {
        RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, "pmsgreceivepraisevideo", new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                MHLogUtil.e(TAG, "----clear receive-zan unread message----");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MHLogUtil.e(TAG, "----clear message error----");
            }
        });
        RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, ConstantsValue.MessageCommend.MSG_NEW_FRIEND, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                MHLogUtil.e(TAG, "----clear receive-zan unread message----");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MHLogUtil.e(TAG, "----clear message error----");
            }
        });
    }

    private void getNetData() {
        final MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("msg_command", "pmsgreceivepraisevideo");
        requestParams.addParams("msg_command", "pmsgnotification");
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
        List<PushedMsgResult> li = response.getData().getMsg_result();

        if (pageIndex == 0 && (response.getData() == null || null == response.getData().getMsg_result() || response.getData().getMsg_result().size() == 0)) {
            pulltorefreshs_listview.showBlankView();
        } else {
            pulltorefreshs_listview.hideAllTipView();
        }

        List<PushMsgReceiveZanObj> li_msg = new ArrayList<>();
        List userIdLi = new ArrayList();
        for (int i = 0; i < li.size(); i++) {
            PushMsgReceiveZanObj data = gson.fromJson(Base64Util.getFromBase64(li.get(i).getMsg_content()), PushMsgReceiveZanObj.class);
            li_msg.add(data);
            userIdLi.add(data.getData().getSender_id());
        }
        if (pageIndex == 0) {
            list.clear();
            list_msg.clear();
            userIdList.clear();
        }
        if (null != li) {
            list.addAll(li);
            list_msg.addAll(li_msg);
        }
        userIdList.addAll(userIdLi);
        if (adapter != null)
            adapter.notifyDataSetChanged();
        getAttentionState(li);
        pageIndex++;
    }

    private void getAttentionState(final List<PushedMsgResult> li) {
        if (pageIndex == 0 && isFirst) {
            pulltorefreshs_listview.showMHLoading();
            isFirst = false;
        }
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("user_list", gson.toJson(userIdList));
        MHHttpClient.getInstance().post(UserAttentionStatusResponse.class, ConstantsValue.Url.GET_USERS_ATTENTION_STATUS, requestParams, new MHHttpHandler<UserAttentionStatusResponse>() {
            @Override
            public void onSuccess(UserAttentionStatusResponse response) {
                List<UserAttentionStatusObj> listUserAttenttion = response.getData().getPage_result();
                for (UserAttentionStatusObj obj : listUserAttenttion) {
                    userStatusMap.put(obj.getUser_id(), obj.isAttention_state());
                }
                //数据正常，进行显示
                if (adapter == null) {
                    adapter = new MessageNoticeAdapter(MessageNoticeActivity.this, list, list_msg, userStatusMap);
                    lv_message_receive_expression.setAdapter(adapter);
                }
                lv_message_receive_expression.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.intoDetail(list_msg.get(position));
                    }
                });
                adapter.notifyDataSetChanged();
                if (null == li || li.size() == 0)
                    pulltorefreshs_listview.onLoadComplete(false);
                else
                    pulltorefreshs_listview.onLoadComplete(true);
                hiddenLoadingView();
            }

            @Override
            public void onFailure(String content) {
                pulltorefreshs_listview.onLoadComplete(true);
            }

            @Override
            public void onStatusIsError(String message) {
                pulltorefreshs_listview.onLoadComplete(true);
                super.onStatusIsError(message);
            }
        });
    }

    /**
     * 更改消息状态值
     */
    private void changeMsgState(final int i, int position) {
        List<ChangeMsgStateBean> jArray = new ArrayList();
        MHRequestParams requestParams = new MHRequestParams();
        if (i == 0) {
            jArray.add(new ChangeMsgStateBean(list.get(position).getMsg_id(), "-1"));
            requestParams.addParams("push_msg", new Gson().toJson(jArray));
        } else {
            requestParams.addParams("msg_command", ConstantsValue.MessageCommend.MSG_RECEIVE_ZAN);
            requestParams.addParams("msg_state", "20");
//            requestParams.addParams("time_millis", ""+System.currentTimeMillis());
        }
        MHHttpClient.getInstance().post(PushedMsgResponse.class, context, i == 0 ? ConstantsValue.Url.SET_PUSHED_MSG_STSTE : ConstantsValue.Url.SET_PUSHED_MSG_STSTE_BY_TIME, requestParams, new MHHttpHandler<PushedMsgResponse>() {
            @Override
            public void onSuccess(PushedMsgResponse response) {
                if (i == 0) showToastAtBottom("消息已删除");
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    class AnalysisMsg extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            List<PushedMsgResult> li = (List<PushedMsgResult>) params[0];
            List<MsgQaResponse> li_msg = new ArrayList<>();
            for (int i = 0; i < li.size(); i++) {
                //解析加密字符串
                String content = li.get(i).getMsg_content();
                content = Base64Util.getFromBase64(content);
                //获取加密字符内的bean类
                MsgQaResponse response = gson.fromJson(content, MsgQaResponse.class);
                String commond = response.getBase().getCommand();
                li_msg.add(response);
            }
            return li_msg;
        }
    }

    //重写onCreateContextMenu方法
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.setHeaderTitle("选择操作");
        menu.clearHeader();
        menu.add(0, 1, Menu.NONE, "删除");
    }

    //重写onContextItemSelected方法,只实现了删除列表项的功能，其他功能另行添加
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 1:
                //删除列表项...
                changeMsgState(0, menuInfo.position);
                list.remove(menuInfo.position);
                list_msg.remove(menuInfo.position);
                adapter.notifyDataSetChanged();
                if (list_msg.size() <= 0)
                    pulltorefreshs_listview.showBlankView();

                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterForContextMenu(lv_message_receive_expression);
        super.onDestroy();
    }
}

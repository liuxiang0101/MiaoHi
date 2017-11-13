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
import com.haiqiu.miaohi.adapter.MessageSystemAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.ChangeMsgStateBean;
import com.haiqiu.miaohi.bean.PushVo;
import com.haiqiu.miaohi.bean.PushedMsgResult;
import com.haiqiu.miaohi.bean.SystemMsgObj;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.MsgContentResponse;
import com.haiqiu.miaohi.response.PushedMsgResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 消息-系统消息
 * Created by Guoy on 2016/4/11.
 */
public class MessageSystemActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private int pageIndex = 0;

    private List<PushedMsgResult> list;
    private List<SystemMsgObj> list_msg;
    private CommonNavigation commonnavigation;
    private MessageSystemAdapter adapter;
    private ListView lv_message_system;
    private PullToRefreshListView pulltorefreshs_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_common);

        setResult(1, new Intent());

        initView();
        initData();
        registerForContextMenu(lv_message_system);
    }

    protected void initView() {
        list = new ArrayList<>();
        list_msg = new ArrayList<>();
        pulltorefreshs_listview = (PullToRefreshListView) findViewById(R.id.pulltorefreshs_listview);
        pulltorefreshs_listview.setPullLoadEnabled(true);
        lv_message_system = pulltorefreshs_listview.getRefreshableView();
        commonnavigation = (CommonNavigation) findViewById(R.id.commonnavigation);
        commonnavigation.setTitle(getString(R.string.title_msg_system));
        initLv();
    }

    private void initLv() {
//        为listview添加侧滑菜单
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//            @Override
//            public void create(SwipeMenu menu) {
//                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
//                deleteItem.setWidth(DensityUtil.dip2px(MessageSystemActivity.this, 80));
//                deleteItem.setIcon(R.drawable.ic_delete);
//                menu.addMenuItem(deleteItem);
//            }
//        };
//        lv_message_system.setMenuCreator(creator);          //设置listView的侧滑菜单
//        lv_message_system.smoothOpenMenu(0);                //此方法用来禁止出现侧滑删除
//        lv_message_system.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        changeMsgState(0, position);
//                        list.remove(position);
//                        adapter.notifyDataSetChanged();
//                        showToastAtBottom("消息已删除");
//                        break;
//                }
//            }
//        });
        lv_message_system.setDividerHeight(0);
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

        lv_message_system.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                SystemMsgObj object = list_msg.get(position);
                String msgType = object.getObjectType();
                String objectId = object.getObjectId();
                if (msgType.equals("1")) {
                    intent.setClass(context, VideoAndImgActivity.class);
                    ArrayList<VideoAndImg> data = new ArrayList<>();
                    VideoAndImg obj = new VideoAndImg();
                    obj.setElement_type(1);
                    obj.setContent_type(1);
                    obj.setPhoto_id(objectId);
                    obj.setVideo_id(objectId);
                    data.add(obj);
                    intent.putParcelableArrayListExtra("data", data)
                            .putExtra("isNeedBack", true)
                            .putExtra("currentIndex", 0)
                            .putExtra("userId", objectId)
                            .putExtra("pageIndex", 0)
                            .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
                } else if (msgType.equals("2")) {
                    intent.setClass(context, WebViewActivity.class);
                    intent.putExtra("uri", object.getActivity_uri());
                    intent.putExtra("title", object.getActivity_name());
                    intent.putExtra("activity_note", object.getObjectNote());
                    intent.putExtra("activity_picture", object.getActivity_picture());
                } else if (msgType.equals("3")) {
                    intent.setClass(context, InterlocutionDetailsActivity.class);
                    intent.putExtra("question_id", objectId);
                } else if (msgType.equals("4")) {
                    if (!isLogin(false)) return;
                    intent.setClass(context, PersonalHomeActivity.class);
                    intent.putExtra("userId", objectId);
                } else if (msgType.equals("6")) {
                    intent.setClass(context, VideoAndImgActivity.class);
                    ArrayList<VideoAndImg> data = new ArrayList<>();
                    VideoAndImg obj = new VideoAndImg();
                    obj.setElement_type(2);
                    obj.setContent_type(2);
                    obj.setPhoto_id(objectId);
                    obj.setVideo_id(objectId);
                    data.add(obj);
                    intent.putParcelableArrayListExtra("data", data)
                            .putExtra("isNeedBack", true)
                            .putExtra("currentIndex", 0)
                            .putExtra("userId", objectId)
                            .putExtra("pageIndex", 0)
                            .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
                } else {
                    return;
                }
                if (intent != null)
                    context.startActivity(intent);
            }
        });
    }

    private void initData() {
        pulltorefreshs_listview.showMHLoading();
        getNetData();
        clearUnreadMsg();
        changeMsgState(1, 0);
    }

    private void clearUnreadMsg() {
        RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, "pmsgreceivesystem", new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                MHLogUtil.e(TAG, "----clear system unread message----");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MHLogUtil.e(TAG, "----clear message error----");
            }
        });
    }

    private void getNetData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("msg_command", "pmsgreceivesystem");
        requestParams.addParams("page_index", "" + pageIndex);
        requestParams.addParams("page_size", "20");
        MHHttpClient.getInstance().post(PushedMsgResponse.class, context, ConstantsValue.Url.GET_PUSHED_MSG, requestParams, new MHHttpHandler<PushedMsgResponse>() {
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
        List<SystemMsgObj> li_msg = null;
        try {
            li_msg = (List<SystemMsgObj>) analysisMsg.get();
        } catch (InterruptedException e) {
            MHLogUtil.e(TAG,e);
        } catch (ExecutionException e) {
            MHLogUtil.e(TAG,e);
        }

        if (pageIndex == 0) {
            list.clear();
            list_msg.clear();
        }
        if (null != li) {
            list.addAll(li);
            list_msg.addAll(li_msg);
        }
        if (li.size() == 0)
            pulltorefreshs_listview.onLoadComplete(false);
        else {
            pulltorefreshs_listview.onLoadComplete(true);
        }

        //数据正常，进行显示
        if (adapter == null) {
            adapter = new MessageSystemAdapter(MessageSystemActivity.this, list);
            lv_message_system.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        pageIndex++;
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
            requestParams.addParams("msg_command", ConstantsValue.MessageCommend.MSG_SYSTEM);
            requestParams.addParams("msg_state", "20");
//            requestParams.addParams("time_millis", ""+System.currentTimeMillis());
        }
        MHHttpClient.getInstance().post(PushedMsgResponse.class, context, i == 0 ? ConstantsValue.Url.SET_PUSHED_MSG_STSTE : ConstantsValue.Url.SET_PUSHED_MSG_STSTE_BY_TIME, requestParams, new MHHttpHandler<PushedMsgResponse>() {
            @Override
            public void onSuccess(PushedMsgResponse response) {
                if (i == 0) showToastAtBottom("删除成功");
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    /**
     * 异步任务--解密消息数据(解密消息--耗时操作)
     */
    class AnalysisMsg extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            List<PushedMsgResult> li = (List<PushedMsgResult>) params[0];
            List<SystemMsgObj> li_msg = new ArrayList<>();
            for (int i = 0; i < li.size(); i++) {
                String msgContent = Base64Util.getFromBase64(li.get(i).getMsg_content());
                PushVo vo;
                String msgType = "0";
                String objectId = "";
                try {
                    vo = new Gson().fromJson(msgContent, MsgContentResponse.class).getData();
                } catch (Exception e) {
                    vo = null;
                }
                if (vo != null) {
                    msgType = vo.getObjectType() == null ? "0" : vo.getObjectType();
                    objectId = vo.getObjectId();
                } else {
                }
                li_msg.add(new SystemMsgObj(msgType, vo.getObjectId(),
                        vo.getActivity_name(), vo.getObjectNote(),
                        vo.getActivity_picture(), vo.getActivity_uri()));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterForContextMenu(lv_message_system);
        super.onDestroy();
    }
}

package com.haiqiu.miaohi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.MessageAtMineActivity;
import com.haiqiu.miaohi.activity.MessageCommentMsgActivity;
import com.haiqiu.miaohi.activity.MessageNoticeActivity;
import com.haiqiu.miaohi.activity.MessageQaActivity;
import com.haiqiu.miaohi.activity.MessageSystemActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.tendcloud.tenddata.TCAgent;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


/**
 * 首页-消息
 * Created by zhandalin on 2016/5/23.
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    private CommonNavigation navigation;
    private RelativeLayout rl_message_at_mine;
    private RelativeLayout rl_message_comment;
    private RelativeLayout rl_message_notice;
    private RelativeLayout rl_message_qa;
    private RelativeLayout rl_message_system;
    private TextView tv_atme_bubblecount;
    private TextView tv_comment_bubblecount;
    private TextView tv_notic_bubblecount;
    private TextView tv_qa_bubblecount;
    private TextView tv_system_bubblecount;

    private int QaCount = 0;        //映答数
    private IntentFilter receivedMsgFilter;
    private ReceivedMsgReceiver receivedMsgReceiver;
    private String[] strings_msg = {
//            ConstantsValue.MessageCommend.MSG_AT_ME,
//            ConstantsValue.MessageCommend.MSG_BE_INVITATE,
//            ConstantsValue.MessageCommend.MSG_RECEIVE_COMMENT,
            ConstantsValue.MessageCommend.MSG_NEW_FRIEND,
            ConstantsValue.MessageCommend.MSG_RECEIVE_ZAN,
//            ConstantsValue.MessageCommend.MSG_SYSTEM
    };
    private String[] strings_qa = {ConstantsValue.MessageCommend.MSG_VIP_ANSWER_USER,
            ConstantsValue.MessageCommend.MSG_VIP_ANSWER_VIP,
            ConstantsValue.MessageCommend.MSG_OBSERVE_VIDEO,
            ConstantsValue.MessageCommend.MSG_VIP_RECEIVE_QUESTION};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        registerBroadCast();
        initView(view);

        return view;
    }

    private void initView(View view) {
        navigation = (CommonNavigation) view.findViewById(R.id.navigation);
        navigation.hideLeftLayout();

        tv_atme_bubblecount = (TextView) view.findViewById(R.id.tv_atme_bubblecount);
        rl_message_at_mine = (RelativeLayout) view.findViewById(R.id.rl_message_at_mine);
        tv_comment_bubblecount = (TextView) view.findViewById(R.id.tv_comment_bubblecount);
        rl_message_comment = (RelativeLayout) view.findViewById(R.id.rl_message_comment);
        tv_notic_bubblecount = (TextView) view.findViewById(R.id.tv_notic_bubblecount);
        rl_message_notice = (RelativeLayout) view.findViewById(R.id.rl_message_notice);
        tv_qa_bubblecount = (TextView) view.findViewById(R.id.tv_qa_bubblecount);
        tv_system_bubblecount = (TextView) view.findViewById(R.id.tv_system_bubblecount);
        rl_message_qa = (RelativeLayout) view.findViewById(R.id.rl_message_qa);
        rl_message_system = (RelativeLayout) view.findViewById(R.id.rl_message_system);
        rl_message_qa.setOnClickListener(this);
        rl_message_notice.setOnClickListener(this);
        rl_message_at_mine.setOnClickListener(this);
        rl_message_comment.setOnClickListener(this);
        rl_message_system.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUnreadQACount(0, 0);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            TCAgent.onPageEnd(context, "消息Tab");
        } else {
            TCAgent.onPageStart(context, "消息Tab");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        TCAgent.onPageStart(context, "消息Tab");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_message_at_mine:
                intent = new Intent(getActivity(), MessageAtMineActivity.class);
//                intent = new Intent(getActivity(), LRPerfectInformationActivity.class);
                break;
            case R.id.rl_message_comment:
                intent = new Intent(getActivity(), MessageCommentMsgActivity.class);
//                intent = new Intent(getActivity(), RecommendUserActivity.class);
                break;
            case R.id.rl_message_notice:
                intent = new Intent(getActivity(), MessageNoticeActivity.class);
                break;
            case R.id.rl_message_qa:
                intent = new Intent(getActivity(), MessageQaActivity.class);
                break;
            case R.id.rl_message_system:
                intent = new Intent(getActivity(), MessageSystemActivity.class);
//                intent = new Intent(getActivity(), LRPerfectInformationActivity.class);
                break;
            default:
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

    /**
     * 注册广播
     */
    private void registerBroadCast() {
        receivedMsgFilter = new IntentFilter();
        receivedMsgReceiver = new ReceivedMsgReceiver();
        receivedMsgFilter.addAction("receivedMsg");
        getActivity().registerReceiver(receivedMsgReceiver, receivedMsgFilter);
    }

    /**
     * 广播接收类--实时接收消息
     */
    public class ReceivedMsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getUnreadQACount(0, 0);
        }
    }

    /**
     * 未读消息--映答类
     */
    private void getUnreadQACount(final int position, final int total) {
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, strings_qa[position], new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                int totalUnreadCount = total + integer;
//                MHLogUtil.e(TAG, "问答未读消息" + strings_qa[position] + "----" + integer);
                if (position == strings_qa.length - 1) {
                    QaCount = totalUnreadCount < 0 ? 0 : totalUnreadCount;
                    msgShowRule(QaCount, tv_qa_bubblecount);
                    getUnreadAtmeCount(totalUnreadCount);
                } else {
                    getUnreadQACount(position + 1, totalUnreadCount);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                tv_qa_bubblecount.setVisibility(View.INVISIBLE);
                getUnreadCountFail();
            }
        });
    }

    /**
     * 未读消息--@我的
     */
    private void getUnreadAtmeCount(final int total) {
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, ConstantsValue.MessageCommend.MSG_AT_ME,
                new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        int totalUnreadCount = total + integer;
                        msgShowRule(integer, tv_atme_bubblecount);
                        getUnreadCommentCount(totalUnreadCount);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        tv_atme_bubblecount.setVisibility(View.INVISIBLE);
                        getUnreadCountFail();
                    }
                });
    }

    /**
     * 未读消息-评论
     */
    private void getUnreadCommentCount(final int total) {
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, ConstantsValue.MessageCommend.MSG_RECEIVE_COMMENT, new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                msgShowRule(integer, tv_comment_bubblecount);
                int totalUnreadCount = integer + total;
                getUnreadNotifyCount(0, 0, totalUnreadCount);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                tv_comment_bubblecount.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 未读消息-通知(粉丝+收到的赞)
     */
    private void getUnreadNotifyCount(final int i, final int count, final int total) {
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, strings_msg[i], new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                if (0 == i) {
                    getUnreadNotifyCount(1, integer, total);
                } else {
                    msgShowRule(integer + count, tv_notic_bubblecount);
                    getUnreadSystemCount(total + count + integer);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                tv_notic_bubblecount.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 未读消息--系统(秒嗨小助手)
     */
    private void getUnreadSystemCount(final int total) {
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, ConstantsValue.MessageCommend.MSG_SYSTEM, new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                msgShowRule(integer, tv_system_bubblecount);
                Intent intent = new Intent("broadcast_bubble");
                intent.putExtra("giftCount", "0");
                intent.putExtra("QaCount", "0");
                intent.putExtra("messageCount", total + integer > 0 ? total + integer + "" : "0");
                intent.putExtra("fansCount", "0");
                intent.putExtra("draftsCount", "0");
                getActivity().sendBroadcast(intent);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                tv_system_bubblecount.setVisibility(View.INVISIBLE);
                getUnreadCountFail();
            }
        });
    }

    /**
     * 获取未读消息失败
     */
    private void getUnreadCountFail() {
        Intent intent = new Intent("broadcast_bubble");
        intent.putExtra("giftCount", "0");
        intent.putExtra("QaCount", "0");
        intent.putExtra("fansCount", "0");
        intent.putExtra("messageCount", "0");
        intent.putExtra("draftsCount", "0");
        getActivity().sendBroadcast(intent);
    }

    /**
     * 角标数字显示规则
     */
    private void msgShowRule(int num, TextView textView) {
        if (num <= 0) {
            textView.setVisibility(View.INVISIBLE);
        } else if (num > 99) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("99+");
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText("" + num);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receivedMsgReceiver != null) {
            getActivity().unregisterReceiver(receivedMsgReceiver);
        }
//        if (loginoutReceiver != null) {
//            getActivity().unregisterReceiver(loginoutReceiver);
//        }
//
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
    }
}

package com.haiqiu.miaohi.rong;

import android.content.Context;
import android.content.Intent;

import com.haiqiu.miaohi.ConstantsValue;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by LiuXiang on 2016/7/28.
 */
public class GetRongUnreadCount {
    private List<String> list_msg;
    private List<String> list_qa;
    private Context context;
    private int QaCount = 0;
    private boolean isGetRongFail = true;

    public GetRongUnreadCount(Context context) {
        this.context = context;
        list_msg = new ArrayList<>();
        list_msg.add(ConstantsValue.MessageCommend.MSG_AT_ME);
        list_msg.add(ConstantsValue.MessageCommend.MSG_NEW_FRIEND);
        list_msg.add(ConstantsValue.MessageCommend.MSG_RECEIVE_ZAN);
        list_msg.add(ConstantsValue.MessageCommend.MSG_RECEIVE_COMMENT);
        list_msg.add(ConstantsValue.MessageCommend.MSG_SYSTEM);
        list_qa = new ArrayList<>();
        list_qa.add(ConstantsValue.MessageCommend.MSG_VIP_ANSWER_USER);
        list_qa.add(ConstantsValue.MessageCommend.MSG_VIP_ANSWER_VIP);
        list_qa.add(ConstantsValue.MessageCommend.MSG_VIP_RECEIVE_QUESTION);
        list_qa.add(ConstantsValue.MessageCommend.MSG_OBSERVE_VIDEO);
        //清除未读的礼物消息(版本兼容，此版本无需礼物消息，故清除)
        RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, ConstantsValue.MessageCommend.MSG_RECEIVE_GIFT, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        getQaCount(0, 0);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    //问答消息数目
    private void getQaCount(final int position, final int total) {
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, list_qa.get(position), new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                int totalUnreadCount = total + integer;
                if (position == list_qa.size() - 1) {
                    QaCount = totalUnreadCount < 0 ? 0 : totalUnreadCount;
                    getMsgCount(0, 0);
                } else {
                    getQaCount(position + 1, totalUnreadCount);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                getRongFail();
            }
        });
    }

    //消息数目
    private void getMsgCount(final int i, final int a) {
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, list_msg.get(i),
                new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        int totalUnreadCount = a + integer;
                        if (i == list_msg.size() - 1) {
                            getGiftCount(totalUnreadCount);
                        } else {
                            getMsgCount(i + 1, totalUnreadCount);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        getRongFail();
                    }
                });
    }

    //礼物数目
    private void getGiftCount(final int totalUnreadCount) {
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, ConstantsValue.MessageCommend.MSG_RECEIVE_GIFT,
                new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Intent intent = new Intent("broadcast_bubble");
                        intent.putExtra("giftCount", integer > 0 ? integer + "" : "0");
                        intent.putExtra("QaCount", QaCount > 0 ? QaCount + "" : "0");
                        intent.putExtra("fansCount", "0");
                        intent.putExtra("messageCount", totalUnreadCount > 0 ? totalUnreadCount + "" : "0");
                        intent.putExtra("draftsCount", "0");
                        context.sendBroadcast(intent);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        getRongFail();
                    }
                });
    }

    //融云消息获取失败
    private void getRongFail() {
        if (isGetRongFail) {
            Intent intent = new Intent("broadcast_bubble");
            intent.putExtra("giftCount", "0");
            intent.putExtra("QaCount", "0");
            intent.putExtra("fansCount", "0");
            intent.putExtra("messageCount", "0");
            intent.putExtra("draftsCount", "0");
            context.sendBroadcast(intent);
            isGetRongFail = false;
        }
    }
}

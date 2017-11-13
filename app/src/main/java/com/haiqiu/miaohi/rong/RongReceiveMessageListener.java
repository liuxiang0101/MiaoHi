package com.haiqiu.miaohi.rong;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.bean.PushMsgNewFriendObj;
import com.haiqiu.miaohi.bean.PushVo;
import com.haiqiu.miaohi.db.UserInfoManager;
import com.haiqiu.miaohi.receiver.AttentionNewDataEvent;
import com.haiqiu.miaohi.receiver.RefreshChannel;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.LocalNotification;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.MessageDigestUtils;
import com.haiqiu.miaohi.utils.SecurityMethod;
import com.haiqiu.miaohi.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * Created by miaohi on 2016/6/27.
 * <p/>
 * 融云消息接收监听
 */
public class RongReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
    private static final String TAG = "RongReceiveMessageListener";
    private Context context;
    private Gson gson;

    public RongReceiveMessageListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onReceived(Message message, int left) {
        MHLogUtil.e(TAG, "消息--聊天机制");
        gson = new Gson();
        MessageContent messageContent = message.getContent();
        //接收推荐栏目更新消息时(自定义消息1)
        if (messageContent instanceof CustomTxtMessage) {
            CustomTxtMessage customTxtMessage = (CustomTxtMessage) messageContent;
            String content = customTxtMessage.getContent();
            String targetId = message.getTargetId().replace("=", "");

            printRelevantInfo(0, content, targetId);
            if (SpUtils.getBoolean(ConstantsValue.Sp.IS_LOGINOUT_APP, true))
                return true;
            if (targetId.equals(ConstantsValue.MessageCommend.MSG_RECEIVE_VIDEO_KIND)) {
                //分类更新通知
                EventBus.getDefault().post(new RefreshChannel());
                MHLogUtil.e(TAG, "--频道有更新了");
            } else if (targetId.equals(ConstantsValue.MessageCommend.MSG_RECEIVE_ATTENTION_NEW)) {
                //关注页有更新了
                EventBus.getDefault().post(new AttentionNewDataEvent(content));
                MHLogUtil.e(TAG, "--关注页有更新了");
            }
            return true;
        }

        //判断账户是否登出
//        if (SpUtils.getBoolean("isLoginoutApp", true)) {
        if (SpUtils.getBoolean(ConstantsValue.Sp.IS_LOGINOUT_APP, true)) {
            return true;
        }

        //消息开关设置(自定义消息2)
        if (messageContent instanceof CustomTheMoreTxtMessage) {
            CustomTheMoreTxtMessage customTheMoreTxtMessage = (CustomTheMoreTxtMessage) messageContent;
            String content = customTheMoreTxtMessage.getContent();
            String targetId = message.getTargetId().replace("=", "");

            printRelevantInfo(1, content, targetId);

            Intent intent = new Intent("receivedMsg");
            context.sendBroadcast(intent);
            return true;
        }
        //普通消息
        TextMessage textMessage = (TextMessage) messageContent;
        String showText = textMessage.getContent();
        String extraText = textMessage.getExtra();
        MHLogUtil.e(TAG, "getExtra" + extraText);
        String targetId = message.getTargetId();
        String objectId = null;

        PushVo msgPushVo = null;
        if (targetId.equals(ConstantsValue.MessageCommend.MSG_SYSTEM)) {
            //系统消息加跳转
            try {
                msgPushVo = gson.fromJson(extraText, PushVo.class);
                if (msgPushVo != null) {
                    if (MHStringUtils.isEmpty(msgPushVo.getObjectType()))
                        MHLogUtil.e(TAG, "getObjectType" + msgPushVo.getObjectType());
                    if (!msgPushVo.getObjectType().equals("0")) {
                        targetId = msgPushVo.getObjectType();
                        objectId = msgPushVo.getObjectId();
                    }
                }
            } catch (Exception e) {
                MHLogUtil.e(TAG, "消息解析error");
                MHLogUtil.e(TAG,e);
            }
        }
        if (targetId.equals(ConstantsValue.MessageCommend.MSG_RECEIVE_NOTIFY)) {
            //定时推送消息加跳转
            PushVo pushVo = gson.fromJson(extraText, PushVo.class);
            PushMsgNewFriendObj obj = gson.fromJson(Base64Util.getFromBase64(pushVo.getRes()), PushMsgNewFriendObj.class);
            if (obj != null) {
                switch (obj.getData().getNotify_identify()) {
                    case "atme":
                        targetId = ConstantsValue.MessageCommend.MSG_AT_ME;
                        break;
                    case "comment":
                        targetId = ConstantsValue.MessageCommend.MSG_RECEIVE_COMMENT;
                        break;
                    case "praisevideo":
                        targetId = ConstantsValue.MessageCommend.MSG_RECEIVE_ZAN;
                        break;
                    case "newfans":
                        targetId = ConstantsValue.MessageCommend.MSG_NEW_FRIEND;
                        UserInfoManager.syncData(context.getApplicationContext());
                        break;
                    case "receivegift":
                        targetId = ConstantsValue.MessageCommend.MSG_RECEIVE_GIFT;
                        break;
                    case "iask":
                        targetId = ConstantsValue.MessageCommend.MSG_VIP_RECEIVE_QUESTION;
                        break;
                    case "askme":
                        targetId = ConstantsValue.MessageCommend.MSG_VIP_RECEIVE_QUESTION;
                        break;
                    case "observeme":
                        targetId = ConstantsValue.MessageCommend.MSG_OBSERVE_VIDEO;
                        break;
                    default:
                        MHLogUtil.e(TAG, "定时推送消息类型错误");
                        break;
                }
            }
        }
        if (showText.indexOf(":") == 0) {
            showText = showText.replaceFirst(":", "");
        }
//        try {
        printRelevantInfo(2, showText, targetId, extraText);
        //接收到消息,唤起本地通知
        LocalNotification.getLocalNotification().showNotification(context, extraText, showText, targetId, objectId, msgPushVo);
//        parseStringByRes(extraText,"");
        //接收到消息,发送广播通知显示红点
        Intent intent = new Intent("receivedMsg");
        context.sendBroadcast(intent);
//        } catch (Exception e) {
//            MHLogUtil.e(TAG,e);
//        }
        return true;
    }

    /**
     * 解密AES加密数据的方法
     */
    private void parseStringByRes(String extra, String targetId) {
        Gson g = new Gson();
        //----------------------查看加密的数据用------------------------//
        //解析
        PushVo pushVo = g.fromJson(extra, PushVo.class);
        //获取mac值，用于解密
        String sha = MessageDigestUtils.getStringSHA256(pushVo.getMac()).toLowerCase();
        String securityKey = sha.substring(0, 16);
        String securityVector = sha.substring(sha.length() - 16);
        MHLogUtil.e(TAG, SecurityMethod.aesBase64StringDecrypt(pushVo.getRes(), securityKey, securityVector));
    }

    /**
     * 打印相关信息,便于控制台查看
     */
    private void printRelevantInfo(int i, String... strings) {
        switch (i) {
            case 0:
//                MHLogUtil.e(TAG, "标题栏分类更新消息");
                break;
            case 1:
                MHLogUtil.e(TAG, "消息开关改变,自定义消息");
                break;
            case 2:
                MHLogUtil.e(TAG, " getExtra(携带额外信息)----" + strings[2]);
                break;
        }
        MHLogUtil.e(TAG, " getContent(展示信息) ---- " + strings[0]);
        MHLogUtil.e(TAG, "getTargetId(信息类型) ---- " + strings[1]);
    }
}

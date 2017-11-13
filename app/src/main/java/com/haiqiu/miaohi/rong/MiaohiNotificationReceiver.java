package com.haiqiu.miaohi.rong;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.PushVo;
import com.haiqiu.miaohi.db.UserInfoManager;
import com.haiqiu.miaohi.response.ObjectIdResponse;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MessageDigestUtils;
import com.haiqiu.miaohi.utils.SecurityMethod;
import com.haiqiu.miaohi.utils.SpUtils;
import com.qiniu.android.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 消息推送接收(app退出时)
 * Created by miaohi on 2016/6/16.
 */
public class MiaohiNotificationReceiver extends PushMessageReceiver {
    private final String TAG = "MiaohiNotificationReceiver";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private int notifyId = 10000;
    private Gson gson;

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        MHLogUtil.e(TAG, "消息--远程推送机制");
        gson = new Gson();
        String showText = pushNotificationMessage.getPushContent();     //用于展示的文本
        String extraText = pushNotificationMessage.getExtra();          //消息携带的额外信息
        String targetId = pushNotificationMessage.getTargetId();        //消息类型
        String pushData = pushNotificationMessage.getPushData();        //推送消息通过这个携带额外信息
        String objectId = null;                                         //系统消息--可以点击跳转时携带此信息

        PushVo msgPushVo = null;
        if (!StringUtils.isNullOrEmpty(pushData)) {
            MHLogUtil.e(TAG, "pushData----" + pushData);
            switch (targetId) {
                case ConstantsValue.MessageCommend.MSG_SYSTEM:
                    try {
                        msgPushVo = gson.fromJson(pushData, PushVo.class);
                        if (!msgPushVo.getObjectType().equals("0")) {
                            targetId = msgPushVo.getObjectType();
                            objectId = msgPushVo.getObjectId();
                        }
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                    break;
                case ConstantsValue.MessageCommend.MSG_RECEIVE_NOTIFY:
                    switch (pushData) {
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
                            targetId = "";
                            MHLogUtil.e(TAG, "定时推送消息类型错误");
                            break;
                    }
                    break;
            }
        }
        MHLogUtil.e(TAG, "融云-消息推送--" + showText);
        MHLogUtil.e(TAG, "融云-消息推送-TargetId-" + targetId);
        if (ConstantsValue.MessageCommend.MSG_RECEIVE_VIDEO_KIND.equals(targetId)) {
            //推荐栏目更新消息远程推送时，不进行通知
            return true;
        }
//        if (SpUtils.getBoolean("isLoginoutApp", true)) {
        if (SpUtils.getBoolean(ConstantsValue.Sp.IS_LOGINOUT_APP, true)) {
            //用户登出时,消息不进行通知
            return true;
        }
        //如果通知栏显示内容首字符为:替换掉
        if (showText.indexOf(":") == 0) {
            showText = showText.replaceFirst(":", "");
        }
        showNotification(context, showText, pushData, targetId, objectId, msgPushVo);
        return true;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        return true;
    }

    /**
     * 接收到消息时显示自定义的Notification
     */
    public void showNotification(Context context, String msgContent, String pushData, String targetId, String objectId, PushVo msgPushVo) {
        notifyId++;
        RemoteViews view_custom = new RemoteViews(context.getPackageName(), R.layout.view_notification_custom);
        //获取当前时间,格式化处理
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);

        //设置对应IMAGEVIEW的ID的资源图片
        view_custom.setImageViewResource(R.id.custom_icon, R.mipmap.ic_launcher);
        view_custom.setTextViewText(R.id.tv_custom_title, "秒嗨");
        view_custom.setTextViewText(R.id.tv_custom_time, str);
//        view_custom.setTextViewText(R.id.tv_custom_content, SpUtils.getInt("is_show_msg_info", 1) == 0 ? "您收到了一条新消息" : msgContent);
        view_custom.setTextViewText(R.id.tv_custom_content, msgContent);
        view_custom.setTextViewText(R.id.tv_custom_num, "");

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("秒嗨")
                .setContentText(msgContent)
                .setContentIntent(getDefalutIntent(context, Notification.FLAG_AUTO_CANCEL))
                .setOnlyAlertOnce(false).setContent(view_custom)
                .setNumber(2)                                   //显示数量
//                .setTicker(SpUtils.getInt("is_show_msg_info", 1) == 0 ? "您收到了一条新消息" : "秒嗨助手:" + msgContent)//通知首次出现在通知栏，带上升动画效果的
                .setTicker("秒嗨助手:" + msgContent)//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())            //通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)     //设置该通知优先级
                .setAutoCancel(true)                            //设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)                              //ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)          //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.drawable.icon_notification_bar);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_launcher_small);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        mBuilder.setLargeIcon(bitmapDrawable.getBitmap());
//        // 5.0
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            mBuilder.setSmallIcon(R.drawable.icon_notification_bar);
//        } else {
//            mBuilder.setSmallIcon(R.mipmap.ic_launcher_small);
//        }
        //点击通知栏推送消息时跳转到Intent
        Intent resultIntent = selectActivityIntent(context, pushData, targetId, Intent.FLAG_ACTIVITY_SINGLE_TOP, objectId, msgPushVo);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        //Notification
        Notification notify = mBuilder.build();
        notify.contentView = view_custom;
        mNotificationManager.notify(notifyId, notify);
    }

    /**
     * 通过推送消息的类型跳转不同界面
     *
     * @param context
     * @param msgType
     * @param flags
     * @return
     */
    private Intent selectActivityIntent(Context context, String pushData, String msgType, int flags, String objectId, PushVo msgPushVo) {
        Intent intent = new Intent();
        Uri.Builder builder = Uri.parse("rong://" + context.getPackageName()).buildUpon();
        builder.appendPath("conversationlist");
        Uri uri = builder.build();
        intent.setData(uri);
        intent.putExtra("isIntoFromPush", true);
        intent.putExtra("type", msgType);
        if ("1".equals(msgType) || "3".equals(msgType) || "4".equals(msgType) || "6".equals(msgType)) {
            //系统消息可跳转类型
            intent.putExtra("objectId", objectId);
        } else if (msgType.equals("2")) {
            //系统消息-专题H5
            intent.putExtra("activity_uri", msgPushVo.getActivity_uri());
            intent.putExtra("activity_note", msgPushVo.getObjectNote());
            intent.putExtra("activity_name", msgPushVo.getActivity_name());
            intent.putExtra("activity_picture", "");
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_VIP_ANSWER_VIP) || msgType.equals(ConstantsValue.MessageCommend.MSG_VIP_ANSWER_USER)) {
            //问题被回答
            intent.putExtra("question_id", pushData);
        }
        MHApplication.mMiaohiToken = SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI);
        intent.setFlags(flags);
        return intent;
    }

    private String getQuestionId(String extra) {
        Gson g = new Gson();
        PushVo pushVo = g.fromJson(extra, PushVo.class);
        String sha = MessageDigestUtils.getStringSHA256(pushVo.getMac()).toLowerCase();
        String securityKey = sha.substring(0, 16);
        String securityVector = sha.substring(sha.length() - 16);
        String result = SecurityMethod.aesBase64StringDecrypt(pushVo.getRes(), securityKey, securityVector);
        ObjectIdResponse questionIdResponse = g.fromJson(result, ObjectIdResponse.class);
        String question_id = questionIdResponse.getData().getQuestion_id();

        return question_id;
    }

    /**
     * 获取默认的PendingIntent
     */
    private PendingIntent getDefalutIntent(Context context, int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }
}

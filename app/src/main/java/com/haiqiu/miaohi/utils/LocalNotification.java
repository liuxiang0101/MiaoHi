package com.haiqiu.miaohi.utils;

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
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.PushVo;
import com.haiqiu.miaohi.response.ObjectIdResponse;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by miaohi on 2016/6/25.
 * 本地通知管理类
 */
public class LocalNotification {
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private final String TAG = "LocalNotification";
    private static LocalNotification localNotification;

    private int notifyId = 0;
    private int QA_ASK_ME = 0;
    private int QA_I_ASK = 1;
    private Gson gson;

    public LocalNotification() {
        gson = new Gson();
    }

    public synchronized static LocalNotification getLocalNotification() {
        if (localNotification == null) {
            localNotification = new LocalNotification();
        }
        return localNotification;
    }

    /**
     * 接收到消息时显示自定义的Notification
     */
    public void showNotification(Context context, String extra, String msgContent, String targetId, String objectId, PushVo msgPushVo) {
        if (SpUtils.getInt(ConstantsValue.Sp.IS_APP_ON_FOREGROUND, 1) == 0) {
            //程序运行在前台，不弹出通知栏提示
            return;
        }
        notifyId++;
        RemoteViews view_custom = new RemoteViews(context.getPackageName(), R.layout.view_notification_custom);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
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
        //点击通知栏推送消息时跳转到Intent
        Intent resultIntent = selectActivityIntent(context, extra, targetId, Intent.FLAG_ACTIVITY_SINGLE_TOP, objectId, msgPushVo);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifyId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
    private Intent selectActivityIntent(final Context context, String extra, String msgType, int flags, String objectId, PushVo msgPushVo) {
        Intent intent = new Intent();
        Uri.Builder builder = Uri.parse("rong://" + context.getPackageName()).buildUpon();
        builder.appendPath("conversationlist");
        Uri uri = builder.build();
        intent.setData(uri);
        intent.putExtra("isIntoFromPush", true);

        String[] strings = {ConstantsValue.MessageCommend.MSG_NEW_FRIEND,
                ConstantsValue.MessageCommend.MSG_RECEIVE_COMMENT,
                ConstantsValue.MessageCommend.MSG_AT_ME,
                ConstantsValue.MessageCommend.MSG_RECEIVE_ZAN,
                ConstantsValue.MessageCommend.MSG_BE_INVITATE,
                ConstantsValue.MessageCommend.MSG_SYSTEM,
                ConstantsValue.MessageCommend.MSG_RECEIVE_GIFT,
                ConstantsValue.MessageCommend.MSG_VIP_RECEIVE_QUESTION,
                ConstantsValue.MessageCommend.MSG_VIP_ANSWER_USER,
                ConstantsValue.MessageCommend.MSG_VIP_ANSWER_VIP,
                ConstantsValue.MessageCommend.MSG_OBSERVE_VIDEO};

        intent.putExtra("type", msgType);
        if (strings[7].equals(msgType)) {
            intent.putExtra(ConstantsValue.Sp.QA_TYPE, QA_ASK_ME);
        } else if (strings[8].equals(msgType) || strings[9].equals(msgType)) {
            intent.putExtra("question_id", getObjectId(extra, 0));
            intent.putExtra(ConstantsValue.Sp.QA_TYPE, QA_I_ASK);
        } else if ("2".equals(msgType)) {
            intent.putExtra("activity_uri", msgPushVo.getActivity_uri());
            intent.putExtra("activity_note", msgPushVo.getObjectNote());
            intent.putExtra("activity_name", msgPushVo.getActivity_name());
            intent.putExtra("activity_picture", msgPushVo.getActivity_picture());
        } else if ("1".equals(msgType) || "3".equals(msgType) || "4".equals(msgType) || "6".equals(msgType)) {
            intent.putExtra("objectId", objectId);
        } else if (strings[0].equals(msgType)) {
            intent.putExtra("userId", getObjectId(extra, 1));
        }
        intent.setFlags(flags);
        return intent;
    }

    /**
     * 获取question_id
     *
     * @param extra
     * @return
     */
    private String getObjectId(String extra, int type) {
        if (MHStringUtils.isEmpty(extra)) return null;
        PushVo pushVo = gson.fromJson(extra, PushVo.class);
        //解密
        String sha = MessageDigestUtils.getStringSHA256(pushVo.getMac()).toLowerCase();
        String securityKey = sha.substring(0, 16);
        String securityVector = sha.substring(sha.length() - 16);
        String result = SecurityMethod.aesBase64StringDecrypt(pushVo.getRes(), securityKey, securityVector);
        //获取解密后需要的信息
        ObjectIdResponse questionIdResponse = gson.fromJson(result, ObjectIdResponse.class);
        String object_id;
        if (type == 0)
            object_id = questionIdResponse.getData().getQuestion_id();
        else
            object_id = questionIdResponse.getData().getSender_id();

        return object_id;
    }

    /**
     * 获取默认的PendingIntent
     *
     * @param context
     * @param flags
     * @return
     */
    private PendingIntent getDefalutIntent(Context context, int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }
}

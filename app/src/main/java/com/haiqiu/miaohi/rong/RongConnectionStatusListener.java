package com.haiqiu.miaohi.rong;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.activity.MainActivity;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.CommonUserInfoResponse;
import com.haiqiu.miaohi.utils.FileUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.widget.CommonDialog;

import java.util.Timer;
import java.util.TimerTask;

import io.rong.imlib.RongIMClient;

/**
 * Created by miaohi on 2016/6/27.
 * <p/>
 * 融云连接状态监听
 */
public class RongConnectionStatusListener implements RongIMClient.ConnectionStatusListener {
    private final static String TAG = "RongConnectionStatusListener";
    private final int TOTAL_TIMES = 9;            //重请求次数
    private final long REQUEST_TIME = 3000;       //请求间隔时间
    private final int NET_NOT_AVAILABLE = -1;     //网络不可用
    private final int CONNECT_SUCCESS = 0;        //连接成功
    private final int CONNECTING = 1;             //连接中
    private final int SERVER_DISCONNECTION = 2;   //与融云服务器连接断开
    private final int ACCOUNT_BE_LOGINED = 3;     //账户被登录
    private final int RONG_TOKEN_INVALID = 4;     //rong_token失效
    private final int SERVER_ABNORMAL = 5;        //融云服务器端异常
    private int getValidTokenTimes = 0;
    private int repeatConnecTimes1 = 0;
    private int repeatConnecTimes2 = 0;
    private Context context;
    private Timer timer;

    public RongConnectionStatusListener(Context context) {
        this.context = context;
    }

    @Override
    public void onChanged(final ConnectionStatus connectionStatus) {
        switch (connectionStatus.getValue()) {
            case NET_NOT_AVAILABLE:
                MHLogUtil.i(TAG, "connect state---网络不可用");
//                new MyAsyncTask().execute(NET_NOT_AVAILABLE);
                break;
            case CONNECT_SUCCESS:
                MHLogUtil.i(TAG, "connect state---连接成功");
                break;
            case CONNECTING:
                MHLogUtil.i(TAG, "connect state---连接中......");
                break;
            case SERVER_DISCONNECTION:
                MHLogUtil.i(TAG, "connect state---连接断开");
                repeatConnectRongService(SERVER_DISCONNECTION);
                break;
            case ACCOUNT_BE_LOGINED:
                MHLogUtil.i(TAG, "connect state---用户账户在其他设备登录，本机掉线");
                new MyAsyncTask().execute(ACCOUNT_BE_LOGINED);
                break;
            case RONG_TOKEN_INVALID:
                MHLogUtil.i(TAG, "connect state---Token 不正确");
                repeatGetValidRongToken();
                break;
            case SERVER_ABNORMAL:
                MHLogUtil.i(TAG, "connect state---服务器异常或无法连接");
                repeatConnectRongService(SERVER_ABNORMAL);
                break;
        }
    }

    /**
     * 重连融云服务器
     */
    private void repeatConnectRongService(final int i) {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                switch (i) {
                    case SERVER_DISCONNECTION:
                        if (repeatConnecTimes1 < TOTAL_TIMES) {
                            connectRongService(SpUtils.getString(ConstantsValue.Sp.RONG_TOKEN));
                            repeatConnecTimes1++;
                            MHLogUtil.e(TAG, "融云服务重连" + repeatConnecTimes1 + "次");
                        } else {
                            timer.cancel();
                        }
                        break;
                    case SERVER_ABNORMAL:
                        if (repeatConnecTimes2 < TOTAL_TIMES) {
                            connectRongService(SpUtils.getString(ConstantsValue.Sp.RONG_TOKEN));
                            repeatConnecTimes2++;
                            MHLogUtil.e(TAG, "融云服务重连" + repeatConnecTimes1 + "次");
                        } else {
                            timer.cancel();
                        }
                        break;
                }
            }
        };
        timer.schedule(task, REQUEST_TIME);
    }

    /**
     * 重新获取有效的融云token
     */
    private void repeatGetValidRongToken() {
        if (!isLogin()) {
            return;
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (getValidTokenTimes > TOTAL_TIMES) {
                    getRongValidToken();
                    getValidTokenTimes++;
                } else {
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, REQUEST_TIME);
    }

    /**
     * 获取有效的融云token并连接
     */
    private void getRongValidToken() {
        if (!isLogin()) return;
        MHRequestParams requestParams = new MHRequestParams();
        MHHttpClient.getInstance().post(CommonUserInfoResponse.class, ConstantsValue.Url.GET_RONG_VALID_TOKEN, requestParams, new MHHttpHandler<CommonUserInfoResponse>() {
            @Override
            public void onSuccess(CommonUserInfoResponse response) {
                String rong_token = response.getData().getRong_token();
                RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(context));
                RongIMClient.connect(rong_token, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        MHLogUtil.e(TAG, "rong_token失效");
                    }

                    @Override
                    public void onSuccess(String s) {
                        MHLogUtil.i(TAG, "rong_token，恢复连接userId---" + s);
                        if (timer != null) {
                            timer.cancel();
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        MHLogUtil.e(TAG, "融云未连接--errorCode:" + errorCode.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(String content) {
                MHLogUtil.e(TAG, "融云连接失败--" + content);
            }
        });
    }

    /**
     * 连接融云
     *
     * @param token
     */
    private void connectRongService(String token) {
        RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(context));
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                MHLogUtil.i(TAG, "融云token失效");
            }

            @Override
            public void onSuccess(String s) {
                MHLogUtil.i(TAG, "融云已恢复连接--userId:" + s);
                ToastUtils.showToastAtBottom(context, "连接恢复");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MHLogUtil.e(TAG, "融云未连接--errorCode:" + errorCode.getMessage());
            }
        });
    }

    /**
     * 子线程里接收到的消息异步(主线程中)进行处理
     */
    private class MyAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(Object o) {
            switch ((int) o) {
                case NET_NOT_AVAILABLE://网络不可用时
//                    commonDialog.setContentMsg("当前网络不可用!");
//                    commonDialog.setLeftButtonMsg("确定");
//                    commonDialog.hideRightButton();
//                    commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
//                        @Override
//                        public void onLeftButtonOnClick() {
//                            commonDialog.hide();
//                        }
//                    });
                    break;
                case ACCOUNT_BE_LOGINED://账号异地登陆
//                    if (SpUtils.getBoolean("isLoginoutApp", false)) {
                    if (SpUtils.getBoolean(ConstantsValue.Sp.IS_LOGINOUT_APP, false)) {
                        return;
                    }
                    //发送广播关闭相关的界面
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

                    forceLoginoutDealData();
                    dialogLogic();
                    break;
            }
            super.onPostExecute(o);
        }
    }

    /**
     * 强制登出时的数据处理
     */
    private void forceLoginoutDealData() {
        //登出时的处理
//        SpUtils.put("is_login_success", false);
        SpUtils.put(ConstantsValue.Sp.IS_LOGIN_SUCCESS, false);
        UserInfoUtil.logout();
        FileUtils.deleteFile(context, ConstantsValue.Other.CHANNEL_NAME_NEW);

        //发送广播,底部消息tab图标右上角 角标置为空(MainActivity接收)
        Intent intent = new Intent("broadcast_bubble");
        intent.putExtra("giftCount", "0");
        intent.putExtra("QaCount", "0");
        intent.putExtra("fansCount", "0");
        intent.putExtra("messageCount", "0");
        intent.putExtra("draftsCount", "0");
        context.sendBroadcast(intent);

        //主界面fragment停留在首页
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setSelect(0);
        mainActivity.switchFragment(1);
        mainActivity.setViewpager();
    }

    /**
     * 弹出框逻辑
     */
    private void dialogLogic() {
        CommonDialog commonDialog = new CommonDialog(context);
        commonDialog.setContentMsg("该账户在其他设备登录!");
        commonDialog.setLeftButtonMsg("确定");
        commonDialog.hideRightButton();
//        commonDialog.setRightButtonMsg("重新登录");
//        commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
//            @Override
//            public void onRightButtonOnClick() {
//                context.startActivity(new Intent(context, LoginActivity.class));
//            }
//        });
        commonDialog.show();
    }

    /**
     * 判断是否已登录
     */
    public boolean isLogin() {
        return !MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI));
    }
}

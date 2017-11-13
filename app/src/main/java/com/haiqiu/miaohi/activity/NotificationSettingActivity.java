package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.MsgSwitchResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.MsgSwitchResponse;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.view.CommonNavigation;

import java.util.List;

/**
 * Created by LiuXiang on 2016/9/19.
 * 通知消息接收设置界面
 */
public class NotificationSettingActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "NotificationSettingActivity";
    private CommonNavigation navigation;
    private ToggleButton tb_switch_at_me;
    private LinearLayout ll_switch_at_me;
    private ToggleButton tb_switch_comment;
    private LinearLayout ll_switch_comment;
    private ToggleButton tb_switch_receive_zan;
    private LinearLayout ll_switch_receive_zan;
    private ToggleButton tb_switch_new_fans;
    private LinearLayout ll_switch_new_fans;
    private ToggleButton tb_switch_i_ask;
    private LinearLayout ll_switch_i_ask;
    private ToggleButton tb_switch_ask_me;
    private LinearLayout ll_switch_ask_me;
    private ToggleButton tb_switch_observer;
    private LinearLayout ll_switch_observer;
    private ToggleButton tb_switch_receive_gift;
    private LinearLayout ll_switch_receive_gift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        initView();
        getPushMsgState();
    }

    /**
     * 控件初始化
     */
    private void initView() {
        navigation = (CommonNavigation) findViewById(R.id.navigation);
        tb_switch_at_me = (ToggleButton) findViewById(R.id.tb_switch_at_me);
        ll_switch_at_me = (LinearLayout) findViewById(R.id.ll_switch_at_me);
        tb_switch_comment = (ToggleButton) findViewById(R.id.tb_switch_comment);
        ll_switch_comment = (LinearLayout) findViewById(R.id.ll_switch_comment);
        tb_switch_receive_zan = (ToggleButton) findViewById(R.id.tb_switch_receive_zan);
        ll_switch_receive_zan = (LinearLayout) findViewById(R.id.ll_switch_receive_zan);
        tb_switch_new_fans = (ToggleButton) findViewById(R.id.tb_switch_new_fans);
        ll_switch_new_fans = (LinearLayout) findViewById(R.id.ll_switch_new_fans);
        tb_switch_receive_gift = (ToggleButton) findViewById(R.id.tb_switch_receive_gift);
        ll_switch_receive_gift = (LinearLayout) findViewById(R.id.ll_switch_receive_gift);
        tb_switch_i_ask = (ToggleButton) findViewById(R.id.tb_switch_i_ask);
        ll_switch_i_ask = (LinearLayout) findViewById(R.id.ll_switch_i_ask);
        tb_switch_ask_me = (ToggleButton) findViewById(R.id.tb_switch_ask_me);
        ll_switch_ask_me = (LinearLayout) findViewById(R.id.ll_switch_ask_me);
        tb_switch_observer = (ToggleButton) findViewById(R.id.tb_switch_observer);
        ll_switch_observer = (LinearLayout) findViewById(R.id.ll_switch_observer);

        ll_switch_at_me.setOnClickListener(this);
        ll_switch_comment.setOnClickListener(this);
        ll_switch_receive_zan.setOnClickListener(this);
        ll_switch_new_fans.setOnClickListener(this);
        ll_switch_i_ask.setOnClickListener(this);
        ll_switch_ask_me.setOnClickListener(this);
        ll_switch_observer.setOnClickListener(this);
        ll_switch_receive_gift.setOnClickListener(this);
    }

    /**
     * 获取推送消息开关状态
     */
    private void getPushMsgState() {
        showLoading("请等待...", true, false);
        MHRequestParams requestParams = new MHRequestParams();
        MHHttpClient.getInstance().post(MsgSwitchResponse.class, context, ConstantsValue.Url.GET_SWITCH_FOR_PUSH_NOTIFY, requestParams, new MHHttpHandler<MsgSwitchResponse>() {
            @Override
            public void onSuccess(MsgSwitchResponse response) {
                setData(response.getData().getSwitch_result());
            }

            @Override
            public void onFailure(String content) {
            }

            @Override
            public void onStatusIsError(String message) {
                showErrorView();
                super.onStatusIsError(message);
            }
        });
    }

    private void setData(List<MsgSwitchResult> list) {
        for (MsgSwitchResult result : list) {
            boolean isChecked = result.isSwitch_mark();
            switch (result.getNotify_identify()) {
                case "atme":
                    tb_switch_at_me.setChecked(isChecked);
                    break;
                case "comment":
                    tb_switch_comment.setChecked(isChecked);
                    break;
                case "praisevideo":
                    tb_switch_receive_zan.setChecked(isChecked);
                    break;
                case "newfans":
                    tb_switch_new_fans.setChecked(isChecked);
                    break;
                case "receivegift":
                    tb_switch_receive_gift.setChecked(isChecked);
                    break;
                case "iask":
                    tb_switch_i_ask.setChecked(isChecked);
                    break;
                case "askme":
                    tb_switch_ask_me.setChecked(isChecked);
                    break;
                case "observeme":
                    tb_switch_observer.setChecked(isChecked);
                    break;
                default:
                    MHLogUtil.e(TAG, "Notify_identify:" + result.getNotify_identify());
                    break;
            }
        }
    }

    /**
     * 设置后台消息开关状态
     */
    private MHRequestParams requestParams;
    private void setServerMessagePushState(final ToggleButton toggleButton, String commend, final String isChecked) {
        showLoading("设置中...", true, false);
        requestParams = new MHRequestParams();
        requestParams.addParams("notify_identify", commend);
        requestParams.addParams("switch_mark", isChecked);

        ll_switch_ask_me.postDelayed(new Runnable() {
            @Override
            public void run() {
                MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.SET_SWITCH_FOR_PUSH_NOTIFY, requestParams, new MHHttpHandler<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        toggleButton.setChecked(Boolean.parseBoolean(isChecked));
                    }

                    @Override
                    public void onFailure(String content) {
                    }

                    @Override
                    public void onStatusIsError(String message) {
                        super.onStatusIsError(message);
                    }
                });
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_switch_at_me:
                setServerMessagePushState(tb_switch_at_me, "atme", !tb_switch_at_me.isChecked() + "");
                break;
            case R.id.ll_switch_comment:
                setServerMessagePushState(tb_switch_comment, "comment", !tb_switch_comment.isChecked() + "");
                break;
            case R.id.ll_switch_receive_zan:
                setServerMessagePushState(tb_switch_receive_zan, "praisevideo", !tb_switch_receive_zan.isChecked() + "");
                break;
            case R.id.ll_switch_new_fans:
                setServerMessagePushState(tb_switch_new_fans, "newfans", !tb_switch_new_fans.isChecked() + "");
                break;
            case R.id.ll_switch_receive_gift:
                setServerMessagePushState(tb_switch_receive_gift, "receivegift", !tb_switch_receive_gift.isChecked() + "");
                break;
            case R.id.ll_switch_i_ask:
                setServerMessagePushState(tb_switch_i_ask, "iask", !tb_switch_i_ask.isChecked() + "");
                break;
            case R.id.ll_switch_ask_me:
                setServerMessagePushState(tb_switch_ask_me, "askme", !tb_switch_ask_me.isChecked() + "");
                break;
            case R.id.ll_switch_observer:
                setServerMessagePushState(tb_switch_observer, "observeme", !tb_switch_observer.isChecked() + "");
                break;
        }
    }
}

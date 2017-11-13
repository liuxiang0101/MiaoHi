package com.haiqiu.miaohi.utils;

import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.fragment.LoginDialogFragment;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.HomeFoundResponse;
import com.haiqiu.miaohi.utils.callback.ChangeAttentionStateCallBack;
import com.haiqiu.miaohi.widget.CommonDialog;

/**
 * Created by LiuXiang on 2016/12/15.
 * 点击关注按钮进行关注或取消关注
 */
public class ChangeAttentionStateUtil {
    private View progress_bar;
    private BaseActivity activity;
    private CommonDialog commonDialog;
    private LoginDialogFragment loginDialog;

    public ChangeAttentionStateUtil(BaseActivity activity) {
        this.activity = activity;
    }

    public void changeAttentionState(final String userId, final boolean targetAttentionState, final View view, View progress_bar, final ChangeAttentionStateCallBack callBack) {
        this.progress_bar = progress_bar;
        if (MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI))) {
            loginDialog = new LoginDialogFragment();
            loginDialog.show(activity.getSupportFragmentManager(), "loginDialog");

            return;
        }
        if (MHStringUtils.isEmpty(userId) || !(view instanceof TextView)) return;
        //用户取消关注时弹框提醒
        if (!targetAttentionState) {
            commonDialog = new CommonDialog(activity);
            commonDialog.setLeftButtonMsg("取消");
            commonDialog.setRightButtonMsg("确定");
            commonDialog.setContentMsg("确定要取消关注吗？");
            commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                @Override
                public void onRightButtonOnClick() {
                    synchronizationServer((TextView) view, userId, targetAttentionState, callBack);
                }
            });
            commonDialog.show();
            commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    commonDialog = null;
                }
            });
        } else {
            synchronizationServer((TextView) view, userId, targetAttentionState, callBack);
        }
    }

    private void synchronizationServer(final TextView view, final String id, final boolean state, final ChangeAttentionStateCallBack callBack) {
        if (progress_bar != null) progress_bar.setVisibility(View.VISIBLE);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("action_mark", state + "");
        requestParams.addParams("user_id", id);
        view.setEnabled(false);
        view.setText(null);
        MHHttpClient.getInstance().post(HomeFoundResponse.class, activity, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<HomeFoundResponse>() {
            @Override
            public void onSuccess(HomeFoundResponse response) {
                resetState(view, state, callBack);
                MHStateSyncUtil.pushSyncEvent(activity, id, state);
            }

            @Override
            public void onFailure(String content) {
                resetState(view, !state, callBack);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                resetState(view, !state, callBack);
            }
        });
    }

    private void resetState(TextView view, boolean state, ChangeAttentionStateCallBack callBack) {
        if (progress_bar != null) progress_bar.setVisibility(View.GONE);
        callBack.callBackInfo(state);
        view.setEnabled(true);
        if (state) {
            view.setText("已关注");
            view.setTextColor(activity.getResources().getColor(R.color.color_c4));
            view.setBackgroundResource(R.drawable.tag_bg);
        } else {
            view.setText("关注");
            view.setTextColor(activity.getResources().getColor(R.color.fontblue));
            view.setBackgroundResource(R.drawable.shape_attention_blue_selector);
        }
    }
}

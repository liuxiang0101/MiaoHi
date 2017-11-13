package com.haiqiu.miaohi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.WalletInfoData;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.CustomPrivilegeDialog;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshScrollView;
import com.tendcloud.tenddata.TCAgent;

/**
 * 钱包
 * Created by hackest on 16/8/15.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener {
    private View rl_trading_record;
    private TextView textview_content;
    private TextView tv_balance;
    private TextView my_regular_tv;
    private TextView my_current_tv;
    private TextView notice_note;
    private RelativeLayout rl_recharge;
    private RelativeLayout rl_wallet_bg;
    private LinearLayout ll_make_money_for_answer;
    private CommonNavigation title_bar;
    private CustomPrivilegeDialog customPrivilegeDialog;
    private PullToRefreshScrollView pullToRefreshScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initScrollView();
    }

    private void initScrollView() {
        showLoading("加载中...", true, false);
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.ptr_sv);
        View view = View.inflate(this, R.layout.activity_wallet_inside, null);
        pullToRefreshScrollView.getRefreshableView().addView(view);
        initView(view);

        pullToRefreshScrollView.setPullLoadEnabled(false);
        pullToRefreshScrollView.setPullRefreshEnabled(true);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }

            @Override
            public void onLoadMore() {
            }

        });
    }

    private void initView(View view) {
        title_bar = (CommonNavigation) findViewById(R.id.title_bar);
        title_bar.setRightText("说明");
        title_bar.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WalletActivity.this, WebActivity.class);
                intent.putExtra("uri", getResources().getString(R.string.explain_html));
                intent.putExtra("title", "说明");
                startActivity(intent);
            }
        });
        rl_trading_record = view.findViewById(R.id.rl_trading_record);
        rl_wallet_bg = (RelativeLayout) view.findViewById(R.id.rl_wallet_bg);
        ll_make_money_for_answer = (LinearLayout) view.findViewById(R.id.ll_make_money_for_answer);
        rl_recharge = (RelativeLayout) view.findViewById(R.id.rl_recharge);
        textview_content = (TextView) view.findViewById(R.id.textview_content);
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        my_regular_tv = (TextView) view.findViewById(R.id.my_regular_tv);
        my_current_tv = (TextView) view.findViewById(R.id.my_current_tv);
        notice_note = (TextView) view.findViewById(R.id.notice_note);
        tv_balance.setOnClickListener(this);
        rl_recharge.setOnClickListener(this);
        textview_content.setOnClickListener(this);
        rl_trading_record.setOnClickListener(this);
        ll_make_money_for_answer.setOnClickListener(this);
        (findViewById(R.id.ll_buy_qa)).setOnClickListener(this);
        (findViewById(R.id.ll_buy_observe)).setOnClickListener(this);
        (findViewById(R.id.ll_make_money_for_observe)).setOnClickListener(this);
        if ("1".equals(SpUtils.getString(ConstantsValue.Sp.ANSER_DROIT))) {
            ll_make_money_for_answer.setVisibility(View.VISIBLE);
        } else {
            ll_make_money_for_answer.setVisibility(View.GONE);
        }
    }

    private void initData() {
        MHHttpClient.getInstance().post(WalletInfoData.class, context, ConstantsValue.Url.GETWALLETINFO, new MHHttpHandler<WalletInfoData>() {
            @Override
            public void onSuccess(WalletInfoData response) {
                pullToRefreshScrollView.onLoadComplete(true);
                notice_note.setText(response.getData().getNotice_note());

                tv_balance.setText(CommonUtil.formatPrice4Point(response.getData().getBalance()));
                my_regular_tv.setText(CommonUtil.formatPrice4Point(response.getData().getMonth_total()));
                my_current_tv.setText(CommonUtil.formatPrice4Point(response.getData().getIncome_total()));
            }

            @Override
            public void onFailure(String content) {
                pullToRefreshScrollView.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                pullToRefreshScrollView.showErrorView();
                super.onStatusIsError(message);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_recharge:              //充值
                TCAgent.onEvent(context, "钱包-去充值" + ConstantsValue.android);
                startActivity(new Intent(WalletActivity.this, RechargingActivity1.class));
                break;
            case R.id.rl_trading_record:        //交易记录
                startActivity(new Intent(WalletActivity.this, TradingRecordActivity1.class));
                break;
            case R.id.ll_buy_qa:                //付费提问
                showDialog(getResources().getString(R.string.privilege2), R.drawable.pay_to_ask_show_icon);
                break;
            case R.id.ll_buy_observe:           //一元围观
                showDialog(getResources().getString(R.string.privilege3), R.drawable.yiyuanweiguan_show_icon);
                break;
            case R.id.ll_make_money_for_answer: //回答提问
                showDialog(getResources().getString(R.string.get_hi_coin1), R.drawable.make_money_for_qa_show_icon);
                break;
            case R.id.ll_make_money_for_observe://提问被围观
                showDialog(getResources().getString(R.string.get_hi_coin2), R.drawable.make_money_for_observe_show_icon);
                break;
            default:
                break;

        }
    }

    /**
     * 显示特权的dialog
     *
     * @param info
     * @param imageId
     */
    private void showDialog(String info, int imageId) {
        if (customPrivilegeDialog == null) {
            customPrivilegeDialog = new CustomPrivilegeDialog(this, info, imageId, new CustomPrivilegeDialog.OnCustomDialogListener() {
                @Override
                public void back(String name) {

                }
            });
            customPrivilegeDialog.setCancelable(false);
            customPrivilegeDialog.show();
        }
        customPrivilegeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                customPrivilegeDialog = null;
            }
        });
        customPrivilegeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                customPrivilegeDialog = null;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}

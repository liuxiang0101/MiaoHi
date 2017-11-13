package com.haiqiu.miaohi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.QAHomeAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.WeakHandler;
import com.haiqiu.miaohi.bean.OtherQAData;
import com.haiqiu.miaohi.bean.OthterQAInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.HomeFoundResponse;
import com.haiqiu.miaohi.response.OtherQAInfoResponse;
import com.haiqiu.miaohi.umeng.IUMShareResultListener;
import com.haiqiu.miaohi.umeng.ShareImg;
import com.haiqiu.miaohi.umeng.UmengShare;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.TextUtil;
import com.haiqiu.miaohi.utils.shareImg.SharePersonalHomeImgView;
import com.haiqiu.miaohi.utils.shareImg.ShareQAHomeImageView;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.CommonPersonalInfoView;
import com.haiqiu.miaohi.view.ShareDialog;
import com.haiqiu.miaohi.view.SmoothCheckBox;
import com.haiqiu.miaohi.widget.ShareLayout;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * 应答主页
 * Created by ningl on 16/12/8.
 */
public class QAHomeActivity extends BaseActivity {

    private CommonNavigation cn_qahome;
    private PullToRefreshListView ptrl_qahome;
    private TextView tv_qahomequestionbtn;
    private View headerView;
    private CommonPersonalInfoView cpiv_qahomeheader;
    private TextView tv_qahomejob;
    private TextView tv_qahomedescribe;
    private TextView tv_qahomeincome;
    private TextView tv_qahomeanswercount;
    private TextView tv_qahomecircuseedcount;
    private TextView tv_ohterqapaycount;
    private TextView tv_needpay;
    private TextView tv_qahomemoneyunit;
    private TextView tv_homeqalimitfree;
    private ShareQAHomeImageView sqi_qahome;
    private Dialog reportDialog;

    private ListView lv;
    private QAHomeAdapter adapter;
    private String userId;
    private String userName;
    private int pageIndex;
    private List<OthterQAInfo> data;
    private OtherQAData otherQAData;
    private Handler handler;
    private boolean isDistroy = false;
    private View view;
    private LinearLayout ll_answerandcirclesee_count;
    private boolean isCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qahome);
        userId = getIntent().getExtras().getString("userId");
        userName = getIntent().getExtras().getString("userName");
        data = new ArrayList<>();
        initView();
        lv.setDividerHeight(0);
        showLoading();
        getNetDate();
        tv_qahomequestionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin(false)) return;
                if (otherQAData == null) return;
                startActivityForResult(new Intent(context, QAQuestionActivity.class)
                        .putExtra("answer_userId", userId)
                        .putExtra("question_price", otherQAData.getQuestion_cost()), 200);
//                        .putExtra("userName", otherQAData.getUser_name()), 200);
//                TCAgent.onEvent(context, "向ta提问" + ConstantsValue.android);
            }
        });
        ptrl_qahome.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 0;
                getNetDate();
            }

            @Override
            public void onLoadMore() {
                getNetDate();
            }

        });
    }

    @Override
    protected void onResume() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        if (otherQAData != null && cpiv_qahomeheader != null) {
            MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(userId);
            if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
                otherQAData.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
            }
            cpiv_qahomeheader.setAttention(otherQAData.getAttention_state());
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hiddenLoadingView();
    }

    private void initView() {
        cn_qahome = (CommonNavigation) findViewById(R.id.cn_qahome);
        ptrl_qahome = (PullToRefreshListView) findViewById(R.id.ptrl_qahome);
        tv_qahomequestionbtn = (TextView) findViewById(R.id.tv_qahomequestionbtn);
        tv_ohterqapaycount = (TextView) findViewById(R.id.tv_ohterqapaycount);
        tv_needpay = (TextView) findViewById(R.id.tv_needpay);
        tv_qahomemoneyunit = (TextView) findViewById(R.id.tv_qahomemoneyunit);
        tv_homeqalimitfree = (TextView) findViewById(R.id.tv_homeqalimitfree);
        sqi_qahome = (ShareQAHomeImageView) findViewById(R.id.sqi_qahome);
        lv = ptrl_qahome.getRefreshableView();
        ptrl_qahome.setPullLoadEnabled(true);
        ImageView rightView = new ImageView(context);
        rightView.setImageResource(R.drawable.share);
        cn_qahome.setRightLayoutView(rightView);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rightView.getLayoutParams();
        lp.setMargins(0, 0, DensityUtil.dip2px(context, 10), 0);
        rightView.setLayoutParams(lp);
        if (userName != null) {
            cn_qahome.getBar_title().setText(TextUtil.setBold(userName + "的映答", 0, userName.length()));
        }
        //头部
        headerView = View.inflate(context, R.layout.item_qahomeheader, null);
        ll_answerandcirclesee_count = (LinearLayout) headerView.findViewById(R.id.ll_answerandcirclesee_count);
        cpiv_qahomeheader = (CommonPersonalInfoView) headerView.findViewById(R.id.cpiv_qahomeheader);
        tv_qahomejob = (TextView) headerView.findViewById(R.id.tv_qahomejob);
        tv_qahomedescribe = (TextView) headerView.findViewById(R.id.tv_qahomedescribe);
        tv_qahomeincome = (TextView) headerView.findViewById(R.id.tv_qahomeincome);
        tv_qahomeanswercount = (TextView) headerView.findViewById(R.id.tv_qahomeanswercount);
        tv_qahomecircuseedcount = (TextView) headerView.findViewById(R.id.tv_qahomecircuseedcount);
        lv.addHeaderView(headerView);
        adapter = new QAHomeAdapter(context, data);
        lv.setAdapter(adapter);
        //分享图片
        cn_qahome.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                if (!isLogin(false))
                    return;
                if (otherQAData == null) return;
                final ShareDialog shareDialog = new ShareDialog(QAHomeActivity.this);
                shareDialog.setData();
                shareDialog.setShareLink(otherQAData.getShare_link_address());
                shareDialog.setShareLable(ShareDialog.IMG);
                if (otherQAData != null) {
                    ShareImg shareImg = new ShareImg(QAHomeActivity.this, userId, "", "", "", "");
                    shareImg.setShowDelete(true);
                    shareImg.setDeleteBtnType(ShareLayout.PEOPLE_REPORT);
                    shareDialog.setShareInfo(shareImg);
                }
                shareDialog.setDeleteOrReportListener(new ShareLayout.OnDeleteOrReportListener() {
                    @Override
                    public void onDeleteOrReport(int type) {
                        shareDialog.dismiss();
                    }
                });
                shareDialog.setOnShareImgPath(new ShareLayout.OnShareImgPath() {
                    @Override
                    public void getimgPath(final SHARE_MEDIA platform) {
                        shareDialog.dismiss();
                        ((BaseActivity) context).showLoading();
                        MHLogUtil.i("");
                        sqi_qahome.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                            @Override
                            public void onFinish(Object path) {
                                UmengShare.sharedIMG(QAHomeActivity.this, platform, path, otherQAData.getShare_link_address(), getResources().getString(R.string.share_qaHome), new IUMShareResultListener((BaseActivity) context));
                            }
                        });
                        sqi_qahome.genderImage(otherQAData, platform);
                    }
                });
            }
        });

        cpiv_qahomeheader.setOnAttentionListener(new CommonPersonalInfoView.OnAttentionListener() {
            @Override
            public void onAttention() {
                if (((BaseActivity) context).isLogin(false))
                    attentionOrCancle();
            }
        });
    }

    /**
     * 获取数据
     */
    public void getNetDate() {
        MHRequestParams params = new MHRequestParams();
        params.addParams("user_id", userId);
        params.addParams("page_index", String.valueOf(pageIndex));
        params.addParams("page_size", ConstantsValue.Other.PAGESIZE);
        MHHttpClient.getInstance().post(OtherQAInfoResponse.class, context, ConstantsValue.Url.GETUSERANSWEREDQUESTION, params, new MHHttpHandler<OtherQAInfoResponse>() {
            @Override
            public void onSuccess(OtherQAInfoResponse response) {
                otherQAData = response.getData();
                if (otherQAData == null) {
                    ptrl_qahome.showErrorView();
                    return;
                } else {
                    ptrl_qahome.hideAllTipView();
                }
                if (pageIndex == 0) {
                    CommonPersonInfo info = new CommonPersonInfo();
                    info.setHeadUri(otherQAData.getUser_portrait());
                    info.setName(otherQAData.getUser_name());
                    info.setName_nodescribe(otherQAData.getUser_name());
                    info.setDescribe(otherQAData.getVip_note());
                    info.setUserType(otherQAData.getUser_type());
                    info.setShownGender(true);
                    info.setUserId(userId);
                    info.setGender(otherQAData.getUser_gender());
                    cpiv_qahomeheader.setUserInfo(info);
                    cpiv_qahomeheader.isShowAttentionBtn(true);

                    cpiv_qahomeheader.setAttention(otherQAData.getAttention_state());
                    tv_qahomejob.setVisibility(MHStringUtils.isEmpty(otherQAData.getUser_vip_remarks()) ? View.GONE : View.VISIBLE);
                    tv_qahomejob.setText(otherQAData.getUser_vip_remarks());
                    tv_qahomedescribe.setText(otherQAData.getUser_note());
                    tv_qahomeincome.setText("总收入 " + CommonUtil.formatPrice(otherQAData.getIncome_amount()) + "嗨币");
                    tv_qahomeanswercount.setText("回答 " + otherQAData.getAnswered_amount());
                    tv_qahomecircuseedcount.setText("被围观 " + otherQAData.getObserved_amount());
                    if(otherQAData.getAnswered_amount() == 0
                            && otherQAData.getObserved_amount() == 0){
                        ll_answerandcirclesee_count.setVisibility(View.GONE);
                    }
                    tv_ohterqapaycount.setText(CommonUtil.formatPrice(otherQAData.getQuestion_cost()));
                    if (otherQAData.getUser_gender() == 1) {
                        tv_qahomequestionbtn.setText("向他提问");
                    } else {
                        tv_qahomequestionbtn.setText("向她提问");
                    }
                    if (otherQAData.getQuestion_cost() == 0) {
                        tv_homeqalimitfree.setVisibility(View.VISIBLE);
                    } else {
                        tv_homeqalimitfree.setVisibility(View.GONE);
                    }
                    if (!isCountDown) {
                        isCountDown = true;
                        handlerCountDown();
                    }
                }

                if (otherQAData.getQuestion_cost() == 0) {
                    //限时免费
                    tv_needpay.setVisibility(View.GONE);
                    tv_qahomemoneyunit.setVisibility(View.GONE);
                    tv_ohterqapaycount.setVisibility(View.GONE);
                    tv_homeqalimitfree.setVisibility(View.VISIBLE);
                } else {
                    //需支付
                    tv_needpay.setVisibility(View.VISIBLE);
                    tv_qahomemoneyunit.setVisibility(View.VISIBLE);
                    tv_ohterqapaycount.setVisibility(View.VISIBLE);
                    tv_homeqalimitfree.setVisibility(View.GONE);

                }
                if (pageIndex == 0 && (null == otherQAData || null == otherQAData.getPage_result() || otherQAData.getPage_result().size() == 0)) {
                } else {
                    ptrl_qahome.hideAllTipView();
                }

                if (null == otherQAData || null == otherQAData.getPage_result() || otherQAData.getPage_result().size() == 0)
                    ptrl_qahome.onLoadComplete(false);
                else {
                    ptrl_qahome.onLoadComplete(true);
                }
                setData(otherQAData.getPage_result());
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                ptrl_qahome.onLoadComplete(true);
                if (pageIndex == 0)
                    ptrl_qahome.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                ptrl_qahome.onLoadComplete(true);
                if (pageIndex == 0)
                    ptrl_qahome.showErrorView();
            }
        });
    }

    /**
     * 添加数据
     *
     * @param pageResult
     */
    private void setData(List<OthterQAInfo> pageResult) {
        if (pageIndex == 0) {
            if (pageResult == null || pageResult.isEmpty()) {
                addBlank();
                return;
            }
            data.clear();
        }
        this.data.addAll(pageResult);
        if (!data.isEmpty()) removeBlank();
        if (adapter == null) {
            adapter = new QAHomeAdapter(context, data);
            lv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 倒计时
     */
    public void handlerCountDown() {
        if (handler != null) return;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDistroy) return;
                handler.postDelayed(this, 1000);
                handleTime();
            }
        }, 1000);
    }

    /**
     * 处理显示的时间
     */
    public void handleTime() {
        for (int i = 0; i < data.size(); i++) {
            OthterQAInfo qaInfo = data.get(i);
            if (qaInfo.getTime_remain() <= 0) { //如果时间到了则变成xx元围观
                qaInfo.setTemporary_free(false);
            } else if (qaInfo.getTime_remain() > 0
                    && !qaInfo.is_question_owner()
                    && qaInfo.getTemporary_free()) {
                qaInfo.setTime_remain(qaInfo.getTime_remain() - 1000);
            }
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDistroy = true;
        if (reportDialog != null) reportDialog.dismiss();
        UMShareAPI.get(this).release();
    }

    /**
     * 添加空白页
     */
    private void addBlank() {
        if (view == null) {
            view = View.inflate(context, R.layout.qahomeblank, null);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.width = ScreenUtils.getScreenWidth(context);
            lp.height = ScreenUtils.getScreenWidth(context);
            view.setLayoutParams(lp);
            view.setEnabled(false);
            lv.addFooterView(view);
        }
    }

    /**
     * 移除空白页
     */
    private void removeBlank() {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hiddenLoadingView();
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            if (data == null) return;
            if (resultCode == ConstantsValue.Other.ASKQUESTION_RESULT) {
                showSelectDialog(data.getExtras().getLong("questionprice"));
            }
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    /**
     * 关注
     */
    public void attentionOrCancle() {
        if (otherQAData == null) return;
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("action_mark", !otherQAData.getAttention_state() + "");
        requestParams.addParams("user_id", userId);
        MHHttpClient.getInstance().post(HomeFoundResponse.class, context, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<HomeFoundResponse>() {
            @Override
            public void onSuccess(HomeFoundResponse response) {
                otherQAData.setAttention_state(!otherQAData.getAttention_state());
                MHStateSyncUtil.pushSyncEvent(context, userId, otherQAData.getAttention_state());
                cpiv_qahomeheader.setAttention(otherQAData.getAttention_state());
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

    /**
     * 支付成功
     *
     * @param question_price
     */
    private void showSelectDialog(long question_price) {
        View view = getLayoutInflater().inflate(R.layout.dialog_pay_success, null);
        SmoothCheckBox smoothCheckBox = (SmoothCheckBox) view.findViewById(R.id.scb);
        smoothCheckBox.setChecked(true, true);
        TextView tv_title = (TextView) view.findViewById(R.id.title);
        tv_title.setText("提问成功");
        TextView message = (TextView) view.findViewById(R.id.message);
        if (question_price == 0) {
//            message.setVisibility(View.GONE);
            showToastAtCenter("提问成功");
            return;
        } else {
            message.setVisibility(View.VISIBLE);
            message.setText("扣款" + CommonUtil.formatPrice((int) question_price) + "嗨币\n" + "余额请去【我的】 -【钱包】页面查看");
        }
        if (reportDialog == null) {
            reportDialog = new Dialog(context, R.style.Dialog_loading);
            reportDialog.getWindow().setGravity(Gravity.CENTER);
            reportDialog.getWindow().setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = reportDialog.getWindow().getAttributes();
            lp.width = display.getWidth();
            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lp.dimAmount = 0.6f;
            reportDialog.getWindow().setAttributes(lp);
            reportDialog.show();
        } else {
            reportDialog.show();
        }

        //防止handler内存泄漏
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    if (reportDialog != null && reportDialog.isShowing()) {
                        reportDialog.dismiss();
                    }
                }
            }
        }, 3000);
    }
}

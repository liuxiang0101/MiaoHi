package com.haiqiu.miaohi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.WeakHandler;
import com.haiqiu.miaohi.bean.GetQAInfoRecommendObject;
import com.haiqiu.miaohi.bean.GetQAObserveUserObject;
import com.haiqiu.miaohi.bean.GetQuestionInfoData;
import com.haiqiu.miaohi.bean.ShareVideoAndImgInfo;
import com.haiqiu.miaohi.bean.VideoExtraInfo;
import com.haiqiu.miaohi.bean.VideoInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.RefreshChoiceListEvent;
import com.haiqiu.miaohi.response.ConfirmRequestResponse;
import com.haiqiu.miaohi.response.GetQuestionInfoResponse;
import com.haiqiu.miaohi.response.PayQAResultResponse;
import com.haiqiu.miaohi.umeng.IUMShareResultListener;
import com.haiqiu.miaohi.umeng.ShareImg;
import com.haiqiu.miaohi.umeng.UmengShare;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.NoDoubleClickUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.shareImg.AbsShareImg;
import com.haiqiu.miaohi.utils.shareImg.ShareQADetailView;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.CommonPersonalInfoView;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.view.ShareDialog;
import com.haiqiu.miaohi.view.SlowScrollView;
import com.haiqiu.miaohi.view.SmoothCheckBox;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.haiqiu.miaohi.widget.PriceChangeDialog;
import com.haiqiu.miaohi.widget.ShareLayout;
import com.haiqiu.miaohi.widget.mediaplayer.BaseVideoView;
import com.haiqiu.miaohi.widget.mediaplayer.MyMediaPlayer;
import com.haiqiu.miaohi.widget.mediaplayer.VideoView;
import com.haiqiu.miaohi.widget.mediaplayer.VideoViewWrap;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by LiuXiang on 2016/7/19.
 * 映答详情界面
 */
public class InterlocutionDetailsActivity extends BaseActivity {
    private final static String TAG = "InterlocutionDetailsActivity";
    private RelativeLayout rl_contain_video;
    private CommonNavigation cn;
    private VideoView videoView;
    private VideoViewWrap vvw_videodetail;
    private MyMediaPlayer mediaPlayer;
    private MyCircleView iv_questioner_portrait;
    private TextView tv_questioner_name;
    private TextView tv_question_price;
    private TextView tv_the_question;
    private TextView tv_observe_info;
    private TextView tv_qa_private;
    private ImageView iv_share;
    private ImageView iv_isvip_small;
    private ImageView iv_unpay_video_cover;
    private CommonPersonalInfoView personalview;
    private CommonPersonalInfoView cpiv_qa_recommend;
    private TextView tv_qa_recommenddescribe;
    private TextView tv_qarecommendcircusee_count;
    private TextView tv_circuseecount;
    private RelativeLayout rl_qadetailbtn;
    private TextView tv_qadetailbtntext;
    private ShareQADetailView sqdv_shareqadetailview;
    private RelativeLayout rl_qadetail_btntop;
    private ImageView iv_timelimitfree_top;
    private TextView tv_qadetailbtntext_top;
    private LinearLayout ll_qadetaillist;
    private TextView tv_qaetail_remaintime;
    private TextView tv_headertime;

    private ShareDialog shareDialog;
    private LinearLayout ll_value_look;
    private LinearLayout ll_observer_portrait;
    private SlowScrollView sv;
    private GetQuestionInfoData data;
    private String question_id;
    private Dialog reportDialog;
    private int selectedPosition;
    private ImageLoader imageLoader;
    //    private ScreenSwitchUtils screenSwitchUtils;
    private Handler handler;
    private int multipleScreenSize;
    private int screenWidth;
    private static final int CLICKPLAY = 0;     //点击播放
    private static final int LIMITTIMEFREE = 1; //限时免费
    private static final int PAYCIRCUSEE = 2;   //xx元围观

    public static final int PAYRESULT = 111;
    private boolean needCountDown = true;//是否开启倒计时 默认开启
    private View qadetailvideoline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlocution_detail);
        question_id = getIntent().getStringExtra("question_id");
        selectedPosition = getIntent().getIntExtra("selectedPosition", -1);
//        screenSwitchUtils = ScreenSwitchUtils.init(this);
//        setOnSwitchScreenListener();
        initView();
        RelativeLayout.LayoutParams videoLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        videoLp.width = ScreenUtils.getScreenWidth(context);
        videoLp.height = ScreenUtils.getScreenWidth(context);
        vvw_videodetail.setLayoutParams(videoLp);
        ImageView rightView = new ImageView(context);
        rightView.setImageResource(R.drawable.share);
        cn.setRightLayoutView(rightView);
        TCAgent.onEvent(context, "映答详情打开总次数" + ConstantsValue.android);
        showLoading();
        getNetData();
        cn.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                //分享
                if (isLogin(false))
                    share();
            }
        });
        tv_circuseecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterlocutionDetailsActivity.this, OnLookersListAvtivity.class);
                intent.putExtra("question_id", question_id);
                intent.putExtra("isObserve", true);
                startActivity(intent);
                TCAgent.onEvent(context, "映答详情围观列表" + ConstantsValue.android);
            }
        });

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "yingdadetailspage");
            jsonObject.put("description", "映答详情页");
            if (null != question_id)
                jsonObject.put("id", question_id);
            BehaviourStatistic.uploadBehaviourInfo(jsonObject);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 控件的初始化
     */
    private void initView() {
        screenWidth = ScreenUtils.getScreenSize(this).x;
        multipleScreenSize = (int) (screenWidth * 0.6);
        sv = (SlowScrollView) findViewById(R.id.sv);
        rl_contain_video = (RelativeLayout) findViewById(R.id.rl_contain_video);
        cn = (CommonNavigation) findViewById(R.id.cn);
        vvw_videodetail = (VideoViewWrap) findViewById(R.id.vvw_videodetail);
        personalview = (CommonPersonalInfoView) findViewById(R.id.personalview);
        cpiv_qa_recommend = (CommonPersonalInfoView) findViewById(R.id.cpiv_qa_recommend);
        tv_qa_recommenddescribe = (TextView) findViewById(R.id.tv_qa_recommenddescribe);
        sqdv_shareqadetailview = (ShareQADetailView) findViewById(R.id.sqdv_shareqadetailview);
        tv_headertime = (TextView) findViewById(R.id.tv_headertime);

        tv_questioner_name = (TextView) findViewById(R.id.tv_questioner_name);
        tv_question_price = (TextView) findViewById(R.id.tv_question_price);
        tv_the_question = (TextView) findViewById(R.id.tv_the_question);
        tv_qa_private = (TextView) findViewById(R.id.tv_qa_private);
        tv_observe_info = (TextView) findViewById(R.id.tv_observe_info);
        tv_qarecommendcircusee_count = (TextView) findViewById(R.id.tv_qarecommendcircusee_count);

        iv_isvip_small = (ImageView) findViewById(R.id.iv_isvip_small);
        iv_unpay_video_cover = (ImageView) findViewById(R.id.iv_unpay_video_cover);
        iv_questioner_portrait = (MyCircleView) findViewById(R.id.iv_questioner_portrait);
        tv_circuseecount = (TextView) findViewById(R.id.tv_circuseecount);
        rl_qadetailbtn = (RelativeLayout) findViewById(R.id.rl_qadetailbtn);
        tv_qadetailbtntext = (TextView) findViewById(R.id.tv_qadetailbtntext);
        rl_qadetail_btntop = (RelativeLayout) findViewById(R.id.rl_qadetail_btntop);
        iv_timelimitfree_top = (ImageView) findViewById(R.id.iv_timelimitfree_top);
        tv_qadetailbtntext_top = (TextView) findViewById(R.id.tv_qadetailbtntext_top);
        ll_qadetaillist = (LinearLayout) findViewById(R.id.ll_qadetaillist);
        tv_qaetail_remaintime = (TextView) findViewById(R.id.tv_qaetail_remaintime);
        qadetailvideoline = findViewById(R.id.qadetailvideoline);

        ll_value_look = (LinearLayout) findViewById(R.id.ll_value_look);
        ll_observer_portrait = (LinearLayout) findViewById(R.id.ll_observer_portrait);
        mediaPlayer = new MyMediaPlayer();
        iv_share = new ImageView(context);
        imageLoader = ImageLoader.getInstance();

        videoView = (VideoView) vvw_videodetail.getVideoView();
        addEvent();
    }

    private void addEvent() {
        sv.setVisibility(View.INVISIBLE);
        iv_questioner_portrait.setOnClickListener(l);
//        tv_watched_total.setOnClickListener(l);
        tv_observe_info.setOnClickListener(l);
        ll_value_look.setOnClickListener(l);
        ll_observer_portrait.setOnClickListener(l);
        iv_unpay_video_cover.setOnClickListener(l);
        tv_questioner_name.setOnClickListener(l);
        rl_qadetail_btntop.setOnClickListener(l);
    }

    /**
     * 获取服务器数据
     */
    private void getNetData() {
        showMHLoading(true, false);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("question_id", question_id);
        MHHttpClient.getInstance().post(GetQuestionInfoResponse.class, context, ConstantsValue.Url.GET_QUESTION_INFO, requestParams, new MHHttpHandler<GetQuestionInfoResponse>() {
            @Override
            public void onSuccess(GetQuestionInfoResponse response) {
                data = response.getData();
                if (null == data) {
                    showBlankView();
                } else {
                    hideBlankView();
                }
                iv_share.setEnabled(true);
                setDataForView(response);
                //存在限时免费则启动倒计时
                if (getQAStat(data) == LIMITTIMEFREE
                        || getRecommendQAStat(data.getRecommend_question()) == LIMITTIMEFREE) {
                    handlerCountDown(data);
                }
                sv.setVisibility(View.VISIBLE);
                hideErrorView();
            }

            @Override
            public void onFailure(String content) {
                showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
    }

    /**
     * 无网络时点击重新刷新
     */
    @Override
    protected void reTry() {
        super.reTry();
        showMHLoading(true, false);
        getNetData();
    }

    /**
     * 播放视频，隐藏覆盖控件的逻辑
     */
    private void startPlayVideo() {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setVideo_state(data.getVideo_state());
        videoInfo.setHls_uri(data.getHls_uri());
        videoInfo.setVideo_uri(data.getVideo_uri());
        videoInfo.setVideo_id(data.getVideo_id());
        videoInfo.setHls_uri_state(data.getHls_uri_state());
        videoView.startPlay(mediaPlayer, videoInfo);
    }

    /**
     * 将请求到的数据展示
     */
    private void setDataForView(GetQuestionInfoResponse response) {
        ll_observer_portrait.removeAllViews();
        final GetQuestionInfoData data = response.getData();
        CommonPersonInfo personInfo = new CommonPersonInfo();
        personInfo.setHeadUri(data.getAnswer_portrait_uri());
        personInfo.setDescribe(data.getAnswer_authentic());
        personInfo.setName(data.getAnswer_user_name());
        personInfo.setName_nodescribe(data.getAnswer_user_name());
        personInfo.setUserType(data.getAnswer_user_type());
        personInfo.setTime(data.getAnswer_time_text());
        personInfo.setUserId(data.getAnswer_user_id());
        personalview.setUserInfo(personInfo);
        personalview.setAttention(false);
        personalview.isShowAttentionBtn(true);
        if (null != data.getAnswer_user_id() && data.getAnswer_user_id().equals(UserInfoUtil.getUserId(context))) {
            personalview.isShowAttentionBtn(false);
        }

        personalview.setText("我也要问");
        personalview.setOnAttentionListener(new CommonPersonalInfoView.OnAttentionListener() {
            @Override
            public void onAttention() {
                if (isLogin(false)) {
                    if (null != data && !MHStringUtils.isEmpty(data.getAnswer_user_id())) {
                        startActivityForResult(new Intent(context, QAHomeActivity.class)
                                .putExtra("userId", data.getAnswer_user_id())
                                .putExtra("userName", data.getAnswer_user_name()), 200);
                    }
                }
            }
        });
        tv_the_question.setText(data.getQuestion_text());
        tv_questioner_name.setText(data.getQuestion_user_name());

        //是否是私密问题
        if (data.getQuestion_private() == 1) {
            tv_qa_private.setVisibility(View.VISIBLE);
        } else {
            tv_qa_private.setVisibility(View.GONE);
//            titleBarSetting();
        }
        //提问者是否大咖
        iv_isvip_small.setVisibility(data.getQuestion_user_type() > 10 ? View.VISIBLE : View.GONE);
        tv_question_price.getPaint().setFakeBoldText(true);
        tv_question_price.setText(CommonUtil.formatPrice(data.getQuestion_price()) + "嗨币");
//        iv_to_pay.setImageResource(data.getVideo_price() <= 0 ? R.drawable.button_pay_limit_free : R.drawable.button_pay_weiguan);
        imageLoader.displayImage(data.getQuestion_portrait_uri(), iv_questioner_portrait, DisplayOptionsUtils.getHeaderDefaultImageOptions());

        //围观人的头像显示
//        setObserverList(data.getObserve_user_result());
        addCircusee(tv_circuseecount, ll_observer_portrait, data.getObserve_user_result(), false);
        tv_observe_info.setText("围观 " + data.getAnswer_user_name() + " 的全部" + data.getAnswer_total() + "条映答 >");
        if (data.getObserve_user_result().size() <= 1) tv_observe_info.setVisibility(View.GONE);
        GetQAInfoRecommendObject obj = data.getRecommend_question();
        if (data.getObserve_count() == 0) {
            qadetailvideoline.setVisibility(View.GONE);
        }

        if (obj != null) {
            CommonPersonInfo info = new CommonPersonInfo();
            info.setTime(obj.getAnswer_time_text());
            info.setName_nodescribe(obj.getAnswer_user_name());
            info.setName(obj.getAnswer_user_name());
            info.setUserType(obj.getAnswer_user_type());
            info.setDescribe(obj.getAnswer_authentic());
            info.setUserId(obj.getAnswer_user_id());
            info.setHeadUri(obj.getAnswer_user_portrait());
            cpiv_qa_recommend.setUserInfo(info);
            tv_qa_recommenddescribe.setText(obj.getQuestion_text());
            tv_qarecommendcircusee_count.setText(obj.getObserve_count() + "");
        }
        //设置视频播放
        setVideo(data);
        tv_qadetailbtntext_top.setText(data.getObserve_price_text());
        tv_qadetailbtntext.setText(obj.getObserve_price_text());
        if (obj.is_question_owner()) {
            //点击播放
            rl_qadetailbtn.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
        } else {
            if (obj.isTemporary_free()) {
                //限时免费
                rl_qadetailbtn.setBackgroundResource(R.drawable.shape_limit_free_radius_red_selector);
            } else {
                //xx元围观
                rl_qadetailbtn.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
            }
        }
        //设置顶部qabutton状态
        switch (getQAStat(data)) {
            case CLICKPLAY: //点击播放
                rl_qadetail_btntop.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                break;
            case LIMITTIMEFREE: //限时免费
                rl_qadetail_btntop.setBackgroundResource(R.drawable.shape_limit_free_radius_red_selector);
                break;
            case PAYCIRCUSEE: //xx元围观
                rl_qadetail_btntop.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                break;
        }
        //设置播放器尺寸
        double rat = 0;
        RelativeLayout.LayoutParams videoLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (data.getVideo_width() == 0
                || data.getVideo_height() == 0) {
            videoLp.width = ScreenUtils.getScreenWidth(context);
            videoLp.height = ScreenUtils.getScreenWidth(context);
        } else if (data.getVideo_width() >= data.getVideo_height()) {
            rat = data.getVideo_height() / data.getVideo_width();
            videoLp.width = ScreenUtils.getScreenWidth(context);
            videoLp.height = (int) (ScreenUtils.getScreenWidth(context) * rat);
        } else if (data.getVideo_width() < data.getVideo_height()) {
            //高大于宽
            if (data.getVideo_height() * (3d / 4d) >= data.getVideo_width()) {
                rat = 3d / 4d;
            } else {
                rat = data.getVideo_width() / data.getVideo_height();
            }
            videoLp.width = ScreenUtils.getScreenWidth(context);
            videoLp.height = (int) (ScreenUtils.getScreenWidth(context) / rat);
        }
        vvw_videodetail.setLayoutParams(videoLp);


        VideoExtraInfo videoExtraInfo = new VideoExtraInfo();
        videoExtraInfo.videoId = data.getVideo_id();
//        videoExtraInfo.playNum = data.getPlay_total();
        videoExtraInfo.videoDuration = data.getDuration_second();
        videoExtraInfo.setVideoType(VideoExtraInfo.VideoType.VIDEO_TYPE_YD);
        videoExtraInfo.subjectName = data.getActivity_name();
        videoExtraInfo.subjectUri = data.getActivity_uri();
//        videoExtraInfo.recommendStr = data.getRecommend_text();
        videoView.setVideoExtraInfo(videoExtraInfo);
    }


    /**
     * 对围观的视频进行支付
     */
    private void payVideo() {
        if (null == data
                || data.is_question_owner())
            return;//表示已经支付过了
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("question_id", question_id);
        if (!data.is_question_owner() && data.isTemporary_free()) {
            //限时免费
            requestParams.addParams("onlook_price", "0");
        } else {
            requestParams.addParams("onlook_price", data.getObserve_price());
        }
        MHHttpClient.getInstance().post(PayQAResultResponse.class, ConstantsValue.Url.ON_LOOK_ANSWER, requestParams, new MHHttpHandler<PayQAResultResponse>() {
            @Override
            public void onSuccess(PayQAResultResponse response) {
                if (!data.is_question_owner() && data.isTemporary_free())
                    TCAgent.onEvent(context, "映答详情围观成功" + ConstantsValue.android);
                else
                    TCAgent.onEvent(context, "映答详情围观支付成功" + ConstantsValue.android);
//                if (data.getPlay_total() < 99998) {
//                    data.setPlay_total(data.getPlay_total() + 1);
//                }
                if (!data.isTemporary_free() && !data.isTemporary_free()) {
                    //xx元围观
                    showToastAtCenter("您已支付成功");
                    data.setIs_question_owner(true);
                    ((TextView) rl_qadetail_btntop.getChildAt(1)).setText("点击播放");
                }
                // 刷新状态
                if (selectedPosition != -1) {
                    RefreshChoiceListEvent event = new RefreshChoiceListEvent(question_id, selectedPosition);
                    EventBus.getDefault().post(event);
                }
                Intent intent = new Intent(ConstantsValue.IntentFilterAction.PAY_SUCCESS_ACTION);
                intent.putExtra(ConstantsValue.IntentFilterAction.PAY_SUCCESS_QUESTION_ID_KEY, question_id);
                sendBroadcast(intent);

                if (response.getData().getObserve_increase() == ConstantsValue.Other.PAYQARESULT_NO) {
                    GetQAObserveUserObject observeUserObject = new GetQAObserveUserObject();
                    observeUserObject.setObserve_portrait_uri(UserInfoUtil.getUserHeader(context));
                    observeUserObject.setObserve_user_id(UserInfoUtil.getUserId(context));
                    observeUserObject.setObserve_user_type(UserInfoUtil.getUserType1(context));
                    List<GetQAObserveUserObject> list = data.getObserve_user_result();
                    list.add(0, observeUserObject);
                    addCircusee(tv_circuseecount, ll_observer_portrait, list, true);
                }
                rl_qadetail_btntop.setVisibility(View.GONE);
                enlargeVideoLogic();
                startPlayVideo();
                hiddenLoadingView();
            }

            @Override
            public void onFailure(String content) {
                showToastAtBottom("支付围观费用失败");
                hiddenLoadingView();
            }

            @Override
            public void onStatusIsError(String message) {
                hiddenLoadingView();
                showToastAtBottom(message);
                super.onStatusIsError(message);
            }
        });
    }

    //后台服务器确认支付成功
    public void confirmPaySuccess() {
        TCAgent.onEvent(context, "映答详情围观" + ConstantsValue.android);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("type", "10");//二次确认类型，10：围观，30：提问
        requestParams.addParams("answer_user", data.getAnswer_user_id());
        requestParams.addParams("question_id", data.getQuestion_id());
        if (!data.is_question_owner() && data.getTemporary_free()) {
            requestParams.addParams("price", 0 + "");
        } else {
            requestParams.addParams("price", data.getObserve_price());
        }
        MHHttpClient.getInstance().post(ConfirmRequestResponse.class, ConstantsValue.Url.CONFIRMPAYSECONDREQUEST, requestParams, new MHHttpHandler<ConfirmRequestResponse>() {
            @Override
            public void onSuccess(ConfirmRequestResponse response) {
                ConfirmRequestResponse.DataBean dataBean = response.getData();
                if (dataBean == null) return;
                if (dataBean.getState() == ConstantsValue.Other.CONFIRM_PAYSECONDRE_BALANCE) {//余额足够时
                    //记录围观数量
                    payVideo();
                } else if (dataBean.getState() == ConstantsValue.Other.CONFIRM_PAYSECONDRE_CIRCUSEE) {//围观金额改变
                    data.setObserve_price(dataBean.getPrice());
                    PriceChangeDialog dialog = new PriceChangeDialog(context, dataBean.getPrice());
                    dialog.show();
                    dialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                        @Override
                        public void onRightButtonOnClick() {
                            confirmPaySuccess();
                        }
                    });
                    hiddenLoadingView();
                } else if (dataBean.getState() == ConstantsValue.Other.CONFIRM_PAYSECONDRE_NOBALANCE) {//余额不足时
                    final CommonDialog commonDialog = new CommonDialog(context);
                    commonDialog.setCancelable(false);
                    commonDialog.setLeftButtonMsg("取消");
                    commonDialog.setRightButtonMsg("去充值");
                    commonDialog.setTitleMsg(response.getData().getTitle());
                    commonDialog.setContentMsg(response.getData().getContent());

                    commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
                        @Override
                        public void onLeftButtonOnClick() {
                            if (!isFinishing()) {
                                commonDialog.dismiss();
                            }
                            hiddenLoadingView();
                        }
                    });
                    commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                        @Override
                        public void onRightButtonOnClick() {
                            Intent intent = new Intent();
                            intent.setClass(InterlocutionDetailsActivity.this, RechargingActivity1.class);
                            intent.putExtra("not_wallet", "not_wallet");

                            startActivityForResult(intent, 400);
                        }
                    });
                    commonDialog.show();
                }
            }

            @Override
            public void onFailure(String content) {
                showToastAtBottom("网络不佳，请稍后重试");
                hiddenLoadingView();
            }

            @Override
            public void onStatusIsError(String message) {
                hiddenLoadingView();
                super.onStatusIsError(message);
            }
        });
    }

    /**
     * 设置视频相关
     */
    private void setVideo(final GetQuestionInfoData data) {
        //视频封面
        imageLoader.displayImage(data.getCover_uri(), iv_unpay_video_cover);
        imageLoader.displayImage(data.getCover_uri(), videoView.getPreviewImageView());
        videoView.setVideoControlListener(new BaseVideoView.VideoControlListener() {
            @Override
            public void videoViewState(BaseVideoView.VideoViewState videoViewState, int extra) {
                switch (videoViewState) {
                    case ON_START_PLAY_CLICK:
                        VideoInfo videoInfo = new VideoInfo();
                        videoInfo.setVideo_state(data.getVideo_state());
                        videoInfo.setHls_uri(data.getHls_uri());
                        videoInfo.setVideo_uri(data.getVideo_uri());
                        videoInfo.setVideo_id(data.getVideo_id());
                        videoInfo.setHls_uri_state(data.getHls_uri_state());
                        videoView.startPlay(mediaPlayer, videoInfo);
                        break;
                    case ON_VIDEO_COMPLETE:
                        rl_qadetail_btntop.setVisibility(View.VISIBLE);
                        videoView.setPlayButtonEnableVisible(false);
                        break;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        hiddenLoadingView();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        if (null != shareDialog && shareDialog.isShowing())
            shareDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hiddenLoadingView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();

        if (null != reportDialog) {
            reportDialog.dismiss();
            reportDialog = null;
        }
        UMShareAPI.get(this).release();
    }

    /**
     * 控件点击事件监听
     */
    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.iv_unpay_video_cover:
                    if (data.getVideo_price() > 0) return;
//                    case R.id.iv_to_pay:
//                        MHLogUtil.e(TAG, "点击 一元围观/限时免费 按钮");
//                        clickToPlayLogic();
                    break;
                case R.id.tv_questioner_name:
                case R.id.iv_questioner_portrait:
                    if (!isLogin(false)) return;
                    intent = new Intent(InterlocutionDetailsActivity.this, PersonalHomeActivity.class);
                    intent.putExtra("userId", data.getQuestion_user_id());
                    startActivity(intent);
                    break;
                case R.id.tv_observe_info:
                    if (!isLogin(false)) return;
                    if (null != data && !MHStringUtils.isEmpty(data.getAnswer_user_id())) {
                        TCAgent.onEvent(context, "映答详情跳全部页" + ConstantsValue.android);
                        intent = new Intent(InterlocutionDetailsActivity.this, QAHomeActivity.class);
                        intent.putExtra("userId", data.getAnswer_user_id());
                        intent.putExtra("userName", data.getAnswer_user_name());
                        startActivity(intent);
                    }
                    break;
                case R.id.ll_observer_portrait:
                    if (data.getPlay_total() != 0) {
                        intent = new Intent(InterlocutionDetailsActivity.this, OnLookersListAvtivity.class);
                        intent.putExtra("question_id", question_id);
                        intent.putExtra("isObserve", true);
                        startActivity(intent);
                        TCAgent.onEvent(context, "映答详情围观列表" + ConstantsValue.android);
                    }
                    break;
                case R.id.ll_value_look:
                    TCAgent.onEvent(context, "映答详情-值得一看" + ConstantsValue.android);
                    intent = new Intent(InterlocutionDetailsActivity.this, InterlocutionDetailsActivity.class);
                    intent.putExtra("question_id", data.getRecommend_question().getQuestion_id());
                    startActivityForResult(intent, PAYRESULT);
                    break;

                case R.id.rl_qadetail_btntop://点击应答按钮
                    if (!isLogin(false)) {
                        return;
                    }
                    switch (getQAStat(data)) {
                        case CLICKPLAY: //点击播放
                            rl_qadetail_btntop.setVisibility(View.GONE);
                            enlargeVideoLogic();
                            startPlayVideo();
                            break;
                        case LIMITTIMEFREE: //限时免费
                            confirmPaySuccess();
                            break;

                        case PAYCIRCUSEE: //xx围观
                            confirmPaySuccess();
                            break;
                    }
                    break;
            }
        }
    };

    private void clickToPlayLogic() {
        if (!NoDoubleClickUtils.isDoubleClick()) {
            if (data.getVideo_price() <= 0) {
                //点击播放
                enlargeVideoLogic();
//                if (data.getObserve_state() == 0) {
//                    payVideo();
//                }
//                startPlayVideo();
            } else {
                //一元围观
                TCAgent.onEvent(context, "映答详情-一元围观支付" + ConstantsValue.android);
                confirmPaySuccess();
            }
        } else {
            showToastAtBottom("您的点击过于频繁，稍后再试");
        }
    }

    private void enlargeVideoLogic() {
        sv.smoothScrollToSlow(0, (int) rl_contain_video.getY(), 800);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != VideoView.backDownListener) {
                return VideoView.backDownListener.OnBackDown();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hiddenLoadingView();
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
        if (data == null) return;
        //充值成功后
        if (resultCode == RechargingActivity1.PAYRESULT_CODE) {
            confirmPaySuccess();
        } else if (resultCode == PAYRESULT) {
            boolean isQuestionOwner = data.getBooleanExtra("is_question_owner", false);
            if (isQuestionOwner) {
                //上一界面该视频已经播放过 则该界面修改状态
                rl_qadetailbtn.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                tv_qadetailbtntext.setText("点击播放");
                this.data.setIs_question_owner(isQuestionOwner);
            }
        } else if (resultCode == ConstantsValue.Other.ASKQUESTION_RESULT) {
            showSelectDialog(data.getExtras().getLong("questionprice"));
        }
    }

    /**
     * 设置屏幕旋转监听
     */
//    private void setOnSwitchScreenListener() {
//        screenSwitchUtils.setOnScreenRotateListener(new ScreenSwitchUtils.OnScreenRotate() {
//            @Override
//            public void onScreenRotate(int requestedOrientation) {
//                if (videoView.getVideoWidth() > videoView.getVideoHeight()
//                        && videoView.getCurrentOrientation() != requestedOrientation) {
//                    switch (requestedOrientation) {
//                        //切换成横屏
//                        case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
//                        case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
//                            closeAllDialog();
//                            videoView.setRequestOrientation(requestedOrientation);
//                            videoView.setFullScreen(requestedOrientation);
//                            break;
//
//                        //切换成竖屏
//                        case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
//                            closeAllDialog();
//                            videoView.quitFullScreen();
//                            break;
//                    }
//                }
//            }
//        });
//    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 关闭所有dialog
     */
    private void closeAllDialog() {
        if (null != shareDialog && shareDialog.isShowing()) shareDialog.dismiss();
        if (null != reportDialog && reportDialog.isShowing()) reportDialog.dismiss();
    }

    /**
     * @param data
     * @return 0:点击播放 1：限时免费 2：xx元围观（倒计时）
     */
    private int getQAStat(GetQuestionInfoData data) {
        if (data.is_question_owner()) return CLICKPLAY;
        if (data.getTemporary_free()) return LIMITTIMEFREE;
        else return PAYCIRCUSEE;
    }

    /**
     * @param obj
     * @return 0:点击播放 1：限时免费 2：xx元围观（倒计时）
     */
    private int getRecommendQAStat(GetQAInfoRecommendObject obj) {
        if (obj.is_question_owner()) return CLICKPLAY;
        if (obj.isTemporary_free()) return LIMITTIMEFREE;
        else return PAYCIRCUSEE;
    }


    /**
     * 分享
     */
    private void share() {
        if (data == null) return;
        final ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.setData();
        shareDialog.setShareLink(data.getShare_link_address());
        shareDialog.setShareLable(ShareDialog.IMG);
        if (!TextUtils.equals(data.getAnswer_user_id(), UserInfoUtil.getUserId(context))
                || !TextUtils.equals(data.getAnswer_user_id(), UserInfoUtil.getUserId(context))) {
            ShareImg shareImg = new ShareImg(InterlocutionDetailsActivity.this, question_id, "", "", "", "");
            shareImg.setShowDelete(true);
            shareImg.setDeleteBtnType(ShareLayout.QA_REPORT);
            shareDialog.setShareInfo(shareImg);
        }
        shareDialog.setOnShareImgPath(new ShareLayout.OnShareImgPath() {
            @Override
            public void getimgPath(final SHARE_MEDIA platform) {
                showLoading();
                shareDialog.dismiss();
                ShareVideoAndImgInfo videoAndImgInfo = new ShareVideoAndImgInfo();
                videoAndImgInfo.setQaCode_str(data.getShare_link_address());
                videoAndImgInfo.setNote(data.getQuestion_text());
                videoAndImgInfo.setName(data.getAnswer_user_name());
                videoAndImgInfo.setImgUrl(data.getCover_uri());
                videoAndImgInfo.setHeight(data.getVideo_height());
                videoAndImgInfo.setWidth(data.getVideo_width());
                videoAndImgInfo.setAnswerTime(data.getAnswer_time());
                sqdv_shareqadetailview.setOnLoadFinishListener(new AbsShareImg.OnLoadFinish() {
                    @Override
                    public void onFinish(Object path) {
                        UmengShare.sharedIMG(InterlocutionDetailsActivity.this, platform, path
                                , data.getShare_link_address(), data.getQuestion_text(), new IUMShareResultListener((BaseActivity) context));
                    }
                });
                sqdv_shareqadetailview.genderImage(videoAndImgInfo, platform);
            }
        });

    }

    /**
     * 添加围观人头像
     *
     * @param tv        围观数
     * @param ll        围观头像容器
     * @param circusees 数据源
     */
    public void addCircusee(TextView tv, LinearLayout ll, final List<GetQAObserveUserObject> circusees, boolean isAdd) {
//        if(ll.getChildCount()!=0) return;
//        final List<ObserverQA> circusees = qa.getObserver_list();
        qadetailvideoline.setVisibility(View.VISIBLE);
        if (circusees == null) return;
        if (ll == null) return;
        ll.removeAllViews();
        if (circusees.isEmpty()) {
            ll_qadetaillist.setVisibility(View.GONE);
        } else {
            ll_qadetaillist.setVisibility(View.VISIBLE);
        }
        if (isAdd) {
            data.setObserve_count(data.getObserve_count() + 1);
        }
        tv.setText(data.getObserve_count() + "");
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv.measure(w, h);
        int width = tv.getMeasuredWidth();
        int circuseeWidth = ScreenUtils.getScreenWidth(context) - width - DensityUtil.dip2px(context, 30);
        int circuseeCount = circuseeWidth / (DensityUtil.dip2px(context, 30));
        int count;
        if (circusees.size() == circuseeCount) count = circuseeCount;
        else {
            count = Math.min(circuseeCount, circusees.size());
        }
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.width = DensityUtil.dip2px(context, 25);
            lp.height = DensityUtil.dip2px(context, 25);
            lp.setMargins(0, 0, DensityUtil.dip2px(context, 5), 0);
            MyCircleView circleView = new MyCircleView(context);
            circleView.setLayoutParams(lp);
            ImageLoader.getInstance().displayImage(circusees.get(i).getObserve_portrait_uri(), circleView, DisplayOptionsUtils.getHeaderDefaultImageOptions());
            ll.addView(circleView);
            final int position = i;
            circleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLogin(false)) return;
                    String userId = circusees.get(position).getObserve_user_id();
                    context.startActivity(new Intent(context, PersonalHomeActivity.class)
                            .putExtra("userId", userId));
                }
            });
        }

    }

    /**
     * 倒计时
     */
    public void handlerCountDown(final GetQuestionInfoData data) {
        if (handler != null) return;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (needCountDown) {
                    if (getQAStat(data) == LIMITTIMEFREE) {
                        //该应答视频限时免费
                        if (data.getTime_remain() <= 1000) {
                            if (!mediaPlayer.isPlaying()) {
                                //当播放器为停止状态可更改映答视频状态为xx元围观
                                data.setIs_question_owner(false);
                                data.setTemporary_free(false);
                                rl_qadetail_btntop.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                                tv_qadetailbtntext_top.setText(CommonUtil.formatPrice2(data.getSrcObservPrice()) + "元围观");
                            }
                        } else {
                            data.setTime_remain(data.getTime_remain() - 1000);
                        }
                    }

                    GetQAInfoRecommendObject obj = data.getRecommend_question();
                    if (getRecommendQAStat(obj) == LIMITTIMEFREE) {
                        //值得一看限时免费
                        if (obj.getTime_remain() / (3600 * 1000) < 1) {
                            if (obj.getTime_remain() < 1000) {
                                //免费时间到
                                tv_qadetailbtntext.setVisibility(View.GONE);
                                rl_qadetail_btntop.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                                tv_qadetailbtntext.setText(CommonUtil.formatPrice2(obj.getObserve_price()) + "元围观");
                                obj.setIs_question_owner(false);
                                obj.setTemporary_free(false);
                            } else {
                                //免费时间内
                                tv_qadetailbtntext.setVisibility(View.VISIBLE);
                                tv_qadetailbtntext.setText("还剩 " + TimeFormatUtils.getCountDownFormat((int) obj.getTime_remain()));
                                obj.setTime_remain(obj.getTime_remain() - 1000);
                            }
                        }
                    }


                    if (getQAStat(data) == LIMITTIMEFREE
                            || getRecommendQAStat(data.getRecommend_question()) == LIMITTIMEFREE) {
                        //映答视频和推荐视频中存在现实免费 则需要倒计时
                        handler.postDelayed(this, 1000);
                    } else {
                        needCountDown = false;
                    }
                }
            }
        }, 1000);
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

package com.haiqiu.miaohi.activity;

//import android.animation.Animator;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.SpannableStringBuilder;
//import android.text.TextUtils;
//import android.text.method.LinkMovementMethod;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AccelerateDecelerateInterpolator;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.animation.ScaleAnimation;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.haiqiu.miaohi.ConstantsValue;
//import com.haiqiu.miaohi.R;
//import com.haiqiu.miaohi.adapter.VideoDetailAdapter;
//import com.haiqiu.miaohi.base.BaseActivity;
//import com.haiqiu.miaohi.bean.Notify_user_result;
//import com.haiqiu.miaohi.bean.SendCommentResponse;
//import com.haiqiu.miaohi.bean.TextInfo;
//import com.haiqiu.miaohi.bean.VideoDetailAttentionResponse;
//import com.haiqiu.miaohi.bean.VideoDetailBean;
//import com.haiqiu.miaohi.bean.VideoDetailPraiseResponse;
//import com.haiqiu.miaohi.bean.VideoDetailResponse;
//import com.haiqiu.miaohi.bean.VideoDetailUserCommentBean;
//import com.haiqiu.miaohi.bean.VideoDetailUserCommentResponse;
//import com.haiqiu.miaohi.bean.VideoDetail_dialogGiftBean;
//import com.haiqiu.miaohi.bean.VideoDetail_dialogGiftResponse;
//import com.haiqiu.miaohi.bean.VideoInfo;
//import com.haiqiu.miaohi.bean.VideoItemPageResult;
//import com.haiqiu.miaohi.bean.WalletInfoData;
//import com.haiqiu.miaohi.okhttp.MHHttpClient;
//import com.haiqiu.miaohi.okhttp.MHHttpHandler;
//import com.haiqiu.miaohi.okhttp.MHRequestParams;
//import com.haiqiu.miaohi.receiver.CloseEmojiKeyboard;
//import com.haiqiu.miaohi.receiver.RefreshBalanceEvent;
//import com.haiqiu.miaohi.receiver.RefreshCommentCountEvent;
//import com.haiqiu.miaohi.receiver.RefreshGiftCountEvent;
//import com.haiqiu.miaohi.response.BaseResponse;
//import com.haiqiu.miaohi.umeng.AbsUMShare;
//import com.haiqiu.miaohi.umeng.ShareVideo;
//import com.haiqiu.miaohi.utils.AbstractTextUtil;
//import com.haiqiu.miaohi.utils.Base64Util;
//import com.haiqiu.miaohi.utils.DensityUtil;
//import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
//import com.haiqiu.miaohi.utils.KeyboardWatcher;
//import com.haiqiu.miaohi.utils.LongClickableLinkMovementMethod;
//import com.haiqiu.miaohi.utils.MHLogUtil;
//import com.haiqiu.miaohi.utils.MHStateSyncUtil;
//import com.haiqiu.miaohi.utils.MHStringUtils;
//import com.haiqiu.miaohi.utils.NoDoubleClickUtils;
//import com.haiqiu.miaohi.utils.PraisedUtil;
//import com.haiqiu.miaohi.utils.ScreenSwitchUtils;
//import com.haiqiu.miaohi.utils.ScreenUtils;
//import com.haiqiu.miaohi.utils.SpUtils;
//import com.haiqiu.miaohi.utils.SysMethod;
//import com.haiqiu.miaohi.utils.TextUtil;
//import com.haiqiu.miaohi.utils.ToastUtils;
//import com.haiqiu.miaohi.utils.UserInfoUtil;
//import com.haiqiu.miaohi.view.CommonNavigation;
//import com.haiqiu.miaohi.view.MyCircleView;
//import com.haiqiu.miaohi.view.NoteEditText;
//import com.haiqiu.miaohi.view.SendGiftDialog;
//import com.haiqiu.miaohi.view.ShareDialog;
//import com.haiqiu.miaohi.view.VideoDetail_gift_dialog;
//import com.haiqiu.miaohi.widget.KeyBoardView;
//import com.haiqiu.miaohi.widget.mediaplayer.MyMediaPlayer;
//import com.haiqiu.miaohi.widget.ShareLayout;
//import com.haiqiu.miaohi.widget.mediaplayer.VideoView;
//import com.haiqiu.miaohi.widget.mediaplayer.VideoViewWrap;
//import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
//import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshSwipeMenuListView;
//import com.haiqiu.miaohi.widget.swipemenulistview.SwipeMenu;
//import com.haiqiu.miaohi.widget.swipemenulistview.SwipeMenuCreator;
//import com.haiqiu.miaohi.widget.swipemenulistview.SwipeMenuItem;
//import com.haiqiu.miaohi.widget.swipemenulistview.SwipeMenuListView;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.tendcloud.tenddata.TCAgent;
//import com.umeng.socialize.UMShareAPI;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.json.JSONArray;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

import com.haiqiu.miaohi.base.BaseActivity;

/**
 * 视频详情
 * Created by ningl on 2016/6/21.
 */
public class VideoDetailActivity extends BaseActivity {
//        implements View.OnClickListener
//        , PullToRefreshBase.OnRefreshListener, VideoDetail_gift_dialog.OnAddDataListener, DialogInterface.OnDismissListener
//        , AbsListView.OnItemClickListener, ShareLayout.DeleteVideoListener, KeyBoardView.OnEmojiDoListener
//        , VideoView.OnPlayCompleteControl, NoteEditText.OnCursorChange, KeyboardWatcher.OnKeyboardToggleListener {
//
//    private CommonNavigation cn_videodetail;
//    private PullToRefreshSwipeMenuListView ptrl_videodetail;
//    private NoteEditText et_videodetail_input;
//    private SwipeMenuListView lv;
//    private ViewGroup headerView;
//    private MyCircleView mcv_videodetail_header;
//    private TextView tv_videodetail_name;
//    private TextView tv_videodetail_date;
//    private ImageView iv_videodetail_time;
//    private TextView tv_videodetail_playcount;
//    private VideoViewWrap vvw_videodetail;
//    private ImageView iv_videodetail_praise;
//    private TextView tv_videodetail_praise;
//    private ImageView iv_videodetail_gift;
//    private TextView tv_videodetail_gift;
//    private View v_videodetail_sendgiftline;
//    private TextView tv_videodetail_inviteinfo;
//    private TextView tv_videodetail_totalcomment;
//    private TextView tv_videodetail_attention;
//    private TextView tv_videodetail_describe;
//    private ImageView iv_videodetail_headervip;
//    private LinearLayout ll_videodetail_praise;
//    private LinearLayout ll_videodetail_gift;
//    private LinearLayout ll_videodetail_sendgift;
//    private TextView tv_videodetail_invite;
//    private LinearLayout rl_videodetail_subject;
//    private ImageView iv_share;
//    private ImageView iv_gift_coming;
//    private KeyBoardView kv_videodetail;
//    private LinearLayout ll_outside;
//    private TextView tv_videodetaildescription;
//    private LinearLayout ll_smallvideoview;
//    private LinearLayout ll_bigvideoview;
//    private TextView tv_title;
//    private ImageView iv_videoViewPraise;
//    private RelativeLayout rl_videoviewsmallclick;
//    private RelativeLayout rl_videoviewsmallcontrol;
//    private View controlView;
//    private RelativeLayout ll_videoviewreplay;
//    private RelativeLayout ll_videoviewpraise;
//    private View v_videoverticalline;
//    private FrameLayout fl_keboardmarsk;
//    private ImageView videoView_praise;
//    private RelativeLayout rl_videodetailcontentview;
//    private View noComment;// 无评论抢沙发view
//    private RelativeLayout rl_videodetail_totalcommentparent;
//
//    private String video_id;//视频id
//    private Intent intent;
//    public VideoDetailBean videoDetailBean;//视频详情数据
//    private List<VideoDetailUserCommentBean> commentParent;//视频详情评论数据
//    private List<VideoDetailUserCommentBean> commentTotal;//所有视频详情评论数据
//    private List<VideoDetailUserCommentBean> commentVip;//大咖视频详情评论数据
//    private List<VideoDetail_dialogGiftBean> videoDetail_dialogGiftBeans;//视频详情礼物对话框数据
//    private List<Notify_user_result> notify_user_results;//@的好友
//    private List<TextInfo> textInfos;
//    private int pageIndex_gift;//礼物页码
//    private VideoDetailAdapter commentAdapter;//评论适配器
//    private boolean attentionState;//关注状态
//    private boolean praiseState;//点赞状态
//    private VideoDetail_gift_dialog videoDetail_gift_dialog;//查看礼物dialog
//    private int commentType = 0;//当前评论类型 0评论视频 1回复
//    private VideoDetailUserCommentBean curCommentByReply;//当前要回复的评论
//    private boolean isLoading_getReceiveGift;//获取礼物是否正在加载礼物
//    private String totalCount;//总评论数
//    private String vipCount;//大咖评论数
//    private ShareDialog shareDialog;
//    private ArrayList<String> namesByat, userIdByat;
//    private VideoView videoView;
//    private MyMediaPlayer mediaPlayer;
//    private boolean isSendGift;
//    private boolean isGift;
//    private String shared_content_sina;
//    private int pageIndex;
//    private int selectPosition;
//    private KeyboardWatcher keyboardWatcher;
//
//    private static final String VIDEODETAIL_SENDGIFT_BUBBLE = "videodetail_sendgift_bubble";//送礼物
//    private static final String VIDEODETAIL_GIFT_BUBBLE = "videodetail_gift_bubble";//查看礼物
//    private static final String VIDEODETAIL_TOTALCOMMENT_BUBBLE = "videodetail_totalcomment_bubble";//总评论
//
//    private static final int VIDEODETAIL_VIP_GIFTTYPE = 20;
//    private static final int VIDEODETAIL_NORMAL_GIFTTYPE = 10;
//    private View progress_bar;
//
//    private String notify_comment_user_name;
//    private String notify_comment_id;
//    private String notify_comment_user_id;
//    private boolean isEnterFromNotify;//是否来自通知
//
//    private int videoPlayerSwitchHeight;
//    private boolean isBig = true;
//    private int screenWith;
//    private boolean isPlay;//是否已经启动过播放
//    private ScaleAnimation scaleAnimation;
//    private ScreenSwitchUtils screenSwitchUtils;
//    public static VideoDetailActivity videoDetailActivity;
//    private int scrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
//    private String userNames;
//
//    String kind_tag; //首页的tag 为空就传"0"
//
//    private SendGiftDialog sendGiftDialog;
//
//    private TextUtil edit_textUtil;
//    private boolean isFromCommentList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_videodetail);
//        TCAgent.onEvent(context, "视频详情页总打开数" + ConstantsValue.android);
//        videoDetailActivity = this;
//        edit_textUtil = TextUtil.getInstance();//编辑框文本工具类实例
//        if (!EventBus.getDefault().isRegistered(VideoDetailActivity.this)) {
//            EventBus.getDefault().register(VideoDetailActivity.this);
//        }
//        keyboardWatcher = new KeyboardWatcher((Activity) context);
//        keyboardWatcher.setListener(this);
//        initView();
//        screenSwitchUtils = ScreenSwitchUtils.init(this);
//        setOnSwitchScreenListener();
//        screenWith = ScreenUtils.getScreenSize(this).x;
//        intent = getIntent();
//        if (null != intent && null != intent.getExtras()) {
//            kind_tag = intent.getExtras().getString("kind_tag");
//            video_id = intent.getExtras().getString("video_id");
//            if (!MHStringUtils.isEmpty(intent.getExtras().getString("comment_user_name"))//是否来自通知
//                    && !MHStringUtils.isEmpty(intent.getExtras().getString("comment_id"))
//                    && !MHStringUtils.isEmpty(intent.getExtras().getString("comment_user_id"))) {
//                notify_comment_user_name = intent.getExtras().getString("comment_user_name");
//                notify_comment_id = intent.getExtras().getString("comment_id");
//                notify_comment_user_id = intent.getExtras().getString("comment_user_id");
//                isEnterFromNotify = true;
//            }
//            isFromCommentList = intent.getBooleanExtra("isFromCommentList", false);
//        }
//        commentParent = new ArrayList<>();
//        commentTotal = new ArrayList<>();
//        commentVip = new ArrayList<>();
//        namesByat = new ArrayList<>();
//        userIdByat = new ArrayList<>();
////        commentAdapter = new VideoDetailAdapter(this, commentParent, this);
//        lv.setAdapter(commentAdapter);
//        showMHLoading();
//        getVideoDetail(video_id);
//        getComment(video_id);
//        et_videodetail_input = kv_videodetail.getEt();
//        et_videodetail_input.setMovementMethod(new LongClickableLinkMovementMethod());
//        et_videodetail_input.setOnCursorChangeListener(this);
//        checkDeleteAtFriend();
//        SpannableStringBuilder builder = new SpannableStringBuilder("aaaa");
//        kv_videodetail.setOnEmojiDoListener(this);
//        et_videodetail_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                ptrl_videodetail.resetFooterLayout();
////                if (hasFocus)
////                    kv_videodetail.hideLayout();
//            }
//        });
//
//        videoPlayerSwitchHeight = DensityUtil.dip2px(this, 90) + ScreenUtils.getScreenSize(this).x;
//        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
//
//                switch (scrollState) {
//                    case SCROLL_STATE_IDLE:
//                        //滑动停止时调用
//                        MHLogUtil.i("onScrollStateChanged", "SCROLL_STATE_IDLE");
//                        VideoDetailActivity.this.scrollState = SCROLL_STATE_IDLE;
//                        break;
//                    case SCROLL_STATE_TOUCH_SCROLL:
//                        //正在滚动时调用
//                        MHLogUtil.i("onScrollStateChanged", "SCROLL_STATE_TOUCH_SCROLL");
//                        VideoDetailActivity.this.scrollState = SCROLL_STATE_TOUCH_SCROLL;
//                        break;
//                    case SCROLL_STATE_FLING:
//                        //手指快速滑动时,在离开ListView由于惯性滑动
//                        MHLogUtil.i("onScrollStateChanged", "SCROLL_STATE_FLING");
//                        VideoDetailActivity.this.scrollState = SCROLL_STATE_FLING;
//                        break;
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                MHLogUtil.i("scrollState", scrollState + "");
//
//                //                kv_videodetail.hidenSoft4RotateScreen();
//                int offset = getScrollY(lv) - videoPlayerSwitchHeight;
//                MHLogUtil.i("getScrollY", getScrollY(lv) + "   " + offset);
//                if (offset > 0 && isBig && mediaPlayer.isPlaying()
//                        && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
//                        || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//                        ) {//从大视频切换到小视频并且视频处于播放状态
//                    playBig2Small();
//                    tv_title.setEnabled(false);
//                } else if (offset > 0 && isBig && !mediaPlayer.isPlaying()) {//从大视频切换到小视频并且视频处于暂停状态
//                    tv_title.setText("立即播放");
//                    tv_title.setCompoundDrawablePadding(DensityUtil.dip2px(context, 3));
//                    setTitleClickPlay();
//                    isBig = false;
//                    tv_title.setEnabled(true);
//                } else if (offset < 0 && !isBig
//                        && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
//                        || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)) {//从小视频切换到大视频
//                    playSmall2Big();
//                    tv_title.setEnabled(false);
//                    tv_title.setText("视频详情");
//                    tv_title.setCompoundDrawables(null, null, null, null);
//                }
//            }
//        });
//        if (null == scaleAnimation) {
//            scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            scaleAnimation.setDuration(400);
//            scaleAnimation.setFillAfter(true);
//        }
//
//        kv_videodetail.setOnKeyboardListener(new KeyBoardView.OnKeboardListener() {
//            @Override
//            public void onOpen() {
//                fl_keboardmarsk.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onClose() {
//                fl_keboardmarsk.setVisibility(View.GONE);
//                if (et_videodetail_input.getText().toString().trim().length() == 0) {
//                    et_videodetail_input.setHint(" 发射评论...");
//                    commentType = 0;
//                    selectPosition = -1;
//                }
//            }
//        });
//
//        fl_keboardmarsk.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                kv_videodetail.hideKeyboard(context);
//                kv_videodetail.hideLayout();
//                fl_keboardmarsk.setVisibility(View.GONE);
//
//                if (et_videodetail_input.getText().length() == 0) {
//                    kv_videodetail.setGiftText();
//                } else {
//                    kv_videodetail.tv_videodetail_send.setText("发送");
//                }
//
//                return true;
//            }
//        });
//
//    }
//
//    /**
//     * 设置标题可点击 点击切换到小播放器
//     */
//    private void setTitleClickPlay() {
//        Drawable leftDrawable = getResources().getDrawable(R.drawable.svg_videodetail_titleicon);
//        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
//        tv_title.setCompoundDrawables(leftDrawable, null, null, null);
//        tv_title.startAnimation(scaleAnimation);
//    }
//
//    public int getScrollY(ListView lv) {//获取滚动距离
//
//        View c = lv.getChildAt(0);
//        if (c == null) {
//            return 0;
//        }
//
//        int firstVisiblePosition = lv.getFirstVisiblePosition();
//        int top = c.getTop();
//
//        int headerHeight = 0;
//        if (firstVisiblePosition >= 1) {
//            headerHeight = lv.getHeight();
//            return -top + kv_videodetail.getSoftKeyboardHeight() + firstVisiblePosition * c.getHeight() + headerHeight;
//        }
//        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
//    }
//
//    /**
//     * 播放状态下从大播放器切换到小播放器
//     */
//    private void playBig2Small() {
//        kv_videodetail.hidenSoft4RotateScreen();
//        vvw_videodetail.clearAnimation();
//        videoView.switchSmallVideo();
//        rl_videoviewsmallclick.setVisibility(View.VISIBLE);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
//                , LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, -screenWith, 0, 0);
//        lp.height = screenWith;
//        lp.width = screenWith;
//        vvw_videodetail.setLayoutParams(lp);
//        ((LinearLayout) vvw_videodetail.getParent()).removeView(vvw_videodetail);
//        ll_smallvideoview.addView(vvw_videodetail);
//        isBig = false;
//        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(vvw_videodetail, "translationY", screenWith * 2 / 5);
//        ObjectAnimator scalAnimX = ObjectAnimator.ofFloat(vvw_videodetail, "scaleX", 1f, 0.4f);
//        ObjectAnimator scalAnimY = ObjectAnimator.ofFloat(vvw_videodetail, "scaleY", 1f, 0.4f);
//        vvw_videodetail.setPivotX(screenWith);
//        vvw_videodetail.setPivotY(screenWith);
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.play(moveAnim).with(scalAnimX).with(scalAnimY);
//        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
//        animSet.setDuration(400);
//        animSet.start();
//    }
//
//    /**
//     * 播放状态下从小播放器切换到大播放器
//     */
//    private void playSmall2Big() {
//        //        kv_videodetail.hidenSoft4RotateScreen();
//        vvw_videodetail.clearAnimation();
//        rl_videoviewsmallclick.setVisibility(View.GONE);
//        isBig = true;
//        if (ll_smallvideoview.getChildCount() == 0)
//            return;
//        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(vvw_videodetail, "translationY", -screenWith * 2 / 5);
//        ObjectAnimator scalAnimX = ObjectAnimator.ofFloat(vvw_videodetail, "scaleX", 1f);
//        ObjectAnimator scalAnimY = ObjectAnimator.ofFloat(vvw_videodetail, "scaleY", 1f);
//        vvw_videodetail.setPivotX(screenWith);
//        vvw_videodetail.setPivotY(screenWith);
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.play(moveAnim).with(scalAnimX).with(scalAnimY);
//        //        animSet.setDuration(200);
//        animSet.start();
//        ((LinearLayout) vvw_videodetail.getParent()).removeView(vvw_videodetail);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
//                , LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 2 * screenWith / 5, 0, 0);
//        lp.height = screenWith;
//        lp.width = screenWith;
//        vvw_videodetail.setLayoutParams(lp);
//        ll_bigvideoview.addView(vvw_videodetail);
//        animSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                //                ll_smallvideoview.removeView(vvw_videodetail);
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (UserInfoUtil.isLogin()) {
//            RefreshBalance();
//        }
//
//        if (shareDialog != null)
//            shareDialog.setData();
//    }
//
//
//    /**
//     * 刷新余额
//     */
//    private void RefreshBalance() {
//
//        MHHttpClient.getInstance().post(WalletInfoData.class, context, ConstantsValue.Url.GETWALLETINFO, new MHHttpHandler<WalletInfoData>() {
//            @Override
//            public void onSuccess(WalletInfoData response) {
//                EventBus.getDefault().post(new RefreshBalanceEvent(response.getData().getBalance()));
//            }
//
//            @Override
//            public void onFailure(String content) {
//
//            }
//        });
//
//
//    }
//
//    /**
//     * 初始化控件
//     */
//    private void initView() {
//        cn_videodetail = (CommonNavigation) findViewById(R.id.cn_videodetail);
//        iv_gift_coming = (ImageView) findViewById(R.id.iv_gift_coming);
//
//        ptrl_videodetail = (PullToRefreshSwipeMenuListView) findViewById(R.id.ptrl_videodetail);
//        kv_videodetail = (KeyBoardView) findViewById(R.id.kv_videodetail);
//        ll_outside = (LinearLayout) findViewById(R.id.ll_outside);
//        ll_smallvideoview = (LinearLayout) findViewById(R.id.ll_smallvideoview);
//        fl_keboardmarsk = (FrameLayout) findViewById(R.id.fl_keboardmarsk);
//        rl_videodetailcontentview = (RelativeLayout) findViewById(R.id.rl_videodetailcontentview);
//        ptrl_videodetail.setAutoLoadMoreIsEnable(true);
//        ll_outside.setOnClickListener(this);
//        ll_outside.setClickable(false);
//        kv_videodetail.setOutsideView(ll_outside);
//        lv = ptrl_videodetail.getRefreshableView();
//        lv.setDividerHeight(0);
//        ptrl_videodetail.setPullRefreshEnabled(false);//不允许下拉刷新
//        ptrl_videodetail.setOnRefreshListener(this);
//        iv_share = new ImageView(context);
//        iv_share.setPadding(0, 0, DensityUtil.dip2px(context, 10), 0);
//        iv_share.setImageResource(R.drawable.svg_more);
//        cn_videodetail.setRightLayoutView(iv_share);
//        cn_videodetail.showRightLayout();
//        iv_share.setEnabled(false);
//        iv_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TCAgent.onEvent(context, "视频详情-分享" + ConstantsValue.android);
//                if (!isLogin(false)) {
//                } else {
//                    if (!NoDoubleClickUtils.isDoubleClick()) {
//                        shareDialog = new ShareDialog(VideoDetailActivity.this);
//                        shareDialog.setData();
//                        AbsUMShare umShare = new ShareVideo(VideoDetailActivity.this
//                                , video_id
//                                , videoDetailBean.getCover_uri()
//                                , videoDetailBean.getUpload_user_name()
//                                , videoDetailBean.getUpload_user_id()
//                                , videoDetailBean.getVideo_note()
//                                , videoDetailBean.getShared_tag_text());
//                        shareDialog.setShareInfo(umShare);
//
//                        if (videoDetailBean.getUpload_user_id().equals(SpUtils.getString(ConstantsValue.Sp.USER_ID))) {
//                            shareDialog.hidenReport();
//                        }
//
//                        shareDialog.setOnDeleteListener(VideoDetailActivity.this);
//                    }
//                }
//            }
//        });
//
//        cn_videodetail.setOnLeftLayoutClickListener(new CommonNavigation.OnLeftLayoutClick() {
//            @Override
//            public void onClick(View v) {
//                hiddenKeyboard();
//                kv_videodetail.hidenSoft4RotateScreen();
//                finish();
//            }
//        });
//
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//            @Override
//            public void create(SwipeMenu menu) {
//                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
//                deleteItem.setWidth(DensityUtil.dip2px(VideoDetailActivity.this, 80));
//                deleteItem.setIcon(R.drawable.ic_delete);
//                menu.addMenuItem(deleteItem);
//            }
//        };
//        lv.setMenuCreator(creator);
//        //删除评论
//        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                if (index == 0) {
//                    deleteComment(position);
//                }
//            }
//        });
//
//        headerView = (ViewGroup) View.inflate(this, R.layout.item_videodetail_header, null);
//        iv_videodetail_headervip = (ImageView) headerView.findViewById(R.id.iv_videodetail_headervip);
//        mcv_videodetail_header = (MyCircleView) headerView.findViewById(R.id.mcv_videodetail_header);
//        tv_videodetail_name = (TextView) headerView.findViewById(R.id.tv_videodetail_name);
//        tv_videodetail_date = (TextView) headerView.findViewById(R.id.tv_videodetail_date);
//        iv_videodetail_time = (ImageView) headerView.findViewById(R.id.iv_videodetail_time);
//        tv_videodetail_playcount = (TextView) headerView.findViewById(R.id.tv_videodetail_playcount);
//        vvw_videodetail = (VideoViewWrap) headerView.findViewById(R.id.vvw_videodetail);
//        iv_videodetail_praise = (ImageView) headerView.findViewById(R.id.iv_videodetail_praise);
//        tv_videodetail_praise = (TextView) headerView.findViewById(R.id.tv_videodetail_praise);
//        iv_videodetail_gift = (ImageView) headerView.findViewById(R.id.iv_videodetail_gift);
//        tv_videodetail_gift = (TextView) headerView.findViewById(R.id.tv_videodetail_gift);
//        v_videodetail_sendgiftline = headerView.findViewById(R.id.v_videodetail_sendgiftline);
//        tv_videodetail_inviteinfo = (TextView) headerView.findViewById(R.id.tv_videodetail_inviteinfo);
//        tv_videodetail_totalcomment = (TextView) headerView.findViewById(R.id.tv_videodetail_totalcomment);
//        tv_videodetail_attention = (TextView) headerView.findViewById(R.id.tv_videodetail_attention);
//        progress_bar = headerView.findViewById(R.id.progress_bar);
//        tv_videodetail_describe = (TextView) headerView.findViewById(R.id.tv_videodetail_describe);
//        ll_videodetail_praise = (LinearLayout) headerView.findViewById(R.id.ll_videodetail_praise);
//        ll_videodetail_gift = (LinearLayout) headerView.findViewById(R.id.ll_videodetail_gift);
//        ll_videodetail_sendgift = (LinearLayout) headerView.findViewById(R.id.ll_videodetail_sendgift);
//        tv_videodetail_invite = (TextView) headerView.findViewById(R.id.tv_videodetail_invite);
//        rl_videodetail_subject = (LinearLayout) headerView.findViewById(R.id.rl_videodetail_subject);
//        tv_videodetaildescription = (TextView) headerView.findViewById(R.id.tv_videodetaildescription);
//        ll_bigvideoview = (LinearLayout) headerView.findViewById(R.id.ll_bigvideoview);
//        tv_title = cn_videodetail.getBar_title();
//        tv_videodetail_attention.setOnClickListener(this);
//        mcv_videodetail_header.setOnClickListener(this);
//        lv.setOnItemClickListener(this);
//        tv_title.setOnClickListener(this);
//        tv_title.setEnabled(false);
//        videoView = (VideoView) vvw_videodetail.getVideoView();
//        videoView.setPlayBtnSize(60, 60);
//        mediaPlayer = new MyMediaPlayer();
//        //设置视频播放控件为正方形
//        ViewGroup.LayoutParams lp = vvw_videodetail.getLayoutParams();
//        lp.height = ScreenUtils.getScreenSize(this).x;
//        vvw_videodetail.setLayoutParams(lp);
//        lv.addHeaderView(headerView);
//        ll_bigvideoview.setLayoutParams(lp);
//        videoView.setOnPlayCompleteControl(this);
//        iv_videoViewPraise = videoView.getPraiseView();
//        rl_videoviewsmallclick = videoView.getRl_videoviewsmallclick();
//        rl_videoviewsmallcontrol = videoView.getRl_videoviewsmallcontrol();
//        controlView = videoView.getControlView();
//        ll_videoviewreplay = (RelativeLayout) controlView.findViewById(R.id.ll_videoviewreplay);
//        ll_videoviewpraise = (RelativeLayout) controlView.findViewById(R.id.ll_videoviewpraise);
//        v_videoverticalline = controlView.findViewById(R.id.v_videoverticalline);
//        rl_videoviewsmallcontrol.setBackgroundColor(Color.parseColor("#88000000"));
//    }
//
//    /**
//     * 判断自己是否是自己  决定是否可以送礼物
//     */
//    private void sendGiftAuthority() {
//        ll_videodetail_sendgift.setVisibility(View.GONE);
//        v_videodetail_sendgiftline.setVisibility(View.GONE);
//        isSendGift = true;
//    }
//
//    /**
//     * 设置视频相关
//     */
//    private void setVideo(final VideoDetailBean videoDetailBean) {
//        ImageLoader.getInstance().displayImage(videoDetailBean.getCover_uri(), videoView.getPreviewImageView(), DisplayOptionsUtils.getDefaultMaxRectImageOptions());
//        //        videoView.setDuration(Integer.parseInt(videoDetailBean.getDuration_second()) * 1000);
//
//        videoView.setOnInfoListener(new VideoView.OnInfoListener() {
//            @Override
//            public void onPlayClick(View view) {
//                startPlay();
//                caculatePlayCount();
//            }
//
//            @Override
//            public void onFullScreen(boolean isFullScreen) {
//
//            }
//        });
//    }
//
//    /**
//     * 启动播放器
//     */
//    private void startPlay() {
//        if (isPlay)
//            mediaPlayer.start();
//        else {
//            VideoInfo videoInfo = new VideoInfo();
//            videoInfo.setVideo_state(videoDetailBean.getVideo_state());
//            videoInfo.setHls_uri(videoDetailBean.getHls_uri());
//            videoInfo.setVideo_uri(videoDetailBean.getVideo_uri());
//            videoInfo.setVideo_id(video_id);
//            videoInfo.setHls_uri_state(videoDetailBean.getHls_state());
//            videoView.startPlay(mediaPlayer, videoInfo);
//        }
//        isPlay = true;
//    }
//
//    /**
//     * 获取视频详情
//     */
//    private void getVideoDetail(final String video_id) {
//        final MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("video_id", video_id);
//
//        if (!TextUtils.isEmpty(kind_tag)) {
//            requestParams.addParams("kind_tag", kind_tag);
//        } else {
//            requestParams.addParams("kind_tag", "0");
//        }
//
//        MHHttpClient.getInstance().post(VideoDetailResponse.class, VideoDetailActivity.this, ConstantsValue.Url.GETVIDEOINFO, requestParams, new MHHttpHandler<VideoDetailResponse>() {
//            @Override
//            public void onSuccess(VideoDetailResponse response) {
//
//                videoDetailBean = response.getData();
//
//                if (videoDetailBean.getVideo_state() < 10 || video_id == null) {
//                    showToastAtBottom("该视频不存在");
//                    finish();
//                    return;
//                }
//                shared_content_sina = videoDetailBean.getShared_tag_text();
//                sendGiftAuthority();
//                showGuideBubble();
//                ll_videodetail_praise.setOnClickListener(VideoDetailActivity.this);
//                ll_videodetail_gift.setOnClickListener(VideoDetailActivity.this);
//                ll_videodetail_sendgift.setOnClickListener(VideoDetailActivity.this);
//                tv_videodetail_attention.setEnabled(true);
//                iv_share.setEnabled(true);
//                setVideoDetail(videoDetailBean);//设置视频详情
//                setVideo(videoDetailBean);
//                ptrl_videodetail.setVisibility(View.VISIBLE);
//                kv_videodetail.setVisibility(View.VISIBLE);
//                ll_outside.setVisibility(View.VISIBLE);
//                hideErrorView();
//            }
//
//            @Override
//            public void onFailure(String content) {
//                ptrl_videodetail.setVisibility(View.INVISIBLE);
//                kv_videodetail.setVisibility(View.INVISIBLE);
//                ll_outside.setVisibility(View.INVISIBLE);
//                showErrorView();
//            }
//
//            @Override
//            public void onStatusIsError(String message) {
//                showToastAtBottom("该视频不存在");
//                finish();
//                super.onStatusIsError(message);
//            }
//        });
//    }
//
//    @Override
//    protected void reTry() {
//        super.reTry();
//        showMHLoading();
//        getVideoDetail(video_id);
//    }
//
//    /**
//     * 获取评论
//     */
//    private void getComment(String video_id) {
//        MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("root_id", video_id);
//        requestParams.addParams("root_type", "2");
//        requestParams.addParams("page_size", "10");
//        requestParams.addParams("page_index", pageIndex + "");
//
//        MHHttpClient.getInstance().post(VideoDetailUserCommentResponse.class, ConstantsValue.Url.GETUSERCOMMENTINFO, requestParams, new MHHttpHandler<VideoDetailUserCommentResponse>() {
//            @Override
//            public void onSuccess(VideoDetailUserCommentResponse response) {
//                if (isEnterFromNotify) {//通知进入弹出输入框
//                    commentType = 1;
//                    curCommentByReply = new VideoDetailUserCommentBean();
//                    curCommentByReply.setComment_id(notify_comment_id);
//                    curCommentByReply.setComment_user_id(notify_comment_user_id);
//                    curCommentByReply.setComment_user_name(notify_comment_user_name);
//                    et_videodetail_input.setHint("回复@" + curCommentByReply.getComment_user_name());
//                    ptrl_videodetail.resetFooterLayout();
//                    et_videodetail_input.requestFocus();
//                    openKeyBord(et_videodetail_input, VideoDetailActivity.this);
//                }
//                List<VideoDetailUserCommentBean> videoDetailUserCommentBeans = response.getData().getPage_result();
//                //设置评论数
//                totalCount = response.getData().getComments_count();
//                tv_videodetail_totalcomment.setText("共" + MHStringUtils.countFormat(totalCount) + "条评论");
//                commentParent.addAll(videoDetailUserCommentBeans);
//                if (!commentParent.isEmpty()
//                        && isGift
//                        && isSendGift) {
//                    showGuideBubble();
//                }
//                if (pageIndex == 0 && commentParent.size() == 0) {
//                    ptrl_videodetail.setPullLoadEnabled(false);
//                    setNoCommentVisible(View.VISIBLE);
//                } else {
//                    ptrl_videodetail.setPullLoadEnabled(true);
//                }
//                if (videoDetailUserCommentBeans.size() == 0)
//                    ptrl_videodetail.onLoadComplete(false);
//                else {
//                    ptrl_videodetail.onLoadComplete(true);
//                }
//                commentAdapter.notifyDataSetChanged();
//                if (isFromCommentList) {
//                    positionFromCommentList();
//                }
//                pageIndex++;
//            }
//
//            @Override
//            public void onFailure(String content) {
//                ptrl_videodetail.onLoadComplete(true);
//            }
//        });
//    }
//
//    /**
//     * 加关注&取消关注
//     */
//    private void setAttentionOrCancleAttention(final VideoDetailBean videoDetailBean) {
//        MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("action_mark", !attentionState + "");
//        requestParams.addParams("user_id", videoDetailBean.getUpload_user_id());
//        tv_videodetail_attention.setEnabled(false);
//        tv_videodetail_attention.setText(null);
//        progress_bar.setVisibility(View.VISIBLE);
//        MHHttpClient.getInstance().post(VideoDetailAttentionResponse.class, context, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<VideoDetailAttentionResponse>() {
//            @Override
//            public void onSuccess(VideoDetailAttentionResponse response) {
//                attentionState = !attentionState;
//                setAttentionBtnState(attentionState);
//            }
//
//            @Override
//            public void onFailure(String content) {
//                setAttentionBtnState(attentionState);
//            }
//
//            @Override
//            public void onStatusIsError(String message) {
//                super.onStatusIsError(message);
//                setAttentionBtnState(attentionState);
//            }
//        });
//
//    }
//
//    /**
//     * 视频点赞&取消点赞
//     */
//    private void setPraiseOrCanclePraise(final VideoDetailBean videoDetailBean) {
//        MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("action_mark", !praiseState + "");
//        requestParams.addParams("video_id", videoDetailBean.getVideo_id());
//        ll_videodetail_praise.setEnabled(false);
//        MHHttpClient.getInstance().post(VideoDetailPraiseResponse.class, VideoDetailActivity.this, ConstantsValue.Url.PRAISEVIDEODO, requestParams, new MHHttpHandler<VideoDetailPraiseResponse>() {
//            @Override
//            public void onSuccess(VideoDetailPraiseResponse response) {
//                NoDoubleClickUtils.setEnable(ll_videodetail_praise);
//                if (videoView_praise != null) {
//                    NoDoubleClickUtils.setEnable(videoView_praise);
//                }
//
//                praiseState = !praiseState;
//                setPraiseState(praiseState);
//
//                videoDetailBean.setPraise_state(!videoDetailBean.isPraise_state());
//                videoDetailBean.setPraise_count(videoDetailBean.isPraise_state() ? videoDetailBean.getSrcPraise_count() + 1 : videoDetailBean.getSrcPraise_count() - 1);
//                tv_videodetail_praise.setText(videoDetailBean.getPraise_count());
//
////                VideoPraiseSyncUtil.pushPraiseEvent(videoDetailBean);
//                PraisedUtil.showPop(iv_videodetail_praise, context, praiseState);
//            }
//
//            @Override
//            public void onFailure(String content) {
//                ll_videodetail_praise.setEnabled(true);
//                videoView_praise.setEnabled(true);
//            }
//
//            @Override
//            public void onStatusIsError(String message) {
//                super.onStatusIsError(message);
//                ll_videodetail_praise.setEnabled(true);
//                if (videoView_praise != null) {
//                    videoView_praise.setEnabled(true);
//                }
//
//            }
//        });
//    }
//
//    /**
//     * 获取视频视频收到的所有礼物
//     */
//    private void getVideoReceiveGift() {
//        if (isLoading_getReceiveGift)
//            return;
//        isLoading_getReceiveGift = true;
//        MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("video_id", video_id);
//        requestParams.addParams("page_size", "10");
//        requestParams.addParams("page_index", String.valueOf(pageIndex_gift));
//        MHHttpClient.getInstance().post(VideoDetail_dialogGiftResponse.class, VideoDetailActivity.this, ConstantsValue.Url.GIFTRECEIVEDBYVIDEO, requestParams, new MHHttpHandler<VideoDetail_dialogGiftResponse>() {
//            @Override
//            public void onSuccess(VideoDetail_dialogGiftResponse response) {
//                videoDetail_dialogGiftBeans = response.getData().getPage_result();
//                if (videoDetail_gift_dialog == null || !videoDetail_gift_dialog.isShowing()) {
//                    videoDetail_gift_dialog = new VideoDetail_gift_dialog(VideoDetailActivity.this, videoDetail_dialogGiftBeans);
//                    videoDetail_gift_dialog.setOnAddDataListerner(VideoDetailActivity.this);
//                    videoDetail_gift_dialog.setOnDismissListener(VideoDetailActivity.this);
//                } else {
//                    if (videoDetail_dialogGiftBeans.size() != 0) {
//                        videoDetail_gift_dialog.addData(videoDetail_dialogGiftBeans);
//                    }
//                }
//                if (videoDetail_dialogGiftBeans.size() < 10) {
//                    videoDetail_gift_dialog.setHasMoreData(false);
//                }
//                videoDetail_gift_dialog.setLoadingState(false);
//                pageIndex_gift++;
//                isLoading_getReceiveGift = false;
//            }
//
//            @Override
//            public void onFailure(String content) {
//                isLoading_getReceiveGift = false;
//            }
//        });
//    }
//
//
//    /**
//     * 发送评论
//     */
//    private void sendComment() {
//        kv_videodetail.setKeyboardEnable(false);
//        if (videoDetailBean == null)
//            return;
//        MHRequestParams requestParams = new MHRequestParams();
//        if (commentType == 0) {//评论视频
//            requestParams.addParams("commented_id", videoDetailBean.getVideo_id());
//            requestParams.addParams("commented_type", "2");
//            requestParams.addParams("commented_user", videoDetailBean.getUpload_user_id());
//        } else if (commentType == 1) {//回复评论
//            requestParams.addParams("commented_id", curCommentByReply.getComment_id());
//            requestParams.addParams("commented_type", "3");
//            requestParams.addParams("commented_user", curCommentByReply.getComment_user_id());
//        }
//        requestParams.addParams("root_id", videoDetailBean.getVideo_id());
//        requestParams.addParams("root_type", "2");
//        requestParams.addParams("root_user", videoDetailBean.getUpload_user_id());
//        requestParams.addParams("root_uri", videoDetailBean.getVideo_uri());
//        requestParams.addParams("root_cover_uri", videoDetailBean.getCover_uri());
//        if (notify_user_results != null) {
//            requestParams.addParams("comment_text", Base64Util.getBase64Str(getCommentText()));
//            //拼装@的好友id
//            JSONArray array = new JSONArray();
//            for (int i = 0; i < notify_user_results.size(); i++) {
//                Notify_user_result notify_user_result = notify_user_results.get(i);
//                if (notify_user_result != null)
//                    array.put(notify_user_results.get(i).getNotify_user_id());
//            }
//            requestParams.addParams("notify_user", array.toString());
//        } else {
//            requestParams.addParams("comment_text", Base64Util.getBase64Str(et_videodetail_input.getText().toString().replaceAll("\n", " ")));
//        }
//
//        MHHttpClient.getInstance().post(SendCommentResponse.class, VideoDetailActivity.this, ConstantsValue.Url.DOCOMMENT, requestParams, new MHHttpHandler<SendCommentResponse>() {
//            @Override
//            public void onSuccess(SendCommentResponse response) {
//                EventBus.getDefault().post(new CloseEmojiKeyboard());
//                kv_videodetail.reset();
//                VideoDetailUserCommentBean videoDetailUserCommentBean = response.getData();
//                videoDetailUserCommentBean.setComment_text(getCommentText());
//                videoDetailUserCommentBean.setComment_time_text("刚刚");
//                List<Notify_user_result> notify_user_resultsTemp = new ArrayList<>();
//                if (notify_user_results != null) {
//                    for (int i = 0; i < notify_user_results.size(); i++) {
//                        Notify_user_result notifyInfo = new Notify_user_result();
//                        notifyInfo.setNotify_user_name(notify_user_results.get(i).getNotify_user_name());
//                        notifyInfo.setNotify_user_id(notify_user_results.get(i).getNotify_user_id());
//                        notify_user_resultsTemp.add(notifyInfo);
//                    }
//                }
//                videoDetailUserCommentBean.setNotify_user_result(notify_user_resultsTemp);
//                //重置文本框
//                et_videodetail_input.setText("");
//                if (notify_user_results != null) notify_user_results.clear();
//                if (namesByat != null) namesByat.clear();
//                if (userIdByat != null) userIdByat.clear();
//                commentTotal.add(0, videoDetailUserCommentBean);
//                //回复
//                if (commentType == 1) {
//                    videoDetailUserCommentBean.setRoot_id(videoDetailUserCommentBean.getComment_id());
//                    videoDetailUserCommentBean.setCommented_user_name(curCommentByReply.getComment_user_name());
//                }
//                if (commentParent.isEmpty()) setNoCommentVisible(View.GONE);
//                commentParent.add(0, videoDetailUserCommentBean);
//                commentAdapter.notifyDataSetChanged();
//                //评论类型改为评论视频
//                commentType = 0;
//                et_videodetail_input.setHint(" 发射评论...");
//                kv_videodetail.setKeyboardEnable(true);
//                //设置评论数
//                if (MHStringUtils.isEmpty(totalCount)) {
//                    totalCount = "0";
//                } else if (MHStringUtils.isEmpty(vipCount)) {
//                    vipCount = "0";
//                }
//                totalCount = Integer.parseInt(totalCount) + 1 + "";
//                tv_videodetail_totalcomment.setText("共" + MHStringUtils.countFormat(totalCount) + "条评论");
//                EventBus.getDefault().post(new RefreshCommentCountEvent(Integer.parseInt(totalCount), video_id));
//                Intent intent = new Intent(ConstantsValue.IntentFilterAction.VIDEO_COMMIT_COUNT_ACTION);
//                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_COMMIT_COUNT_KEY, Long.parseLong(totalCount));
//                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_ID_KEY, video_id);
//                sendBroadcast(intent);
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        positionFromCommentList();
//                    }
//                }, 200);
//            }
//
//            @Override
//            public void onFailure(String content) {
//                kv_videodetail.setKeyboardEnable(true);
//
//            }
//
//            @Override
//            public void onStatusIsError(String message) {
//                super.onStatusIsError(message);
//                kv_videodetail.setKeyboardEnable(true);
//
//            }
//        });
//
//    }
//
//    /**
//     * 删除评论
//     */
//    private void deleteComment(final int position) {
//        MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("comment_id", commentParent.get(position).getComment_id());
//        MHHttpClient.getInstance().post(BaseResponse.class, VideoDetailActivity.this, ConstantsValue.Url.DELETECOMMENT, requestParams, new MHHttpHandler<BaseResponse>() {
//            @Override
//            public void onSuccess(BaseResponse response) {
//                commentParent.remove(position);
//                commentAdapter.notifyDataSetChanged();
//                if (commentParent.isEmpty()) setNoCommentVisible(View.VISIBLE);
//            }
//
//            @Override
//            public void onFailure(String content) {
//
//            }
//        });
//    }
//
//    /**
//     * 设置视频详情
//     *
//     * @param videoDetailBean 视频详情数据
//     */
//    private void setVideoDetail(VideoDetailBean videoDetailBean) {
//        attentionState = videoDetailBean.isAttention_state();
//        praiseState = videoDetailBean.isPraise_state();
//        ImageLoader.getInstance().displayImage(videoDetailBean.getUpload_user_portrait_uri(), mcv_videodetail_header, DisplayOptionsUtils.getHeaderDefaultImageOptions());
//        tv_videodetail_name.setText(videoDetailBean.getUpload_user_name());
//        tv_videodetail_date.setText(videoDetailBean.getUpload_time_text());
//        tv_videodetail_playcount.setText(MHStringUtils.countFormat(videoDetailBean.getPlay_total()) + " 播放");
//        //自己的视频不可以送礼物
//        if (!TextUtils.isEmpty(videoDetailBean.getUpload_user_name())
//                && TextUtils.equals(videoDetailBean.getUpload_user_id(), UserInfoUtil.getUserId(VideoDetailActivity.this))) {
//            ll_videodetail_sendgift.setVisibility(View.GONE);
//            v_videodetail_sendgiftline.setVisibility(View.GONE);
//        }
//        if (!TextUtils.isEmpty(videoDetailBean.getVideo_note())) {
//            //字数控制最多100字
//            String videoNote = null;
////            if (videoDetailBean.getVideo_note().length() <= 100) {
////                videoNote = videoDetailBean.getVideo_note();
////            } else {
////                videoNote = videoDetailBean.getVideo_note().substring(0, 100) + "...";
////            }
//            videoNote = videoDetailBean.getVideo_note();
//            VideoItemPageResult pageResult = new VideoItemPageResult();
//            pageResult.setVideo_note(videoNote);
//            pageResult.setNotify_user_result(videoDetailBean.getNotify_user_result());
//            tv_videodetail_describe.setMovementMethod(LinkMovementMethod.getInstance());
//            tv_videodetail_describe.setText(TextUtil.getInstance().handleVideoDescribe(pageResult, new AbstractTextUtil() {
//                @Override
//                public void onClickSpan(TextInfo textInfo) {
//                    if (!isLogin(false)) {
//                    } else {
//                        skipPersonalCenter(context, textInfo.getTarget());
//                    }
//                }
//            }));
//        }
//
//        //设置点赞数
//        tv_videodetail_praise.setText("赞" + videoDetailBean.getPraise_count());
//
//        //判断礼物数是否为0 更换颜色
//        if (!TextUtils.isEmpty(videoDetailBean.getGift_count()) && !TextUtils.equals("null", videoDetailBean.getGift_count())) {
//            if (Integer.parseInt(videoDetailBean.getGift_count()) != 0) {
//                iv_videodetail_gift.setImageResource(R.drawable.svg_giftblue);
//            } else {
//                iv_videodetail_gift.setImageResource(R.drawable.svg_icon_gift);
//            }
//            tv_videodetail_gift.setText("礼物" + MHStringUtils.countFormat(videoDetailBean.getGift_count()));
//        }
//        //是否是大咖
//        if (Integer.parseInt(videoDetailBean.getUpload_user_type()) > 10) {
//            iv_videodetail_headervip.setVisibility(View.VISIBLE);
//            if (!MHStringUtils.isEmpty(videoDetailBean.getVip_note())) {
//                tv_videodetaildescription.setVisibility(View.VISIBLE);
//                tv_videodetaildescription.setText(videoDetailBean.getVip_note());
//            } else {
//                tv_videodetaildescription.setVisibility(View.GONE);
//            }
//        }
//        //设置点赞状态
//        setPraiseState(praiseState);
////        VideoPraiseSyncInfo videoPraiseSyncInfo = new VideoPraiseSyncInfo();
////        videoPraiseSyncInfo.setVideoInfo(videoDetailBean);
////        videoPraiseSyncInfo.setUpdateViewOBj(this);
////        VideoPraiseSyncUtil.addPraiseSyncListener(videoPraiseSyncInfo);
//
//        //设置关注状态
//        setInitAttentionBtnState(attentionState);
//        //设置邀请专题
//        tv_videodetail_inviteinfo.setMovementMethod(LinkMovementMethod.getInstance());
//        if (null != videoDetailBean && null != videoDetailBean.getInvite_result() && !videoDetailBean.getInvite_result().isEmpty()) {
//            String subjectInvite = "";
//            for (int i = 0; i < videoDetailBean.getInvite_result().size(); i++) {
//                if (TextUtils.equals(videoDetailBean.getUpload_user_id(), videoDetailBean.getInvite_result().get(i).getRecever_id())) {
//                    subjectInvite += " 受 " + "<font color='#00a0e9'>@" + videoDetailBean.getInvite_result().get(i).getSender_name() + "</font>" + "邀请";
//                } else if (TextUtils.equals(videoDetailBean.getUpload_user_id(), videoDetailBean.getInvite_result().get(i).getSender_id())) {
//                    subjectInvite += " 邀请 " + "<font color='#00a0e9'>@" + videoDetailBean.getInvite_result().get(i).getReceiver_name() + "</font>";
//                }
//            }
//            if (!TextUtils.isEmpty(videoDetailBean.getActivity_name())) {
//                tv_videodetail_inviteinfo.setText(setActivity(videoDetailBean.getActivity_name(), videoDetailBean.getActivity_id()));
//                rl_videodetail_subject.setVisibility(View.VISIBLE);
//            } else {
//                rl_videodetail_subject.setVisibility(View.GONE);
//            }
//        } else {
//            if (!TextUtils.isEmpty(videoDetailBean.getActivity_name())) {
//                tv_videodetail_inviteinfo.setText(setActivity(videoDetailBean.getActivity_name(), videoDetailBean.getActivity_id()));
//                rl_videodetail_subject.setVisibility(View.VISIBLE);
//            } else {
//                rl_videodetail_subject.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    /**
//     * 跳转到个人中心
//     *
//     * @param context
//     * @param userId
//     */
//    private void skipPersonalCenter(Context context, String userId) {
//        Intent intent = new Intent(context, PersonalHomeActivity.class);
//        intent.putExtra("userId", userId);
//        if (TextUtils.equals(UserInfoUtil.getUserId(context), userId)) {
//            intent.putExtra("isSelf", true);
//        } else {
//            intent.putExtra("isSelf", false);
//        }
//        intent.putExtra("activityType", 0);
//        context.startActivity(intent);
//    }
//
//    /**
//     * 设置专题变色和点击
//     *
//     * @param activityName
//     * @param activityId
//     */
//    private SpannableStringBuilder setActivity(final String activityName, final String activityId) {
//        if (MHStringUtils.isEmpty(activityName) || MHStringUtils.isEmpty(activityId)) return null;
//        String activityNameStr = "参加#" + activityName + "#专题";
//        TextInfo textInfo = new TextInfo();
//        textInfo.setOriginalStr(activityNameStr);
//        textInfo.setStart(2);
//        textInfo.setEnd(2 + activityName.length() + 2);
//        textInfo.setColor(Color.parseColor("#00a0e9"));
//        textInfo.setTarget(activityId);
//        return TextUtil.getInstance().setClickAndColor(textInfo, new AbstractTextUtil() {
//            @Override
//            public void onClickSpan(TextInfo textInfo) {
//                Intent intent = new Intent();
//                intent.setClass(context, ActiveDetailActivity.class)
//                        .putExtra("activityId", activityId)
//                        .putExtra("activity_name", activityName);
//                startActivity(intent);
//            }
//        });
//    }
//
//    /**
//     * 设置关注按钮状态
//     *
//     * @param attentionState 当前关注状态
//     */
//    private void setAttentionBtnState(final boolean attentionState) {
//        progress_bar.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progress_bar.setVisibility(View.GONE);
//                tv_videodetail_attention.setEnabled(true);
//                tv_videodetail_attention.setSelected(attentionState);
//                if (attentionState) {
//                    tv_videodetail_attention.setText("已关注");
//                } else {
//                    tv_videodetail_attention.setText("+关注");
//                }
//                MHStateSyncUtil.pushSyncEvent(context, videoDetailBean.getUser_id(), videoDetailBean.isAttention_state());
//            }
//        }, 300);
//    }
//
//    private void setInitAttentionBtnState(boolean attentionState) {
//        //如果当前是自己的视频隐藏关注按钮
//        tv_videodetail_attention.setEnabled(true);
//        tv_videodetail_attention.setSelected(attentionState);
//        if (attentionState) {
//            tv_videodetail_attention.setText("已关注");
//        } else {
//            tv_videodetail_attention.setText("+关注");
//        }
//        if (TextUtils.equals(UserInfoUtil.getUserId(VideoDetailActivity.this), videoDetailBean.getUpload_user_id())) {
//            tv_videodetail_attention.setVisibility(View.GONE);
//        } else {
//            tv_videodetail_attention.setVisibility(View.VISIBLE);
//        }
//    }
//
//    /**
//     * 设置点赞状态
//     *
//     * @param praiseState 当前点赞状态
//     */
//    private void setPraiseState(boolean praiseState) {
//        if (praiseState) {
//            iv_videodetail_praise.setImageResource(R.drawable.svg_videodetail_zanred);
//            iv_videoViewPraise.setImageResource(R.drawable.svg_videoview_praise);
//        } else {
//            iv_videodetail_praise.setImageResource(R.drawable.svg_icon_prise_normal);
//            iv_videoViewPraise.setImageResource(R.drawable.svg_videoview_unpraise);
//        }
//    }
//
////    /**
////     * 设置评论内容
////     */
////    private String getCommentText() {
////        //发送的字符串处理
////        String comment_text = et_videodetail_input.getText().toString().trim();
////        comment_text = comment_text.replaceAll("\\s+", " ").replaceAll("\\\n+", " ");
////        return comment_text;
////    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//        }
////        et_videodetail_input.clearFocus();
//        if (shareDialog != null && shareDialog.isShowing())
//            shareDialog.dismiss();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        hiddenLoadingView();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        if (videoDetail_gift_dialog != null)
//            videoDetail_gift_dialog.dismiss();
//        if (shareDialog != null)
//            shareDialog.dismiss();
//
//        SendGiftDialog.selectPos = 0;
//
//        if (EventBus.getDefault().isRegistered(kv_videodetail)) {
//            EventBus.getDefault().unregister(kv_videodetail);
//        }
//        if (EventBus.getDefault().isRegistered(VideoDetailActivity.this)) {
//            EventBus.getDefault().unregister(VideoDetailActivity.this);
//        }
//        if (PraisedUtil.popupWindow != null) {
//            PraisedUtil.popupWindow.dismiss();
//            PraisedUtil.popupWindow = null;
//        }
//
//        kv_videodetail.keyboardWatcher.destroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_videodetail_attention://关注&取消关注
//                //未登录状态需先登录
//                if (!isLogin(false)) {
//                } else {
//                    if (videoDetailBean != null)
//                        setAttentionOrCancleAttention(videoDetailBean);//获取视频详情后可执行
//                }
//                break;
//
//            case R.id.ll_videodetail_praise://点赞&取消点赞
//                TCAgent.onEvent(context, "视频详情-点赞" + ConstantsValue.android);
//                if (!isLogin(false)) {
//                } else {
//                    if (videoDetailBean != null)
//                        setPraiseOrCanclePraise(videoDetailBean);
//                }
//                break;
//
//            //TODO 废弃
//            case R.id.ll_videodetail_sendgift://送礼物
//                TCAgent.onEvent(context, "视频详情-礼物" + ConstantsValue.android);
//                if (!isLogin(false)) {
//                } else {
//                    if (videoDetailBean != null)
//                        showMHLoading();
//                }
//                break;
//
//            case R.id.ll_videodetail_gift://查看礼物
//                TCAgent.onEvent(context, "视频详情-收礼物" + ConstantsValue.android);
//                if (!isLogin(false)) {
//                } else {
//                    if (videoDetailBean != null && videoDetailBean.getGift_count() != null && Integer.parseInt(videoDetailBean.getGift_count()) != 0) {
//                        startActivity(new Intent(VideoDetailActivity.this, VideoDetailReceiveGiftActivity.class)
//                                .putExtra("videoId", video_id));
//                    } else {
//                        ToastUtils.showToastAtCenter(context, "暂无礼物");
//                    }
//                }
//                break;
//
//            case R.id.mcv_videodetail_header:
//                if (!isLogin(false)) {
//                } else {
//                    if (videoDetailBean != null) {
//                        Intent intent = new Intent(VideoDetailActivity.this, PersonalHomeActivity.class);
//                        intent.putExtra("userId", videoDetailBean.getUpload_user_id());
//                        if (TextUtils.equals(UserInfoUtil.getUserId(context), videoDetailBean.getUpload_user_id())) {
//                            intent.putExtra("isSelf", true);
//                        } else {
//                            intent.putExtra("isSelf", false);
//                        }
//                        intent.putExtra("activityType", 0);
//                        startActivity(intent);
//                    }
//                }
//                break;
//            case R.id.ll_outside:
//                kv_videodetail.reset();
//                break;
//            case R.id.bar_title:
//                tv_title.setText("视频详情");
//                tv_title.setCompoundDrawables(null, null, null, null);
//                tv_title.setEnabled(false);
//                if (!isPlay)
//                    caculatePlayCount();
//                playBig2Small();
//                startPlay();
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//        } catch (Exception e) {
//            MHLogUtil.e(TAG,e);
//        }
//        if (data != null) {
//            if (resultCode == 102) {
//                namesByat = data.getStringArrayListExtra("nameList");
//                userIdByat = data.getStringArrayListExtra("userIds");
//                if (null == namesByat)
//                    namesByat = new ArrayList<>();
//                if (null == userIdByat)
//                    userIdByat = new ArrayList<>();
//
////                userNames = data.getStringExtra("userNames");
//                if (namesByat == null || userIdByat == null) return;
//                if (notify_user_results == null)
//                    notify_user_results = new ArrayList<Notify_user_result>();
////                SpannableStringBuilder oldSpan = new SpannableStringBuilder(et_videodetail_input.getText().toString());
//                for (int i = 0; i < namesByat.size(); i++) {
//                    Notify_user_result notify_user_result = new Notify_user_result();
//                    notify_user_result.setNotify_user_id(userIdByat.get(i));
//                    notify_user_result.setNotify_user_name(namesByat.get(i).trim().replace("@", ""));
//                    notify_user_results.add(notify_user_result);
//                }
//                String oldStr = et_videodetail_input.getText().toString();
//                for (int i = 0; i < namesByat.size(); i++) {
//                    if (i == namesByat.size() - 1) {
//                        oldStr += " " + namesByat.get(i).trim();
//                    } else {
//                        oldStr += " " + namesByat.get(i).trim() + " ";
//                    }
//                }
//                et_videodetail_input.setText(edit_textUtil.handlerString(oldStr, notify_user_results, new AbstractTextUtil() {
//                    @Override
//                    public void onClickSpan(final TextInfo textInfo) {
////                        et_videodetail_input.setSelection(textInfo.getEnd());
//                    }
//
//                    @Override
//                    public void getTextInfos(List<TextInfo> textInfos) {
//                        VideoDetailActivity.this.textInfos = textInfos;
//                    }
//                }));
//            }
//        }
//    }
//
//    /**
//     * 下拉刷新
//     *
//     * @param refreshView 刷新的View
//     */
//    @Override
//    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//
//    }
//
//    /**
//     * 上啦加载更多
//     *
//     * @param refreshView 刷新的View
//     */
//    @Override
//    public void onLoadMore(PullToRefreshBase refreshView) {
//        getComment(video_id);
//    }
//
//    /**
//     * 所送礼物添加数据回调
//     */
//    @Override
//    public void callBack() {
//        getVideoReceiveGift();
//    }
//
//    /**
//     * 获得的礼物对话框监听
//     *
//     * @param dialog
//     */
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        pageIndex_gift = 0;
//    }
//
//    /**
//     * 点击评论回调
//     *
//     * @param parent
//     * @param view
//     * @param position
//     * @param id
//     */
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        //指定评论类型
//        if (position == 0)
//            return;
//        if (commentParent.isEmpty()) return;
//        commentType = 1;
//        curCommentByReply = commentParent.get(position - 1);
//        et_videodetail_input.setHint("回复@" + curCommentByReply.getComment_user_name());
//        ptrl_videodetail.resetFooterLayout();
//        et_videodetail_input.requestFocus();
//        this.selectPosition = position;
//        openKeyBord(et_videodetail_input, VideoDetailActivity.this);
//    }
//
//    //打开软键盘
//    public static void openKeyBord(EditText editText, Context context) {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
//                InputMethodManager.HIDE_IMPLICIT_ONLY);
//    }
//
//    /**
//     * 关闭软键盘
//     */
//    public static void closeKeyBord(Context context) {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(
//                InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//    }
//
//    /**
//     * 成功删除视频
//     */
//    @Override
//    public void deleteVideoSuccess() {
//        finish();
//    }
//
//    /**
//     * 删除视频失败
//     */
//    @Override
//    public void deleteVideoFail() {
//
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (null != VideoView.backDownListener) {
//                return VideoView.backDownListener.OnBackDown();
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    private String getEmojiStringByUnicode(int unicode) {
//        return new String(Character.toChars(unicode));
//    }
//
//    /**
//     * 显示新手引导气泡
//     */
//    public synchronized void showGuideBubble() {
//        Animation anim = AnimationUtils.loadAnimation(context, R.anim.popupwindowbubble);
//        if (!TextUtils.equals(SysMethod.getVersionCode(context), SpUtils.getString(VIDEODETAIL_GIFT_BUBBLE))
//                && !TextUtils.isEmpty(videoDetailBean.getGift_count())
//                && !TextUtils.equals("null", videoDetailBean.getGift_count())
//                && Integer.parseInt(videoDetailBean.getGift_count()) > 0) {//查看礼物
//            SpUtils.put(VIDEODETAIL_GIFT_BUBBLE, SysMethod.getVersionCode(context));
//            //            rl_gift_bubble.setVisibility(View.VISIBLE);
//            //            tv_gift_bubble.setAnimation(anim);
//            //// TODO: 2016/9/9
//            iv_gift_coming.setVisibility(View.VISIBLE);
//            iv_gift_coming.setAnimation(anim);
//
//            anim.setAnimationListener(new Bubble(VIDEODETAIL_GIFT_BUBBLE));
//            return;
//        } else {
//            isSendGift = true;
//            isGift = true;
//        }
//    }
//
//    //@好友
//    @Override
//    public void onAtFriend() {
//        if (!isLogin(false)) {
//        } else {
//            Intent intent = new Intent(context, UserListActivity.class);                    //@好友界面
//            intent.putExtra("type", UserListActivity.USER_LIST_TYPE_AT_FRIENDS);
//            startActivityForResult(intent, UserListActivity.AT_FRIENDS_REQUEST_CODE);
//        }
//        TCAgent.onEvent(context, "视频详情-@" + ConstantsValue.android);
//    }
//
//    //发送评论
//    @Override
//    public void onSendComment() {
//        if (!isLogin(false)) {
//        } else {
//            TCAgent.onEvent(context, "视频详情-发送" + ConstantsValue.android);
//            if (et_videodetail_input.getText().toString().trim().length() == 0) {
//                showToastAtCenter("评论内容不能为空!");
//            } else {
//                sendComment();
//            }
//            ptrl_videodetail.resetFooterLayout();
//        }
//    }
//
//    //发送礼物
//    @Override
//    public void onSendGift() {
//        if (sendGiftDialog == null) {
//            int giftCount = 0;
//            if (videoDetailBean != null) {
//                String giftCountStr = videoDetailBean.getGift_count();
//                if (!MHStringUtils.isEmpty(giftCountStr)) {
//                    giftCount = Integer.parseInt(giftCountStr);
//                }
//            }
//            sendGiftDialog = new SendGiftDialog(context, giftCount, video_id);
//        } else {
//            sendGiftDialog.show();
//        }
//    }
//
//    @Override
//    public void onTextChangeAfter(Editable s) {
//        edit_textUtil.handlerString(s.toString(), notify_user_results, new AbstractTextUtil() {
//            @Override
//            public void onClickSpan(final TextInfo textInfo) {
////                et_videodetail_input.setSelection(textInfo.getEnd());
//            }
//
//            @Override
//            public void getTextInfos(List<TextInfo> textInfos) {
//                super.getTextInfos(textInfos);
//                VideoDetailActivity.this.textInfos = textInfos;
//            }
//        });
//    }
//
//    @Override
//    public void onTextChangeBefore(CharSequence s, int start, int count, int after) {
////        if(textInfos==null) return;
////        for (int i = 0; i < textInfos.size(); i++) {
////            TextInfo textInfo = textInfos.get(i);
////            if(textInfo!=null){
////                if(start>textInfo.getStart()&&start<=textInfo.getEnd()){
////
////                }
////            }
////        }
//    }
//
//    /**
//     * 视频播放器内点赞
//     */
//    @Override
//    public void onClickPraise(ImageView view) {
//        TCAgent.onEvent(context, "视频详情-点赞" + ConstantsValue.android);
//        if (!isLogin(false)) {
//        } else {
//            if (videoDetailBean != null) {
//                view.setEnabled(false);
//                videoView_praise = view;
//                setPraiseOrCanclePraise(videoDetailBean);
//            }
//
//        }
//    }
//
//    @Override
//    public void onClickReplay() {
//
//    }
//
//    /**
//     * 点击小视频view
//     */
//    @Override
//    public void onClickSmallVideoView() {
//        mediaPlayer.pause();
//        tv_title.setText("立即播放");
//        Drawable leftDrawable = getResources().getDrawable(R.drawable.svg_videodetail_titleicon);
//        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
//        tv_title.setCompoundDrawables(leftDrawable, null, null, null);
//        tv_title.setCompoundDrawablePadding(DensityUtil.dip2px(context, 3));
//        tv_title.setEnabled(true);
//        tv_title.startAnimation(scaleAnimation);
//        playSmall2Big();
//    }
//
//    @Override
//    public void onPlayComplete(ImageView iv_play) {
//        if (isBig) {
//            isPlay = false;
//            iv_play.setVisibility(View.GONE);
//            rl_videoviewsmallclick.setVisibility(View.GONE);
//            videoView.showPraiseAndReStart(praiseState);
//        } else {
//            videoView.reStart();
//        }
//    }
//
//    @Override
//    public void onFirstPlay(View ll_bottom_control, ProgressBar pb_bottom) {
//        if (!isBig && !isPlay) {
//            ll_bottom_control.setVisibility(View.GONE);
//            pb_bottom.setVisibility(View.VISIBLE);
//        }
//    }
//
//    /**
//     * 设置屏幕旋转监听
//     */
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
//                            kv_videodetail.hidenSoft4RotateScreen();
//                            videoView.setRequestOrientation(requestedOrientation);
//                            videoView.setFullScreen(requestedOrientation);
//                            break;
//
//                        //切换成竖屏
//                        case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
//                            closeAllDialog();
//                            kv_videodetail.hidenSoft4RotateScreen();
//                            videoView.quitFullScreen();
//                            break;
//                    }
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 光标位置监听
//     *
//     * @param selStart
//     * @param selEnd
//     */
//    @Override
//    public void change(int selStart, int selEnd) {
//        if (textInfos != null) {
//            for (int i = 0; i < textInfos.size(); i++) {
//                TextInfo textInfo = textInfos.get(i);
//                if (textInfo != null) {
//                    if (selStart > textInfo.getStart() && selStart <= textInfo.getEnd()) {
//                        et_videodetail_input.setSelection(textInfo.getEnd());
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 软键盘弹起
//     *
//     * @param keyboardSize
//     */
//    private int mDistance;
//
//    @Override
//    public void onKeyboardShown(int keyboardSize) {
//        if (et_videodetail_input.getText().length() == 0) {
//            et_videodetail_input.setSelection(0);
//        }
//        if (selectPosition <= 0) return;
//        int firstListItemPosition = lv.getFirstVisiblePosition();
//        int lastListItemPosition = firstListItemPosition + lv.getChildCount() - 1;
//        View view = null;
//        if (selectPosition < firstListItemPosition || selectPosition > lastListItemPosition) {
//            view = lv.getAdapter().getView(selectPosition, null, lv);
//        } else {
//            int childIndex = selectPosition - firstListItemPosition;
//            view = lv.getChildAt(childIndex);
//        }
//        if (view == null) return;
//        int[] kv_xy = new int[2];
//        kv_videodetail.getLocationOnScreen(kv_xy);
//        int kv_height = kv_xy[1];
//        int[] child_xy = new int[2];
//        view.getLocationOnScreen(child_xy);
//        int child_height = child_xy[1] + view.getHeight();
//        int distance = child_height - kv_height;
//        if (distance > 0) {
//            VideoDetailUserCommentBean videoDetailUserCommentBean = new VideoDetailUserCommentBean();
//            videoDetailUserCommentBean.setSize(distance + view.getHeight());
//            commentParent.add(videoDetailUserCommentBean);
//            commentAdapter.notifyDataSetChanged();
//            mDistance = distance;
//            lv.post(new Runnable() {
//                @Override
//                public void run() {
//                    lv.smoothScrollBy(mDistance, 500);
//                }
//            });
//        }
//    }
//
//    /**
//     * 软键盘关闭
//     */
//    @Override
//    public void onKeyboardClosed() {
//        for (int i = 0; i < commentParent.size(); i++) {
//            if (commentParent.get(i).getSize() != -1) {
//                commentParent.remove(i);
//                commentAdapter.notifyDataSetChanged();
//                return;
//            }
//        }
//    }
//
//
//    //气泡动画监听
//    public class Bubble implements Animation.AnimationListener {
//        private String type;
//
//        public Bubble(String type) {
//            this.type = type;
//        }
//
//        @Override
//        public void onAnimationStart(Animation animation) {
//
//        }
//
//        @Override
//        public void onAnimationEnd(Animation animation) {
//            if (TextUtils.equals(VIDEODETAIL_SENDGIFT_BUBBLE, type)) {
//                iv_gift_coming.setVisibility(View.GONE);
//                isSendGift = true;
//                showGuideBubble();
//            } else if (TextUtils.equals(VIDEODETAIL_GIFT_BUBBLE, type)) {
//                iv_gift_coming.setVisibility(View.GONE);
//                isGift = true;
//                showGuideBubble();
//            } else if (TextUtils.equals(VIDEODETAIL_TOTALCOMMENT_BUBBLE, type)) {
//            }
//        }
//
//        @Override
//        public void onAnimationRepeat(Animation animation) {
//
//        }
//    }
//
//    /**
//     * 判断是否是emoji
//     *
//     * @param source
//     * @return
//     */
//    public boolean containsEmoji(String source) {
//        if (TextUtils.isEmpty(source)) {
//            return false;
//        }
//        int len = source.length();
//        for (int i = 0; i < len; i++) {
//            char codePoint = source.charAt(i);
//            if (isEmojiCharacter(codePoint)) {
//                //do nothing，判断到了这里表明，确认有表情字符
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean isEmojiCharacter(char codePoint) {
//        return !((codePoint == 0x0) ||
//                (codePoint == 0x9) ||
//                (codePoint == 0xA) ||
//                (codePoint == 0xD) ||
//                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
//                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
//                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
//    }
//
//    @Override
//    public void onReTry() {
//
//    }
//
//    @Override
//    public void onAutoLoadMore() {
//        getComment(video_id);
//    }
//
//    /**
//     * 关闭dialog
//     */
//    private void closeAllDialog() {
//        if (null != videoDetail_gift_dialog && videoDetail_gift_dialog.isShowing())
//            videoDetail_gift_dialog.dismiss();
//        if (null != shareDialog && shareDialog.isShowing())
//            shareDialog.dismiss();
//    }
//
//    /**
//     * 计算点击次数
//     */
//    private void caculatePlayCount() {
//        long count = 0;
//        if (MHStringUtils.isEmpty(videoDetailBean.getPlay_total())) {
//            count = 1;
//        } else {
//            count = Long.valueOf(videoDetailBean.getPlay_total()) + 1;
//        }
//        tv_videodetail_playcount.setText(MHStringUtils.countFormat(count + "") + " 播放");
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void RefreshGiftCountEvent(RefreshGiftCountEvent event) {
//
//        tv_videodetail_gift.setText("礼物" + MHStringUtils.countFormat(event.getCurrentCount() + ""));
//        videoDetailBean.setGift_count(event.getCurrentCount() + "");
//
//    }
//
//    /**
//     * 监听软键盘删除@好友,并删除
//     */
//    public void checkDeleteAtFriend() {
//        et_videodetail_input.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DEL) {
//                    String inputStr = et_videodetail_input.getText().toString();
//                    int selectorIndex = et_videodetail_input.getSelectionStart();
//                    if (textInfos != null)
//                        for (int i = 0; i < textInfos.size(); i++) {
//                            TextInfo textInfo = textInfos.get(i);
//                            if (selectorIndex > textInfo.getStart() && selectorIndex <= textInfo.getEnd()) {
//                                textInfos.remove(i);
//                                notify_user_results.remove(i);
//                                Editable editable = et_videodetail_input.getText();
//                                editable.delete(textInfo.getStart(), textInfo.getEnd());
//                            }
//                        }
//                }
//                return false;
//            }
//        });
//    }
//
//    /**
//     * 获取评论内容
//     *
//     * @return
//     */
//    private String getCommentText() {
//        String commentText = et_videodetail_input.getText()
//                .toString()
//                .trim()
//                .replaceAll("\\s+", " ")
//                .replaceAll("\\\n", " ");
//        StringBuilder commentTextBuilder = new StringBuilder(commentText);
//        Map<String, Integer> stageMap = new HashMap<>();
//        int index = 0;
//        if (notify_user_results == null) return commentText;
//        for (int i = 0; i < notify_user_results.size(); i++) {
//            Notify_user_result notify_user_result = notify_user_results.get(i);
//            if (notify_user_result == null
//                    || notify_user_result.getNotify_user_id() == null
//                    || notify_user_result.getNotify_user_name() == null) continue;
//            //map中是否存在重名
//            if (stageMap.containsKey(notify_user_result.getNotify_user_name())) {//存在
//                index = commentTextBuilder.indexOf("@" + notify_user_result.getNotify_user_name(), stageMap.get(notify_user_result.getNotify_user_name()));
//            } else {
//                index = commentTextBuilder.indexOf("@" + notify_user_result.getNotify_user_name());
//            }
//            stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length());
//            int lastIndex = index + notify_user_result.getNotify_user_name().length();
//            //向@好友加空格
//            if (commentTextBuilder.indexOf(" ", lastIndex) == lastIndex + 1) {
//                commentTextBuilder.insert(lastIndex + 1, " ");
//            } else {
//                commentTextBuilder.insert(lastIndex + 1, "  ");
//            }
//            if (index == 0) {
//                commentTextBuilder.insert(0, "  ");
//                stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 2);
//            } else if (index == 1) {
//                if (commentTextBuilder.indexOf(" ") == 0) {
//                    commentTextBuilder.insert(0, " ");
//                    stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 1);
//                } else {
//                    commentTextBuilder.insert(1, "  ");
//                    stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 2);
//                }
//            } else {
//                if (commentTextBuilder.indexOf(" ", index - 1) == index - 1) {
//                    commentTextBuilder.insert(index - 1, " ");
//                    stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 1);
//                } else if (commentTextBuilder.indexOf(" ", index - 1) == -1) {
//                    commentTextBuilder.insert(index - 1, "  ");
//                    stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 2);
//                }
//            }
//        }
//        return commentTextBuilder.toString();
//    }
//
//    /**
//     * 设置是否显示无评论提示
//     *
//     * @param visible
//     */
//    private void setNoCommentVisible(int visible) {
//        if (noComment == null)
//            noComment = View.inflate(context, R.layout.layout_videodetail_nocomment, null);
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.height = DensityUtil.dip2px(context, 120);
//        noComment.setLayoutParams(lp);
//        if (visible == View.VISIBLE) {
//            lv.addFooterView(noComment);
//        } else if (visible == View.GONE) {
//            lv.removeFooterView(noComment);
//        }
//    }
//
//    /**
//     * 从关注也或者个人中心点击评论进入视频详情或者发送评论后评论数量条目
//     * 需定位到导航栏下方如果评论条目不足则显示到底部
//     */
//    public void positionFromCommentList() {
//        int[] cn_location = new int[2];
//        cn_videodetail.getLocationOnScreen(cn_location);
//        int[] commentCount = new int[2];
//        rl_videodetail_totalcommentparent.getLocationOnScreen(commentCount);
//        int offset = 0;
//        if (commentCount[1] > 0) {
//            offset = commentCount[1] - cn_location[1] - cn_videodetail.getHeight();
//        } else {
//            lv.setSelection(1);
//            offset = -rl_videodetail_totalcommentparent.getHeight();
//        }
//        lv.smoothScrollBy(offset, 1000);
//    }

}

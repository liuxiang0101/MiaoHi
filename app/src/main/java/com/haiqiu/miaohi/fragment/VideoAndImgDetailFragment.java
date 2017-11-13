package com.haiqiu.miaohi.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.CommonPersonInfo;
import com.haiqiu.miaohi.activity.OnLookersListAvtivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.QAHomeActivity;
import com.haiqiu.miaohi.activity.UserListActivity;
import com.haiqiu.miaohi.activity.VideoAndImgActivity;
import com.haiqiu.miaohi.adapter.VideoDetailAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.ImgDetail;
import com.haiqiu.miaohi.bean.Notify_user_result;
import com.haiqiu.miaohi.bean.PraiseBean;
import com.haiqiu.miaohi.bean.SendCommentResponse;
import com.haiqiu.miaohi.bean.ShareVideoAndImgInfo;
import com.haiqiu.miaohi.bean.TextInfo;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.bean.VideoDetailAttentionResponse;
import com.haiqiu.miaohi.bean.VideoDetailBean;
import com.haiqiu.miaohi.bean.VideoDetailPraiseResponse;
import com.haiqiu.miaohi.bean.VideoDetailResponse;
import com.haiqiu.miaohi.bean.VideoDetailUserCommentBean;
import com.haiqiu.miaohi.bean.VideoDetailUserCommentResponse;
import com.haiqiu.miaohi.bean.VideoDetailUserCommentResult;
import com.haiqiu.miaohi.bean.VideoDetail_dialogGiftBean;
import com.haiqiu.miaohi.bean.VideoExtraInfo;
import com.haiqiu.miaohi.bean.VideoInfo;
import com.haiqiu.miaohi.bean.VideoItemPageResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.CloseEmojiKeyboard;
import com.haiqiu.miaohi.receiver.DeleteVideoAndImgAsyncEvent;
import com.haiqiu.miaohi.receiver.RefreshCommentCountEvent;
import com.haiqiu.miaohi.receiver.StopVideoEvent;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.VideoAndImgResponse;
import com.haiqiu.miaohi.umeng.IUMShareResultListener;
import com.haiqiu.miaohi.umeng.ShareImg;
import com.haiqiu.miaohi.umeng.UmengShare;
import com.haiqiu.miaohi.utils.AbstractTextUtil;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.LongClickableLinkMovementMethod;
import com.haiqiu.miaohi.utils.MHContentSyncUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.NoDoubleClickUtils;
import com.haiqiu.miaohi.utils.PraisedUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.utils.TextUtil;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.shareImg.SharePersonalHomeImgView;
import com.haiqiu.miaohi.utils.shareImg.ShareVideoAndImgView2;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.CommonPersonalInfoView;
import com.haiqiu.miaohi.view.DeleteCommentDialog;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.view.NoteEditText;
import com.haiqiu.miaohi.view.SendGiftDialog;
import com.haiqiu.miaohi.view.ShareDialog;
import com.haiqiu.miaohi.widget.KeyBoardView;
import com.haiqiu.miaohi.widget.PraiseImageView;
import com.haiqiu.miaohi.widget.ShareLayout;
import com.haiqiu.miaohi.widget.mediaplayer.BaseVideoView;
import com.haiqiu.miaohi.widget.mediaplayer.MyMediaPlayer;
import com.haiqiu.miaohi.widget.mediaplayer.VideoView;
import com.haiqiu.miaohi.widget.mediaplayer.VideoViewWrap;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 视频和图片详情
 * Created by ningl on 16/12/13.
 */
public class VideoAndImgDetailFragment extends BaseFragment implements View.OnClickListener
        , PullToRefreshBase.OnRefreshListener, AbsListView.OnItemClickListener
        , ShareLayout.DeleteVideoListener, KeyBoardView.OnEmojiDoListener
        , VideoView.VideoControlListener, NoteEditText.OnCursorChange
        , VideoDetailAdapter.OnCommentLongClick {

    private CommonNavigation cn_videodetail;
    private NoteEditText et_videodetail_input;
    private ViewGroup headerView;

    private TextView tv_title;
    private TextView tv_deletetext;
    private TextView tv_videoandimg_time;
    private TextView tv_videodetail_name;
    private TextView tv_videodetail_date;
    private TextView tv_videodetail_praise;
    private TextView tv_videodetail_describe;
    private TextView tv_videodetail_attention;
    private TextView tv_videodetail_playcount;
    private TextView tv_videodetaildescription;
    private TextView tv_videoandimgpraise_count;
    private TextView tv_videodetail_totalcomment;

    private ImageView iv_share;
    private ImageView iv_videoandimgask;
    private ImageView iv_videoandimgback;
    private ImageView iv_videoandimgshare;
    private ImageView iv_videoandimgpraise;
    private ImageView iv_videoandimgcomment;
    private ImageView iv_videodetail_headervip;

    private LinearLayout ll_outside;
    private LinearLayout ll_praiselist;
    private LinearLayout ll_smallvideoview;
    private LinearLayout ll_videodetail_praise;
    private LinearLayout ll_videoandimg_bottom;
    private LinearLayout ll_videoandimg_praise;

    private RelativeLayout rl_deleted;
    private FrameLayout fl_keboardmarsk;

    private ListView lv;
    private KeyBoardView kv_videodetail;
    private MyCircleView mcv_videodetail_header;
    private PullToRefreshListView ptrl_videodetail;
    private CommonPersonalInfoView cpiv_videoandimg;
    private PraiseImageView iv_imgdetail;
    private VideoView videoView;
    private ShareVideoAndImgView2 sviv_videoandimg;

    private View progress_bar;
    private View videoView_praise;
    private View noComment;// 无评论抢沙发view
    private VideoViewWrap vvw_videodetail;

    private List<TextInfo> textInfos;
    private List<Notify_user_result> notify_user_results;//@的好友
    private List<VideoDetailUserCommentBean> commentTotal;//所有视频详情评论数据
    private List<VideoDetailUserCommentBean> commentParent;//视频详情评论数据
    private List<VideoDetail_dialogGiftBean> videoDetail_dialogGiftBeans;//视频详情礼物对话框数据
    private ArrayList<String> namesByat, userIdByat;

    private MyMediaPlayer mediaPlayer;
    private VideoDetailAdapter commentAdapter;//评论适配器

    private ShareDialog shareDialog;
    private DeleteCommentDialog dialog;

    private VideoDetailUserCommentResult data;//评论数据
    private VideoAndImg videoAndImg;
    private VideoDetailBean mVideoDetail;
    private ImgDetail mImgDetail;
    private VideoDetailUserCommentBean curCommentByReply;//当前要回复的评论

    private String video_id;//视频id
    private String photo_id;
    private String vipCount;//大咖评论数
    private String totalCount;//总评论数
    private String kind_tag; //首页的tag 为空就传"0"
    private String userNames;
    private String notify_comment_id;
    private String notify_comment_user_id;
    private String notify_comment_user_name;

    private static final String VIDEODETAIL_SENDGIFT_BUBBLE = "videodetail_sendgift_bubble";//送礼物
    private static final String VIDEODETAIL_GIFT_BUBBLE = "videodetail_gift_bubble";//查看礼物
    private static final String VIDEODETAIL_TOTALCOMMENT_BUBBLE = "videodetail_totalcomment_bubble";//总评论

    private int type;//视频或者图片
    private int pageIndex;
    private int screenWith;
    private int commentType = 0;//当前评论类型 0评论视频 1回复
    private int firstVisibleItem;
    private int videoPlayerSwitchHeight;
    private int scrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private final int IMG = 2;
    private final int VIDEO = 1;

    private TextUtil edit_textUtil;
    private ScaleAnimation scaleAnimation;

    private boolean isPlay;//是否已经启动过播放
    private boolean isDelete;
    private boolean praiseState;//点赞状态
    private boolean isSmallToBig;//是否是从小的播放窗口
    private boolean attentionState;//关注状态
    private boolean isFromCommentList;
    private boolean isEnterFromNotify;//是否来自通知
    private boolean isBig = true;
    private boolean isFirstRequest = false;//视频或者图片删除后是否自动返回

//    private RelativeLayout rl_videodetail_totalcommentparent;
//    private FrameLayout fl_videoandimg;
//    private PhotoView pv_videoandimg;
//    public VideoDetailBean videoDetailBean = new VideoDetailBean();//视频详情数据
//    private ScreenSwitchUtils screenSwitchUtils;
//    public static VideoDetailActivity videoDetailActivity;
//    private ImgDetail imgDetail;

    public VideoAndImgDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = View.inflate(context, R.layout.activity_videodetail, null);
        registEvenBus();
        videoAndImg = getArguments().getParcelable("videoandimg");
        isFromCommentList = getArguments().getBoolean("isFromCommentList");
        isDelete = getArguments().getBoolean("isDelete");
        this.video_id = videoAndImg.getVideo_id();
        this.photo_id = videoAndImg.getPhoto_id();
        this.type = videoAndImg.getType();
        initView(rootView);
        if (type == VIDEO) {
            //视频
            vvw_videodetail.setVisibility(View.VISIBLE);
            iv_imgdetail.setVisibility(View.GONE);
        } else if (type == IMG) {
            //图片
            vvw_videodetail.setVisibility(View.GONE);
            iv_imgdetail.setVisibility(View.VISIBLE);
        }
        pageIndex = 0;
        TCAgent.onEvent(context, "视频详情页总打开数" + ConstantsValue.android);
//        videoDetailActivity = this;
        edit_textUtil = TextUtil.getInstance();//编辑框文本工具类实例
//        screenSwitchUtils = ScreenSwitchUtils.init(context);
//        setOnSwitchScreenListener();
        screenWith = ScreenUtils.getScreenSize(context).x;
        commentParent = new ArrayList<>();
        commentTotal = new ArrayList<>();
        namesByat = new ArrayList<>();
        userIdByat = new ArrayList<>();
        commentAdapter = new VideoDetailAdapter((BaseActivity) getActivity(), commentParent, VideoAndImgDetailFragment.this);
        lv.setAdapter(commentAdapter);
        et_videodetail_input = kv_videodetail.getEt();
        et_videodetail_input.setMovementMethod(new LongClickableLinkMovementMethod());
        et_videodetail_input.setOnCursorChangeListener(this);
        checkDeleteAtFriend();
        SpannableStringBuilder builder = new SpannableStringBuilder("aaaa");
        kv_videodetail.setOnEmojiDoListener(this);
        et_videodetail_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ptrl_videodetail.resetFooterLayout();
//                if (hasFocus)
//                    kv_videodetail.hideLayout();
            }
        });

        videoPlayerSwitchHeight = ScreenUtils.getScreenSize(context).x;
        ptrl_videodetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        //滑动停止时调用，
                        VideoAndImgDetailFragment.this.scrollState = SCROLL_STATE_IDLE;
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        //正在滚动时调用
                        VideoAndImgDetailFragment.this.scrollState = SCROLL_STATE_TOUCH_SCROLL;
                        break;
                    case SCROLL_STATE_FLING:
                        //手指快速滑动时,在离开ListView由于惯性滑动
                        VideoAndImgDetailFragment.this.scrollState = SCROLL_STATE_FLING;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                VideoAndImgDetailFragment.this.firstVisibleItem = firstVisibleItem;
                int offset = getScrollY(lv) - videoPlayerSwitchHeight;
                if (offset > 0 && isBig && mediaPlayer.isPlaying()
                        && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                        || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                        ) {//从大视频切换到小视频并且视频处于播放状态
                    playBig2Small();
                    tv_title.setEnabled(false);
                } else if (offset > 0 && isBig && !mediaPlayer.isPlaying()) {//从大视频切换到小视频并且视频处于暂停状态
                    tv_title.setText("立即播放");
                    tv_title.setCompoundDrawablePadding(DensityUtil.dip2px(context, 3));
                    setTitleClickPlay();
                    tv_title.setEnabled(true);
                } else if (offset < 0 && !isBig
                        && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                        || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)) {//从小视频切换到大视频
                    playSmall2Big();
                    tv_title.setEnabled(false);
                    tv_title.setText("视频详情");
                    tv_title.setCompoundDrawables(null, null, null, null);
                } else if (offset < 0 && isBig
                        && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                        || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                        && videoView.getCurrentMediaState() == BaseVideoView.MEDIA_STATE_PAUSE
                        && isSmallToBig) {
                    isSmallToBig = false;
                    mediaPlayer.start();
                }
            }
        });
        if (null == scaleAnimation) {
            scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(400);
            scaleAnimation.setFillAfter(true);
        }

        kv_videodetail.setOnKeyboardListener(new KeyBoardView.OnKeboardListener() {
            @Override
            public void onOpen() {
                ll_videoandimg_bottom.setVisibility(View.GONE);
                kv_videodetail.setVisibility(View.VISIBLE);
                fl_keboardmarsk.setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(fl_keboardmarsk, "alpha", 0, 0.3f)
                        .setDuration(200)
                        .start();
            }

            @Override
            public void onClose() {
                ll_videoandimg_bottom.setVisibility(View.VISIBLE);
                kv_videodetail.setVisibility(View.GONE);
                fl_keboardmarsk.setVisibility(View.GONE);
                if (et_videodetail_input.getText().toString().trim().length() == 0) {
                    et_videodetail_input.setHint(" 发射评论...(500字)");
                    commentType = 0;
                }
            }
        });

        fl_keboardmarsk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                kv_videodetail.hideKeyboard(context);
                kv_videodetail.hideLayout();
                ll_videoandimg_bottom.setVisibility(View.VISIBLE);
                kv_videodetail.setVisibility(View.GONE);
                fl_keboardmarsk.setVisibility(View.GONE);

                if (et_videodetail_input.getText().length() == 0) {
                    kv_videodetail.setGiftText();
                } else {
                    kv_videodetail.tv_videodetail_send.setText("发送");
                }

                return true;
            }
        });
        videoView.setOnPraiseListener(new VideoView.OnPraiseListener() {
            @Override
            public void onPraiseClick(View view, boolean isPraise) {
                TCAgent.onEvent(context, "视频详情-点赞" + ConstantsValue.android);
                if (isLogin(false)) {
                    if (mVideoDetail != null) {
                        view.setEnabled(false);
                        videoView_praise = view;
                        switchVideoPraise(mVideoDetail, false);
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            isDelete = savedInstanceState.getBoolean("isDelete");
            rl_deleted.setVisibility(isDelete ? View.VISIBLE : View.GONE);
            if (type == VIDEO) {
                //视频详情
                mVideoDetail = (VideoDetailBean) savedInstanceState.getParcelable("videoDetail");
                video_id = savedInstanceState.getString("video_id");
            } else if (type == IMG) {
                //图片
                mImgDetail = (ImgDetail) savedInstanceState.getParcelable("imgDetail");
                photo_id = savedInstanceState.getString("photo_id");
            }
            data = (VideoDetailUserCommentResult) savedInstanceState.getParcelable("commentData");
            //如果未加载完数据就被销毁 需重新加载
            if (mVideoDetail == null || mImgDetail == null) {
                if (type == VIDEO) {
                    getVideoDetail(video_id, false);
                } else if (type == IMG) {
                    getImgDetail(photo_id, false);
                }
            } else {
                pageIndex = savedInstanceState.getInt("pageIndex");
                praiseState = savedInstanceState.getBoolean("praiseState");
                attentionState = savedInstanceState.getBoolean("attentionState");
                firstVisibleItem = savedInstanceState.getInt("lastPosition");
                lv.setSelection(firstVisibleItem);
                if (type == VIDEO) {
                    setVideoDetail(mVideoDetail, false);
                } else if (type == IMG) {
                    setImgDetail(mImgDetail, false);
                }
            }
            //如果评论数据为空则重新加载
            if (data == null) {
                getComment(video_id);
            } else {
                if (data != null) setComment(data);
            }
        } else {
            ptrl_videodetail.showMHLoading();
            MHLogUtil.i(TAG, "请求");
            if (type == VIDEO) {
                getVideoDetail(video_id, false);
            } else if (type == IMG) {
                getImgDetail(photo_id, false);
            }
            getComment(video_id);
        }

        try {
            JSONObject jsonObject = new JSONObject();
            if (type == VIDEO) {
                jsonObject.put("type", "videodetailspage");
                jsonObject.put("description", "视频详情页");
                if (null != video_id)
                    jsonObject.put("id", video_id);
            } else {
                jsonObject.put("type", "picdetailspage");
                jsonObject.put("description", "图片详情页");
                if (null != photo_id)
                    jsonObject.put("id", photo_id);
            }
            BehaviourStatistic.uploadBehaviourInfo(jsonObject);
        } catch (Exception e) {
            MHLogUtil.e(TAG, e);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("commentData", data);//评论数据
        outState.putInt("lastPosition", firstVisibleItem);
        outState.putBoolean("praiseState", praiseState);//点赞状态
        outState.putBoolean("attentionState", attentionState);//关注状态
        outState.putBoolean("isDelete", isDelete);
        outState.putInt("pageIndex", pageIndex);
        if (type == VIDEO) {
            //视频
            outState.putParcelable("videoDetail", mVideoDetail);//视频数据
            outState.putString("video_id", video_id);
        } else if (type == IMG) {
            //图片
            outState.putParcelable("imgDetail", mImgDetail);
            outState.putString("photo_id", photo_id);
        }
        MHLogUtil.i("销毁");
    }

    /**
     * 设置标题可点击 点击切换到小播放器
     */
    private void setTitleClickPlay() {
        Drawable leftDrawable = getResources().getDrawable(R.drawable.svg_videodetail_titleicon);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        tv_title.setCompoundDrawables(leftDrawable, null, null, null);
        tv_title.startAnimation(scaleAnimation);
    }

    /**
     * 获取滚动距离
     *
     * @param lv
     * @return
     */
    public int getScrollY(ListView lv) {//获取滚动距离
        View c = lv.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = lv.getFirstVisiblePosition();
        int top = c.getTop();
        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = lv.getHeight();
            return -top + kv_videodetail.getSoftKeyboardHeight() + firstVisiblePosition * c.getHeight() + headerHeight;
        }
        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    /**
     * 播放状态下从大播放器切换到小播放器
     */
    private void playBig2Small() {
        if (!isBig) return;

        kv_videodetail.hidenSoft4RotateScreen();
        vvw_videodetail.clearAnimation();
        videoView.switchSmallVideo();
        ((ViewGroup) videoView.getParent()).removeView(videoView);

        isBig = false;

        float tempX = 3 * screenWith / 5;
        if (videoView.getVideoWidth() >= videoView.getVideoHeight()) {//长视频
            tempX = 2 * screenWith / 5;
        }
        //最终的X值
        final float targetX = tempX;
        float targetWidth = screenWith - targetX;
        float targetHeight = (int) (targetWidth * videoView.getVideoHeight() / videoView.getVideoWidth());

        ViewGroup.LayoutParams params = videoView.getLayoutParams();
        params.width = (int) targetWidth;
        params.height = (int) targetHeight;
        videoView.setLayoutParams(params);

        final float scaleX = 1.0f * (videoView.getVideoRenderWidth() - targetWidth) / videoView.getVideoRenderWidth();
        final float scaleY = 1.0f * (videoView.getVideoRenderHeight() - targetHeight) / videoView.getVideoRenderHeight();


        //setScaleX 与setScaleY 不会改变控件的布局参数,只会改变子View的大小
        ll_smallvideoview.addView(videoView);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(400);
        final float finalTargetWidth = targetWidth;
        final float finalTargetHeight = targetHeight;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                videoView.setX((targetX + (finalTargetWidth * scaleX / 2)) * animatedValue);
                videoView.setY(-videoView.getVideoRenderHeight() + (finalTargetHeight - finalTargetHeight * scaleY / 2) * animatedValue);
                videoView.setScaleX(1 - scaleX * animatedValue);
                videoView.setScaleY(1 - scaleY * animatedValue);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                videoView.changeSize();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    /**
     * 播放状态下从小播放器切换到大播放器
     */
    private void playSmall2Big() {
        if (isBig) return;
        videoView.clearAnimation();
        isBig = true;
        if (ll_smallvideoview.getChildCount() == 0)
            return;

        videoView.switchBigVideo();

        final float targetX = 3 * screenWith / 5;
        final float targetWidth = screenWith - targetX;
        final float targetHeight = (int) (targetWidth * videoView.getVideoHeight() / videoView.getVideoWidth());

        ViewGroup.LayoutParams params = videoView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        videoView.setLayoutParams(params);

        final float scaleX = 1.0f * (videoView.getWidth() - targetWidth) / videoView.getWidth();
        final float scaleY = 1.0f * (videoView.getHeight() - targetHeight) / videoView.getHeight();


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.setDuration(400);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                videoView.setX(targetX * animatedValue);
                videoView.setY(-(videoView.getHeight() - targetHeight) / 2 * animatedValue);
                videoView.setScaleX(1 - scaleX * animatedValue);
                videoView.setScaleY(1 - scaleY * animatedValue);
            }
        });
        valueAnimator.start();

        ((LinearLayout) videoView.getParent()).removeView(videoView);
        vvw_videodetail.addView(videoView);
    }

    /**
     * 初始化控件
     */
    private void initView(View rootView) {
        cn_videodetail = (CommonNavigation) rootView.findViewById(R.id.cn_videodetail);
        cpiv_videoandimg = (CommonPersonalInfoView) rootView.findViewById(R.id.cpiv_videoandimg);
        ptrl_videodetail = (PullToRefreshListView) rootView.findViewWithTag("ptrl_videodetail");
        kv_videodetail = (KeyBoardView) rootView.findViewById(R.id.kv_videodetail);
        ll_outside = (LinearLayout) rootView.findViewById(R.id.ll_outside);
        ll_smallvideoview = (LinearLayout) rootView.findViewById(R.id.ll_smallvideoview);
        fl_keboardmarsk = (FrameLayout) rootView.findViewById(R.id.fl_keboardmarsk);
        ll_videoandimg_bottom = (LinearLayout) rootView.findViewById(R.id.ll_videoandimg_bottom);
        sviv_videoandimg = (ShareVideoAndImgView2) rootView.findViewById(R.id.sviv_videoandimg);
        rl_deleted = (RelativeLayout) rootView.findViewById(R.id.rl_deleted);
        tv_deletetext = (TextView) rootView.findViewById(R.id.tv_deletetext);

        iv_videoandimgback = (ImageView) rootView.findViewById(R.id.iv_videoandimgback);
        iv_videoandimgpraise = (ImageView) rootView.findViewById(R.id.iv_videoandimgpraise);
        iv_videoandimgcomment = (ImageView) rootView.findViewById(R.id.iv_videoandimgcomment);
        iv_videoandimgshare = (ImageView) rootView.findViewById(R.id.iv_videoandimgshare);
        iv_videoandimgask = (ImageView) rootView.findViewById(R.id.iv_videoandimgask);
        kv_videodetail.setRootView(rootView.findViewById(R.id.ll_rootview_videoandimg));
        iv_videoandimgback.setOnClickListener(this);
        iv_videoandimgcomment.setOnClickListener(this);
        iv_videoandimgpraise.setOnClickListener(this);
        iv_videoandimgshare.setOnClickListener(this);
        iv_videoandimgask.setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(iv_videoandimgback);
        SetClickStateUtil.getInstance().setStateListener(iv_videoandimgcomment);
        SetClickStateUtil.getInstance().setStateListener(iv_videoandimgpraise);
        SetClickStateUtil.getInstance().setStateListener(iv_videoandimgshare);
        SetClickStateUtil.getInstance().setStateListener(iv_videoandimgask);
        ptrl_videodetail.setAutoLoadMoreIsEnable(true);
        ll_outside.setOnClickListener(this);
        ll_outside.setClickable(false);
        lv = ptrl_videodetail.getRefreshableView();
        lv.setDividerHeight(0);
        ptrl_videodetail.setPullRefreshEnabled(false);//不允许下拉刷新
        ptrl_videodetail.setOnRefreshListener(this);
        iv_share = new ImageView(context);
        iv_share.setPadding(0, 0, DensityUtil.dip2px(context, 10), 0);
        iv_share.setImageResource(R.drawable.svg_more);
        cn_videodetail.setRightLayoutView(iv_share);
        cn_videodetail.showRightLayout();
        iv_share.setEnabled(false);
        rl_deleted.setOnClickListener(this);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cn_videodetail.setOnLeftLayoutClickListener(new CommonNavigation.OnLeftLayoutClick() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).hiddenKeyboard();
                kv_videodetail.hidenSoft4RotateScreen();
                context.finish();
            }
        });

        headerView = (ViewGroup) View.inflate(context, R.layout.item_videodetail_header, null);
        ll_videoandimg_praise = (LinearLayout) headerView.findViewById(R.id.ll_videoandimg_praise);
        tv_videoandimg_time = (TextView) headerView.findViewById(R.id.tv_videoandimg_time);
        iv_videodetail_headervip = (ImageView) headerView.findViewById(R.id.iv_videodetail_headervip);
        mcv_videodetail_header = (MyCircleView) headerView.findViewById(R.id.mcv_videodetail_header);
        tv_videodetail_name = (TextView) headerView.findViewById(R.id.tv_videodetail_name);
        tv_videodetail_date = (TextView) headerView.findViewById(R.id.tv_videodetail_date);
        tv_videodetail_playcount = (TextView) headerView.findViewById(R.id.tv_videodetail_playcount);
        vvw_videodetail = (VideoViewWrap) headerView.findViewById(R.id.vvw_videodetail);
        tv_videodetail_praise = (TextView) headerView.findViewById(R.id.tv_videodetail_praise);
        tv_videodetail_totalcomment = (TextView) headerView.findViewById(R.id.tv_videodetail_totalcomment);
        tv_videodetail_attention = (TextView) headerView.findViewById(R.id.tv_videodetail_attention);
        progress_bar = headerView.findViewById(R.id.progress_bar);
        tv_videodetail_describe = (TextView) headerView.findViewById(R.id.tv_videodetail_describe);
        ll_videodetail_praise = (LinearLayout) headerView.findViewById(R.id.ll_videodetail_praise);
        tv_videodetaildescription = (TextView) headerView.findViewById(R.id.tv_videodetaildescription);
        iv_imgdetail = (PraiseImageView) headerView.findViewById(R.id.iv_imgdetail);
        tv_videoandimgpraise_count = (TextView) headerView.findViewById(R.id.tv_videoandimgpraise_count);
        ll_praiselist = (LinearLayout) headerView.findViewById(R.id.ll_praiselist);
//        rl_videodetail_totalcommentparent = (RelativeLayout) headerView.findViewById(R.id.rl_videodetail_totalcommentparent);
        tv_title = cn_videodetail.getBar_title();
        tv_videodetail_attention.setOnClickListener(this);
        mcv_videodetail_header.setOnClickListener(this);
        lv.setOnItemClickListener(this);
        tv_title.setOnClickListener(this);
        tv_title.setEnabled(false);
        videoView = (VideoView) vvw_videodetail.getVideoView();
        videoView.setPlayBtnSize(60, 60);
        mediaPlayer = new MyMediaPlayer();
        //设置视频播放控件为正方形
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vvw_videodetail.getLayoutParams();
        lp.height = ScreenUtils.getScreenSize(context).x;
        vvw_videodetail.setLayoutParams(lp);
//        ll_bigvideoview.setLayoutParams(lp);
        //设置图片显示空间为方形
        LinearLayout.LayoutParams imgLp = (LinearLayout.LayoutParams) iv_imgdetail.getLayoutParams();
        imgLp.height = ScreenUtils.getScreenSize(context).x;
        iv_imgdetail.setLayoutParams(imgLp);
        lv.addHeaderView(headerView);
    }

    /**
     * 设置视频相关
     */
    private void setVideo(final VideoDetailBean videoDetailBean) {
        ImageLoader.getInstance().displayImage(videoDetailBean.getCover_uri(), videoView.getPreviewImageView(), DisplayOptionsUtils.getDefaultMaxRectImageOptions());
        VideoExtraInfo videoExtraInfo = new VideoExtraInfo();
        videoExtraInfo.videoId = videoDetailBean.getVideo_id();
        videoExtraInfo.playNum = Long.valueOf(videoDetailBean.getPlay_total());
        videoExtraInfo.videoDuration = Long.valueOf(videoDetailBean.getDuration_second());
        videoExtraInfo.setVideoType(VideoExtraInfo.VideoType.VIDEO_TYPE_COMMON);
        videoExtraInfo.subjectName = videoDetailBean.getActivity_name();
        videoExtraInfo.subjectUri = videoDetailBean.getActivity_uri();
        videoView.setVideoExtraInfo(videoExtraInfo);
        videoView.setVideoControlListener(this);
    }

    /**
     * 启动播放器
     */
    private void startPlay(VideoDetailBean videoDetailBean) {
        if (isPlay) {
            mediaPlayer.start();
            isPlay = true;
        } else if (null != videoDetailBean) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideo_state(videoDetailBean.getVideo_state());
            videoInfo.setHls_uri(videoDetailBean.getHls_uri());
            videoInfo.setVideo_uri(videoDetailBean.getVideo_uri());
            videoInfo.setVideo_id(video_id);
            videoInfo.setHls_uri_state(videoDetailBean.getHls_state());
            videoView.startPlay(mediaPlayer, videoInfo);
            isPlay = true;
        }
    }

    /**
     * 获取视频详情
     */
    private void getVideoDetail(final String video_id, final boolean isClickShare) {
        if (isDelete) {
            rl_deleted.setVisibility(View.VISIBLE);
            return;
        }
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("video_id", video_id);
        requestParams.addParams("kind_tag", "0");

        MHHttpClient.getInstance().post(VideoDetailResponse.class, context, ConstantsValue.Url.GETVIDEOINFO, requestParams, new MHHttpHandler<VideoDetailResponse>() {
            @Override
            public void onSuccess(VideoDetailResponse response) {
                VideoDetailBean videoDetailBean = response.getData();
                mVideoDetail = videoDetailBean;
                setVideoDetail(videoDetailBean, isClickShare);//设置视频详情
            }

            @Override
            public void onFailure(String content) {
                ptrl_videodetail.setVisibility(View.INVISIBLE);
                ll_outside.setVisibility(View.INVISIBLE);
                ptrl_videodetail.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                if (shareDialog != null) shareDialog.dismiss();
                try {
//                    if(isNeedBack)getActivity().finish();
                    setVideoAndImgDelete();
                } catch (Exception e) {
                    MHLogUtil.e(TAG, e);
                }
                super.onStatusIsError(message);
            }
        });
    }

    /**
     * 获取图片详情
     *
     * @param photo_id
     */
    private void getImgDetail(String photo_id, final boolean isClickShare) {
        if (isDelete) {
            rl_deleted.setVisibility(View.VISIBLE);
            return;
        }
        MHRequestParams params = new MHRequestParams();
        params.addParams("photo_id", photo_id);
        MHHttpClient.getInstance().post(VideoAndImgResponse.class, context, ConstantsValue.Url.GETPHOTOINFO, params, new MHHttpHandler<VideoAndImgResponse>() {
            @Override
            public void onSuccess(VideoAndImgResponse response) {
                ImgDetail imgDetail = response.getData();
                mImgDetail = imgDetail;
                setImgDetail(imgDetail, isClickShare);
            }

            @Override
            public void onFailure(String content) {
                if (shareDialog != null) shareDialog.dismiss();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                try {
//                    if(isNeedBack)getActivity().finish();
                    setVideoAndImgDelete();
                } catch (Exception e) {
                    MHLogUtil.e(TAG, e);
                }
            }
        });
    }

    /**
     * 获取评论
     */
    private void getComment(String video_id) {
        final MHRequestParams requestParams = new MHRequestParams();
        String rootType = null;
        requestParams.addParams("root_id", type == VIDEO ? video_id : photo_id);
        requestParams.addParams("root_type", type == VIDEO ? "2" : "4");
        requestParams.addParams("page_size", "10");
        requestParams.addParams("page_index", pageIndex + "");

        MHHttpClient.getInstance().post(VideoDetailUserCommentResponse.class, ConstantsValue.Url.GETUSERCOMMENTINFO, requestParams, new MHHttpHandler<VideoDetailUserCommentResponse>() {
            @Override
            public void onSuccess(VideoDetailUserCommentResponse response) {
                if (isEnterFromNotify) {//通知进入弹出输入框
                    commentType = 1;
                    curCommentByReply = new VideoDetailUserCommentBean();
                    curCommentByReply.setComment_id(notify_comment_id);
                    curCommentByReply.setComment_user_id(notify_comment_user_id);
                    curCommentByReply.setComment_user_name(notify_comment_user_name);
                    et_videodetail_input.setHint("回复@" + curCommentByReply.getComment_user_name());
                    ptrl_videodetail.resetFooterLayout();
                    et_videodetail_input.requestFocus();
                    openKeyBord(et_videodetail_input, context);
                }
                data = response.getData();
                setComment(data);
            }

            @Override
            public void onFailure(String content) {
                ptrl_videodetail.onLoadComplete(true);
            }
        });
    }

    /**
     * 设置评论数据
     */
    public void setComment(VideoDetailUserCommentResult data) {
        List<VideoDetailUserCommentBean> videoDetailUserCommentBeans = data.getPage_result();
        //设置评论数
        totalCount = data.getComments_count();
        tv_videodetail_totalcomment.setText("评论 (" + MHStringUtils.countFormat(totalCount) + ")");
        commentParent.addAll(videoDetailUserCommentBeans);
        MHContentSyncUtil.setContentSync(type == VIDEO ? this.video_id : this.photo_id, commentParent);
        isFirstRequest = true;
        if (pageIndex == 0 && videoDetailUserCommentBeans != null && videoDetailUserCommentBeans.isEmpty()) {
            tv_videodetail_totalcomment.setBackgroundResource(R.color.common_bg);
        } else {
            tv_videodetail_totalcomment.setBackgroundResource(R.color.white);
        }
        if (pageIndex == 0 && commentParent.size() == 0) {
            ptrl_videodetail.setPullLoadEnabled(false);
            setNoCommentVisible(View.VISIBLE);
        } else {
            ptrl_videodetail.setPullLoadEnabled(true);
        }
        if (videoDetailUserCommentBeans.size() == 0) {
            ptrl_videodetail.onLoadComplete(false);
        } else {
            ptrl_videodetail.onLoadComplete(true);
        }

        pageIndex++;
        commentAdapter.notifyDataSetChanged();
        if (isFromCommentList
                && videoDetailUserCommentBeans != null) {
            positionFromCommentList();
        }
    }

    /**
     * 切换视频关注状态
     *
     * @param mVideoDetailBean 视频数据
     */
    private void switchVideoAttentionState(final VideoDetailBean mVideoDetailBean) {
        TCAgent.onEvent(context, "视频详情关注点击" + ConstantsValue.android);
        if (!isLogin(false)) return;
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("action_mark", !attentionState + "");
        requestParams.addParams("user_id", mVideoDetailBean.getUpload_user_id());
        tv_videodetail_attention.setEnabled(false);
        MHHttpClient.getInstance().post(VideoDetailAttentionResponse.class, context, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<VideoDetailAttentionResponse>() {
            @Override
            public void onSuccess(VideoDetailAttentionResponse response) {
                attentionState = !attentionState;
                mVideoDetailBean.setAttention_state(attentionState);
                cpiv_videoandimg.setAttention(attentionState);
                MHStateSyncUtil.pushSyncEvent(context, mVideoDetailBean.getUpload_user_id(), mVideoDetailBean.isAttention_state());
                tv_videodetail_attention.setEnabled(true);
            }

            @Override
            public void onFailure(String content) {
                tv_videodetail_attention.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                tv_videodetail_attention.setEnabled(true);
            }
        });
    }

    /**
     * 切换图片关注状态
     *
     * @param mImgDetail 图片数据
     */
    private void switchImgAttentionState(final ImgDetail mImgDetail) {
        TCAgent.onEvent(context, "图片详情关注点击" + ConstantsValue.android);
        if (!isLogin(false)) return;
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("action_mark", !mImgDetail.isAttention_state() + "");
        requestParams.addParams("user_id", mImgDetail.getUpload_user_id());
        tv_videodetail_attention.setEnabled(false);
        MHHttpClient.getInstance().post(VideoDetailAttentionResponse.class, context, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<VideoDetailAttentionResponse>() {
            @Override
            public void onSuccess(VideoDetailAttentionResponse response) {
                mImgDetail.setAttention_state(!mImgDetail.isAttention_state());
                cpiv_videoandimg.setAttention(mImgDetail.isAttention_state());
                MHStateSyncUtil.pushSyncEvent(context, mImgDetail.getUpload_user_id(), mImgDetail.isAttention_state());
                tv_videodetail_attention.setEnabled(true);
            }

            @Override
            public void onFailure(String content) {
                tv_videodetail_attention.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                tv_videodetail_attention.setEnabled(true);
            }
        });
    }

    /**
     * 切换视频点赞
     *
     * @param videoDetailBean
     * @param isDoubleTap
     */
    private void switchVideoPraise(final VideoDetailBean videoDetailBean, boolean isDoubleTap) {
        if (!isLogin(false)) {
            return;
        }
        MHRequestParams requestParams = new MHRequestParams();
        String url = null;
        ll_videodetail_praise.setEnabled(false);
        requestParams.addParams("action_mark", !praiseState + "");
        requestParams.addParams("video_id", videoDetailBean.getVideo_id());
        url = ConstantsValue.Url.PRAISEVIDEODO;
        MHHttpClient.getInstance().post(VideoDetailPraiseResponse.class, context, url, requestParams, new MHHttpHandler<VideoDetailPraiseResponse>() {
            @Override
            public void onSuccess(VideoDetailPraiseResponse response) {
                NoDoubleClickUtils.setEnable(ll_videodetail_praise);
                if (videoView_praise != null) {
                    NoDoubleClickUtils.setEnable(videoView_praise);
                }
                praiseState = !praiseState;
                setPraiseState(praiseState);

                videoDetailBean.setPraise_state(!videoDetailBean.isPraise_state());
                videoDetailBean.setPraise_count(videoDetailBean.isPraise_state() ? videoDetailBean.getSrcPraise_count() + 1 : videoDetailBean.getSrcPraise_count() - 1);
//                    tv_videodetail_praise.setText(videoDetailBean.getPraise_count()+"");
                tv_videoandimgpraise_count.setText(videoDetailBean.getPraise_count());
                //处理点赞后添加头像
                if (videoDetailBean.isPraise_state()) {
                    //已经点赞了 添加头像并加入
                    PraiseBean praiseBean = new PraiseBean();
                    praiseBean.setPortrait_url(UserInfoUtil.getUserHeader(context));
                    praiseBean.setUser_id(UserInfoUtil.getUserId(context));
//                        videoDetailBean.getPraise_user_list().add(praiseBean);
                    List<PraiseBean> praiseBeanList = new ArrayList<>();
                    praiseBeanList.add(praiseBean);
                    praiseBeanList.addAll(videoDetailBean.getPraise_user_list());
                    videoDetailBean.setPraise_user_list(praiseBeanList);
                } else {
                    //未点赞
                    int index = -1;
                    for (int i = 0; i < videoDetailBean.getPraise_user_list().size(); i++) {
                        PraiseBean praiseBean = videoDetailBean.getPraise_user_list().get(i);
                        if (UserInfoUtil.getUserId(context) == null || praiseBean.getUser_id() == null)
                            return;
                        if (TextUtils.equals(UserInfoUtil.getUserId(context), praiseBean.getUser_id())) {
                            index = i;
                        }
                    }
                    if (index != -1) {
                        videoDetailBean.getPraise_user_list().remove(index);
                    }
                }
                addCircusee(tv_videoandimgpraise_count, ll_praiselist, videoDetailBean.getPraise_user_list());
                MHStateSyncUtil.pushSyncEvent(context, videoDetailBean.getVideo_id(), videoDetailBean.isPraise_state());

                videoView.setPraiseState(videoDetailBean.isPraise_state());
            }

            @Override
            public void onFailure(String content) {
                ll_videodetail_praise.setEnabled(true);
                videoView_praise.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                ll_videodetail_praise.setEnabled(true);
                if (videoView_praise != null) {
                    videoView_praise.setEnabled(true);
                }
            }
        });
    }

    /**
     * 切换图片点赞
     *
     * @param imgDetail
     * @param isDoubleTap
     */
    private void switchImgPraise(final ImgDetail imgDetail, final boolean isDoubleTap) {
        if (!isLogin(false)) {
            return;
        }
        MHRequestParams requestParams = new MHRequestParams();
        String url = null;
        ll_videodetail_praise.setEnabled(false);
        requestParams.addParams("action_mark", !imgDetail.isPraise_state() + "");
        requestParams.addParams("photo_id", imgDetail.getPhoto_id());
        url = ConstantsValue.Url.PRAISEPHOTODO;
        MHHttpClient.getInstance().post(VideoDetailPraiseResponse.class, context, url, requestParams, new MHHttpHandler<VideoDetailPraiseResponse>() {
            @Override
            public void onSuccess(VideoDetailPraiseResponse response) {
                NoDoubleClickUtils.setEnable(ll_videodetail_praise);
                if (videoView_praise != null) {
                    NoDoubleClickUtils.setEnable(videoView_praise);
                }
                imgDetail.setPraise_state(!imgDetail.isPraise_state());
                setPraiseState(imgDetail.isPraise_state());

                imgDetail.setPraise_count(imgDetail.isPraise_state() ? imgDetail.getSrcPraise_count() + 1 : imgDetail.getSrcPraise_count() - 1);
                tv_videoandimgpraise_count.setText(imgDetail.getPraise_count());
                if (imgDetail.isPraise_state()) {
                    //已经点赞了 添加头像并加入
                    PraiseBean praiseBean = new PraiseBean();
                    praiseBean.setPortrait_url(UserInfoUtil.getUserHeader(context));
                    praiseBean.setUser_id(UserInfoUtil.getUserId(context));
//                        imgDetail.getPraise_user_list().add(praiseBean);
                    List<PraiseBean> praiseBeanList = new ArrayList<>();
                    praiseBeanList.add(praiseBean);
                    praiseBeanList.addAll(imgDetail.getPraise_user_list());
                    imgDetail.setPraise_user_list(praiseBeanList);
                } else {
                    //未点赞
                    int index = -1;
                    for (int i = 0; i < imgDetail.getPraise_user_list().size(); i++) {
                        PraiseBean praiseBean = imgDetail.getPraise_user_list().get(i);
                        if (UserInfoUtil.getUserId(context) == null || praiseBean.getUser_id() == null)
                            return;
                        if (TextUtils.equals(UserInfoUtil.getUserId(context), praiseBean.getUser_id())) {
                            index = i;
                        }
                    }
                    if (index != -1) {
                        imgDetail.getPraise_user_list().remove(index);
                    }
                }
                addCircusee(tv_videoandimgpraise_count, ll_praiselist, imgDetail.getPraise_user_list());
                MHStateSyncUtil.pushSyncEvent(context, imgDetail.getPhoto_id(), imgDetail.isPraise_state());
                if (imgDetail.isPraise_state() && !isDoubleTap) {
                    TCAgent.onEvent(context, "视频双击点赞" + ConstantsValue.android);
                    PraisedUtil.showPop(iv_videoandimgpraise, context, true, DensityUtil.dip2px(context, 22) * 2);
                }
            }

            @Override
            public void onFailure(String content) {
                ll_videodetail_praise.setEnabled(true);
                videoView_praise.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                ll_videodetail_praise.setEnabled(true);
                if (videoView_praise != null) {
                    videoView_praise.setEnabled(true);
                }
            }
        });
    }

    /**
     * 发送评论
     */
    private void sendComment() {
        if ((type == VIDEO && mVideoDetail == null)
                || (type == IMG && mImgDetail == null)) {
            return;
        }
        kv_videodetail.setKeyboardEnable(false);
        MHRequestParams requestParams = new MHRequestParams();
        if (commentType == 0) {//评论视频
            requestParams.addParams("commented_id", type == VIDEO ? mVideoDetail.getVideo_id() : photo_id);//视频或者图片
            requestParams.addParams("commented_type", type == VIDEO ? "2" : "4");
            requestParams.addParams("commented_user", type == VIDEO ? mVideoDetail.getUpload_user_id() : mImgDetail.getUpload_user_id());//视频或者图片
        } else if (commentType == 1) {//回复评论
            requestParams.addParams("commented_id", curCommentByReply.getComment_id());
            requestParams.addParams("commented_type", "1");
            requestParams.addParams("commented_user", curCommentByReply.getComment_user_id());
        }
        requestParams.addParams("root_id", type == VIDEO ? mVideoDetail.getVideo_id() : photo_id);//视频或者图片
        requestParams.addParams("root_type", type == VIDEO ? "2" : "4");//视频或者图片
        requestParams.addParams("root_user", type == VIDEO ? mVideoDetail.getUpload_user_id() : mImgDetail.getUpload_user_id());//视频或者图片
        requestParams.addParams("root_uri", type == VIDEO ? mVideoDetail.getVideo_uri() : mImgDetail.getPhoto_uri());//视频或者图片
        if (type == VIDEO) {
            requestParams.addParams("root_cover_uri", mVideoDetail.getCover_uri());
        }
        if (notify_user_results != null) {
            requestParams.addParams("comment_text", Base64Util.getBase64Str(getCommentText()));
            //拼装@的好友id
            JSONArray array = new JSONArray();
            for (int i = 0; i < notify_user_results.size(); i++) {
                Notify_user_result notify_user_result = notify_user_results.get(i);
                if (notify_user_result != null)
                    array.put(notify_user_results.get(i).getNotify_user_id());
            }
            requestParams.addParams("notify_user", array.toString());
        } else {
            requestParams.addParams("comment_text", Base64Util.getBase64Str(et_videodetail_input.getText().toString().replaceAll("\n", " ")));
        }

        MHHttpClient.getInstance().post(SendCommentResponse.class, context, ConstantsValue.Url.DOCOMMENT, requestParams, new MHHttpHandler<SendCommentResponse>() {
            @Override
            public void onSuccess(SendCommentResponse response) {
                EventBus.getDefault().post(new CloseEmojiKeyboard());
                kv_videodetail.reset();
                VideoDetailUserCommentBean videoDetailUserCommentBean = response.getData();
                videoDetailUserCommentBean.setComment_text(getCommentText());
                videoDetailUserCommentBean.setComment_time_text("刚刚");
                List<Notify_user_result> notify_user_resultsTemp = new ArrayList<>();
                if (notify_user_results != null) {
                    for (int i = 0; i < notify_user_results.size(); i++) {
                        Notify_user_result notifyInfo = new Notify_user_result();
                        notifyInfo.setNotify_user_name(notify_user_results.get(i).getNotify_user_name());
                        notifyInfo.setNotify_user_id(notify_user_results.get(i).getNotify_user_id());
                        notify_user_resultsTemp.add(notifyInfo);
                    }
                }
                videoDetailUserCommentBean.setNotify_user_result(notify_user_resultsTemp);
                //重置文本框
                if (notify_user_results != null) notify_user_results.clear();
                if (namesByat != null) namesByat.clear();
                if (userIdByat != null) userIdByat.clear();
                commentTotal.add(0, videoDetailUserCommentBean);
                //回复
                if (commentType == 1) {
                    videoDetailUserCommentBean.setRoot_id(videoDetailUserCommentBean.getComment_id());
                    videoDetailUserCommentBean.setCommented_user_name(curCommentByReply.getComment_user_name());
                }
                if (commentParent.isEmpty()) setNoCommentVisible(View.GONE);
                commentParent.add(0, videoDetailUserCommentBean);
                MHContentSyncUtil.setContentSync(type == VIDEO ? video_id : photo_id, commentParent);
                commentAdapter.notifyDataSetChanged();
                //评论类型改为评论视频
                commentType = 0;
                et_videodetail_input.setHint(" 发射评论...");
                kv_videodetail.setKeyboardEnable(true);
                //设置评论数
                if (MHStringUtils.isEmpty(totalCount)) {
                    totalCount = "0";
                } else if (MHStringUtils.isEmpty(vipCount)) {
                    vipCount = "0";
                }
                totalCount = Integer.parseInt(totalCount) + 1 + "";
                tv_videodetail_totalcomment.setText("共" + MHStringUtils.countFormat(totalCount) + "条评论");
                EventBus.getDefault().post(new RefreshCommentCountEvent(Integer.parseInt(totalCount), video_id));
                Intent intent = new Intent(ConstantsValue.IntentFilterAction.VIDEO_COMMIT_COUNT_ACTION);
                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_COMMIT_COUNT_KEY, Long.parseLong(totalCount));
                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_ID_KEY, video_id);
                context.sendBroadcast(intent);
                tv_videodetail_totalcomment.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        positionFromCommentList();
                    }
                }, 200);
                tv_videodetail_totalcomment.setBackgroundResource(R.color.white);
                et_videodetail_input.setText("");
                kv_videodetail.setKeyboardEnable(true);
            }

            @Override
            public void onFailure(String content) {
                kv_videodetail.setKeyboardEnable(true);

            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                kv_videodetail.setKeyboardEnable(true);
            }
        });

    }

    /**
     * 删除评论
     */
    private void deleteComment(final int position) {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("comment_id", commentParent.get(position).getComment_id());
        MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.DELETECOMMENT, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                commentParent.remove(position);
                MHContentSyncUtil.setContentSync(type == VIDEO ? video_id : photo_id, commentParent);
                commentAdapter.notifyDataSetChanged();
                if (commentParent.isEmpty()) setNoCommentVisible(View.VISIBLE);
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    /**
     * 设置图片详情
     *
     * @param imgDetail
     */
    private void setImgDetail(final ImgDetail imgDetail, boolean isClickShare) {
        if (isDelete) {
            rl_deleted.setVisibility(View.VISIBLE);
            return;
        }
        if (imgDetail.getPhoto_state() < 10 || videoAndImg.getPhoto_id() == null) {
//            if(!isDelete)((BaseActivity) getActivity()).showToastAtBottom("该图片不存在");
            setVideoAndImgDelete();
//            context.finish();
            return;
        }

        if (isClickShare) {
            shareImg(imgDetail);
            return;
        }
        ImageLoader.getInstance().displayImage(imgDetail.getPhoto_uri(), iv_imgdetail.getImageView(), DisplayOptionsUtils.getDefaultMaxRectImageOptions());
        //是否显示关注按钮
        if (UserInfoUtil.isMyself(context, imgDetail.getUpload_user_id())) {
            //是自己
            cpiv_videoandimg.isShowAttentionBtn(false);
        } else {
            //不是自己
            cpiv_videoandimg.isShowAttentionBtn(true);
        }
        cpiv_videoandimg.setOnAttentionListener(new CommonPersonalInfoView.OnAttentionListener() {
            @Override
            public void onAttention() {
                switchImgAttentionState(imgDetail);
            }
        });
        tv_videoandimgpraise_count.setText(imgDetail.getPraise_count());
        ll_praiselist.setVisibility(View.VISIBLE);
        addCircusee(tv_videoandimgpraise_count, ll_praiselist, imgDetail.getPraise_user_list());
        LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        double rat = 0;
        if (imgDetail.getPhoto_width() == 0
                || imgDetail.getPhoto_height() == 0) {
            imgLp.width = ScreenUtils.getScreenWidth(context);
            imgLp.height = ScreenUtils.getScreenWidth(context);
        } else if (imgDetail.getPhoto_width() >= imgDetail.getPhoto_height()) {
            rat = imgDetail.getPhoto_height() / imgDetail.getPhoto_width();
            imgLp.width = ScreenUtils.getScreenWidth(context);
            imgLp.height = (int) (ScreenUtils.getScreenWidth(context) * rat);
        } else if (imgDetail.getPhoto_width() < imgDetail.getPhoto_height()) {
            //高大于宽
            rat = imgDetail.getPhoto_width() / imgDetail.getPhoto_height();
            imgLp.width = ScreenUtils.getScreenWidth(context);
            imgLp.height = (int) (ScreenUtils.getScreenWidth(context) / rat);
        }
        iv_imgdetail.setLayoutParams(imgLp);
//        shared_content_sina = videoDetailBean.getShared_tag_text();
        attentionState = imgDetail.isAttention_state();
        praiseState = imgDetail.isPraise_state();
        tv_videoandimg_time.setText(imgDetail.getUpload_time_text());
        //设置点赞状态
        setPraiseState(praiseState);
        //设置头部导航栏
        CommonPersonInfo info = new CommonPersonInfo();
        info.setDescribe(imgDetail.getVip_note());
        info.setName(imgDetail.getUpload_user_name());
        info.setHeadUri(imgDetail.getUpload_user_portrait_uri());
        info.setUserId(imgDetail.getUpload_user_id());
        info.setUserType(imgDetail.getUpload_user_type());
        //显示是否有应答权限
        if (imgDetail.getAnswer_auth() == 1) {
            //有应答权限
            info.setShownQA(true);
            if (!UserInfoUtil.isMyself(context, imgDetail.getUpload_user_id()))
                iv_videoandimgask.setVisibility(View.VISIBLE);
        } else {
            //无映答权限
            info.setShownQA(false);
            iv_videoandimgask.setVisibility(View.GONE);
        }
        //显示性别
        info.setShownGender(true);
        info.setGender(imgDetail.getUpload_user_gender());
        cpiv_videoandimg.setUserInfo(info);

        if (!MHStringUtils.isEmpty(imgDetail.getUpload_user_id())
                && !TextUtils.equals(imgDetail.getUpload_user_id(), UserInfoUtil.getUserId(context))) {
            cpiv_videoandimg.isShowAttentionBtn(true);//显示关注按钮
            cpiv_videoandimg.setAttention(imgDetail.isAttention_state());//设置关注按钮状态
        }
        iv_imgdetail.setSubject(imgDetail.getActivity_name(), imgDetail.getActivity_uri());

        if (!TextUtils.isEmpty(imgDetail.getPhoto_note())) {
            //字数控制最多100字
            String imgNote = null;
            imgNote = imgDetail.getPhoto_note();
            VideoItemPageResult pageResult = new VideoItemPageResult();
            tv_videodetail_describe.setMovementMethod(LinkMovementMethod.getInstance());
            tv_videodetail_describe.setText(TextUtil.getInstance().handlerString(imgNote, imgDetail.getNotify_user_result(), new AbstractTextUtil() {
                @Override
                public void onClickSpan(TextInfo textInfo) {
                    if (!isLogin(false)) {

                    } else {
                        if (type == VIDEO)
                            TCAgent.onEvent(context, "视频详情头像昵称" + ConstantsValue.android);
                        else
                            TCAgent.onEvent(context, "图片详情头像昵称" + ConstantsValue.android);
                        startActivity(new Intent(context, PersonalHomeActivity.class)
                                .putExtra("userId", textInfo.getTarget()));
                    }
                }
            }));
        }
        //图片点击
        iv_imgdetail.setOnPraiseImageViewClick(new PraiseImageView.PraiseImageViewClickListener() {
            @Override
            public void onSingleTap(View view) {
//                fl_videoandimg.setVisibility(View.VISIBLE);
//                ObjectAnimator.ofFloat(fl_videoandimg, "alpha", 0, 1f)
//                        .setDuration(400)
//                        .start();
//                ImageLoader.getInstance().displayImage(imgDetail.getPhoto_uri(), pv_videoandimg, new AnimateFirstDisplayListener(pv_videoandimg));
                ((VideoAndImgActivity) getActivity()).onShowPicture(imgDetail.getPhoto_uri());
            }

            @Override
            public void onDoubleTap(View view) {
                TCAgent.onEvent(context, "图片双击点赞" + ConstantsValue.android);
                if (!imgDetail.isPraise_state()) {
                    PraisedUtil.showPop(iv_videoandimgpraise, context, true, DensityUtil.dip2px(context, 22) * 2);
                }
                if (!imgDetail.isPraise_state()) {
                    switchImgPraise(mImgDetail, true);
                }
            }
        });

    }

    /**
     * 设置视频详情
     *
     * @param videoDetailBean 视频详情数据
     */
    public void setVideoDetail(final VideoDetailBean videoDetailBean, boolean isClickShare) {
        if (isDelete) {
            rl_deleted.setVisibility(View.VISIBLE);
            return;
        }
        if (videoDetailBean.getVideo_state() < 10 || video_id == null) {
//            ((BaseActivity) getActivity()).showToastAtBottom("该视频不存在");
            setVideoAndImgDelete();
            return;
        }
        //分享前需判断当前视频是否已经被删除
        if (isClickShare) {
            shareVideo(videoDetailBean);
            return;
        }

        //是否显示关注按钮
        if (UserInfoUtil.isMyself(context, videoDetailBean.getUpload_user_id())) {
            //是自己
            cpiv_videoandimg.isShowAttentionBtn(false);
        } else {
            //不是自己
            cpiv_videoandimg.isShowAttentionBtn(true);
            cpiv_videoandimg.setAttention(videoDetailBean.isAttention_state());
        }
        cpiv_videoandimg.setOnAttentionListener(new CommonPersonalInfoView.OnAttentionListener() {
            @Override
            public void onAttention() {
                switchVideoAttentionState(videoDetailBean);
            }
        });
        tv_videoandimgpraise_count.setText(videoDetailBean.getPraise_count());
        ll_praiselist.setVisibility(View.VISIBLE);
        addCircusee(tv_videoandimgpraise_count, ll_praiselist, videoDetailBean.getPraise_user_list());
        ViewGroup.LayoutParams videoLp = vvw_videodetail.getLayoutParams();
        double rat = 0;
        if (videoDetailBean.getVideo_width() == 0
                || videoDetailBean.getVideo_height() == 0) {
            rat = 1;
            videoLp.width = ScreenUtils.getScreenWidth(context);
            videoLp.height = ScreenUtils.getScreenWidth(context);
        } else if (videoDetailBean.getVideo_width() >= videoDetailBean.getVideo_height()) {
            rat = videoDetailBean.getVideo_height() / videoDetailBean.getVideo_width();
            videoLp.width = ScreenUtils.getScreenWidth(context);
            videoLp.height = (int) (ScreenUtils.getScreenWidth(context) * rat);
        } else if (videoDetailBean.getVideo_width() < videoDetailBean.getVideo_height()) {
            //高大于宽
            if (videoDetailBean.getVideo_height() * (3d / 4d) >= videoDetailBean.getVideo_width()) {
                rat = 3d / 4d;
            } else {
                rat = videoDetailBean.getVideo_width() / videoDetailBean.getVideo_height();
            }
            videoLp.width = ScreenUtils.getScreenWidth(context);
            videoLp.height = (int) (ScreenUtils.getScreenWidth(context) / rat);
        }
        vvw_videodetail.setLayoutParams(videoLp);

        //同步关注
        attentionState = videoDetailBean.isAttention_state();
        praiseState = videoDetailBean.isPraise_state();
        ImageLoader.getInstance().displayImage(videoDetailBean.getUpload_user_portrait_uri(), mcv_videodetail_header, DisplayOptionsUtils.getHeaderDefaultImageOptions());
        tv_videodetail_name.setText(videoDetailBean.getUpload_user_name());
        tv_videodetail_date.setText(videoDetailBean.getUpload_time_text());
        tv_videodetail_playcount.setText(MHStringUtils.countFormat(videoDetailBean.getPlay_total()) + " 播放");
        tv_videoandimg_time.setText(videoDetailBean.getUpload_time_text());
        //设置上传视频的用户信息
        CommonPersonInfo info = new CommonPersonInfo();
        info.setUserType(Integer.parseInt(videoDetailBean.getUpload_user_type()));
        info.setName(videoDetailBean.getUpload_user_name());
        info.setName_nodescribe(videoDetailBean.getUpload_user_name());
        info.setDescribe(videoDetailBean.getVip_note());
        info.setHeadUri(videoDetailBean.getUpload_user_portrait_uri());
        info.setUserId(videoDetailBean.getUpload_user_id());
        //显示是否有应答权限
        if (videoDetailBean.getAnswer_auth() == 1) {
            //有应答权限
            info.setShownQA(true);
            if (!UserInfoUtil.isMyself(context, videoDetailBean.getUpload_user_id()))
                iv_videoandimgask.setVisibility(View.VISIBLE);
        } else {
            //无映答权限
            info.setShownQA(false);
            iv_videoandimgask.setVisibility(View.GONE);
        }
        //显示性别
        info.setShownGender(true);
        info.setGender(videoDetailBean.getUpload_user_gender());
        cpiv_videoandimg.setUserInfo(info);
        if (!MHStringUtils.isEmpty(videoDetailBean.getUpload_user_id())
                && !TextUtils.equals(videoDetailBean.getUpload_user_id(), UserInfoUtil.getUserId(context))) {
            cpiv_videoandimg.isShowAttentionBtn(true);//显示关注按钮
            cpiv_videoandimg.setAttention(videoDetailBean.isAttention_state());//设置关注按钮状态
        }
        if (!TextUtils.isEmpty(videoDetailBean.getVideo_note())) {
            //字数控制最多100字
            String videoNote = null;
            videoNote = videoDetailBean.getVideo_note();
            VideoItemPageResult pageResult = new VideoItemPageResult();
            pageResult.setVideo_note(videoNote);
            pageResult.setNotify_user_result(videoDetailBean.getNotify_user_result());
            tv_videodetail_describe.setMovementMethod(LinkMovementMethod.getInstance());
            tv_videodetail_describe.setText(TextUtil.getInstance().handleVideoDescribe(pageResult, new AbstractTextUtil() {
                @Override
                public void onClickSpan(TextInfo textInfo) {
                    if (!isLogin(false)) {
                    } else {
                        skipPersonalCenter(context, textInfo.getTarget());
                    }
                }
            }));
        }

        //设置点赞数
        tv_videodetail_praise.setText("赞" + videoDetailBean.getPraise_count());

        //是否是大咖
        if (Integer.parseInt(videoDetailBean.getUpload_user_type()) > 10) {
            iv_videodetail_headervip.setVisibility(View.VISIBLE);
            if (!MHStringUtils.isEmpty(videoDetailBean.getVip_note())) {
                tv_videodetaildescription.setVisibility(View.VISIBLE);
                tv_videodetaildescription.setText(videoDetailBean.getVip_note());
            } else {
                tv_videodetaildescription.setVisibility(View.GONE);
            }
        }
        //设置点赞状态
        setPraiseState(praiseState);
        ptrl_videodetail.setVisibility(View.VISIBLE);
        ll_outside.setVisibility(View.VISIBLE);
        ((BaseActivity) getActivity()).hideErrorView();
        //设置视频数据
        setVideo(videoDetailBean);
    }

    /**
     * 跳转到个人中心
     *
     * @param context
     * @param userId
     */
    private void skipPersonalCenter(Context context, String userId) {
        if (type == VIDEO) {
            TCAgent.onEvent(context, "视频详情头像昵称" + ConstantsValue.android);
        } else {
            TCAgent.onEvent(context, "图片详情头像昵称" + ConstantsValue.android);
        }
        Intent intent = new Intent(context, PersonalHomeActivity.class);
        intent.putExtra("userId", userId);
        if (TextUtils.equals(UserInfoUtil.getUserId(context), userId)) {
            intent.putExtra("isSelf", true);
        } else {
            intent.putExtra("isSelf", false);
        }
        intent.putExtra("activityType", 0);
        context.startActivity(intent);
    }

    /**
     * 设置点赞状态
     *
     * @param praiseState 当前点赞状态
     */
    private void setPraiseState(boolean praiseState) {
        if (praiseState) {
            iv_videoandimgpraise.setImageResource(R.drawable.praiseselected);
        } else {
            iv_videoandimgpraise.setImageResource(R.drawable.videoandimgpraise);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        if (shareDialog != null && shareDialog.isShowing())
            shareDialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((BaseActivity) getActivity()).hiddenLoadingView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shareDialog != null)
            shareDialog.dismiss();
        if (PraisedUtil.popupWindow != null) {
            PraisedUtil.popupWindow.dismiss();
            PraisedUtil.popupWindow = null;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_videodetail_praise://点赞&取消点赞
                if (isLogin(false)) {
                    PraisedUtil.showPop(iv_videoandimgpraise, context, true, DensityUtil.dip2px(context, 22) * 2);
                    if (mVideoDetail != null) {
                        if (type == VIDEO && null != mVideoDetail) {
                            switchVideoPraise(mVideoDetail, false);
                        } else if (null != mImgDetail) {
                            switchImgPraise(mImgDetail, false);
                        }
                    }
                }
                break;

            case R.id.mcv_videodetail_header:
                if (isLogin(false)) {
                    if (mVideoDetail != null) {
                        if (type == VIDEO)
                            TCAgent.onEvent(context, "视频详情头像昵称" + ConstantsValue.android);
                        else
                            TCAgent.onEvent(context, "图片详情头像昵称" + ConstantsValue.android);
                        Intent intent = new Intent(context, PersonalHomeActivity.class);
                        intent.putExtra("userId", mVideoDetail.getUpload_user_id());
                        if (TextUtils.equals(UserInfoUtil.getUserId(context), mVideoDetail.getUpload_user_id())) {
                            intent.putExtra("isSelf", true);
                        } else {
                            intent.putExtra("isSelf", false);
                        }
                        intent.putExtra("activityType", 0);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.ll_outside:
                kv_videodetail.reset();
                break;
            case R.id.bar_title:
                if (null == mVideoDetail) {
                    break;
                }
                tv_title.setText("视频详情");
                tv_title.setCompoundDrawables(null, null, null, null);
                tv_title.setEnabled(false);
                if (!isPlay)
                    caculatePlayCount(mVideoDetail);
                playBig2Small();
                startPlay(mVideoDetail);
                break;
            case R.id.iv_videoandimgback://返回
                context.finish();
                break;
            case R.id.iv_videoandimgcomment://评论
                if (isDelete) {
                    context.showToastAtCenter("该" + (type == VIDEO ? "视频" : "图片") + "已经被删除");
                    return;
                }
                if (type == VIDEO)
                    TCAgent.onEvent(context, "视频详情评论" + ConstantsValue.android);
                else
                    TCAgent.onEvent(context, "图片详情评论" + ConstantsValue.android);
                handleCommentText();
                if (textInfos != null) textInfos.clear();
                if (namesByat != null) namesByat.clear();
                if (userIdByat != null) userIdByat.clear();
                ptrl_videodetail.resetFooterLayout();
                et_videodetail_input.requestFocus();
                openKeyBord(et_videodetail_input, context);
                break;
            case R.id.iv_videoandimgpraise://点赞
                if (type == VIDEO) {
                    TCAgent.onEvent(context, "视频详情点赞" + ConstantsValue.android);
                } else {
                    TCAgent.onEvent(context, "图片详情点赞" + ConstantsValue.android);
                }
                if (isDelete) {
                    context.showToastAtCenter("该" + (type == VIDEO ? "视频" : "图片") + "已经被删除");
                    return;
                }
                if (isLogin(false)) {
                    if (type == VIDEO) {
                        if (!mVideoDetail.isPraise_state()) {
                            PraisedUtil.showPop(iv_videoandimgpraise, context, true, DensityUtil.dip2px(context, 22) * 2);
                        }
                        if (null == mVideoDetail) {
                            break;
                        }
                        switchVideoPraise(mVideoDetail, false);
                    } else {
                        if (!mImgDetail.isPraise_state()) {
                            PraisedUtil.showPop(iv_videoandimgpraise, context, true, DensityUtil.dip2px(context, 22) * 2);
                        }
                        if (null == mImgDetail) {
                            break;
                        }
                        switchImgPraise(mImgDetail, false);
                    }
                }
                break;
            case R.id.iv_videoandimgshare://分享
                if (isDelete) {
                    context.showToastAtCenter("该" + (type == VIDEO ? "视频" : "图片") + "已经被删除");
                    return;
                }
                //如果isDelete = false  需通过网络判断该视频或者图片是否被删除掉
                if (type == VIDEO) getVideoDetail(video_id, true);
                else if (type == IMG) getImgDetail(photo_id, true);
                break;
            case R.id.iv_videoandimgask://提问
                if (isDelete) {
                    context.showToastAtCenter("该" + (type == VIDEO ? "视频" : "图片") + "已经被删除");
                    return;
                }
                if (type == VIDEO) {
                    TCAgent.onEvent(context, "视频详情映答" + ConstantsValue.android);
                } else {
                    TCAgent.onEvent(context, "图片详情映答" + ConstantsValue.android);
                }
                String userId = null;
                String uploadUserName = null;
                if (type == VIDEO) {
                    if (null == mVideoDetail) {
                        break;
                    }
                    if (mVideoDetail != null) {
                        userId = mVideoDetail.getUpload_user_id();
                        uploadUserName = mVideoDetail.getUpload_user_name();
                    }
                } else if (type == IMG) {
                    if (null == mImgDetail) {
                        break;
                    }
                    if (mImgDetail != null) {
                        userId = mImgDetail.getUpload_user_id();
                        uploadUserName = mImgDetail.getUpload_user_name();
                    }
                }
                if (!MHStringUtils.isEmpty(userId)) {
                    startActivity(new Intent(context, QAHomeActivity.class)
                            .putExtra("userId", userId)
                            .putExtra("userName", uploadUserName));
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG, e);
        }
        if (data != null) {
            if (resultCode == 102) {

                if (null != kv_videodetail && null != kv_videodetail.getEt()) {
                    kv_videodetail.getEt().requestFocus();
                    openKeyBord(kv_videodetail.getEt(), context);
                }

                namesByat = data.getStringArrayListExtra("nameList");
                userIdByat = data.getStringArrayListExtra("userIds");
                if (null == namesByat)
                    namesByat = new ArrayList<>();
                if (null == userIdByat)
                    userIdByat = new ArrayList<>();

                if (namesByat == null || userIdByat == null) return;
                if (notify_user_results == null)
                    notify_user_results = new ArrayList<Notify_user_result>();
//                SpannableStringBuilder oldSpan = new SpannableStringBuilder(et_videodetail_input.getText().toString());
                for (int i = 0; i < namesByat.size(); i++) {
                    Notify_user_result notify_user_result = new Notify_user_result();
                    notify_user_result.setNotify_user_id(userIdByat.get(i));
                    notify_user_result.setNotify_user_name(namesByat.get(i).trim().replace("@", ""));
                    notify_user_results.add(notify_user_result);
                }
                String oldStr = et_videodetail_input.getText().toString();
                for (int i = 0; i < namesByat.size(); i++) {
                    if (i == namesByat.size() - 1) {
                        oldStr += " " + namesByat.get(i).trim();
                    } else {
                        oldStr += " " + namesByat.get(i).trim() + " ";
                    }
                }
                et_videodetail_input.setText(edit_textUtil.handlerStringBlack(oldStr, notify_user_results, new AbstractTextUtil() {
                    @Override
                    public void onClickSpan(final TextInfo textInfo) {
//                        et_videodetail_input.setSelection(textInfo.getEnd());
                    }

                    @Override
                    public void getTextInfos(List<TextInfo> textInfos) {
                        VideoAndImgDetailFragment.this.textInfos = textInfos;
                    }
                }));
                et_videodetail_input.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        et_videodetail_input.requestFocus();
                        context.showKeyboard(et_videodetail_input);
                    }
                }, 200);
            }
        }
    }

    /**
     * 下拉刷新
     *
     * @param refreshView 刷新的View
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        getComment(video_id);
    }

    /**
     * 点击评论回调
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //指定评论类型
        if (position == 0)
            return;
        if (commentParent.isEmpty()) return;
        commentType = 1;
        curCommentByReply = commentParent.get(position - 1);
        et_videodetail_input.setHint("回复@" + curCommentByReply.getComment_user_name());
        ptrl_videodetail.resetFooterLayout();
        et_videodetail_input.requestFocus();
        openKeyBord(et_videodetail_input, context);
    }

    //打开软键盘
    public static void openKeyBord(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 成功删除视频
     */
    @Override
    public void deleteVideoSuccess() {
        context.finish();
    }

    /**
     * 删除视频失败
     */
    @Override
    public void deleteVideoFail() {

    }

    //@好友
    @Override
    public void onAtFriend() {
        if (!isLogin(false)) {
        } else {
            ((BaseActivity) context).hiddenKeyboard();
            Intent intent = new Intent(context, UserListActivity.class);                    //@好友界面
            intent.putExtra("type", UserListActivity.USER_LIST_TYPE_AT_FRIENDS);
            startActivityForResult(intent, UserListActivity.AT_FRIENDS_REQUEST_CODE);
        }
        TCAgent.onEvent(context, "视频详情-@" + ConstantsValue.android);
    }

    //发送评论
    @Override
    public void onSendComment() {
        if (isLogin(false)) {
            TCAgent.onEvent(context, "视频详情-发送" + ConstantsValue.android);
            if (et_videodetail_input.getText().toString().trim().length() == 0) {
                context.showToastAtCenter("评论内容不能为空!");
            } else {
                sendComment();
            }
            ptrl_videodetail.resetFooterLayout();
        }
    }

    @Override
    public void onTextChangeAfter(Editable s) {
        edit_textUtil.handlerString(s.toString(), notify_user_results, new AbstractTextUtil() {
            @Override
            public void onClickSpan(final TextInfo textInfo) {
//                et_videodetail_input.setSelection(textInfo.getEnd());
            }

            @Override
            public void getTextInfos(List<TextInfo> textInfos) {
                super.getTextInfos(textInfos);
                VideoAndImgDetailFragment.this.textInfos = textInfos;
            }
        });
    }

    @Override
    public void onTextChangeBefore(CharSequence s, int start, int count, int after) {
    }

    /**
     * 光标位置监听
     *
     * @param selStart
     * @param selEnd
     */
    @Override
    public void change(int selStart, int selEnd) {
        if (textInfos != null) {
            for (int i = 0; i < textInfos.size(); i++) {
                TextInfo textInfo = textInfos.get(i);
                if (textInfo != null) {
                    if (selStart > textInfo.getStart() && selStart <= textInfo.getEnd()) {
                        et_videodetail_input.setSelection(textInfo.getEnd());
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        doSyscInfo();
    }

    @Override
    protected void onMyResume() {
        super.onMyResume();
        doSyscInfo();
    }

    /**
     * 同步信息
     */
    private void doSyscInfo() {
        if (type == VIDEO && null != mVideoDetail) {
            doSyscVideoInfo(mVideoDetail);
        } else if (null != mImgDetail) {
            doSyscImgInfo(mImgDetail);
        }

        int lastSize = commentParent.size();
        if (isFirstRequest) {
            commentParent.clear();
            commentParent.addAll(MHContentSyncUtil.getContentSync(type == VIDEO ? video_id : photo_id));
            int size = commentParent.size();
            if (lastSize != size) {
                tv_videodetail_totalcomment.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        positionFromCommentList();
                    }
                }, 200);
            }
        }
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 同步视频相关信息
     *
     * @param videoDetailBean
     */
    private void doSyscVideoInfo(VideoDetailBean videoDetailBean) {
        if (null == videoDetailBean) return;
        MHStateSyncUtil.State state = MHStateSyncUtil.getSyncState(video_id);
        //处理点赞
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != state) {
            videoDetailBean.setPraise_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == state);
        }
        setPraiseState(videoDetailBean.isPraise_state());
        long praise_count = videoDetailBean.getSrcPraise_count();
        if (videoDetailBean.isPraise_state()) {
            praise_count++;
        } else {
            praise_count--;
        }
        if (praise_count < 0) praise_count = 0;
        videoDetailBean.setPraise_count(praise_count);
        tv_videodetail_praise.setText("赞" + videoDetailBean.getPraise_count());

        //处理关注
        state = MHStateSyncUtil.getSyncState(videoDetailBean.getUser_id());
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != state) {
            videoDetailBean.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == state);
        }
        this.attentionState = videoDetailBean.isAttention_state();
        if (null != cpiv_videoandimg) {
            cpiv_videoandimg.setAttention(attentionState);
        }
        if (null != videoDetail_dialogGiftBeans && videoDetail_dialogGiftBeans.size() > 0) {
            for (VideoDetail_dialogGiftBean gift : videoDetail_dialogGiftBeans) {
                gift.setSender_attention_state(this.attentionState);
            }
        }
    }

    /**
     * 同步图片相关信息
     *
     * @param imgDetail
     */
    private void doSyscImgInfo(ImgDetail imgDetail) {
        if (null == imgDetail) return;
        MHStateSyncUtil.State state = MHStateSyncUtil.getSyncState(imgDetail.getPhoto_id());
        //处理点赞
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != state) {
            imgDetail.setPraise_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == state);
        }
        setPraiseState(imgDetail.isPraise_state());

        //处理关注
        state = MHStateSyncUtil.getSyncState(imgDetail.getUpload_user_id());
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != state) {
            imgDetail.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == state);
        }
        this.attentionState = imgDetail.isAttention_state();
        if (null != cpiv_videoandimg) {
            cpiv_videoandimg.setAttention(imgDetail.isAttention_state());
        }
        if (null != videoDetail_dialogGiftBeans && videoDetail_dialogGiftBeans.size() > 0) {
            for (VideoDetail_dialogGiftBean gift : videoDetail_dialogGiftBeans) {
                gift.setSender_attention_state(this.attentionState);
            }
        }
    }

    /**
     * 长按删除
     */
    @Override
    public void onLongClick(final int position) {
        if (data == null) return;
        dialog = new DeleteCommentDialog(context);
        dialog.setOnDeleteCommentListener(new DeleteCommentDialog.IDeleteComment() {
            @Override
            public void onDeleteComment() {
                context.showLoading("正在删除...");
                dialog.dismiss();
                deleteComment(position);
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(int position) {
        //指定评论类型
        if (commentParent.isEmpty()) return;
        commentType = 1;
        curCommentByReply = commentParent.get(position);
        et_videodetail_input.setHint("回复@" + curCommentByReply.getComment_user_name());
        ptrl_videodetail.resetFooterLayout();
        et_videodetail_input.requestFocus();
        openKeyBord(et_videodetail_input, context);
    }

    /**
     * 计算点击次数
     */
    private void caculatePlayCount(VideoDetailBean videoDetailBean) {
        long count = 0;
        if (MHStringUtils.isEmpty(videoDetailBean.getPlay_total())) {
            count = 1;
        } else {
            count = Long.valueOf(videoDetailBean.getPlay_total()) + 1;
        }
        tv_videodetail_playcount.setText(MHStringUtils.countFormat(count + "") + " 播放");
    }

    /**
     * 监听软键盘删除@好友,并删除
     */
    public void checkDeleteAtFriend() {
        et_videodetail_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    String inputStr = et_videodetail_input.getText().toString();
                    int selectorIndex = et_videodetail_input.getSelectionStart();
                    if (textInfos != null)
                        for (int i = 0; i < textInfos.size(); i++) {
                            TextInfo textInfo = textInfos.get(i);
                            if (selectorIndex > textInfo.getStart() && selectorIndex <= textInfo.getEnd()) {
                                textInfos.remove(i);
                                notify_user_results.remove(i);
                                Editable editable = et_videodetail_input.getText();
                                editable.delete(textInfo.getStart(), textInfo.getEnd());
                            }
                        }
                }
                return false;
            }
        });
    }

    /**
     * 获取评论内容
     *
     * @return
     */
    private String getCommentText() {
        String commentText = et_videodetail_input.getText()
                .toString()
                .trim()
                .replaceAll("\\s+", " ")
                .replaceAll("\\\n", " ");
        StringBuilder commentTextBuilder = new StringBuilder(commentText);
        Map<String, Integer> stageMap = new HashMap<>();
        int index = 0;
        if (notify_user_results == null) return commentText;
        for (int i = 0; i < notify_user_results.size(); i++) {
            Notify_user_result notify_user_result = notify_user_results.get(i);
            if (notify_user_result == null
                    || notify_user_result.getNotify_user_id() == null
                    || notify_user_result.getNotify_user_name() == null) continue;
            //map中是否存在重名
            if (stageMap.containsKey(notify_user_result.getNotify_user_name())) {//存在
                index = commentTextBuilder.indexOf("@" + notify_user_result.getNotify_user_name(), stageMap.get(notify_user_result.getNotify_user_name()));
            } else {
                index = commentTextBuilder.indexOf("@" + notify_user_result.getNotify_user_name());
            }
            stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length());
            int lastIndex = index + notify_user_result.getNotify_user_name().length();
            //向@好友加空格
            if (commentTextBuilder.indexOf(" ", lastIndex) == lastIndex + 1) {
                commentTextBuilder.insert(lastIndex + 1, " ");
            } else {
                commentTextBuilder.insert(lastIndex + 1, "  ");
            }
            if (index == 0) {
                commentTextBuilder.insert(0, "  ");
                stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 2);
            } else if (index == 1) {
                if (commentTextBuilder.indexOf(" ") == 0) {
                    commentTextBuilder.insert(0, " ");
                    stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 1);
                } else {
                    commentTextBuilder.insert(1, "  ");
                    stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 2);
                }
            } else {
                if (commentTextBuilder.indexOf(" ", index - 1) == index - 1) {
                    commentTextBuilder.insert(index - 1, " ");
                    stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 1);
                } else if (commentTextBuilder.indexOf(" ", index - 1) == -1) {
                    commentTextBuilder.insert(index - 1, "  ");
                    stageMap.put(notify_user_result.getNotify_user_name(), index + notify_user_result.getNotify_user_name().length() + 2);
                }
            }
        }
        return commentTextBuilder.toString();
    }

    /**
     * 设置是否显示无评论提示
     *
     * @param visible
     */
    private void setNoCommentVisible(int visible) {
        if (noComment == null)
            noComment = View.inflate(context, R.layout.layout_videodetail_nocomment, null);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height = DensityUtil.dip2px(context, 120);
        noComment.setLayoutParams(lp);
        if (visible == View.VISIBLE) {
            lv.addFooterView(noComment);
        } else if (visible == View.GONE) {
            lv.removeFooterView(noComment);
        }
    }

    /**
     * 从关注也或者个人中心点击评论进入视频详情或者发送评论后评论数量条目
     * 需定位到导航栏下方如果评论条目不足则显示到底部
     */
    public void positionFromCommentList() {
        int[] cn_location = new int[2];
        cpiv_videoandimg.getLocationOnScreen(cn_location);
        int[] commentCount = new int[2];
        tv_videodetail_totalcomment.getLocationOnScreen(commentCount);
        int offset = 0;
        if (commentCount[1] > 0) {
            offset = commentCount[1] - cn_location[1] - cpiv_videoandimg.getHeight();
        } else {
            lv.setSelection(1);
            offset = -tv_videodetail_totalcomment.getHeight();
        }
        lv.smoothScrollBy(offset, 1000);
    }

    /**
     * 添加围观人头像
     *
     * @param tv             围观数
     * @param ll             围观头像容器
     * @param praiseBeanList 数据源
     */
    public void addCircusee(TextView tv, LinearLayout ll, final List<PraiseBean> praiseBeanList) {
        if (praiseBeanList.isEmpty()) {
            ll_videoandimg_praise.setVisibility(View.GONE);
        } else {
            ll_videoandimg_praise.setVisibility(View.VISIBLE);
        }
        ll.removeAllViews();
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv.measure(w, h);
        int width = tv.getMeasuredWidth();
        int circuseeWidth = ScreenUtils.getScreenWidth(context) - width - DensityUtil.dip2px(context, 20);
        int circuseeCount = circuseeWidth / (DensityUtil.dip2px(context, 30));
        int count;
        if (praiseBeanList.size() == circuseeCount) count = circuseeCount;
        else {
            count = Math.min(circuseeCount, praiseBeanList.size());
        }
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.width = DensityUtil.dip2px(context, 25);
            lp.height = DensityUtil.dip2px(context, 25);
            lp.setMargins(0, 0, DensityUtil.dip2px(context, 5), 0);
            MyCircleView circleView = new MyCircleView(context);
            circleView.setLayoutParams(lp);
            String headImgUrl = praiseBeanList.get(i).getPortrait_url();
            if (headImgUrl.contains("?")) {
                headImgUrl = headImgUrl + "&" + ConstantsValue.Other.HEADER_PARAM;
            } else {
                headImgUrl = headImgUrl + "?" + ConstantsValue.Other.HEADER_PARAM;
            }
            ImageLoader.getInstance().displayImage(headImgUrl, circleView, DisplayOptionsUtils.getHeaderDefaultImageOptions());
            ll.addView(circleView);
            final int position = i;
            circleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = praiseBeanList.get(position).getUser_id();
                    if (isLogin(false))
                        context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                .putExtra("userId", userId));
                }
            });
        }
        ll_videoandimg_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OnLookersListAvtivity.class)
                        .putExtra("type", VideoAndImgDetailFragment.this.type + "")
                        .putExtra("video_id", VideoAndImgDetailFragment.this.video_id)
                        .putExtra("photo_id", VideoAndImgDetailFragment.this.photo_id));
            }
        });

    }

    /**
     * 分享视频
     *
     * @param videoDetailBean 视频所需数据
     */
    public void shareVideo(final VideoDetailBean videoDetailBean) {
        final ShareDialog shareDialog = new ShareDialog((BaseActivity) getActivity());
        shareDialog.setData();
        shareDialog.setShareLable(ShareDialog.IMG);
        ShareImg shareImg = null;
        if (videoDetailBean == null) return;
        shareDialog.setRootType(1);
        shareDialog.setShareLink(videoDetailBean.getShare_link_address());
        shareImg = new ShareImg(((BaseActivity) context), videoDetailBean.getVideo_id(), "", "", "", "");
        if (UserInfoUtil.isMyself(context, videoDetailBean.getUpload_user_id())) {
            shareImg.setShowDelete(true);
            shareImg.setDeleteBtnType(ShareLayout.VIDEO_DELETE);
        } else {
            shareImg.setShowDelete(true);
            shareImg.setDeleteBtnType(ShareLayout.VIDEO_REPORT);
        }
        shareDialog.setShareInfo(shareImg);
        shareDialog.setOnShareImgPath(new ShareLayout.OnShareImgPath() {
            @Override
            public void getimgPath(final SHARE_MEDIA platform) {
                shareDialog.dismiss();
                ((BaseActivity) context).showLoading();
                ShareVideoAndImgInfo videoAndImgInfo = new ShareVideoAndImgInfo();
                shareDialog.setShareLink(videoDetailBean.getShare_link_address());
                videoAndImgInfo.setImgUrl(videoDetailBean.getCover_uri());
                videoAndImgInfo.setNote(videoDetailBean.getVideo_note());
                videoAndImgInfo.setHeight(videoDetailBean.getVideo_height());
                videoAndImgInfo.setWidth(videoDetailBean.getVideo_width());
                videoAndImgInfo.setJoinTime(TimeFormatUtils.formatYMD(videoDetailBean.getUpload_time()));
                videoAndImgInfo.setName(videoDetailBean.getUpload_user_name());
                videoAndImgInfo.setType(type);
                videoAndImgInfo.setHeaderUrl(videoDetailBean.getUpload_user_portrait_uri());
                if (MHStringUtils.isEmpty(videoDetailBean.getShare_link_address()))
                    videoAndImgInfo.setQaCode_str(ConstantsValue.Shared.APPDOWNLOAD);
                else videoAndImgInfo.setQaCode_str(videoDetailBean.getShare_link_address());
                sviv_videoandimg.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                    @Override
                    public void onFinish(Object path) {
                        UmengShare.sharedIMG(getActivity(), platform, path, videoDetailBean.getShare_link_address(), videoDetailBean.getVideo_note(), new IUMShareResultListener((BaseActivity) context));
                    }
                });
                sviv_videoandimg.genderImage(videoAndImgInfo, platform);
            }
        });
        //删除自己的作品或者举报别人的作品
        shareDialog.setDeleteOrReportListener(new ShareLayout.OnDeleteOrReportListener() {
            @Override
            public void onDeleteOrReport(int type) {
                if (type == ShareLayout.IMG_DELETE
                        || type == ShareLayout.VIDEO_DELETE) {
                    shareDialog.dismiss();
                    deleteVideoOrImg(videoDetailBean);
                }
            }
        });

    }

    /**
     * 分享图片
     *
     * @param imgDetail 图片所需数据
     */
    public void shareImg(final ImgDetail imgDetail) {
        final ShareDialog shareDialog = new ShareDialog((BaseActivity) getActivity());
        shareDialog.setData();
        shareDialog.setShareLable(ShareDialog.IMG);
        ShareImg shareImg = null;
        if (imgDetail == null) return;
        shareDialog.setRootType(2);
        shareDialog.setShareLink(imgDetail.getShare_link_address());
        shareImg = new ShareImg(((BaseActivity) context), imgDetail.getPhoto_id(), "", "", "", "");
        if (UserInfoUtil.isMyself(context, imgDetail.getUpload_user_id())) {
            shareImg.setShowDelete(true);
            shareImg.setDeleteBtnType(ShareLayout.IMG_DELETE);
        } else {
            shareImg.setShowDelete(true);
            shareImg.setDeleteBtnType(ShareLayout.IMG_REPORT);
        }
        shareDialog.setShareInfo(shareImg);
        shareDialog.setOnShareImgPath(new ShareLayout.OnShareImgPath() {
            @Override
            public void getimgPath(final SHARE_MEDIA platform) {
                shareDialog.dismiss();
                ((BaseActivity) context).showLoading();
                ShareVideoAndImgInfo videoAndImgInfo = new ShareVideoAndImgInfo();
                shareDialog.setShareLink(imgDetail.getShare_link_address());
                videoAndImgInfo.setImgUrl(imgDetail.getPhoto_uri());
                videoAndImgInfo.setNote(imgDetail.getPhoto_note());
                videoAndImgInfo.setHeight(imgDetail.getPhoto_height());
                videoAndImgInfo.setWidth(imgDetail.getPhoto_width());
                videoAndImgInfo.setJoinTime(TimeFormatUtils.formatYMD(imgDetail.getUpload_time()));
                videoAndImgInfo.setName(imgDetail.getUpload_user_name());
                videoAndImgInfo.setType(type);
                videoAndImgInfo.setHeaderUrl(imgDetail.getUpload_user_portrait_uri());
                if (MHStringUtils.isEmpty(imgDetail.getShare_link_address())) {
                    videoAndImgInfo.setQaCode_str(ConstantsValue.Shared.APPDOWNLOAD);
                } else {
                    videoAndImgInfo.setQaCode_str(imgDetail.getShare_link_address());
                }
                sviv_videoandimg.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                    @Override
                    public void onFinish(Object path) {
                        UmengShare.sharedIMG(getActivity(), platform, path, imgDetail.getShare_link_address(), imgDetail.getPhoto_note(), new IUMShareResultListener((BaseActivity) context));
                    }
                });
                sviv_videoandimg.genderImage(videoAndImgInfo, platform);
            }
        });
        //删除自己的作品或者举报别人的作品
        shareDialog.setDeleteOrReportListener(new ShareLayout.OnDeleteOrReportListener() {
            @Override
            public void onDeleteOrReport(int type) {
                if (type == ShareLayout.IMG_DELETE
                        || type == ShareLayout.VIDEO_DELETE) {
                    shareDialog.dismiss();
                    deleteVideoOrImg(imgDetail);
                }
            }
        });
    }

    /**
     * 删除视频或者图片
     *
     * @param obj 视频或者图片bean
     */
    public void deleteVideoOrImg(final Object obj) {
        MHRequestParams requestParams = new MHRequestParams();
        String url = null;
        if (null == obj) return;
        if (type == VIDEO) {
            //视频
            url = ConstantsValue.Url.DELETEVIDEO;
            requestParams.addParams("video_id", ((VideoDetailBean) obj).getVideo_id());
        } else if (videoAndImg.getContent_type() == IMG) {
            //图片
            url = ConstantsValue.Url.DELETEPHOTO;
            requestParams.addParams("photo_id", ((ImgDetail) obj).getPhoto_id());
        }
        MHHttpClient.getInstance().post(BaseResponse.class, context, url, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                DeleteVideoAndImgAsyncEvent event = new DeleteVideoAndImgAsyncEvent();
                if (type == VIDEO) {
                    event.setTargetId(((VideoDetailBean) obj).getVideo_id());
                } else {
                    event.setTargetId(((ImgDetail) obj).getPhoto_id());
                }
                event.setFromType(ConstantsValue.Other.DELETEVIDEOANDIMG_FROMVIDEOANDIMGDETAIL);
                event.setContentType(type);
                //与关注页和我的页面同步删除该视频/图片
                EventBus.getDefault().post(event);
                setVideoAndImgDelete();
            }

            @Override
            public void onFailure(String content) {
                if (videoAndImg.getContent_type() == IMG) {
                    ToastUtils.showToastAtCenter(context, "删除图片失败");
                } else if (videoAndImg.getContent_type() == VIDEO) {
                    ToastUtils.showToastAtCenter(context, "删除视频失败");
                }
            }
        });
    }

    @Override
    public void videoViewState(BaseVideoView.VideoViewState videoViewState, int extra) {
        switch (videoViewState) {
            case ON_START_PLAY_CLICK:
                if (null != mVideoDetail) {
                    startPlay(mVideoDetail);
                    caculatePlayCount(mVideoDetail);
                }
                break;
            case ON_SINGLE_CLICK:
                if (isBig) return;
                mediaPlayer.pause();
                tv_title.setText("立即播放");
                Drawable leftDrawable = getResources().getDrawable(R.drawable.svg_videodetail_titleicon);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                tv_title.setCompoundDrawables(leftDrawable, null, null, null);
                tv_title.setCompoundDrawablePadding(DensityUtil.dip2px(context, 3));
                tv_title.setEnabled(true);
                tv_title.startAnimation(scaleAnimation);
                playSmall2Big();
                isSmallToBig = true;
                break;

            case ON_DOUBLE_CLICK:
                if (null == mVideoDetail) {
                    break;
                }
                if (null != mVideoDetail && !mVideoDetail.isPraise_state()) {
                    switchVideoPraise(mVideoDetail, true);
                }
                break;
            case ON_VIDEO_COMPLETE:
                if (null == mVideoDetail) {
                    break;
                }
                if (isBig) {
                    isPlay = false;
                } else {
                    videoView.reStart();
                }
                videoView.setPraiseState(mVideoDetail.isPraise_state());
                break;
        }
    }


    public interface DeleteVideoOrImg {
        void onDelete();

        int getPosition();
    }

    public interface OnLastCommentId {
        String getLastCommentId();

        void setLastCommentId(String commentId);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mediaPlayer.stop();
        mediaPlayer.release();
        if (shareDialog != null)
            shareDialog.dismiss();

        SendGiftDialog.selectPos = 0;

        if (PraisedUtil.popupWindow != null) {
            PraisedUtil.popupWindow.dismiss();
            PraisedUtil.popupWindow = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    /**
     * 停止视频
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateEvent(StopVideoEvent event) {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    /**
     * 注册eventbus
     */
    public void registEvenBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 设置视频已经被删除
     */
    public void setVideoAndImgDelete() {
        isDelete = true;
        rl_deleted.setVisibility(View.VISIBLE);
        if (type == VIDEO) tv_deletetext.setText("该视频已经被删除");
        else tv_deletetext.setText("该图片已经被删除");
    }

    /**
     * 处理输入的内容
     */
    private void handleCommentText() {
        String commentId = null;
        if (type == VIDEO) {
            if (null == mVideoDetail) {
                return;
            }
            commentId = video_id;
        } else {
            if (null == mImgDetail) {
                return;
            }
            commentId = photo_id;
        }

        if (!TextUtils.equals(commentId, ((OnLastCommentId) getActivity()).getLastCommentId())) {
            et_videodetail_input.setText("");
            if (null == commentId) {
                commentId = "";
            }
            ((OnLastCommentId) getActivity()).setLastCommentId(commentId);
        }
    }

}

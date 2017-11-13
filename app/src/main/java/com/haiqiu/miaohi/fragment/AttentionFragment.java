package com.haiqiu.miaohi.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.InterlocutionDetailsActivity;
import com.haiqiu.miaohi.activity.MainActivity;
import com.haiqiu.miaohi.activity.MaybeInterestToPeopleActivity;
import com.haiqiu.miaohi.activity.QAHomeActivity;
import com.haiqiu.miaohi.activity.QaSquareActivity;
import com.haiqiu.miaohi.activity.RechargingActivity1;
import com.haiqiu.miaohi.activity.UserListActivity;
import com.haiqiu.miaohi.activity.WebActivity;
import com.haiqiu.miaohi.adapter.AttentionStickyHeaderAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.base.FadeOutFromBottomItem;
import com.haiqiu.miaohi.bean.Attention;
import com.haiqiu.miaohi.bean.Comment;
import com.haiqiu.miaohi.bean.MayBeInterest;
import com.haiqiu.miaohi.bean.Notify_user_result;
import com.haiqiu.miaohi.bean.SendCommentResponse;
import com.haiqiu.miaohi.bean.TextInfo;
import com.haiqiu.miaohi.bean.UserInfo;
import com.haiqiu.miaohi.bean.VideoDetailUserCommentBean;
import com.haiqiu.miaohi.bean.VideoInfo;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.AttentionNewDataEvent;
import com.haiqiu.miaohi.receiver.DeleteVideoAndImgAsyncEvent;
import com.haiqiu.miaohi.receiver.OnAttentionBubbleState;
import com.haiqiu.miaohi.receiver.RefreshCommentCountEvent;
import com.haiqiu.miaohi.receiver.RefreshUploadEvent;
import com.haiqiu.miaohi.response.AttentionData;
import com.haiqiu.miaohi.response.AttentionResponse;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.ConfirmRequestResponse;
import com.haiqiu.miaohi.response.PayQAResultResponse;
import com.haiqiu.miaohi.utils.AbstractTextUtil;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.KeyboardWatcher;
import com.haiqiu.miaohi.utils.MHContentSyncUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.utils.TextUtil;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.shareImg.ShareQADetailView;
import com.haiqiu.miaohi.utils.shareImg.ShareVideoAndImgView2;
import com.haiqiu.miaohi.view.DialogFadeOutFromBottom;
import com.haiqiu.miaohi.view.NoteEditText;
import com.haiqiu.miaohi.view.picturezoom.OnShowPicture;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.haiqiu.miaohi.widget.KeyBoardView;
import com.haiqiu.miaohi.widget.PriceChangeDialog;
import com.haiqiu.miaohi.widget.mediaplayer.MyMediaPlayer;
import com.haiqiu.miaohi.widget.mediaplayer.VideoView;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshRecyclerView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 关注页面
 * Created by ningl on 16/11/30.
 * 该列表显示自己和别人发布的视频 图片 映答 推荐信息
 * 不会出现当前登陆者发布的应答
 */
public class AttentionFragment extends BaseFragment implements View.OnClickListener
        , KeyBoardView.OnStartCommentListener, KeyBoardView.OnKeboardListener
        , KeyBoardView.OnEmojiDoListener, AttentionStickyHeaderAdapter.OnAttentionDo
        , View.OnTouchListener {

    private final int VIDEO = 1;
    private final int IMG = 2;
    private final int QA = 3;
    private PullToRefreshRecyclerView ptrshl_attention;
    private ImageView iv_attention_mabyinsterest;
    private ImageView iv_attention_qa;
    private TextView tv_attention_refreshtip;
    private RecyclerView recyclerView;
    private AttentionStickyHeaderAdapter adapter;
    private KeyBoardView kv_attention;
    private ArrayList<String> namesByat, userIdByat;
    private List<Notify_user_result> notify_user_results;//@的好友
    private TextUtil edit_textUtil;
    private int currentCommonPosition;//当前需评论的位置
    private NoteEditText et_videodetail_input;
    private List<TextInfo> textInfos;
    private FrameLayout fl_keboardmarsk_attention;
    private View bottomView;
    private int pageIndex;
    private ArrayList<Attention> data;
    private MyMediaPlayer mediaPlayer;
    private boolean isHasShowKeyboard;
    private FrameLayout fl_rootview;
    private int keyHeight;
    private LinkedHashMap<String, Attention> uploadTempMap;
    private LinkedHashMap<String, VideoUploadInfo> uploadInfos;
    private Attention payAttention;
    private ShareVideoAndImgView2 sviv_attention;
    private ShareQADetailView sqdv_attention;
    private AttentionData attentionData;
    private boolean isHidden;
    private VideoView videoView;
    private VideoView lastVideoView;
    private int position;
    private Handler handler;
    private boolean isCountDown = false;
    private BroadcastReceiver broadcastReceiver;
    private RelativeLayout btn;
    private RelativeLayout rl_attentiontitle;
    private GestureDetector gestureDetector;
    private boolean isShowPushTip = false;
    private LinearLayoutManager linearLayoutManager;
    private int visibleItemCount, totalItemCount, pastVisiblesItems, firstVisibleItem;
    private String lastCommentId = "";//上一次评论的视频或者图片id

    public AttentionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null == ptrshl_attention || null == intent) return;
                if (ConstantsValue.IntentFilterAction.LOGIN_SUCCESS_ACTION.equals(intent.getAction())) {
                    recyclerView.smoothScrollToPosition(0);
                    ptrshl_attention.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrshl_attention.doPullRefreshing(true);
                        }
                    }, 200);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsValue.IntentFilterAction.LOGIN_SUCCESS_ACTION);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attention, container, false);
        edit_textUtil = TextUtil.getInstance();
        data = new ArrayList<>();
        mediaPlayer = new MyMediaPlayer();
        uploadTempMap = new LinkedHashMap<>();
        uploadInfos = new LinkedHashMap<>();
        registEvenBus();
        initView(rootView);
        kv_attention.setRootView(fl_keboardmarsk_attention);
//        kv_attention.setMarskView(fl_keboardmarsk_attention);
        KeyboardWatcher keyboardWatcher = new KeyboardWatcher(getActivity());
        fl_keboardmarsk_attention.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                kv_attention.hideKeyboard(context);
                kv_attention.hideLayout();
//                closeMarsk();
                fl_keboardmarsk_attention.setVisibility(View.GONE);
                kv_attention.tv_videodetail_send.setText("发送");
                kv_attention.closeAllBoard();
//                fl_keboardmarsk_attention.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ((MineFragment.OnHiddenTabBar) getActivity()).hiddenTabBar(true);
//                    }
//                }, 200);
                return true;
            }
        });
        ptrshl_attention.setPullLoadEnabled(true);
        adapter = new AttentionStickyHeaderAdapter(context, data, mediaPlayer, this, this);

        adapter.setShareAttentionQA(sqdv_attention);
        recyclerView.setAdapter(adapter);
        adapter.setShareVideoAndImgView(sviv_attention);
        ptrshl_attention.showMHLoading();
        getNetData();
        ptrshl_attention.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (null != mediaPlayer)
                    mediaPlayer.reset();
                pageIndex = 0;
                switchMainTabDot(MainActivity.CLOSEBUBBLE);
                if (tv_attention_refreshtip.getAlpha() != 0) {
                    closeRefreshTip();
                }
                getNetData();
            }

            @Override
            public void onLoadMore() {
                getNetData();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "followingfeed_more");
                    jsonObject.put("description", "关注流加载更多");
                    jsonObject.put("pageIndex", pageIndex + "");
                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                    if (null != mediaPlayer) mediaPlayer.reset();
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                }
            }
        });

        adapter.setOnClickPictureListener(new OnShowPicture() {
            @Override
            public void onShowPicture(String url) {
                ((OnShowPicture) (getActivity())).onShowPicture(url);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                firstVisibleItem = pastVisiblesItems;
                if (null != adapter) {
                    int lastPlayPosition = adapter.getLastPlayPosition();
                    if (lastPlayPosition < firstVisibleItem || lastPlayPosition >= firstVisibleItem + visibleItemCount) {
                        if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        }
                    }
                }
            }
        });

        //双击头部回到顶部
        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
//                recyclerView.scrollToPosition(0);
                recyclerView.smoothScrollToPosition(0);
                return super.onDoubleTap(e);
            }
        });

        tv_attention_refreshtip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPushTip) {
                    isShowPushTip = false;
                    recyclerView.smoothScrollToPosition(0);
                    switchMainTabDot(MainActivity.CLOSEBUBBLE);
                    ptrshl_attention.doPullRefreshing(true);
                }
            }
        });
        return rootView;
    }

    /**
     * 关闭刷新提示view
     */
    public void closeRefreshTip() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(tv_attention_refreshtip, "alpha", 1, 0);
        anim.setDuration(1000);
        anim.start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_attention_refreshtip.setVisibility(View.GONE);
                    }
                }, 200);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initView(View rootView) {
        ptrshl_attention = (PullToRefreshRecyclerView) rootView.findViewById(R.id.ptrshl_attention);
        recyclerView = ptrshl_attention.getRefreshableView();
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
//        LayoutTransition transition = new LayoutTransition();
//        transition.setDuration(300);
//        recyclerView.getWrappedList().setLayoutTransition(transition);
        iv_attention_mabyinsterest = (ImageView) rootView.findViewById(R.id.iv_attention_mabyinsterest);
        kv_attention = (KeyBoardView) rootView.findViewById(R.id.kv_attention);
        tv_attention_refreshtip = (TextView) rootView.findViewById(R.id.tv_attention_refreshtip);
        fl_keboardmarsk_attention = (FrameLayout) rootView.findViewById(R.id.fl_keboardmarsk_attention);
        fl_rootview = (FrameLayout) rootView.findViewById(R.id.fl_rootview);
        sviv_attention = (ShareVideoAndImgView2) rootView.findViewById(R.id.sviv_attention);
        sqdv_attention = (ShareQADetailView) rootView.findViewById(R.id.sqdv_attention);
        rl_attentiontitle = (RelativeLayout) rootView.findViewById(R.id.rl_attentiontitle);
        et_videodetail_input = kv_attention.getEt();
        kv_attention.setOnEmojiDoListener(this);
//        kv_attention.setOnKeyboardListener(this);
        iv_attention_mabyinsterest.setOnClickListener(this);
        iv_attention_qa = (ImageView) rootView.findViewById(R.id.iv_attention_qa);
        iv_attention_qa.setOnClickListener(this);
        fl_rootview = (FrameLayout) rootView.findViewById(R.id.fl_rootview);
        kv_attention.setRootView(fl_rootview);
        rl_attentiontitle.setOnTouchListener(this);
        SetClickStateUtil.getInstance().setStateListener(iv_attention_mabyinsterest);
        SetClickStateUtil.getInstance().setStateListener(iv_attention_qa);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_attention_mabyinsterest://可能感兴趣的人
                startActivity(new Intent(context, MaybeInterestToPeopleActivity.class));
                break;

            case R.id.iv_attention_qa://应答
                startActivity(new Intent(context, QaSquareActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
        context.hiddenLoadingView();
        if (data != null) {
            if (resultCode == UserListActivity.AT_FRIENDS_REQUEST_CODE && requestCode == UserListActivity.AT_FRIENDS_REQUEST_CODE) {
                ptrshl_attention.resetFooterLayout();
                namesByat = data.getStringArrayListExtra("nameList");
                userIdByat = data.getStringArrayListExtra("userIds");
                if (null == namesByat)
                    namesByat = new ArrayList<>();
                if (null == userIdByat)
                    userIdByat = new ArrayList<>();

                if (namesByat == null || userIdByat == null) return;
                if (notify_user_results == null)
                    notify_user_results = new ArrayList<Notify_user_result>();
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
                        AttentionFragment.this.textInfos = textInfos;
                    }
                }));
                et_videodetail_input.setSelection(et_videodetail_input.getText().toString().length());
                et_videodetail_input.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        et_videodetail_input.requestFocus();
                        context.showKeyboard(et_videodetail_input);
                    }
                }, 200);

            } else if (resultCode == RechargingActivity1.PAYRESULT_CODE) {
                //充值成功 二次确认
                confirmPaySuccess(payAttention);
            } else if (resultCode == InterlocutionDetailsActivity.PAYRESULT) {
                //从应答详情返回后 如果已经更改映答状态则刷新列表状态
                boolean isQuestionOwner = data.getBooleanExtra("is_question_owner", false);
                String question_id = data.getStringExtra("question_id");
                if (isQuestionOwner && question_id != null) {
                    for (int i = 0; i < this.data.size(); i++) {
                        if (this.data.get(i).getElement_type() == QA) {
                            if (TextUtils.equals(question_id, this.data.get(i).getQuestion_id())) {
                                this.data.get(i).setIs_question_owner(true);
                            }
                        }
                    }
                }
                if (adapter != null) adapter.notifyDataSetChangedBySelf();
            }
        }
    }

    /**
     * 获取数据
     */
    private void getNetData() {
        if (!isLogin()) {
            ptrshl_attention.showErrorView("请先登录");
            return;
        }
        MHRequestParams params = new MHRequestParams();
        params.addParams("page_index", pageIndex + "");
        params.addParams("page_size", ConstantsValue.Other.PAGESIZE);
        MHHttpClient.getInstance().post(AttentionResponse.class, ConstantsValue.Url.ATTENTION, params, new MHHttpHandler<AttentionResponse>() {
            @Override
            public void onSuccess(AttentionResponse response) {
                context.hiddenLoadingView();
                attentionData = response.getData();
                List<Attention> pageData = attentionData.getPage_result();
                //将可能感兴趣的人加入到关注列表中 判断感兴趣的人集合为空则不显示
                if (pageIndex == 0 && !pageData.isEmpty()) {
                    Attention attention = new Attention();
                    //设置类型为可能感兴趣的人
                    attention.setElement_type(0);
                    List<MayBeInterest> mayBeInterests = attentionData.getAttention_user_list();
                    if (null != mayBeInterests && !mayBeInterests.isEmpty()) {
                        attention.setAttention_user_list(mayBeInterests);
                        adapter.setRefresh(true);
                        if (attentionData.getAttention_user_list_index() > pageData.size()) {
                            pageData.add(attention);
                        } else {
                            pageData.add(attentionData.getAttention_user_list_index(), attention);
                        }
                    }
//                    MayBeInterest mayBeInterest = new MayBeInterest();
//                    mayBeInterest.setFindMore(true);
//                    mayBeInterests.add(mayBeInterest);
                }
                setData(pageData);
                if (!isCountDown) {
                    isCountDown = true;
                    handlerCountDown();
                }
                if (null == pageData || pageData.size() == 0)
                    ptrshl_attention.onLoadComplete(false);
                else {
                    ptrshl_attention.onLoadComplete(true);
                }
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                ptrshl_attention.onLoadComplete(true);
                context.hiddenLoadingView();
                showRefreshTip("", false);
                if (data.isEmpty()) ptrshl_attention.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                ptrshl_attention.onLoadComplete(true);
                context.hiddenLoadingView();
                showRefreshTip("", false);
                if (data.isEmpty()) ptrshl_attention.showErrorView();
            }
        });
    }

    /**
     * 设置数据
     *
     * @param pageData
     */
    private void setData(List<Attention> pageData) {
        if (pageIndex == 0) {
            if (null == pageData || pageData.size() == 0) {
                if (data.isEmpty()) {
                    ptrshl_attention.showBlankView();
                }
                return;
            }
            if (attentionData != null) showRefreshTip(attentionData.getUpdate_content(), false);
            ptrshl_attention.hideAllTipView();
            data.clear();
            if (uploadTempMap != null && !uploadTempMap.isEmpty()) {
                List<Attention> attentions = new ArrayList<>();
                for (Iterator it = uploadTempMap.entrySet().iterator(); it.hasNext(); ) {
                    LinkedHashMap.Entry entry = (LinkedHashMap.Entry) it.next();
                    attentions.add(0, (Attention) entry.getValue());
                }
                pageData.addAll(0, attentions);
            }
            data.addAll(pageData);
            if (null == adapter) {
                adapter = new AttentionStickyHeaderAdapter(context, data, mediaPlayer, this, this);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChangedBySelf();
            }
        } else {
            ptrshl_attention.hideAllTipView();
            data.addAll(pageData);
            adapter.notifyDataSetChangedBySelf();
        }
    }

    /**
     * 发送评论
     */
    private void sendComment(final Attention attention) {
        if (null == attention) return;

        kv_attention.setKeyboardEnable(false);
        MHRequestParams requestParams = new MHRequestParams();
        String commented_id = null;
        String rootType = null;
        if (attention.getElement_type() == VIDEO) {
            commented_id = attention.getVideo_id();
            rootType = "2";
            requestParams.addParams("root_uri", attention.getVideo_url());
            requestParams.addParams("root_cover_uri", attention.getCover_uri());
        } else if (attention.getElement_type() == IMG) {
            commented_id = attention.getPhoto_id();
            rootType = "4";
        }
        requestParams.addParams("commented_id", commented_id);
        requestParams.addParams("commented_type", rootType);
        requestParams.addParams("commented_user", attention.getUser_id());
        requestParams.addParams("root_id", commented_id);
        requestParams.addParams("root_type", rootType);
        requestParams.addParams("root_user", attention.getUser_id());
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
                kv_attention.reset();
                VideoDetailUserCommentBean videoDetailUserCommentBean = response.getData();
                videoDetailUserCommentBean.setComment_text(getCommentText());
                videoDetailUserCommentBean.setComment_time_text("刚刚");
                List<Notify_user_result> notify_user_resultsTemp = new ArrayList<>();

                if (notify_user_results == null)
                    notify_user_results = new ArrayList<Notify_user_result>();
                for (int i = 0; i < notify_user_results.size(); i++) {
                    Notify_user_result notifyInfo = new Notify_user_result();
                    notifyInfo.setNotify_user_name(notify_user_results.get(i).getNotify_user_name());
                    notifyInfo.setNotify_user_id(notify_user_results.get(i).getNotify_user_id());
                    notify_user_resultsTemp.add(notifyInfo);
                }
                videoDetailUserCommentBean.setNotify_user_result(notify_user_resultsTemp);
                Comment comment = new Comment();
                comment.setComment_text(et_videodetail_input.getText().toString());
                comment.setComment_id(videoDetailUserCommentBean.getComment_id());
                comment.setComment_user_id(UserInfoUtil.getUserId(context));
                comment.setComment_user_name(videoDetailUserCommentBean.getComment_user_name());
                if (notify_user_results == null) {
                    comment.setNotify_user_result(new ArrayList<Notify_user_result>());
                } else {
                    comment.setNotify_user_result(notify_user_resultsTemp);
                }
                //添加评论的数据到同步池
                VideoDetailUserCommentBean bean = new VideoDetailUserCommentBean();
                bean.setComment_text(comment.getComment_text());
                bean.setComment_id(comment.getComment_id());
                bean.setComment_user_id(comment.getComment_user_id());
                bean.setComment_user_name(comment.getComment_user_name());
                bean.setNotify_user_result(comment.getNotify_user_result());
                MHContentSyncUtil.addContentSync(attention.getContent_type() == 2 ?
                        attention.getPhoto_id() : attention.getVideo_id(), bean);
                if (attention.getComment_list() == null) {
                    attention.setComment_list(new ArrayList<Comment>());
                }
                attention.getComment_list().add(0, comment);
                attention.setComments_count(attention.getComments_count() + 1);
                //重置文本框
                et_videodetail_input.setText("");
                adapter.notifyDataSetChangedBySelf();
                if (notify_user_results != null) notify_user_results.clear();
                if (namesByat != null) namesByat.clear();
                if (userIdByat != null) userIdByat.clear();
//                tv_videodetail_totalcomment.setText("共" + MHStringUtils.countFormat(userWork.getPraise_count()) + "条评论");
                EventBus.getDefault().post(new RefreshCommentCountEvent(attention.getComments_count(), attention.getVideo_id()));
                Intent intent = new Intent(ConstantsValue.IntentFilterAction.VIDEO_COMMIT_COUNT_ACTION);
                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_COMMIT_COUNT_KEY, attention.getComments_count());
                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_ID_KEY, attention.getUser_id());
                context.sendBroadcast(intent);
                kv_attention.setKeyboardEnable(true);
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        positionFromCommentList();
//                    }
//                }, 200);
            }

            @Override
            public void onFailure(String content) {
                kv_attention.setKeyboardEnable(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                kv_attention.setKeyboardEnable(true);

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
     * @好友
     */
    @Override
    public void onAtFriend() {
        Intent intent = new Intent(context, UserListActivity.class);                    //@好友界面
        intent.putExtra("type", UserListActivity.USER_LIST_TYPE_AT_FRIENDS);
//        kv_attention.hideKeyboard(context);
//        kv_attention.hideLayout();
        context.hiddenKeyboard();
        startActivityForResult(intent, UserListActivity.AT_FRIENDS_REQUEST_CODE);
    }

    /**
     * 发送评论
     */
    @Override
    public void onSendComment() {
        sendComment(data.get(currentCommonPosition));
    }

    /**
     * 软键盘或者emoji键盘开启
     */
    @Override
    public void onOpen() {
        if (!isHasShowKeyboard) return;
        try {
            kv_attention.setVisibility(View.VISIBLE);
//            if(bottomView!=null) bottomView.setVisibility(View.GONE);
            ((MineFragment.OnHiddenTabBar) getActivity()).hiddenTabBar(false);
            fl_keboardmarsk_attention.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(fl_keboardmarsk_attention, "alpha", 0, 0.3f)
                    .setDuration(200)
                    .start();

//            kv_attention.setOnKeyboardListener(this);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    /**
     * 软键盘和emoji键盘都关闭
     */
    @Override
    public void onClose() {
        if (!isHasShowKeyboard) return;
        try {
            kv_attention.setVisibility(View.GONE);
//        if(bottomView!=null) bottomView.setVisibility(View.VISIBLE);
            ((MineFragment.OnHiddenTabBar) getActivity()).hiddenTabBar(true);
//            closeMarsk();
            fl_keboardmarsk_attention.setVisibility(View.GONE);

        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    /**
     * 打开软键盘开始评论
     *
     * @param postion
     */
    @Override
    public void onStartCommont(int postion) {
        isHasShowKeyboard = true;
        kv_attention.setOnKeyboardListener(this);
        Attention attention = data.get(postion);
        String commentId = null;
        if (attention.getElement_type() == VIDEO) {
            commentId = attention.getVideo_id();
        } else {
            commentId = attention.getPhoto_id();
        }
        if (!TextUtils.equals(commentId, lastCommentId)) {
            et_videodetail_input.setText("");
            lastCommentId = commentId;
        }

        currentCommonPosition = postion;
        if (textInfos != null
                || namesByat != null
                || userIdByat != null) {
            textInfos.clear();
            namesByat.clear();
            userIdByat.clear();
        }
        et_videodetail_input.requestFocus();
        et_videodetail_input.setFocusable(true);
        ptrshl_attention.resetFooterLayout();
        context.showKeyboard(kv_attention.getEt());
        onOpen();
    }

    @Override
    public void onTextChangeAfter(Editable s) {

    }

    @Override
    public void onTextChangeBefore(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onDelete(final int position) {
        final DialogFadeOutFromBottom dialog = new DialogFadeOutFromBottom(getActivity());
        List<FadeOutFromBottomItem> items = new ArrayList<>();
        //判断是否是自己 如果是自己并且类型是视频或图片 则显示删除
        //否则显示不感兴趣和举报dialog
        switch (data.get(position).getElement_type()) {
            case VIDEO: //视频
            case IMG: //图片
                if (TextUtils.equals(data.get(position).getUser_id(), UserInfoUtil.getUserId(context))) {
                    //是自己
                    FadeOutFromBottomItem myselfItem = new FadeOutFromBottomItem("#ff4545", "删除");
                    items.add(myselfItem);
                } else {
                    //不是自己
                    FadeOutFromBottomItem videoAndImgItem1 = new FadeOutFromBottomItem("#ff4545", "不感兴趣");
                    FadeOutFromBottomItem videoAndImgItem2 = new FadeOutFromBottomItem("#00a2ff", "举报");
                    items.add(videoAndImgItem1);
                    items.add(videoAndImgItem2);
                }

                break;

            case QA: //映答
                FadeOutFromBottomItem qaItem1 = new FadeOutFromBottomItem("#ff4545", "不感兴趣");
                FadeOutFromBottomItem qaItem2 = null;
                if (data.get(position).getAnswer_user_gender() == 1) {
                    qaItem2 = new FadeOutFromBottomItem("#00a2ff", "向他提问");
                } else {
                    qaItem2 = new FadeOutFromBottomItem("#00a2ff", "向她提问");
                }
                FadeOutFromBottomItem qaItem3 = new FadeOutFromBottomItem("#00a2ff", "什么是映答");
                FadeOutFromBottomItem qaItem4 = new FadeOutFromBottomItem("#00a2ff", "举报");
                items.add(qaItem1);
                items.add(qaItem2);
                items.add(qaItem3);
                items.add(qaItem4);
                break;
        }
        dialog.setItem(items);
        dialog.show();

        dialog.setOnSelectItemListener(new DialogFadeOutFromBottom.OnSelectItem() {
            @Override
            public void select(int index) {
                Attention attention = data.get(position);
                if (index == 0) {
                    //第一个按钮
                    if (TextUtils.equals(attention.getUser_id(), UserInfoUtil.getUserId(context))) {
                        //自己
                        showDeleteVideoAndImg(position, 1);
                    } else {
                        //不是自己
                        unLikePeople(position);
                    }
                } else if (index == 1) {
                    //第二个按钮
                    if (!TextUtils.equals(attention.getUser_id(), UserInfoUtil.getUserId(context))) {
                        //不是自己
                        if (attention.getElement_type() == VIDEO
                                || attention.getElement_type() == IMG) {
                            dialog.dismiss();
                            showDeleteVideoAndImg(position, 2);
                        } else if (attention.getElement_type() == QA) {
                            //向他提问
                            if (null != attention && !MHStringUtils.isEmpty(attention.getAnswer_user_id())) {
                                context.startActivity(new Intent(context, QAHomeActivity.class)
                                        .putExtra("userId", attention.getAnswer_user_id())
                                        .putExtra("userName", attention.getAnswer_user_name()));
                            }
                            TCAgent.onEvent(context, "映答向Ta提问" + ConstantsValue.android);
                        }
                    }
                } else if (index == 2) {
                    //第三个按钮
                    if (attention.getElement_type() == QA) {
                        //什么是应答
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("uri", getResources().getString(R.string.open_qa_url) + "?answer_auth=" + UserInfoUtil.getUserANSER_DROIT(context));
                        intent.putExtra("title", "开通映答");
                        startActivity(intent);
                        TCAgent.onEvent(context, "映答什么是映答" + ConstantsValue.android);
                    }
                } else if (index == 3) {
                    //第四个按钮
                    if (attention.getElement_type() == QA) {
                        //举报
                        dialog.dismiss();
                        showDeleteVideoAndImg(position, 2);
                    }
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onConfirmPay(int position, int lastPlayPosition, VideoView videoView, VideoView lastVideoView, RelativeLayout btn) {
        this.videoView = videoView;
        this.lastVideoView = lastVideoView;
        this.position = position;
        this.btn = btn;
        //点击应答按钮调用二次确认
        confirmPaySuccess(data.get(position));
    }

    /**
     * 弹出是否删除视频的对话框
     *
     * @param position 索引
     * @param type     1:删除 2：举报
     */
    private void showDeleteVideoAndImg(final int position, final int type) {
        final CommonDialog commonDialog = new CommonDialog(context);
        if (type == 1) {
            if (data.get(position).getElement_type() == VIDEO) {
                commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要删除这段视频吗？");
            } else {
                commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要删除这张图片吗？");
            }
        } else if (type == 2) {
            if (data.get(position).getElement_type() == VIDEO) {
                commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要举报这段视频吗？");
                TCAgent.onEvent(context, "视频举报" + ConstantsValue.android);
            } else if (data.get(position).getElement_type() == IMG) {
                commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要举报这张图片吗？");
                TCAgent.onEvent(context, "图片举报" + ConstantsValue.android);
            } else if (data.get(position).getElement_type() == QA) {
                commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要举报这个映答吗？");
                TCAgent.onEvent(context, "映答举报" + ConstantsValue.android);
            }
        }
        commonDialog.setRightButtonMsg("确定");
        commonDialog.setLeftButtonMsg("取消");
        commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
            @Override
            public void onLeftButtonOnClick() {
                commonDialog.dismiss();
            }
        });
        commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
            @Override
            public void onRightButtonOnClick() {
                if (type == 1) {
                    deleteVideoOrImg(position);
                } else if (type == 2) {
                    reportVideoOrPhoto(position);
                }
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    /**
     * 删除视频或者图片
     */
    public void deleteVideoOrImg(final int positon) {
        final Attention attention = data.get(positon);
        MHRequestParams requestParams = new MHRequestParams();
        String url = null;
        if (attention.getElement_type() == VIDEO) {
            //视频
            url = ConstantsValue.Url.DELETEVIDEO;
            requestParams.addParams("video_id", attention.getVideo_id());
        } else if (attention.getElement_type() == IMG) {
            //图片
            url = ConstantsValue.Url.DELETEPHOTO;
            requestParams.addParams("photo_id", attention.getPhoto_id());
        }
        MHHttpClient.getInstance().post(BaseResponse.class, context, url, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                DeleteVideoAndImgAsyncEvent event = new DeleteVideoAndImgAsyncEvent();
                if (attention.getElement_type() == IMG) {
                    ToastUtils.showToastAtCenter(context, "删除图片成功");
                    event.setContentType(IMG);
                    event.setTargetId(attention.getPhoto_id());
                    event.setFromType(ConstantsValue.Other.DELETEVIDEOANDIMG_FROMATTENTION);
                    if (uploadTempMap.containsKey(data.get(positon).getVideoSrcPath())) {
                        uploadTempMap.remove(data.get(positon).getVideoSrcPath());
                    }
                } else if (attention.getElement_type() == VIDEO) {
                    ToastUtils.showToastAtCenter(context, "删除视频成功");
                    event.setContentType(VIDEO);
                    event.setTargetId(attention.getVideo_id());
                    event.setFromType(ConstantsValue.Other.DELETEVIDEOANDIMG_FROMATTENTION);
                    if (uploadTempMap.containsKey(data.get(positon).getPhotoSrcPath())) {
                        uploadTempMap.remove(data.get(positon).getPhotoSrcPath());
                    }
                }
                EventBus.getDefault().post(event);
                adapter.remove(positon);
//                data.remove(positon);
//                adapter.notifyDataSetChangedBySelf();
//                deleteListItem(recyclerView.getListView(), positon);

                if (null != mediaPlayer)
                    mediaPlayer.reset();
            }

            @Override
            public void onFailure(String content) {
                if (attention.getElement_type() == IMG) {
                    ToastUtils.showToastAtCenter(context, "删除图片失败");
                } else if (attention.getElement_type() == VIDEO) {
                    ToastUtils.showToastAtCenter(context, "删除视频失败");
                }
            }
        });
    }


    /**
     * 不感兴趣的内容
     * object_type 1:人物 2：视频 3：映答 4：图片
     */
    private void unLikePeople(int position) {
        MHRequestParams params = new MHRequestParams();
        Attention attention = data.get(position);
        int element_type = attention.getElement_type();
        if (element_type == VIDEO) {
            params.addParams("object_id", attention.getVideo_id());
            params.addParams("object_type", "2");
            TCAgent.onEvent(context, "关注视频不感兴趣" + ConstantsValue.android);
        } else if (element_type == QA) {
            params.addParams("object_id", attention.getQuestion_id());
            params.addParams("object_type", "3");
            TCAgent.onEvent(context, "关注映答不感兴趣" + ConstantsValue.android);
        } else if (element_type == IMG) {
            params.addParams("object_id", attention.getPhoto_id());
            params.addParams("object_type", "4");
            TCAgent.onEvent(context, "关注图片不感兴趣" + ConstantsValue.android);
        }
        if (!ConstantsValue.isDeveloperMode(context)) {
            MHHttpClient.getInstance().post(BaseResponse.class, ConstantsValue.Url.UNINTERESTOBJECT, params, new MHHttpHandler<BaseResponse>() {
                @Override
                public void onSuccess(BaseResponse response) {
                }

                @Override
                public void onFailure(String content) {

                }
            });
        }
        adapter.remove(position);
//        data.remove(position);
//        adapter.notifyDataSetChangedBySelf();
//        deleteListItem(recyclerView.getListView(), position);
    }

    /**
     * 举报视频或者图片
     *
     * @param position
     */
    private void reportVideoOrPhoto(int position) {
        MHRequestParams params = new MHRequestParams();
        String url = null;
        if (data.get(position).getElement_type() == VIDEO) {
            url = ConstantsValue.Url.REPORTVIDEO;
            params.addParams("video_id", data.get(position).getVideo_id());
        } else if (data.get(position).getElement_type() == IMG) {
            url = ConstantsValue.Url.REPORTPHOTO;
            params.addParams("photo_id", data.get(position).getPhoto_id());
        } else if (data.get(position).getElement_type() == QA) {
            url = ConstantsValue.Url.REPORT_QUESTION;
            params.addParams("question_id", data.get(position).getQuestion_id());
        }
        MHHttpClient.getInstance().post(BaseResponse.class, context, url, params, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                ToastUtils.showToastAtCenter(context, "举报成功");
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    /**
     * 注册eventbus
     */
    public void registEvenBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    //后台服务器确认支付成功
    public void confirmPaySuccess(final Attention attention) {
        this.payAttention = attention;
        context.showLoading();
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("type", "10");//二次确认类型，10：围观，30：提问
        requestParams.addParams("answer_user", attention.getAnswer_user_id());
        requestParams.addParams("question_id", attention.getQuestion_id());
        if (!attention.is_question_owner() && attention.isTemporary_free()) {
            requestParams.addParams("price", "0");
        } else {
            requestParams.addParams("price", attention.getObserve_price() + "");
        }
        MHHttpClient.getInstance().post(ConfirmRequestResponse.class, context, ConstantsValue.Url.CONFIRMPAYSECONDREQUEST, requestParams, new MHHttpHandler<ConfirmRequestResponse>() {
            @Override
            public void onSuccess(ConfirmRequestResponse response) {
                TCAgent.onEvent(context, "映答围观成功" + ConstantsValue.android);
                ConfirmRequestResponse.DataBean dataBean = response.getData();
                if (dataBean.getState() == ConstantsValue.Other.CONFIRM_PAYSECONDRE_BALANCE) {//余额足够时
                    payVideo(attention);
                } else if (dataBean.getState() == ConstantsValue.Other.CONFIRM_PAYSECONDRE_CIRCUSEE) {//围观价钱改变
                    //更改价钱重新确认
                    attention.setObserve_price(dataBean.getPrice());
                    PriceChangeDialog dialog = new PriceChangeDialog(context, dataBean.getPrice());
                    dialog.show();
                    dialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                        @Override
                        public void onRightButtonOnClick() {
                            confirmPaySuccess(attention);
                        }
                    });
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
                            commonDialog.dismiss();
                            ((BaseActivity) context).hiddenLoadingView();
                        }
                    });
                    commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                        @Override
                        public void onRightButtonOnClick() {
                            Intent intent = new Intent();
                            intent.setClass(context, RechargingActivity1.class);
                            intent.putExtra("not_wallet", "not_wallet");
                            startActivityForResult(intent, 400);
                        }
                    });
                    commonDialog.show();
                }
            }

            @Override
            public void onFailure(String content) {
                context.showToastAtBottom("网络不佳，请稍后重试");
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
            }
        });
    }

    /**
     * 对围观的视频进行支付
     */
    private void payVideo(final Attention attention) {
        if (null == data)
            return;//表示已经支付过了
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("question_id", attention.getQuestion_id());
        if (!attention.is_question_owner() && attention.isTemporary_free()) {
            //显示免费
            requestParams.addParams("onlook_price", "" + 0);
        } else {
            requestParams.addParams("onlook_price", "" + attention.getObserve_price());
        }
        MHHttpClient.getInstance().post(PayQAResultResponse.class, ConstantsValue.Url.ON_LOOK_ANSWER, requestParams, new MHHttpHandler<PayQAResultResponse>() {
            @Override
            public void onSuccess(PayQAResultResponse response) {
                if (!attention.isTemporary_free() && !attention.isTemporary_free()) {
                    //xx元围观
                    context.showToastAtCenter("您已支付成功");
                }
                if (response.getData().getObserve_increase() == ConstantsValue.Other.PAYQARESULT_YES) {
                    attention.setObserve_count(attention.getObserveSrcCount() + 1);
                    adapter.notifyDataSetChangedBySelf();
                }
                if (videoView == null) return;
                final VideoInfo videoInfo = new VideoInfo();
                if (null != lastVideoView) {
                    lastVideoView.reset(position);
                    if (lastVideoView.getLastPlayPosition() == position) {
                        videoInfo.setLastPlayDuration(lastVideoView.getLastPlayPosition());
                    }
                }
                if (!attention.is_question_owner() && !attention.isTemporary_free()) {
                    attention.setIs_question_owner(true);
                    attention.setObserve_price_text("点击播放");
                    ((TextView) btn.getChildAt(1)).setText("点击播放");
                    btn.setVisibility(View.GONE);
                }
                if (null != adapter) {
                    adapter.setLastPlayPosition(position);
                    adapter.lastVideoView = videoView;
                    adapter.lastPlayPosition = position;
                }

                videoInfo.setVideo_id(data.get(position).getVideo_id());
                videoInfo.setVideo_uri(data.get(position).getVideo_url());
                videoInfo.setVideo_state(data.get(position).getVideo_state());
                btn.post(new Runnable() {
                    @Override
                    public void run() {
                        btn.setVisibility(View.GONE);
                        videoView.startPlay(mediaPlayer, videoInfo);
                    }
                });
//                if(adapter!=null) adapter.notifyDataSetChangedBySelf();
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
     * 接收上传进度
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateEvent(final RefreshUploadEvent event) {
        VideoUploadInfo task = event.getTask();
        //准备上传 列表回调顶部
        if (task.getUploadState() == VideoUploadInfo.UPLOAD_PRE) {
            handleUpload(event);
            //延时后处理上传
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(adapter);
                    recyclerView.smoothScrollToPosition(0);
                }
            }, 200);
        } else {
            handleUpload(event);
        }
    }

    /**
     * 处理视频上传
     *
     * @param event
     */
    public void handleUpload(RefreshUploadEvent event) {
        VideoUploadInfo task = event.getTask();

        //来自映答的消息
        if (!MHStringUtils.isEmpty(task.getQuestionId())) {
            if (task.getFromInfo() == VideoUploadInfo.FROM_ASK_AND_ANSWER
                    && task.getUploadState() == VideoUploadInfo.UPLOAD_SUCCESS) {
                context.showToastAtCenter("映答上传成功");
            }
            return;
        }

        UserInfo userInfo = task.getUserInfo();
        Attention uploadAttention = null;
        /**
         *查看当前任务集合中是否存在该任务如果存在则不需要重新添加只需根据id获取当前正在上传的任务并赋值
         *若不存在当前任务则创建新的变添加到当前上传集合中并赋值
         */
        if (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
            if (!uploadTempMap.containsKey(task.getVideoSrcPath())) {
                uploadAttention = new Attention();
                data.add(0, uploadAttention);
                uploadTempMap.put(task.getVideoSrcPath(), uploadAttention);
                uploadInfos.put(task.getVideoSrcPath(), task);
                if (adapter != null) adapter.setUploadTempMap(task);
            } else {
                uploadAttention = uploadTempMap.get(task.getVideoSrcPath());
            }
            uploadAttention.setVideoSrcPath(task.getVideoSrcPath());
        } else if (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
            if (!uploadTempMap.containsKey(task.getPictureSrcPath())) {
                uploadAttention = new Attention();
                data.add(0, uploadAttention);
                uploadTempMap.put(task.getPictureSrcPath(), uploadAttention);
                uploadInfos.put(task.getVideoSrcPath(), task);
                if (adapter != null) adapter.setUploadTempMap(task);
            } else {
                uploadAttention = uploadTempMap.get(task.getPictureSrcPath());
            }
            uploadAttention.setPhotoSrcPath(task.getPictureSrcPath());
        }
        uploadAttention.setProgress(task.getProsess());
        uploadAttention.setUploadState(task.getUploadState());
        uploadAttention.setUploading(true);
        uploadAttention.setPhoto_note(task.getVideoNote());
        uploadAttention.setVideo_note(task.getVideoNote());
        uploadAttention.setNotify_user_result(task.getNotify_user_results());
        uploadAttention.setVideo_uri(task.getVideoUrl());
        uploadAttention.setVideo_id(task.getVideoId());
        uploadAttention.setPhoto_id(task.getPhotoId());
        if (task.getUploadState() == VideoUploadInfo.UPLOAD_SUCCESS) {
            MHLogUtil.i("attentioinVideoid", "url:" + task.getVideoUrl());
            uploadAttention.setShare_link_address(task.getShare_link_address());
        }
        //上传前无网络图片则使用本地图片
        uploadAttention.setLocalCoverPath(task.getVideoPreviewImagePath());
        uploadAttention.setVideo_cover_uri(task.getCoverUrl());

        //上传前无网络图片则使用本地图片
        uploadAttention.setLocalPicturePath(task.getPicturePath());
        uploadAttention.setPhoto_uri(task.getPictureUrl());

        uploadAttention.setUser_name(userInfo.getUser_name());
        uploadAttention.setUser_type(userInfo.getUser_type());
        uploadAttention.setPortrait_uri(userInfo.getPortrait_uri());
        uploadAttention.setUser_authentic(userInfo.getUser_authentic());
        uploadAttention.setVideo_state(10);
        uploadAttention.setUser_note(userInfo.getUser_authentic());
        uploadAttention.setUser_id(UserInfoUtil.getUserId(context));
        //设置上传类型
        if (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
            //上传图片
            uploadAttention.setElement_type(IMG);
            uploadAttention.setWidth(task.getPictureWidth());
            uploadAttention.setHeight(task.getPictureHeight());
            if (task.getUploadState() == VideoUploadInfo.UPLOAD_SUCCESS) {
                //上传成功删除上传任务列表中对应的任务
                uploadTempMap.remove(task.getPictureSrcPath());
                uploadAttention.setUpload_time(System.currentTimeMillis());
                uploadAttention.setUploadState(VideoUploadInfo.UPLOAD_SUCCESS);
            }
        } else if (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
            //上传视频
            uploadAttention.setElement_type(VIDEO);
            uploadAttention.setWidth(task.getVideoWidth());
            uploadAttention.setHeight(task.getVideoHeight());
            if (task.getUploadState() == VideoUploadInfo.UPLOAD_SUCCESS) {
                //上传成功删除上传任务列表中对应的任务
                uploadTempMap.remove(task.getVideoSrcPath());
                if (isHidden) {
                    if (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
                        context.showToastAtCenter("图片发布成功啦");
                    } else {
                        context.showToastAtCenter("视频发布成功啦");
                    }
                }
            }
        }
        uploadAttention.setShowItemMarsk(true);
        if (task.getUploadState() == VideoUploadInfo.UPLOAD_FAILE) {
            //上传失败
            DraftUtil.saveDraft(task);
            //当用户不可见的时候提示
            if (isHidden) {
                if (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
                    context.showToastAtCenter("图片发布失败，快去关注页重新上传");
                } else {
                    context.showToastAtCenter("视频发布失败，快去关注页重新上传");
                }
            }
        }
        if (adapter != null) adapter.notifyDataSetChangedBySelf();
    }

    /**
     * 接受删除消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateEvent(DeleteVideoAndImgAsyncEvent event) {
        if (event.getFromType() != ConstantsValue.Other.DELETEVIDEOANDIMG_FROMATTENTION) {
            if (data == null) return;
            for (int i = 0; i < data.size(); i++) {
                Attention attention = data.get(i);
                if (event.getContentType() == attention.getContent_type()) {
                    if (event.getContentType() == VIDEO) {
                        if (TextUtils.equals(attention.getVideo_id(), event.getTargetId())) {
                            data.remove(i);
                            adapter.notifyDataSetChangedBySelf();

                            try {
                                if (null != mediaPlayer)
                                    mediaPlayer.reset();
                            } catch (Exception e) {
                                MHLogUtil.e(TAG,e);
                            }
                            return;
                        }
                    } else if (event.getContentType() == IMG) {
                        if (TextUtils.equals(attention.getPhoto_id(), event.getTargetId())) {
                            data.remove(i);
                            adapter.notifyDataSetChangedBySelf();
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * 接受关注页新消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateEvent(AttentionNewDataEvent event) {
        isShowPushTip = true;
        switchMainTabDot(MainActivity.OPENBUBBLE);
        showRefreshTip(event.getNotify_message(), true);
    }

    /**
     * 显示或者关闭MainActivity中关注tab的小红点
     *
     * @param bubbleState
     */
    public void switchMainTabDot(int bubbleState) {
        if (bubbleState == MainActivity.OPENBUBBLE) {
            if (isHidden) {
                //如果不是当前页则显示小红点
                ((OnAttentionBubbleState) getActivity()).OnAttentionBubbleState(bubbleState);
            }
        } else {
            ((OnAttentionBubbleState) getActivity()).OnAttentionBubbleState(bubbleState);
        }
    }

    /**
     * 显示刷新条数
     *
     * @param refreshCount 刷新的数量
     * @param fromPush     是否是来自于推送
     */
    private void showRefreshTip(String refreshCount, final boolean fromPush) {
        if (fromPush) {
            tv_attention_refreshtip.setBackgroundColor(Color.parseColor("#cc00a2ff"));
            if (MHStringUtils.isEmpty(refreshCount)) return;
            tv_attention_refreshtip.setText(refreshCount);
        } else {
            //网络是否连接
            boolean isNetConnect = CommonUtil.isNetworkAvailable(context);
            if (isNetConnect) {
                //网络有连接
                tv_attention_refreshtip.setBackgroundColor(Color.parseColor("#cc1b1b1b"));
                if (MHStringUtils.isEmpty(refreshCount)) return;
                tv_attention_refreshtip.setText(refreshCount);
            } else {
                //网络无连接
                tv_attention_refreshtip.setText("网络中断无法更新状态");
                tv_attention_refreshtip.setBackgroundColor(Color.parseColor("#cc999999"));
            }
        }
        tv_attention_refreshtip.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(tv_attention_refreshtip, "alpha", 0, 1);
        anim.setDuration(500);
        anim.start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (fromPush) return;
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(tv_attention_refreshtip, "alpha", 1, 0);
                        anim.setDuration(1000);
                        anim.start();
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                new android.os.Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_attention_refreshtip.setVisibility(View.GONE);
                                    }
                                }, 200);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    }
                }, 1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 倒计时
     */
    public void handlerCountDown() {
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                handler.postDelayed(this, 1000);
                handleTime();
            }
        }, 1000);
    }

    /**
     * 处理倒计时
     */
    public void handleTime() {
        boolean hasOverTime = false;
        for (int i = 0; i < data.size(); i++) {
            Attention attention = data.get(i);
            if (attention.getElement_type() == QA) {
                if (!attention.is_question_owner() && attention.isTemporary_free()) {
                    if (!attention.is_question_owner()
                            && attention.isTemporary_free()
                            && attention.getTime_remain() <= 0) {
                        //限时免费结束
                        attention.setTemporary_free(false);
                        attention.setObserve_price_text(CommonUtil.formatPrice2(attention.getObserve_price()) + "元围观");
                        hasOverTime = true;
                    } else {
                        attention.setTime_remain(attention.getTime_remain() - 1000);
                    }
                }
            }
        }
        if (adapter != null && hasOverTime) adapter.notifyDataSetChangedBySelf();
        hasOverTime = false;
        handlerCountDown();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (hidden) {
            context.hiddenLoadingView();
            if (mediaPlayer != null) mediaPlayer.pause();
        }
        if (hidden && null != mediaPlayer && mediaPlayer.isPlaying())
            mediaPlayer.pause();
        if (!hidden && null != adapter)
            adapter.notifyDataSetChangedBySelf();
    }

    @Override
    protected void onMyResume() {
        super.onMyResume();
        if (null != adapter) adapter.notifyDataSetChangedBySelf();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != adapter) adapter.notifyDataSetChangedBySelf();
        ((BaseActivity) getActivity()).hiddenLoadingView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null)
            context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onMyPause() {
        super.onMyPause();
        if (null != mediaPlayer && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mediaPlayer && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        if (null != recyclerView) {
            recyclerView.scrollToPosition(0);
        }
        if (null != ptrshl_attention) {
            ptrshl_attention.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrshl_attention.doPullRefreshing(true);
                }
            }, 200);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

}

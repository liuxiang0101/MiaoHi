package com.haiqiu.miaohi.activity;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.fragment.AttentionFragment;
import com.haiqiu.miaohi.fragment.FoundSquareFragment;
import com.haiqiu.miaohi.fragment.MessageFragment;
import com.haiqiu.miaohi.fragment.MineFragment;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.receiver.OnAttentionBubbleState;
import com.haiqiu.miaohi.response.LabelSelectedResponse;
import com.haiqiu.miaohi.rong.GetRongUnreadCount;
import com.haiqiu.miaohi.rong.RongConnectionStatusListener;
import com.haiqiu.miaohi.rong.RongReceiveMessageListener;
import com.haiqiu.miaohi.utils.AnimateFirstDisplayListener;
import com.haiqiu.miaohi.utils.CheckUpdateCallback;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.VideoStatisticUtil;
import com.haiqiu.miaohi.utils.crop.Crop;
import com.haiqiu.miaohi.view.picturezoom.OnShowPicture;
import com.haiqiu.miaohi.view.picturezoom.PhotoView;
import com.haiqiu.miaohi.view.picturezoom.PhotoViewAttacher;
import com.haiqiu.miaohi.widget.mediaplayer.VideoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;

/**
 * 主页
 * Created by ningl on 2016/6/8.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener
        , MineFragment.OnHiddenTabBar
        , OnShowPicture
        , OnAttentionBubbleState {
    public static final int MAIN = 2;
    public static final int CLOSEBUBBLE = 0;
    public static final int OPENBUBBLE = 1;
    private long mExitTime;

    private TextView tv_mainminetab_bubbledot;
    private TextView tv_qipao_shot;
    private TextView tv_mainminetab_bubblecount;
    private FrameLayout maincontent;
    private Fragment currentFragment;
    private FragmentTransaction ft;
    private FragmentManager fm;
    private AttentionFragment attentionFragment;
    private View home_tab;
    public List<BaseFragment> fragments = new ArrayList<>();

    //用来判断当前页面是否需要刷新
    public static boolean isDeleteVideo;

    private PhotoView pv_main;
    private FrameLayout fl_showpicturemain;

    private BubbleCountReceiver bubbleCountReceiver = new BubbleCountReceiver();
    private LogoutReceiver logoutReceiver = new LogoutReceiver();
    private View rl_active;
    private View rl_home;
    private View rl_discover;
    private View rl_mine;

    UMShareAPI mShareAPI = null;
    private MineFragment mineFragment;
    private boolean initPageData;

    private TextView tv_refreshcountbubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //注册广播接收
        IntentFilter bubbleFilter = new IntentFilter();
        bubbleFilter.addAction("broadcast_bubble");
        registerReceiver(bubbleCountReceiver, bubbleFilter);

        IntentFilter loguoutFiler = new IntentFilter();
        loguoutFiler.addAction("miaohilogout");
        registerReceiver(logoutReceiver, loguoutFiler);
    }

    @Override
    protected void reTry() {
        super.reTry();
        init();
    }

    /**
     * 检测更新,检测当前用户是否选择了标签
     */
    private void init() {
        boolean hasChooseLabel = SpUtils.getBoolean(ConstantsValue.Sp.CHOOSE_LABEL_FLAG, false);
        if (!hasChooseLabel) {//第一次同步处理
            CommonUtil.checkUpdate(context, true, new CheckUpdateCallback() {
                @Override
                public void doWithSelectedLabel() {//没有强制更新,需要选择标签
                    hideErrorView();
                    if (isLogin()) {
                        MHHttpClient.getInstance().post(LabelSelectedResponse.class, context, ConstantsValue.Url.IS_SELECT_LABEL, new MHHttpHandler<LabelSelectedResponse>() {
                            @Override
                            public void onSuccess(LabelSelectedResponse response) {
                                hideErrorView();
                                if (response.data.label_selected != 1) {
                                    MHLogUtil.d(TAG, "第一次同步处理--需要跳转到选择标签页面");
                                    startActivity(new Intent(context, RecommendSportsActivity.class));
                                } else {
                                    SpUtils.put(ConstantsValue.Sp.CHOOSE_LABEL_FLAG, true);
                                    MHLogUtil.d(TAG, "第一次同步处理--不需要选择标签");
                                    initPageData();
                                }
                            }

                            @Override
                            public void onFailure(String content) {
                                showErrorView();
                            }

                            @Override
                            public void onStatusIsError(String message) {
                                super.onStatusIsError(message);
                                showErrorView();
                            }
                        });
                    } else {
                        initPageData();
                    }
                }
            });
        } else {//第二次异步处理
            initPageData();
            if (!ConstantsValue.isDeveloperMode(null))
                CommonUtil.checkUpdate(context, true, null);
            if (isLogin()) {
                MHHttpClient.getInstance().post(LabelSelectedResponse.class, context, ConstantsValue.Url.IS_SELECT_LABEL, new MHHttpHandler<LabelSelectedResponse>() {
                    @Override
                    public void onSuccess(LabelSelectedResponse response) {
                        if (response.data.label_selected != 1) {
                            MHLogUtil.d(TAG, "第二次异步处理--需要跳转到选择标签页面");
                            startActivity(new Intent(context, RecommendSportsActivity.class));
                        } else {
                            MHLogUtil.d(TAG, "第二次异步处理--不需要选择标签");
                        }
                    }

                    @Override
                    public void onFailure(String content) {

                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!initPageData && SpUtils.getBoolean(ConstantsValue.Sp.CHOOSE_LABEL_FLAG, false)) {
            MHLogUtil.d(TAG, "已经选过标签了,并且没有初始化数据");
            initPageData();
        }
//        if (null != attentionFragment && !attentionFragment.isHidden() && isDeleteVideo) {
//            sendBroadcast(new Intent(REFRESH_DATA_ACTION));
//        }
        isDeleteVideo = false;
    }

    /**
     * 初始化页面数据
     */
    private void initPageData() {
        initPageData = true;
        mShareAPI = UMShareAPI.get(context);

        initView();

        initFragment();
        //创建默认的文件夹
        //设置融云连接状态监听
        RongIMClient.setConnectionStatusListener(new RongConnectionStatusListener(this));

        if (!SpUtils.getBoolean(ConstantsValue.Sp.HAS_CLICKED_MAKE_VIDEO, false)) {
            tv_qipao_shot.setVisibility(View.VISIBLE);
            initAnim();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoStatisticUtil.uploadCount();
        hiddenLoadingView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_qipao_shot = (TextView) findViewById(R.id.tv_qipao_shot);
        maincontent = (FrameLayout) findViewById(R.id.maincontent);
        //        iv_activetab = (ImageView) findViewById(R.id.iv_activetab);
        //        iv_hometab = (ImageView) findViewById(R.id.iv_hometab);
        //        tv_home = (TextView) findViewById(R.id.tv_home);
        //        tv_active = (TextView) findViewById(R.id.tv_active);
        //        tv_discover = (TextView) findViewById(R.id.tv_discover);
        //        tv_mine = (TextView) findViewById(R.id.tv_mine);
        //
        //        iv_discovertab = (ImageView) findViewById(R.id.iv_discovertab);
        //        iv_minetab = (ImageView) findViewById(R.id.iv_minetab);
        tv_mainminetab_bubbledot = (TextView) findViewById(R.id.tv_mainminetab_bubbledot);
        tv_mainminetab_bubblecount = (TextView) findViewById(R.id.tv_mainminetab_bubblecount);
        pv_main = (PhotoView) findViewById(R.id.pv_main);
        fl_showpicturemain = (FrameLayout) findViewById(R.id.fl_showpicturemain);
        home_tab = findViewById(R.id.home_tab);
        tv_refreshcountbubble = (TextView) findViewById(R.id.tv_refreshcountbubble);
        maincontent.post(new Runnable() {
            @Override
            public void run() {
                //进入主界面初始化时，重新获取一下未读消息
                new GetRongUnreadCount(getApplicationContext());
            }
        });
        if (getIntent().getBooleanExtra("isIntoFromPush", false)) {
//            if (("ad_h5").equals(getIntent().getStringExtra("type"))) {
//                Intent intent = new Intent(this, WebViewActivity.class);
//                intent.putExtra("uri", getIntent().getStringExtra("html_uri"));
//                intent.putExtra("title", getIntent().getExtras().getString("html_name", "网页链接"));
//                intent.putExtra("activity_note", getIntent().getStringExtra("html_note"));
//                intent.putExtra("activity_picture", getIntent().getStringExtra("html_picture"));
//                startActivity(intent);
//            }
            maincontent.post(new Runnable() {
                @Override
                public void run() {
                    //从通知栏进入，并且执行初始化，说明并没有连接融云，在用户已登录情况下，执行连接融云的方法
                    if (isLogin()) {
                        intoFormPush(getIntent().getStringExtra("type"), getIntent());
                        RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(MainActivity.this));
                        RongIMClient.connect(SpUtils.get(ConstantsValue.Sp.RONG_TOKEN, "") + "", new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                MHLogUtil.i(TAG, "rong_token失效");
                            }

                            @Override
                            public void onSuccess(String s) {
                                MHLogUtil.e(TAG, "获取rong_token成功---userId:" + s);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                MHLogUtil.e(TAG, "获取rong_token失败");
                            }
                        });
                    } else {//用户已登录情况
                        if (getIntent().getBooleanExtra("isAdLink", false))
                            intoFormPush(getIntent().getStringExtra("type"), getIntent());
                    }
                }
            });
        }
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        //添加关注fragment
        attentionFragment = new AttentionFragment();
        fragments.add(attentionFragment);
        //添加发现fragment
        fragments.add(new FoundSquareFragment());
        //添加消息fragment
        fragments.add(new MessageFragment());
        //添加个人fragment(个人主页评论是隐藏底部tab)
        Bundle mineBndle = new Bundle();
        mineBndle.putString("userId", UserInfoUtil.getUserId(context));
        mineBndle.putBoolean("isFromMain", true);
        mineFragment = new MineFragment();
        mineFragment.setArguments(mineBndle);
        fragments.add(mineFragment);
//        //添加登录fragment
//        fragments.add(new LoginBuiltInFragment());
        //是否登录逻辑(登录跳关注fragment,未登录跳发现fragment)
        if (isLogin())
            currentFragment = fragments.get(0);
        else
            currentFragment = fragments.get(1);
        //提交事务,将fragment添加进去
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.maincontent, currentFragment).commit();

        rl_home = findViewById(R.id.rl_home);
        rl_home.setSelected(true);
        rl_active = findViewById(R.id.rl_active);
        rl_discover = findViewById(R.id.rl_discover);
        rl_mine = findViewById(R.id.rl_mine);

        rl_home.setOnClickListener(this);
        rl_active.setOnClickListener(this);
        rl_discover.setOnClickListener(this);
        rl_mine.setOnClickListener(this);
        findViewById(R.id.make_video).setOnClickListener(this);

        setSelect(isLogin() ? 0 : 1);
    }

    private void initAnim() {
        final TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -0.2f);
        translateAnimation.setDuration(500);
        translateAnimation.setRepeatCount(6);
        //        translateAnimation.setFillAfter(true);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        tv_qipao_shot.setAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SpUtils.put(ConstantsValue.Sp.HAS_CLICKED_MAKE_VIDEO, true);
                //                if (translateAnimation != null) translateAnimation.setFillAfter(false);
                tv_qipao_shot.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void intoFormPush(String msgType, Intent dataIntent) {
        //        switchFragment(3);
        if (null == msgType || null == dataIntent) return;
        TCAgent.onEvent(context, "通知栏点击消息" + ConstantsValue.android);
        Intent intent = null;
        if (msgType.equals(ConstantsValue.MessageCommend.MSG_NEW_FRIEND)) {
            //消息-新的好友
            if (!isLogin()) return;
            intent = new Intent(this, PersonalHomeActivity.class);
            intent.putExtra("userId", dataIntent.getStringExtra("userId"));
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_RECEIVE_COMMENT)) {
            //消息-评论消息
            if (!isLogin()) return;
            intent = new Intent(this, MessageCommentMsgActivity.class);
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_AT_ME)) {
            //消息-@我的
            if (!isLogin()) return;
            intent = new Intent(this, MessageAtMineActivity.class);
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_RECEIVE_ZAN)) {
            //消息-收到的赞
            if (!isLogin()) return;
            intent = new Intent(this, MessageNoticeActivity.class);
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_BE_INVITATE)) {
            //消息-受邀专题
//            intent = new Intent(this, MessageBeInvitedActivity.class);
            MHLogUtil.e(TAG, "受邀专题消息模块已停用");
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_SYSTEM)) {
            //消息-系统消息
            if (!isLogin()) return;
            intent = new Intent(this, MessageSystemActivity.class);
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_RECEIVE_GIFT)) {
            //消息-收到礼物
//            intent = new Intent(this, MineReceiveGiftAct.class);
            MHLogUtil.e(TAG, "收到礼物消息模块已停用");
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_VIP_ANSWER_VIP) || msgType.equals(ConstantsValue.MessageCommend.MSG_VIP_ANSWER_USER)) {
            //消息-我问的
            if (!isLogin()) return;
            intent = new Intent(this, MyQaActivity.class);
            intent.putExtra(ConstantsValue.Sp.QA_TYPE, 1);
            intent.putExtra("question_id", dataIntent.getStringExtra("question_id"));
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_VIP_RECEIVE_QUESTION)) {
            //消息-问我的
            if (!isLogin()) return;
            intent = new Intent(this, MyQaActivity.class);
            intent.putExtra(ConstantsValue.Sp.QA_TYPE, 0);
        } else if (msgType.equals(ConstantsValue.MessageCommend.MSG_OBSERVE_VIDEO)) {
            //消息-视频被围观
            if (!isLogin()) return;
            intent = new Intent(this, TradingRecordActivity1.class);
        } else if (msgType.equals("1")) {//系统消息-视频
            intent = new Intent(this, VideoAndImgActivity.class);
            ArrayList<VideoAndImg> data = new ArrayList<>();
            VideoAndImg obj = new VideoAndImg();
            obj.setElement_type(1);
            obj.setContent_type(1);
            obj.setPhoto_id(dataIntent.getStringExtra("objectId"));
            obj.setVideo_id(dataIntent.getStringExtra("objectId"));
            data.add(obj);
            intent.putParcelableArrayListExtra("data", data)
                    .putExtra("currentIndex", 0)
                    .putExtra("userId", dataIntent.getStringExtra("objectId"))
                    .putExtra("pageIndex", 0)
                    .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
        } else if (msgType.equals("2")) {//系统消息-专题
            if (!isLogin()) return;
            intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("uri", dataIntent.getStringExtra("activity_uri"));
            intent.putExtra("title", dataIntent.getStringExtra("activity_name"));
            intent.putExtra("activity_note", dataIntent.getStringExtra("activity_note"));
            intent.putExtra("activity_picture", dataIntent.getStringExtra("activity_picture"));
        } else if (msgType.equals("3")) {//系统消息-映答
            intent = new Intent(this, InterlocutionDetailsActivity.class);
            intent.putExtra("question_id", dataIntent.getStringExtra("objectId"));
        } else if (msgType.equals("4")) {//系统消息-个人
            if (!isLogin(false)) return;
            intent = new Intent(this, PersonalHomeActivity.class);
            intent.putExtra("userId", dataIntent.getStringExtra("objectId"));
        } else if (msgType.equals("6")) {//系统消息-图片
            intent = new Intent(this, VideoAndImgActivity.class);
            ArrayList<VideoAndImg> data = new ArrayList<>();
            VideoAndImg obj = new VideoAndImg();
            obj.setElement_type(2);
            obj.setContent_type(2);
            obj.setPhoto_id(dataIntent.getStringExtra("objectId"));
            obj.setVideo_id(dataIntent.getStringExtra("objectId"));
            data.add(obj);
            intent.putParcelableArrayListExtra("data", data)
                    .putExtra("currentIndex", 0)
                    .putExtra("userId", dataIntent.getStringExtra("objectId"))
                    .putExtra("pageIndex", 0)
                    .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
        } else if (msgType.equals("ad_personal_page")) {
            if (!isLogin(false)) return;
            intent = new Intent(this, PersonalHomeActivity.class);
            intent.putExtra("userId", dataIntent.getStringExtra("userId"));
        } else if (msgType.equals("ad_h5")) {
            intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("uri", dataIntent.getStringExtra("html_uri"));
            intent.putExtra("title", dataIntent.getStringExtra("html_name"));
            intent.putExtra("activity_note", dataIntent.getStringExtra("html_note"));
            intent.putExtra("activity_picture", dataIntent.getStringExtra("html_picture"));
        }
        if (intent == null) return;
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home:
                TCAgent.onEvent(context, "关注tab" + ConstantsValue.android);
                if (!isLogin(false, 1)) return;
                switchFragment(0);
                break;
            case R.id.rl_active:
                TCAgent.onEvent(context, "发现tab" + ConstantsValue.android);
                switchFragment(1);
                break;
            case R.id.rl_discover:
                TCAgent.onEvent(context, "消息tab" + ConstantsValue.android);
                if (!isLogin(false, 3)) return;
                switchFragment(2);
                break;
            case R.id.rl_mine:
                if (!isLogin(false, 4)) return;
                TCAgent.onEvent(context, "我的tab" + ConstantsValue.android);
                switchFragment(3);
                break;
            case R.id.make_video:
                if (!isLogin(false, 5)) return;
                TCAgent.onEvent(context, "拍摄tab" + ConstantsValue.android);
                if ("x86".equalsIgnoreCase(Build.CPU_ABI)) {
                    showToastAtCenter("您的手机暂不支持");
                    return;
                }
                Intent intent = new Intent(this, VideoRecorderActivity.class);
                startActivityNoAnimation(intent);
                overridePendingTransition(R.anim.slide_bottom_out, 0);
                break;
        }

    }

    /**
     * fragment切换
     *
     * @param index 索引
     */
    public void switchFragment(int index) {
//        if (!isLogin() && index != 1) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//            toFragment = fragments.get(4);
//            ((LoginBuiltInFragment) toFragment).setTabIndex(index);
//        } else {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            toFragment = fragments.get(index);
//        }
        if (index == 0) {
            //切换到关注页的fragment是气泡消失
            tv_refreshcountbubble.setVisibility(View.GONE);
        }
        Fragment toFragment = fragments.get(index);
        if (currentFragment != toFragment/* || toFragment == fragments.get(4)*/) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (!toFragment.isAdded()) {    // 先判断是否被add过
//                fragmentTransaction.setCustomAnimations(R.anim.maintab_fadeshow,R.anim.maintab_fadehide);
                fragmentTransaction.hide(currentFragment).add(R.id.maincontent, toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
//                fragmentTransaction.setCustomAnimations(R.anim.maintab_fadeshow, R.anim.maintab_fadehide);
                fragmentTransaction.hide(currentFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            setSelect(index);
            currentFragment = toFragment;
        } else {
            fragments.get(index).refreshData();
        }
    }

    /**
     * 强制登出后设置首页的viewpager加载默认的
     */
    public void setViewpager() {

    }


    /**
     * 设置选中
     *
     * @param to 选中的索引
     */
    public void setSelect(int to) {
        rl_home.setSelected(false);
        rl_active.setSelected(false);
        rl_discover.setSelected(false);
        rl_mine.setSelected(false);
        switch (to) {
            case 0:
                rl_home.setSelected(true);
                break;
            case 1:
                rl_active.setSelected(true);
                break;
            case 2:
                rl_discover.setSelected(true);
                break;
            case 3:
                rl_mine.setSelected(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            mShareAPI.onActivityResult(requestCode, resultCode, data);
        }
        hiddenLoadingView();
        if (resultCode == MineSettingActivity.LOGINOUT) {
            switchFragment(1);
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivityForResult(intent, 200);
        } else if (resultCode == MAIN) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }

        if ((requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK)
                || requestCode == Crop.REQUEST_CROP
                || requestCode == 37
                || requestCode == 200
                || resultCode == 102) {
            mineFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == 102) {
            //@好友
            attentionFragment.onActivityResult(requestCode, resultCode, data);
        }

        /////////////////////////////
        if (resultCode == 200) {
            Intent intent = new Intent(MainActivity.this, LRPerfectInformationActivity.class);
            intent.putExtra("undefine", "undefine");
            startActivity(intent);
            finish();
        }
        if (resultCode == 201) {
            int jumpType = data.getIntExtra("jumpType", 0);
            if (jumpType == 0) return;
            if (jumpType == 5) {
                startActivity(new Intent(this, VideoRecorderActivity.class));
                return;
            }
            switchFragment(jumpType - 1);
        }
        if (resultCode == 30 || resultCode == 0) {
            hiddenLoadingView();
        }
        //充值成功后或者从应答详情返回
        if (resultCode == 400 || resultCode == InterlocutionDetailsActivity.PAYRESULT) {
            attentionFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 点击两次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //如果是展示大图状态则需要先隐藏大图
            if (fl_showpicturemain.getVisibility() == View.VISIBLE) {
                fl_showpicturemain.setVisibility(View.GONE);
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (null != VideoView.backDownListener) {
                return VideoView.backDownListener.OnBackDown();
            }
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showToastAtBottom("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                //退出时调用disconnect()
                if (RongIMClient.getInstance() != null) {
                    MHLogUtil.e(TAG, "退出时调用disconnect()");
                    RongIMClient.getInstance().disconnect();
                }
                MainActivity.this.finish();
                MobclickAgent.onKillProcess(context);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        RongIMClient.getInstance().getCurrentConnectionStatus();
        //注销广播
        if (bubbleCountReceiver != null) {
            unregisterReceiver(bubbleCountReceiver);
        }
        if (logoutReceiver != null) {
            unregisterReceiver(logoutReceiver);
        }
        UMShareAPI.get(this).release();
        super.onDestroy();
    }

    /**
     * 设置我的tab气泡
     *
     * @param giftCount    新收到礼物数量
     * @param fansCount    新粉丝数量
     * @param messageCount 新消息数量
     */
    private void setMineBubble(int giftCount, int fansCount, int messageCount, int draftsCount, int QaCount) {
        if (!isLogin()) {
            tv_mainminetab_bubbledot.setVisibility(View.GONE);
            tv_mainminetab_bubblecount.setVisibility(View.GONE);
            return;
        }
        if (QaCount == 0 && giftCount == 0 && fansCount == 0 && messageCount == 0 && draftsCount != 0) {
//            tv_mainminetab_bubbledot.setVisibility(View.VISIBLE);
            tv_mainminetab_bubblecount.setVisibility(View.GONE);
        } else if (QaCount == 0 && giftCount == 0 && fansCount == 0 && messageCount == 0 && draftsCount == 0) {
            tv_mainminetab_bubbledot.setVisibility(View.GONE);
            tv_mainminetab_bubblecount.setVisibility(View.GONE);
        } else {
            tv_mainminetab_bubbledot.setVisibility(View.GONE);
            tv_mainminetab_bubblecount.setVisibility(View.VISIBLE);
            int count = QaCount + giftCount + fansCount + messageCount;
            if (count > 99) {
                tv_mainminetab_bubblecount.setText("99+");
            } else {
                tv_mainminetab_bubblecount.setText(count + "");
            }
        }
    }

    @Override
    public void hiddenTabBar(boolean isShow) {
        home_tab.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onShowPicture(String url) {
        fl_showpicturemain.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(fl_showpicturemain, "alpha", 0, 1f)
                .setDuration(400)
                .start();
        ImageLoader.getInstance().displayImage(url, pv_main, new AnimateFirstDisplayListener(pv_main));
        pv_main.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                fl_showpicturemain.setVisibility(View.GONE);
            }

            @Override
            public void onOutsidePhotoTap() {
                fl_showpicturemain.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void OnAttentionBubbleState(int bubbleState) {
        if (bubbleState == OPENBUBBLE) {
            //显示红点
            tv_refreshcountbubble.setVisibility(View.VISIBLE);
        } else {
            //关闭红点
            tv_refreshcountbubble.setVisibility(View.GONE);
        }
    }

    /**
     * 接受气泡信息
     */
    public class BubbleCountReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            int giftCount;
            int fansCount;
            int messageCount;
            int draftsCount;
            int QaCount;
            QaCount = Integer.parseInt(intent.getExtras().getString("QaCount", "0"));
            giftCount = Integer.parseInt(intent.getExtras().getString("giftCount", "0"));
            fansCount = Integer.parseInt(intent.getExtras().getString("fansCount", "0"));
            messageCount = Integer.parseInt(intent.getExtras().getString("messageCount", "0"));

            if (!isLogin()) {
                draftsCount = 0;
            } else {
                draftsCount = DraftUtil.getDraftCount();
            }
            setMineBubble(giftCount, fansCount, messageCount, draftsCount, QaCount);

        }
    }

    /**
     * 退出登陆后设置显示页面
     */
    public class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //被强制退出后跳转至发现Tab
            switchFragment(1);
//            homeFrag.setCurrentSelectedFragment(0);
            setMineBubble(0, 0, 0, 0, 0);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null == intent) return;

        String msgType = intent.getStringExtra("type");
        if (!MHStringUtils.isEmpty(msgType)) {
            intoFormPush(msgType, intent);
        }
        int pageIndex = intent.getIntExtra("pageIndex", 0);
        if (0 != pageIndex) {
            Fragment toFragment = fragments.get(0);
            if (currentFragment != toFragment) {
                switchFragment(0);
            }
//            homeFrag.selectedAttentionFragment();
        }
    }

}

package com.haiqiu.miaohi.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.CommonUserListActivity;
import com.haiqiu.miaohi.activity.MineDataActivity;
import com.haiqiu.miaohi.activity.MineSettingActivity;
import com.haiqiu.miaohi.activity.MyQaActivity;
import com.haiqiu.miaohi.activity.QAHomeActivity;
import com.haiqiu.miaohi.activity.UserListActivity;
import com.haiqiu.miaohi.activity.VideoAndImgActivity;
import com.haiqiu.miaohi.activity.VideoRecorderActivity;
import com.haiqiu.miaohi.activity.WalletActivity;
import com.haiqiu.miaohi.adapter.MineListAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.base.FadeOutFromBottomItem;
import com.haiqiu.miaohi.bean.Comment;
import com.haiqiu.miaohi.bean.Notify_user_result;
import com.haiqiu.miaohi.bean.SendCommentResponse;
import com.haiqiu.miaohi.bean.TextInfo;
import com.haiqiu.miaohi.bean.UserInfo;
import com.haiqiu.miaohi.bean.UserWork;
import com.haiqiu.miaohi.bean.VideoDetailUserCommentBean;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.CloseEmojiKeyboard;
import com.haiqiu.miaohi.receiver.DeleteVideoAndImgAsyncEvent;
import com.haiqiu.miaohi.receiver.OpenQAEvent;
import com.haiqiu.miaohi.receiver.RefreshCommentCountEvent;
import com.haiqiu.miaohi.receiver.RefreshUploadEvent;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.HomeFoundResponse;
import com.haiqiu.miaohi.response.UserInfoResponse1;
import com.haiqiu.miaohi.response.UserWorksResponse;
import com.haiqiu.miaohi.umeng.IUMShareResultListener;
import com.haiqiu.miaohi.umeng.ShareImg;
import com.haiqiu.miaohi.umeng.UmengShare;
import com.haiqiu.miaohi.utils.AbstractTextUtil;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.FileUtils;
import com.haiqiu.miaohi.utils.MHContentSyncUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.TextUtil;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.crop.Crop;
import com.haiqiu.miaohi.utils.shareImg.SharePersonalHomeImgView;
import com.haiqiu.miaohi.utils.shareImg.ShareVideoAndImgView2;
import com.haiqiu.miaohi.utils.upload.GetToken;
import com.haiqiu.miaohi.view.DialogFadeOutFromBottom;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.view.NoteEditText;
import com.haiqiu.miaohi.view.ShareDialog;
import com.haiqiu.miaohi.view.picturezoom.OnShowPicture;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.haiqiu.miaohi.widget.HeaderRecyclerViewAdapter;
import com.haiqiu.miaohi.widget.KeyBoardView;
import com.haiqiu.miaohi.widget.ShareLayout;
import com.haiqiu.miaohi.widget.mediaplayer.MyMediaPlayer;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshZoomRecyclerHeaderView;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullZoomRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的页面
 * Created by ningl on 16/12/4.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener
        , KeyBoardView.OnStartCommentListener, KeyBoardView.OnKeboardListener
        , KeyBoardView.OnEmojiDoListener, MineListAdapter.OnSkipVideoAndImgDetail {

    private PullToRefreshZoomRecyclerHeaderView pz_mine;
    private PullZoomRecyclerView pz;
    private MineListAdapter listAdapter;
    private View headerView;
    private View footerView;
    private ImageView iv_mineheader_grid;
    private ImageView iv_mineheader_list;
    private ImageView iv_mineback;
    private ImageView iv_minesetting;
    private ImageView iv_minewallet;
    private ImageView iv_mineshare;
    private TextView tv_mineattention;
    private TextView tv_mineeditdata;
    private TextView tv_minemyqa;
    private LinearLayout ll_switchlayout;
    private View bottomView;
    private SharePersonalHomeImgView sphi_;
    private TextView tv_minestartmiaohi;
    private TextView tv_mineempty;

    private MyCircleView mcv_minehead;
    private TextView tv_header_name;
    private ImageView iv_minevip;
    private TextView tv_header_describe;
    private TextView tv_mineheader_contentcount;
    private TextView tv_mineheader_attentioncount;
    private TextView tv_mineheader_fanscount;
    private TextView tv_itsmyqa;
    private ImageView iv_minebg;
    private KeyBoardView kv_personalHome;
    private NoteEditText et_videodetail_input;
    private ShareVideoAndImgView2 svaiv_personalhome;
    private List<TextInfo> textInfos;
    private ReceiveLogin receiveLogin;
    private List<UserWork> userWorks;
    private FrameLayout fl_mineheadercontainer;


    private int status;
    private static final int GRID = 0;
    private static final int LIST = 1;
    private LinearLayoutManager llm;
    private GridLayoutManager glm;
    private RelativeLayout rl_minetitle;
    private View v_mineblacktitle;
    private int page_index = 0;
    private UserInfo userInfo;
    private boolean isSelf;
    private ArrayList<UserWork> data;
    private DialogFadeOutFromBottom selectBgDialog;
    private Uri imageUri;
    private static final int PHOTO_WITH_CAMERA = 37;        // 拍摄照片
    private String filePath;
    private Uri resultUri;
    private String userId;
    private MyMediaPlayer mediaPlayer;
    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisiblesItems;
    private int firstVisibleItem;
    private boolean loading = true;
    private SharePersonalHomeImgView sphi_view;
    private FrameLayout fl_keboardmarsk_personalhome;
    private ArrayList<String> namesByat, userIdByat;
    private List<Notify_user_result> notify_user_results;//@的好友
    private TextUtil edit_textUtil;
    private int currentCommonPosition;//当前需评论的位置
    private boolean hasShowKeyBoard;
    private static final int VIDEO = 1;         //视频
    private static final int IMG = 2;           //图片
    private boolean isFromMain;
    private HeaderRecyclerViewAdapter headerRecyclerViewAdapter;
    private String lastCommentId = "";//上次评论的图片id或者视频id

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteVideoRecordDir();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = View.inflate(context, R.layout.fragment_mine, null);
        registLoginReceiver();
        registEvenBus();
        data = new ArrayList<>();
        mediaPlayer = new MyMediaPlayer();
        edit_textUtil = TextUtil.getInstance();
        initView(rootView);
        kv_personalHome.setRootView(rootView.findViewById(R.id.fl_rootview_mine));
        listAdapter.setOnSkipVideoAndImgDetailListener(this);
        pz.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (status == GRID) return;
                if (dy > 0) {
                    visibleItemCount = llm.getChildCount();
                    totalItemCount = llm.getItemCount();
                    pastVisiblesItems = llm.findFirstVisibleItemPosition();

                    firstVisibleItem = pastVisiblesItems;

                    if (null != listAdapter) {
                        int lastPlayPosition = listAdapter.getLastPlayPosition();
                        if (lastPlayPosition < firstVisibleItem - 1 || lastPlayPosition >= firstVisibleItem + visibleItemCount - 1) {
                            if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                            }
                        }
                    }

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            MHLogUtil.v("...", "Last Item Wow !");
                        }
                    }
                }
            }
        });
        listAdapter.setOnClickPictureListener(new OnShowPicture() {
            @Override
            public void onShowPicture(String url) {
                ((OnShowPicture) getActivity()).onShowPicture(url);
            }
        });
        listAdapter.setDeleteAllListener(new MineListAdapter.IDeleteAll() {
            @Override
            public void onDelete() {
                if (data.size() == 1 && data.get(0).getContent_type() == 0) {
                    ll_switchlayout.setVisibility(View.GONE);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "individualpage");
            jsonObject.put("description", "个人主页");
            BehaviourStatistic.uploadBehaviourInfo(jsonObject);
        } catch (Exception e) {
            MHLogUtil.e(TAG, e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        userId = UserInfoUtil.getUserId(context);
        getMineData();
        if (userWorks == null) getUserWorks();
//        ((BaseActivity)context).hiddenLoadingView();
        if (headerRecyclerViewAdapter != null)
            headerRecyclerViewAdapter.notifyDataSetChanged();
    }

    public MineFragment() {
    }

    private void initView(View rootView) {
        this.userId = getArguments().getString("userId");
        isFromMain = getArguments().getBoolean("isFromMain");
        if (MHStringUtils.isEmpty(userId)) {
            isSelf = true;
        } else {
            isSelf = UserInfoUtil.isMyself(context, userId);
        }
        pz_mine = (PullToRefreshZoomRecyclerHeaderView) rootView.findViewWithTag("pz_mine");
        rl_minetitle = (RelativeLayout) rootView.findViewById(R.id.rl_minetitle);
        v_mineblacktitle = rootView.findViewById(R.id.v_mineblacktitle);
        iv_mineback = (ImageView) rootView.findViewById(R.id.iv_mineback);
        iv_minesetting = (ImageView) rootView.findViewById(R.id.iv_minesetting);
        iv_minewallet = (ImageView) rootView.findViewById(R.id.iv_minewallet);
        iv_mineshare = (ImageView) rootView.findViewById(R.id.iv_mineshare);
//        sphi_view = (SharePersonalHomeImgView) rootView.findViewById(R.id.sphi_view);
        kv_personalHome = (KeyBoardView) rootView.findViewById(R.id.kv_personalHome);
        sphi_ = (SharePersonalHomeImgView) rootView.findViewById(R.id.sphi_view);
        fl_keboardmarsk_personalhome = (FrameLayout) rootView.findViewById(R.id.fl_keboardmarsk_personalhome);
        svaiv_personalhome = (ShareVideoAndImgView2) rootView.findViewById(R.id.svaiv_personalhome);
        et_videodetail_input = kv_personalHome.getEt();
        kv_personalHome.setOnEmojiDoListener(this);
        kv_personalHome.setOnKeyboardListener(this);
        iv_mineback.setOnClickListener(this);
        iv_mineshare.setOnClickListener(this);
        iv_minesetting.setOnClickListener(this);
        iv_minewallet.setOnClickListener(this);
        rl_minetitle.setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(iv_mineback);
        SetClickStateUtil.getInstance().setStateListener(iv_minesetting);
        SetClickStateUtil.getInstance().setStateListener(iv_minewallet);
        SetClickStateUtil.getInstance().setStateListener(iv_mineshare);
        pz_mine.setPullRefreshEnabled(false);
        pz = pz_mine.getRefreshableView();
        initHeader();
        initFooter();
        tv_mineeditdata.setEnabled(false);
        llm = new LinearLayoutManager(context);
        glm = new GridLayoutManager(context, 3);

        //默认设置为空数据页
        pz.setLayoutManager(llm);
//        data = new ArrayList<>();
//        UserWork userWork = new UserWork();
//        userWork.setContent_type(0);
//        userWork.setLoading(true);
//        data.add(userWork);
        listAdapter = new MineListAdapter(this, data, 0, mediaPlayer, this);
        headerRecyclerViewAdapter = new HeaderRecyclerViewAdapter(listAdapter);
        headerRecyclerViewAdapter.addHeaderView(headerView);
        headerRecyclerViewAdapter.addFooterView(footerView);
        if(!isSelf){
            tv_mineempty.setVisibility(View.VISIBLE);
            tv_minestartmiaohi.setVisibility(View.GONE);
        }
        pz.setAdapter(headerRecyclerViewAdapter);
        listAdapter.setMyself(isSelf);
        pz.addItemDecoration(new DividerGridItemDecoration(context));
//        pz.setHeaderImage(R.drawable.minebg);
        iv_mineshare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MHLogUtil.i("分享", "share()");
                share();
            }
        });
        pz.setOnPullZoomListener(new PullZoomRecyclerView.OnPullZoomCallback() {
            @Override
            public int[] getLocation() {
                if (rl_minetitle == null) return new int[]{0, 0};
                int[] locationTitleBar = new int[2];
                rl_minetitle.getLocationOnScreen(locationTitleBar);
                return locationTitleBar;
            }

            @Override
            public void callback(float percent) {
                v_mineblacktitle.setAlpha(percent);
            }
        });
        pz.setOnRefreshListener(new PullZoomRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page_index = 0;
                getMineData();
                getUserWorks();
            }
        });
        pz_mine.setPullLoadEnabled(true);
        pz_mine.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<PullZoomRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<PullZoomRecyclerView> refreshView) {
                page_index = 0;
                getMineData();
                getUserWorks();
            }

            @Override
            public void onLoadMore() {
                getUserWorks();
            }
        });

        fl_keboardmarsk_personalhome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                kv_personalHome.hideKeyboard(context);
                kv_personalHome.hideLayout();
                kv_personalHome.tv_videodetail_send.setText("发送");
                kv_personalHome.closeAllBoard();
                fl_keboardmarsk_personalhome.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((OnHiddenTabBar) getActivity()).hiddenTabBar(true);
                    }
                }, 200);
                return true;
            }
        });
//        getMineData();
//        getUserWorks();
    }

    /**
     * 初始化头部
     */
    private void initHeader() {
        headerView = View.inflate(context, R.layout.mineheader, null);
        fl_mineheadercontainer = (FrameLayout) headerView.findViewById(R.id.fl_mineheadercontainer);
        iv_minebg = (ImageView) headerView.findViewById(R.id.iv_headerbg);
        iv_mineheader_grid = (ImageView) headerView.findViewById(R.id.iv_mineheader_grid);
        iv_mineheader_list = (ImageView) headerView.findViewById(R.id.iv_mineheader_list);
        mcv_minehead = (MyCircleView) headerView.findViewById(R.id.mcv_minehead);
        tv_header_name = (TextView) headerView.findViewById(R.id.tv_header_name);
        iv_minevip = (ImageView) headerView.findViewById(R.id.iv_minevip);
        tv_header_describe = (TextView) headerView.findViewById(R.id.tv_header_describe);
        tv_mineheader_contentcount = (TextView) headerView.findViewById(R.id.tv_mineheader_contentcount);
        tv_mineheader_attentioncount = (TextView) headerView.findViewById(R.id.tv_mineheader_attentioncount);
        tv_mineheader_fanscount = (TextView) headerView.findViewById(R.id.tv_mineheader_fanscount);
        tv_mineeditdata = (TextView) headerView.findViewById(R.id.tv_mineeditdata);
        tv_minemyqa = (TextView) headerView.findViewById(R.id.tv_minemyqa);
        tv_mineattention = (TextView) headerView.findViewById(R.id.tv_mineattention);
        tv_itsmyqa = (TextView) headerView.findViewById(R.id.tv_itsmyqa);
        ll_switchlayout = (LinearLayout) headerView.findViewById(R.id.ll_switchlayout);
        headerView.findViewById(R.id.ll_mineheader_attention).setOnClickListener(this);
        headerView.findViewById(R.id.ll_mineheader_fans).setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(headerView.findViewById(R.id.ll_mineheader_attention));
        SetClickStateUtil.getInstance().setStateListener(headerView.findViewById(R.id.ll_mineheader_fans));

        iv_mineheader_grid.setOnClickListener(this);
        iv_mineheader_list.setOnClickListener(this);
        tv_mineattention.setOnClickListener(this);
        tv_mineeditdata.setOnClickListener(this);
        tv_minemyqa.setOnClickListener(this);
        tv_itsmyqa.setOnClickListener(this);
        tv_itsmyqa.setSelected(false);
        mcv_minehead.setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(iv_mineheader_grid);
        SetClickStateUtil.getInstance().setStateListener(iv_mineheader_list);
        //点击tab进入
        if (isFromMain) {
            iv_mineback.setVisibility(View.GONE);
            iv_minesetting.setVisibility(View.VISIBLE);
            iv_minewallet.setVisibility(View.VISIBLE);
            tv_mineeditdata.setVisibility(View.VISIBLE);
            tv_minemyqa.setVisibility(View.VISIBLE);
            tv_mineattention.setVisibility(View.GONE);
            tv_itsmyqa.setVisibility(View.GONE);
        } else {
            iv_mineback.setVisibility(View.VISIBLE);
            iv_minesetting.setVisibility(View.GONE);
            iv_minewallet.setVisibility(View.GONE);
            tv_mineeditdata.setVisibility(View.GONE);
            tv_mineattention.setVisibility(View.GONE);
            if (isSelf) {
                tv_mineeditdata.setVisibility(View.VISIBLE);
                tv_minemyqa.setVisibility(View.VISIBLE);
            } else {
                tv_mineeditdata.setVisibility(View.GONE);
                tv_minemyqa.setVisibility(View.GONE);
            }
        }

        iv_mineheader_grid.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == LIST) {
                    status = GRID;
                    iv_mineheader_grid.setImageResource(R.drawable.minegrid);
                    iv_mineheader_list.setImageResource(R.drawable.minelistselected);
                    listAdapter.setListStyle(status);
                    if (data == null
                            || data.isEmpty() || (data.size() == 1 && data.get(0).getContent_type() == 0)) {
                        return;
                    }
                    int offset = 0;
                    int currentPosition = llm.findFirstVisibleItemPosition();
                    View view = pz.getChildAt(currentPosition);
                    if (view != null) {
                        offset = view.getTop();
                    }
                    pz.setLayoutManager(glm);
                    headerRecyclerViewAdapter.adjustSpanSize(pz);
//                    pz.adjustSpanSize();
                    headerRecyclerViewAdapter.notifyDataSetChanged();
                    glm.scrollToPositionWithOffset(currentPosition, offset);
                    TCAgent.onEvent(context, "个人主页9宫格" + ConstantsValue.android);
                }
            }
        });
        iv_mineheader_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == GRID) {
                    status = LIST;
                    iv_mineheader_grid.setImageResource(R.drawable.minegridselected);
                    iv_mineheader_list.setImageResource(R.drawable.minelist);
                    if (data == null
                            || data.isEmpty() || (data.size() == 1 && data.get(0).getContent_type() == 0)) {
                        return;
                    }
                    listAdapter.setListStyle(status);
                    int offset = 0;
                    int currentPosition = glm.findFirstVisibleItemPosition();
                    View view = pz.getChildAt(currentPosition);
                    if (view != null) {
                        offset = view.getTop();
                    }
                    pz.setLayoutManager(llm);
                    pz.setAdapter(headerRecyclerViewAdapter);
                    headerRecyclerViewAdapter.notifyDataSetChanged();
                    llm.scrollToPositionWithOffset(currentPosition, offset);
                    TCAgent.onEvent(context, "个人主页列表样式" + ConstantsValue.android);
                }
            }
        });
        iv_minebg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelf) return;
                if (selectBgDialog == null) {
                    List<FadeOutFromBottomItem> items = new ArrayList<>();
                    selectBgDialog = new DialogFadeOutFromBottom(getActivity());
                    FadeOutFromBottomItem item1 = new FadeOutFromBottomItem("#00a2ff", "拍照上传");
                    FadeOutFromBottomItem item2 = new FadeOutFromBottomItem("#00a2ff", "相册选择");
                    items.add(item1);
                    items.add(item2);
                    selectBgDialog.setItem(items);
                    selectBgDialog.setOnSelectItemListener(new DialogFadeOutFromBottom.OnSelectItem() {
                        @Override
                        public void select(int index) {
                            switch (index) {
                                case 0:
                                    doTakePhoto();
                                    selectBgDialog.dismiss();
                                    break;
                                case 1:
                                    Crop.pickImage(getActivity());
                                    selectBgDialog.dismiss();
                                    break;
                            }
                        }
                    });
                }
                selectBgDialog.show();

            }
        });
    }

    /**
     * 初始化footer 无作品时使用
     */
    private void initFooter() {
        footerView = View.inflate(context, R.layout.item_mineempty, null);
        tv_mineempty = (TextView) footerView.findViewById(R.id.tv_mineempty);
        tv_minestartmiaohi = (TextView) footerView.findViewById(R.id.tv_minestartmiaohi);
        View rl_footer = footerView.findViewById(R.id.rl_footer);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height = DensityUtil.dip2px(context, 257);
        lp.width = ScreenUtils.getScreenWidth(context);
        rl_footer.setLayoutParams(lp);
        tv_minestartmiaohi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!context.isLogin(false)) return;
                TCAgent.onEvent(context, "拍摄总点击数" + ConstantsValue.android);
                if ("x86".equalsIgnoreCase(Build.CPU_ABI)) {
                    context.showToastAtCenter("您的手机暂不支持");
                    return;
                }
                Intent intent = new Intent(context, VideoRecorderActivity.class);
                context.startActivity(intent);
                context.overridePendingTransition(0, 0);
                context.overridePendingTransition(R.anim.slide_bottom_out, 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mineheader_grid://切换到网格

                break;
            case R.id.iv_mineheader_list://切换到列表

                break;
            case R.id.ll_mineheader_attention://关注
                if (isSelf) {
                    startActivity(new Intent(context, UserListActivity.class).
                            putExtra("type", UserListActivity.USER_LIST_TYPE_ATTENTION));
                } else {
                    startActivity(new Intent(context, CommonUserListActivity.class).
                            putExtra("type", UserListActivity.USER_LIST_TYPE_ATTENTION).
                            putExtra("user_id", userId));
                }

                break;
            case R.id.ll_mineheader_fans://粉丝
                if (isSelf) {
                    startActivity(new Intent(context, UserListActivity.class).
                            putExtra("type", UserListActivity.USER_LIST_TYPE_FANS));

                } else {
                    startActivity(new Intent(context, CommonUserListActivity.class).
                            putExtra("type", UserListActivity.USER_LIST_TYPE_FANS).
                            putExtra("user_id", userId));
                }
                break;
            case R.id.tv_mineeditdata://编辑资料
                context.startActivity(new Intent(context, MineDataActivity.class)
                        .putExtra("isNormal", 0)
                        .putExtra("answer_auth", userInfo.isAnswer_auth()));
                break;
            case R.id.tv_minemyqa://我的映答
                context.startActivity(new Intent(context, MyQaActivity.class));
                break;
            case R.id.iv_minesetting://设置
                context.startActivity(new Intent(context, MineSettingActivity.class));
                break;
            case R.id.iv_mineback://返回
                context.finish();
                break;
            case R.id.iv_mineshare://分享
                share();
                break;
            case R.id.iv_minewallet://钱包
                context.startActivity(new Intent(context, WalletActivity.class));
                break;
            case R.id.tv_itsmyqa://她的映答
                userInfo.setUserId(userId);
                startActivity(new Intent(context, QAHomeActivity.class)
                        .putExtra("userId", userId)
                        .putExtra("userName", userInfo.getUser_name()));
                break;
            case R.id.tv_mineattention://别人的个人主页关注
                if (userInfo == null) return;
                if (userInfo.isAttention_state()) {
                    //提示框确认是否取消关注
                    final CommonDialog commonDialog = new CommonDialog(getContext());
                    commonDialog.setContentMsg("确定要取消关注吗？");
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
                            attentionOrCancle();
                            commonDialog.dismiss();
                        }
                    });
                    commonDialog.show();
                } else {
                    attentionOrCancle();
                }
                break;

            case R.id.mcv_minehead:
                //点击头像
                if (userInfo != null) {
                    ((OnShowPicture) (getActivity())).onShowPicture(userInfo.getPortrait_uri());
                }
                break;

        }

    }

    public View getTitleView() {
        return rl_minetitle;
    }

    public void setEnptyView(boolean isEnpty) {
        pz.setEnptyView(isEnpty);
    }

    /**
     * 获取人信息
     */
    public void getMineData() {
        if (!UserInfoUtil.isLogin()) return;
        MHRequestParams params = new MHRequestParams();
        params.addParams("user_id", userId);
        //TODO UserInfoResponse1 改名
        MHHttpClient.getInstance().post(UserInfoResponse1.class, context, ConstantsValue.Url.GET_USERINFO, params, new MHHttpHandler<UserInfoResponse1>() {
            @Override
            public void onSuccess(UserInfoResponse1 response) {
                userInfo = response.getData();
                if (userInfo == null) return;
                isSelf = UserInfoUtil.isMyself(context, userId);
                listAdapter.setShareData(userInfo.getUser_name(), userInfo.getPortrait_uri(), svaiv_personalhome);
                tv_mineeditdata.setEnabled(true);
                ImageLoader.getInstance().displayImage(userInfo.getPortrait_uri(), mcv_minehead, DisplayOptionsUtils.getHeaderDefaultImageOptions());
                ImageLoader.getInstance().displayImage(userInfo.getBackground_uri(), iv_minebg, DisplayOptionsUtils.getSilenceDisplayMineBgBuilder());
                tv_header_name.setVisibility(View.VISIBLE);
                tv_header_name.setText(userInfo.getUser_name());
                tv_header_describe.setText(userInfo.getUser_authentic());
                tv_mineheader_contentcount.setText(userInfo.getContent_count() + "");
                tv_mineheader_attentioncount.setText(userInfo.getAttention_count() + "");
                tv_mineheader_fanscount.setText(userInfo.getFans_count() + "");
                if (!isSelf) {
                    //是否是自己
                    tv_mineattention.setVisibility(View.VISIBLE);
                    tv_mineeditdata.setVisibility(View.GONE);
//                    tv_itsmyqa.setVisibility(View.VISIBLE);
                    tv_mineattention.setSelected(userInfo.isAttention_state());
                    MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(userInfo.isAttention_state() + "");
                    if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
                        userInfo.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
                    }
                    if (userInfo.isAttention_state()) {
                        tv_mineattention.setText("已关注");
                        tv_mineattention.setTextColor(Color.parseColor("#c4c4c4"));
                    } else {
                        tv_mineattention.setText("关注");
                        tv_mineattention.setTextColor(Color.parseColor("#00a2ff"));
                    }
                } else {
                    if (MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.PORTRAIT_URI)))
                        SpUtils.put(ConstantsValue.Sp.PORTRAIT_URI, userInfo.getPortrait_uri());
                    tv_mineattention.setVisibility(View.GONE);
                    tv_mineeditdata.setVisibility(View.VISIBLE);
                }
                if (userInfo.isAnswer_auth()) {
                    //有映答权限
                    if (isSelf) {
                        tv_minemyqa.setVisibility(View.VISIBLE);
                        tv_itsmyqa.setVisibility(View.GONE);
                    } else {
                        tv_itsmyqa.setVisibility(View.VISIBLE);
                        tv_minemyqa.setVisibility(View.GONE);
                    }
                } else {
                    //无应答权限
                    if (isSelf) {
//                        tv_minemyqa.setVisibility(View.GONE);
                        tv_itsmyqa.setVisibility(View.GONE);
                        tv_minemyqa.setVisibility(View.VISIBLE);
                    } else {
                        tv_itsmyqa.setVisibility(View.GONE);
                        tv_minemyqa.setVisibility(View.GONE);
                    }
                }
                //是否有认证信息
                if (MHStringUtils.isEmpty(userInfo.getUser_authentic())) {
                    tv_header_describe.setVisibility(View.GONE);
                } else {
                    tv_header_describe.setVisibility(View.VISIBLE);
                    tv_header_describe.setText(userInfo.getUser_authentic());
                }
                //是否是大咖
                iv_minevip.setVisibility(userInfo.getUser_type() > 10 ? View.VISIBLE : View.GONE);

                Drawable genderDrawable = null;
                //性别
                if (userInfo.getUser_gender() == 2) {//女
                    genderDrawable = context.getResources().getDrawable(R.drawable.gender_women);
                    tv_itsmyqa.setText("她的映答");
                } else {//男
                    genderDrawable = context.getResources().getDrawable(R.drawable.gender_man);
                    tv_itsmyqa.setText("他的映答");
                }
                genderDrawable.setBounds(0, 0, genderDrawable.getMinimumWidth(), genderDrawable.getMinimumHeight());
                tv_header_name.setCompoundDrawables(null, null, genderDrawable, null);

            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    /**
     * 获取用户作品
     */
    public void getUserWorks() {
        if (!UserInfoUtil.isLogin()) return;
        MHRequestParams params = new MHRequestParams();
        if (isFromMain) {
            params.addParams("user_id", UserInfoUtil.getUserId(context));
        } else {
            params.addParams("user_id", userId);
        }
        params.addParams("page_index", String.valueOf(page_index));
        params.addParams("page_size", "30");
        MHHttpClient.getInstance().post(UserWorksResponse.class, context, ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS, params, new MHHttpHandler<UserWorksResponse>() {
            @Override
            public void onSuccess(UserWorksResponse response) {
                userWorks = response.getData().getPage_result();
                pz_mine.hideAllTipView();
                setData(userWorks);
                if (null == userWorks || userWorks.size() == 0)
                    pz_mine.onLoadComplete(false);
                else {
                    pz_mine.onLoadComplete(true);
                }
                if (!userWorks.isEmpty()) {
                    page_index++;
                }
            }

            @Override
            public void onFailure(String content) {
                pz_mine.onLoadComplete(true);
                if (page_index == 0) ll_switchlayout.setVisibility(View.GONE);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                pz_mine.onLoadComplete(true);
                if (page_index == 0) ll_switchlayout.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 上传个人背景
     *
     * @param bgUrl
     */
    public void uploadMyBgToServer(String bgUrl) {
        MHRequestParams params = new MHRequestParams();
        params.addParams("img_uri", bgUrl);
        MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.UPLOADBACKGROUNDIMG, params, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                iv_minebg.setImageDrawable(null);
                iv_minebg.setImageURI(resultUri);
            }

            @Override
            public void onFailure(String content) {
                ToastUtils.showToastAtCenter(context, "保存失败");
                ((BaseActivity) getActivity()).hiddenLoadingView();
            }
        });
    }

    private void setData(List<UserWork> pageData) {
        if (page_index == 0) {
//            if (null == pageData || pageData.size() == 0) {
//                pz_mine.showBlankView();
//                return;
//            }
            pz_mine.hideAllTipView();
            if (pageData == null) return;
            if (pageData.isEmpty()) {
                ll_switchlayout.setVisibility(View.GONE);
                if (data.isEmpty()) {
                    data.clear();
//                    UserWork userWork = new UserWork();
//                    userWork.setLoading(false);
//                    userWork.setContent_type(0);
//                    data.add(new UserWork());
//                    listAdapter.notifyDataSetChanged();
                    showEmptyView();
                }

                return;
            } else {
                ll_switchlayout.setVisibility(View.VISIBLE);
            }
//            if (data.size() == 1
//                    && data.get(0).getContent_type() == 0
//                    && !pageData.isEmpty()
//                    && listAdapter != null) {
//                data.clear();
//                listAdapter.notifyDataSetChanged();
//            }
            data.clear();
            data.addAll(pageData);
            headerRecyclerViewAdapter.removeFooterView();
            if (status == GRID) {
                pz.setLayoutManager(glm);
                headerRecyclerViewAdapter.adjustSpanSize(pz);
//                pz.adjustSpanSize();
            } else if (status == LIST) pz.setLayoutManager(llm);
            if (null == headerRecyclerViewAdapter) {
                listAdapter = new MineListAdapter(this, data, status, mediaPlayer, this);
                headerRecyclerViewAdapter = new HeaderRecyclerViewAdapter(listAdapter);
                headerRecyclerViewAdapter.addHeaderView(headerView);
                pz.setAdapter(headerRecyclerViewAdapter);
            } else {
                headerRecyclerViewAdapter.notifyDataSetChanged();

            }
        } else {
            pz_mine.hideAllTipView();
            if (null != pageData && pageData.size() > 0) {
                data.addAll(pageData);
                headerRecyclerViewAdapter.notifyDataSetChanged();
                headerRecyclerViewAdapter.removeFooterView();
            }
        }
    }

    /**
     * 拍照获取相片
     */
    private void doTakePhoto() {
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
        // 下面这句指定调用相机拍照后的照片存储的路径
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, PHOTO_WITH_CAMERA);// CAMERA_OK是用作判断返回结果的标识
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0)
            return;
        if (requestCode == Crop.REQUEST_PICK && resultCode == getActivity().RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        } else if (requestCode == PHOTO_WITH_CAMERA) {
            beginCrop(imageUri);
        }

        if (data != null) {
            if (resultCode == 102) {
                if (null != kv_personalHome && null != kv_personalHome.getEt()) {
                    kv_personalHome.getEt().requestFocus();
                    openKeyBord(kv_personalHome.getEt(), context);
                }
                if (null != pz_mine)
                    pz_mine.resetFooterLayout();

                namesByat = data.getStringArrayListExtra("nameList");
                userIdByat = data.getStringArrayListExtra("userIds");
                if (null == namesByat)
                    namesByat = new ArrayList<>();
                if (null == userIdByat)
                    userIdByat = new ArrayList<>();

                if (namesByat == null) return;
                if (notify_user_results == null)
                    notify_user_results = new ArrayList<>();
                for (int i = 0; i < namesByat.size(); i++) {
                    Notify_user_result notify_user_result = new Notify_user_result();
                    notify_user_result.setNotify_user_id(userIdByat.get(i));
                    notify_user_result.setNotify_user_name(namesByat.get(i).trim().replace("@", ""));
                    notify_user_results.add(notify_user_result);
                }
                if (null == et_videodetail_input) return;

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
                        MineFragment.this.textInfos = textInfos;
                    }
                }));
            }
        }
    }


    /**
     * 开始剪裁
     *
     * @param source
     */
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getActivity());
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == getActivity().RESULT_OK) {
            resultUri = Crop.getOutput(result);
            filePath = Crop.getOutput(result).getPath();
            ((BaseActivity) getActivity()).showLoading();
            //获取token
            GetToken.getToken(context, new GetToken.IGetToken() {
                @Override
                public void getResult(boolean isOk) {
                    uploadBg();
                }
            });
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadBg() {
        UploadManager uploadManager = new UploadManager();
        String token = SpUtils.getString(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_ICON);
        String key = getHeaderName();
        final String imgUrl = "http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, "") + "/" + key;
        if (MHStringUtils.isEmpty(filePath)) {
            ToastUtils.showToastAtCenter(context, "保存失败");
        } else {
            uploadManager.put(filePath, key, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        uploadMyBgToServer(imgUrl);
                    } else {
                        ToastUtils.showToastAtCenter(context, "保存失败");
                        ((BaseActivity) getActivity()).hiddenLoadingView();
                    }
                }
            }, null);
        }
    }

    private String getHeaderName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date(System.currentTimeMillis()));
        return "myIcon_" + time;
    }

    /**
     * 分享
     */
    public void share() {
        if (userInfo == null) return;
        isSelf = UserInfoUtil.isMyself(context, userId);
        final ShareDialog shareDialog = new ShareDialog((BaseActivity) getActivity());
        shareDialog.setData();
        if (MHStringUtils.isEmpty(userInfo.getShare_link_address())) {
            shareDialog.setShareLink(ConstantsValue.Shared.APPDOWNLOAD);
        } else {
            shareDialog.setShareLink(userInfo.getShare_link_address());
        }
        shareDialog.setShareLable(ShareDialog.IMG);
        if (!isSelf) {
            //不是自己
            ShareImg shareImg = new ShareImg(getActivity(), userId, "", "", "", "");
            shareImg.setShowDelete(true);
            shareImg.setDeleteBtnType(ShareLayout.PEOPLE_REPORT);
            shareDialog.setShareInfo(shareImg);
        }
        shareDialog.setOnShareImgPath(new ShareLayout.OnShareImgPath() {
            @Override
            public void getimgPath(final SHARE_MEDIA platform) {
                shareDialog.dismiss();
                ((BaseActivity) context).showLoading();
                sphi_.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                    @Override
                    public void onFinish(Object path) {
                        UmengShare.sharedIMG(getActivity(), platform, path, userInfo.getShare_link_address(), getResources().getString(R.string.share_personalHome), new IUMShareResultListener((BaseActivity) context));
                    }
                });
                sphi_.genderImg((BaseActivity) context, userInfo, data, userInfo.getShare_link_address(), platform);
                MHLogUtil.i("");
                sphi_.addData();
            }
        });

    }

    @Override
    public void onStartCommont(int postion) {
        hasShowKeyBoard = true;
        UserWork userWork = data.get(postion);
        String commentId = null;
        if (userWork.getContent_type() == VIDEO) {
            commentId = userWork.getVideo_id();
        } else {
            commentId = userWork.getPhoto_id();
        }
        if (!TextUtils.equals(commentId, lastCommentId)) {
            et_videodetail_input.setText("");
            lastCommentId = commentId;
        }
        currentCommonPosition = postion;
        if (null != textInfos) {
            textInfos.clear();
        }
        if (null != namesByat) {
            namesByat.clear();
        }
        if (null != userIdByat) {
            userIdByat.clear();
        }

        et_videodetail_input.requestFocus();
        pz_mine.resetFooterLayout();
        openKeyBord(kv_personalHome.getEt(), context);
    }

    /**
     * 发送评论
     */
    private void sendComment(final UserWork userWork) {
        kv_personalHome.setKeyboardEnable(false);
        MHRequestParams requestParams = new MHRequestParams();
        String commented_id = null;
        String rootType = null;
        if (userWork.getContent_type() == VIDEO) {
            //视频
            commented_id = userWork.getVideo_id();
            rootType = "2";
            requestParams.addParams("root_uri", userWork.getVideo_uri());
            requestParams.addParams("root_cover_uri", userWork.getVideo_cover_uri());
        } else if (userWork.getContent_type() == IMG) {
            //图片
            commented_id = userWork.getPhoto_id();
            rootType = "4";
        }
        requestParams.addParams("commented_id", commented_id);
        requestParams.addParams("commented_type", rootType);
        if (isFromMain) {
            requestParams.addParams("commented_user", UserInfoUtil.getUserId(context));
        } else {
            requestParams.addParams("commented_user", userId);
        }
        requestParams.addParams("root_id", commented_id);
        requestParams.addParams("root_type", rootType);
        requestParams.addParams("root_user", userId);
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

        final String finalCommented_id = commented_id;
        MHHttpClient.getInstance().post(SendCommentResponse.class, context, ConstantsValue.Url.DOCOMMENT, requestParams, new MHHttpHandler<SendCommentResponse>() {
            @Override
            public void onSuccess(SendCommentResponse response) {
                EventBus.getDefault().post(new CloseEmojiKeyboard());
                kv_personalHome.reset();
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
//                if(userWork.getComment_list().size()<3){
//                }
                Comment comment = new Comment();
                comment.setComment_text(et_videodetail_input.getText().toString());
                comment.setComment_id(videoDetailUserCommentBean.getComment_id());
                comment.setComment_user_id(userId);
                comment.setComment_user_name(videoDetailUserCommentBean.getComment_user_name());
                if (notify_user_results == null) {
                    comment.setNotify_user_result(new ArrayList<Notify_user_result>());
                } else {
                    comment.setNotify_user_result(notify_user_results);
                }
                userWork.getComment_list().add(0, comment);
                userWork.setComments_count(userWork.getComments_count() + 1);
                //将该条评论存入同步池
                VideoDetailUserCommentBean bean = new VideoDetailUserCommentBean();
                bean.setComment_text(comment.getComment_text());
                bean.setComment_id(comment.getComment_id());
                bean.setComment_user_id(comment.getComment_user_id());
                bean.setComment_user_name(comment.getComment_user_name());
                bean.setNotify_user_result(comment.getNotify_user_result());
                MHContentSyncUtil.addContentSync(finalCommented_id, bean);
                //重置文本框
                et_videodetail_input.setText("");
                headerRecyclerViewAdapter.notifyDataSetChanged();
                if (notify_user_results != null) notify_user_results.clear();
                if (namesByat != null) namesByat.clear();
                if (userIdByat != null) userIdByat.clear();
//                tv_videodetail_totalcomment.setText("共" + MHStringUtils.countFormat(userWork.getPraise_count()) + "条评论");
                EventBus.getDefault().post(new RefreshCommentCountEvent(userWork.getComments_count(), userWork.getVideo_id()));
                Intent intent = new Intent(ConstantsValue.IntentFilterAction.VIDEO_COMMIT_COUNT_ACTION);
                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_COMMIT_COUNT_KEY, userWork.getComments_count());
                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_ID_KEY, userWork.getUser_id());
                context.sendBroadcast(intent);
                kv_personalHome.setKeyboardEnable(true);
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        positionFromCommentList();
//                    }
//                }, 200);
            }

            @Override
            public void onFailure(String content) {
                kv_personalHome.setKeyboardEnable(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                kv_personalHome.setKeyboardEnable(true);

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

    //打开软键盘
    public static void openKeyBord(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    /**
     * 软键盘全部打开
     */
    @Override
    public void onOpen() {
        if (!hasShowKeyBoard) return;
        try {
            kv_personalHome.setVisibility(View.VISIBLE);
//            if(bottomView!=null) bottomView.setVisibility(View.GONE);
            ((OnHiddenTabBar) getActivity()).hiddenTabBar(false);
            fl_keboardmarsk_personalhome.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(fl_keboardmarsk_personalhome, "alpha", 0, 0.3f)
                    .setDuration(200)
                    .start();
        } catch (Exception e) {
            MHLogUtil.e(TAG, e);
        }
    }

    /**
     * 软键盘全部关闭
     */
    @Override
    public void onClose() {
        if (!hasShowKeyBoard) return;
        try {
            kv_personalHome.setVisibility(View.GONE);
//        if(bottomView!=null) bottomView.setVisibility(View.VISIBLE);
            ((OnHiddenTabBar) getActivity()).hiddenTabBar(true);
            fl_keboardmarsk_personalhome.setVisibility(View.GONE);
        } catch (Exception e) {
            MHLogUtil.e(TAG, e);
        }
    }

    @Override
    public void onAtFriend() {
        Intent intent = new Intent(context, UserListActivity.class);                    //@好友界面
        intent.putExtra("type", UserListActivity.USER_LIST_TYPE_AT_FRIENDS);
        startActivityForResult(intent, UserListActivity.AT_FRIENDS_REQUEST_CODE);
    }

    @Override
    public void onSendComment() {
        sendComment(data.get(currentCommonPosition));
    }


    @Override
    public void onTextChangeAfter(Editable s) {

    }

    @Override
    public void onTextChangeBefore(CharSequence s, int start, int count, int after) {

    }

    /**
     * 跳转到详情页
     *
     * @param position
     */
    @Override
    public void onSkip(int position) {
        context.startActivity(new Intent(context, VideoAndImgActivity.class)
                .putParcelableArrayListExtra("data", data)
                .putExtra("currentIndex", position)
                .putExtra("userId", userId)
                .putExtra("pageIndex", page_index)
                .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS));
    }

    /**
     * 关注
     */
    public void attentionOrCancle() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("action_mark", !userInfo.isAttention_state() + "");
        requestParams.addParams("user_id", userId);
        MHHttpClient.getInstance().post(HomeFoundResponse.class, context, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<HomeFoundResponse>() {
            @Override
            public void onSuccess(HomeFoundResponse response) {
                userInfo.setAttention_state(!userInfo.isAttention_state());
                tv_mineattention.setSelected(userInfo.isAttention_state());
                if (userInfo.isAttention_state()) {
                    tv_mineattention.setText("已关注");
                    tv_mineattention.setTextColor(Color.parseColor("#c4c4c4"));
                } else {
                    tv_mineattention.setText("关注");
                    tv_mineattention.setTextColor(Color.parseColor("#00a2ff"));
                }
                MHStateSyncUtil.pushSyncEvent(context, userInfo.getUser_id(), userInfo.isAttention_state());
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

    public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

        //系统theme.xml中,可以自定义
        private final int[] ATTRS = new int[]{android.R.attr.listDivider};
        private Drawable mDivider;

        public DividerGridItemDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
//            mDivider = a.getDrawable(0);
            mDivider = getResources().getDrawable(R.drawable.shape_minedivider);
            a.recycle();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            drawHorizontal(c, parent);
            drawVertical(c, parent);

        }

        private int getSpanCount(RecyclerView parent) {
            // 列数
            int spanCount = -1;
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {

                spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                spanCount = ((StaggeredGridLayoutManager) layoutManager)
                        .getSpanCount();
            }
            return spanCount;
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (i < 2) return;
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getLeft() - params.leftMargin;
                final int right = child.getRight() + params.rightMargin
                        + mDivider.getIntrinsicWidth();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);

                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getTop() - params.topMargin;
                final int bottom = child.getBottom() + params.bottomMargin;
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicWidth();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                    int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                    {
                        return true;
                    }
                } else {
                    childCount = childCount - childCount % spanCount;
                    if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                        return true;
                }
            }
            return false;
        }

        private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                                  int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                    return true;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                // StaggeredGridLayoutManager 且纵向滚动
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    childCount = childCount - childCount % spanCount;
                    // 如果是最后一行，则不需要绘制底部
                    if (pos >= childCount)
                        return true;
                } else
                // StaggeredGridLayoutManager 且横向滚动
                {
                    // 如果是最后一行，则不需要绘制底部
                    if ((pos + 1) % spanCount == 0) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition,
                                   RecyclerView parent) {
            int spanCount = getSpanCount(parent);
            int childCount = parent.getAdapter().getItemCount();
            if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
            {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
            {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(),
                        mDivider.getIntrinsicHeight());
            }
        }

    }

    public interface OnHiddenTabBar {
        void hiddenTabBar(boolean isShow);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && isFromMain) {
            userId = UserInfoUtil.getUserId(context);
            getMineData();
//            getUserWorks();
        }
        if (hidden && null != mediaPlayer && mediaPlayer.isPlaying())
            mediaPlayer.pause();

        if (!hidden && null != listAdapter)
            headerRecyclerViewAdapter.notifyDataSetChanged();
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
     * 删除视频录制目录
     */
    private void deleteVideoRecordDir() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //表示草稿箱没有数据,这时候就把视频录制目录下的东西全部删除
                if (DraftUtil.hasDraft(getContext().getApplicationContext())) {
                    MHLogUtil.d(TAG, "草稿箱有东西,不做删除文件处理");
                    return;
                }
                long lastDeleteVideoRecordDir = SpUtils.getLong(ConstantsValue.Sp.LAST_DELETE_VIDEO_RECORD_DIR, 0);
                if (System.currentTimeMillis() - lastDeleteVideoRecordDir > 24 * 60 * 60 * 1000) {//大于一天了处理删除逻辑
                    MHLogUtil.d(TAG, "草稿箱没有东西,并且上次处理时间超过一天了--删除文件");
                    try {
                        FileUtils.deleteDir(new File(ConstantsValue.Video.ROOT_PATH));
                    } catch (Exception e) {
                        MHLogUtil.e(TAG, e);
                    }
                    SpUtils.put(ConstantsValue.Sp.LAST_DELETE_VIDEO_RECORD_DIR, System.currentTimeMillis());
                } else {
                    MHLogUtil.d(TAG, "草稿箱没有东西,上次处理时间不超过一天");
                }
            }
        }).start();
    }

    /**
     * 接受登录广播
     */
    public void registLoginReceiver() {
        receiveLogin = new ReceiveLogin();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsValue.IntentFilterAction.LOGIN_SUCCESS_ACTION);
        context.registerReceiver(receiveLogin, intentFilter);
    }

    public class ReceiveLogin extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            page_index = 0;
            pz.smoothScrollToPosition(0);
            getMineData();
            getUserWorks();
        }
    }

    @Override
    protected void onMyResume() {
        super.onMyResume();
        if (null != listAdapter) headerRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiveLogin != null)
            context.unregisterReceiver(receiveLogin);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateEvent(RefreshUploadEvent event) {
        VideoUploadInfo task = event.getTask();
        //视频或者图片上传成功是刷新作品
        if (task != null
                && isSelf
                && (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE
                || task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO)
                && task.getUploadState() == VideoUploadInfo.UPLOAD_SUCCESS) {
            getUserWorks();
        }
    }

    /**
     * 接收开通映答消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateCountEvent(OpenQAEvent event) {
        tv_minemyqa.setVisibility(View.VISIBLE);
        if (userInfo != null) userInfo.setAnswer_auth(true);
        getMineData();
    }

    /**
     * 接受删除消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateEvent(DeleteVideoAndImgAsyncEvent event) {
//        if(event.getFromType() != ConstantsValue.Other.DELETEVIDEOANDIMG_FROMAPERSONALHOME){
//
//        }
        if (data == null) return;
        for (int i = 0; i < data.size(); i++) {
            UserWork userWork = data.get(i);
            if (event.getContentType() == userWork.getContent_type()) {
                if (event.getContentType() == VIDEO) {
                    if (TextUtils.equals(userWork.getVideo_id(), event.getTargetId())) {
                        if (data.size() == 1) {
                            data.get(i).setContent_type(0);
                            data.get(i).setLoading(false);
                            ll_switchlayout.setVisibility(View.GONE);
                            headerRecyclerViewAdapter.notifyDataSetChanged();

                        } else {
                            if (status == GRID) {
                                data.remove(i);
                                headerRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                if (null != listAdapter) {
                                    listAdapter.remove(i);
                                    headerRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        return;
                    }
                } else if (event.getContentType() == IMG) {
                    if (TextUtils.equals(userWork.getPhoto_id(), event.getTargetId())) {
                        if (data.size() == 1) {
                            data.get(i).setContent_type(0);
                            data.get(i).setLoading(false);
                            ll_switchlayout.setVisibility(View.GONE);
                            headerRecyclerViewAdapter.notifyDataSetChanged();

                        } else {
                            if (status == GRID) {
                                data.remove(i);
                                headerRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                if (null != listAdapter) {
                                    listAdapter.remove(i);
                                    headerRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * 显示空白页
     */
    private void showEmptyView() {
        if (isSelf) {
            tv_mineempty.setVisibility(View.GONE);
            tv_minestartmiaohi.setVisibility(View.VISIBLE);
        } else {
            tv_mineempty.setVisibility(View.VISIBLE);
            tv_minestartmiaohi.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onMyPause() {
        super.onMyPause();
        if (null != mediaPlayer && mediaPlayer.isPlaying())
            mediaPlayer.pause();
//        if (null != listAdapter)
//            listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        MHLogUtil.d(TAG, "onPause");
        if (null != mediaPlayer && mediaPlayer.isPlaying())
            mediaPlayer.pause();
//        if (null != listAdapter)
//            listAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        if (null != pz)
            pz.smoothScrollToPosition(0);
        if (null != pz_mine)
            pz_mine.doPullRefreshing(true);
    }
}

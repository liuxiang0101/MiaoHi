package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.HomeStaggeredgideAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.HomeStaggeredgridData;
import com.haiqiu.miaohi.bean.HomeStaggeredgridResponse;
import com.haiqiu.miaohi.bean.HomeStaggereedgridResult;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.ActivityEvent;
import com.haiqiu.miaohi.umeng.AbsUMShare;
import com.haiqiu.miaohi.umeng.ShareSubject;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.SysMethod;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.view.ShareDialog;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.haiqiu.miaohi.widget.ShareLayout;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshStaggeredGridView;
import com.haiqiu.miaohi.widget.staggeredgridview.StaggeredGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 专题详情
 * Created by ningl on 2016/6/20.
 */
public class ActiveDetailActivity extends BaseActivity implements ShareLayout.DeleteVideoListener {
    private PullToRefreshStaggeredGridView ptsg_activedetail;//刷新控件
    private StaggeredGridView gridView;         // 瀑布流控件
    private CommonNavigation cn_activedetail;   //标题控件
    private String activeId;                    //专题id
    private String activeName;                  //专题名称
    private String activeDescribe;              //专题描述
    private String coverid;                     //专题封面url
    private int pageIndex;                      //页码
    private List<HomeStaggereedgridResult> homeStaggereedgridResults;//专题详情数据源
    private HomeStaggeredgideAdapter adapter;
    private ImageView iv_activedetail_shoot;
    private PopupWindow popupWindow;
    private TextView tv_activedetailbubble;
    private static final String ACTIVEDETAIL_BUBBLE = "activedetail_bubble";

    private ImageView ivActBg;
    private ImageView iv_gender;
    private RelativeLayout rlRoot;
    private MyCircleView mcvHomeitemHeader;
    private TextView tvActPersonName;
    private ImageView ivHomeitemVip;
    private TextView tvContent;
    private TextView tv_person_count;
    private TextView tv_video_count;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private ImageView iv_share;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TCAgent.onEvent(context, "专题详情页" + ConstantsValue.android);
        setContentView(R.layout.activity_activedetail);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            activeId = intent.getStringExtra("activityId");
            activeName = intent.getStringExtra("activity_name");
            coverid = intent.getStringExtra("coverid");
        }
        cn_activedetail.setTitle(activeName);
        homeStaggereedgridResults = new ArrayList<>();
        showMHLoading();
        getNetData();
        ptsg_activedetail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<StaggeredGridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
                pageIndex = 0;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                getNetData();
            }

        });

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "activitydetailspage");
            jsonObject.put("description", "活动专题详情页");
            if (null != activeId)
                jsonObject.put("id", activeId);
            BehaviourStatistic.uploadBehaviourInfo(jsonObject);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }


    /**
     * 标题栏设置
     */
    private void titleBarSetting(final HomeStaggeredgridData vo) {
        iv_share.setPadding(0, 0, DensityUtil.dip2px(context, 10), 0);
        iv_share.setImageResource(R.drawable.svg_more);
        cn_activedetail.setRightLayoutView(iv_share);
        cn_activedetail.showRightLayout();
        iv_share.setEnabled(true);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击分享图标，进行分享
                String str_pre = "http://app.miaohi.com/miaohih5/topic.html?id=";
                TCAgent.onEvent(context, "专题详情-分享" + ConstantsValue.android);
                if (!isLogin()) {
                    Intent intent = new Intent(ActiveDetailActivity.this, LoginActivity.class);
                    intent.putExtra("loginType", "0");
                    startActivity(intent);
                } else {
                    shareDialog = new ShareDialog(ActiveDetailActivity.this);
                    shareDialog.setData();
                    AbsUMShare umShare = new ShareSubject(ActiveDetailActivity.this
                            , activeId
                            , coverid
                            , vo.getActivity_sponsor_nickname()
                            , "subject"
                            , activeDescribe
                            , activeName);
                    shareDialog.setShareInfo(umShare);
                    shareDialog.hidenReport();
                }
            }
        });
    }


    /**
     * 初始化控件
     */
    private void initView() {
        cn_activedetail = (CommonNavigation) findViewById(R.id.cn_activedetail);

        iv_share = new ImageView(context);

        ptsg_activedetail = (PullToRefreshStaggeredGridView) findViewById(R.id.ptsg_activedetail);
        iv_activedetail_shoot = (ImageView) findViewById(R.id.iv_activedetail_shoot);
        tv_activedetailbubble = (TextView) findViewById(R.id.tv_activedetailbubble);
        gridView = ptsg_activedetail.getRefreshableView();
        ptsg_activedetail.setPullLoadEnabled(true);
        iv_activedetail_shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (!isLogin()) {
                    intent = new Intent(ActiveDetailActivity.this, LoginActivity.class);
                    intent.putExtra("loginType", "0");
                    startActivity(intent);
                } else {
                    if ("x86".equalsIgnoreCase(Build.CPU_ABI)) {
                        showToastAtCenter("您的手机暂不支持");
                        return;
                    }
                    intent = new Intent(ActiveDetailActivity.this, VideoEditActivity.class);
                    VideoUploadInfo videoUploadInfo = new VideoUploadInfo();
                    videoUploadInfo.setActivity_id(activeId);
                    videoUploadInfo.setActivity_name(activeName);
                    videoUploadInfo.setFromInfo(VideoUploadInfo.FROM_ACTIVITY);
                    intent.putExtra("videoUploadInfo", videoUploadInfo);
                    startActivityNoAnimation(intent);
                    overridePendingTransition(R.anim.slide_bottom_out, 0);
                }
                TCAgent.onEvent(context, "专题详情-参与专题" + ConstantsValue.android);
            }
        });
    }

    /**
     * 请求网络数据
     */
    private void getNetData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", "10");
        requestParams.addParams("page_index", "" + pageIndex);
        requestParams.addParams("activity_id", activeId + "");
        MHHttpClient.getInstance().post(HomeStaggeredgridResponse.class, context, ConstantsValue.Url.GET_ACTIVITY_DETAIL, requestParams, new MHHttpHandler<HomeStaggeredgridResponse>() {

            @Override
            public void onSuccess(HomeStaggeredgridResponse response) {
                hiddenLoadingView();
                HomeStaggeredgridData homeStaggeredgridData = response.getData();
                if (pageIndex == 0 && null == homeStaggeredgridData) {
                    ptsg_activedetail.showBlankView();
                } else {
                    ptsg_activedetail.hideAllTipView();
                }
                activeDescribe = response.getData().getActivity_detail_note();
                //是否有传来的专题名,没有则接口中获取
                if (MHStringUtils.isEmpty(activeName))
                    cn_activedetail.setTitle(homeStaggeredgridData.getActivity_detail_note());
                //是否显示录制按钮
                if (homeStaggeredgridData.getActivity_publish_state() == 0) {
                    iv_activedetail_shoot.setVisibility(View.VISIBLE);
                    showGuideBubble();
                } else {
                    iv_activedetail_shoot.setVisibility(View.GONE);
                }
                titleBarSetting(homeStaggeredgridData);//分析


                if (adapter == null) {
                    setHeader(homeStaggeredgridData);
                    adapter = new HomeStaggeredgideAdapter(ActiveDetailActivity.this, homeStaggereedgridResults, "");
                    gridView.setAdapter(adapter);
                }

                if (pageIndex == 0) {
                    homeStaggereedgridResults.clear();
                }
                if (homeStaggeredgridData.getPage_result().size() == 0) {
                    ptsg_activedetail.onLoadComplete(false);
                    return;
                } else {
                    pageIndex++;
                    ptsg_activedetail.onLoadComplete(true);
                }
                homeStaggereedgridResults.addAll(homeStaggeredgridData.getPage_result());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String content) {
                hiddenLoadingView();
                if (pageIndex == 0)
                    ptsg_activedetail.showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                if (pageIndex == 0)
                    ptsg_activedetail.showErrorView();
            }
        });
    }


    /**
     * 设置专题详情头部
     */
    public void setHeader(final HomeStaggeredgridData vo) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_activeheader, null);

        ivActBg = (ImageView) v.findViewById(R.id.iv_act_bg);
        rlRoot = (RelativeLayout) v.findViewById(R.id.rl_root);
        mcvHomeitemHeader = (MyCircleView) v.findViewById(R.id.mcv_homeitem_header);
        tvActPersonName = (TextView) v.findViewById(R.id.tv_act_person_name);
        ivHomeitemVip = (ImageView) v.findViewById(R.id.iv_homeitem_vip);
        iv_gender = (ImageView) v.findViewById(R.id.iv_gender);
        tvContent = (TextView) v.findViewById(R.id.tv_content);
        tv_person_count = (TextView) v.findViewById(R.id.tv_person_count);
        tv_video_count = (TextView) v.findViewById(R.id.tv_video_count);

        //imageView宽高比2:1
        ViewGroup.LayoutParams params = ivActBg.getLayoutParams();
        params.height = ScreenUtils.getScreenSize(context).x / 2;
        params.width = ScreenUtils.getScreenSize(context).x;
        ivActBg.setLayoutParams(params);

        imageLoader.displayImage(vo.getActivity_sponsor_icon(), mcvHomeitemHeader, DisplayOptionsUtils.getHeaderDefaultImageOptions());
        tvActPersonName.setText(vo.getActivity_sponsor_nickname());

        if (!TextUtils.isEmpty(vo.getActivity_detail_uri())) {
            coverid = vo.getActivity_detail_uri();
            ivActBg.setVisibility(View.VISIBLE);
            imageLoader.displayImage(vo.getActivity_detail_uri(), ivActBg, DisplayOptionsUtils.getDefaultMaxRectImageOptions());
            ivActBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String URI = vo.getActivity_banner_h5_uri();
                    if (!MHStringUtils.isEmpty(URI)) {
                        Intent intent = new Intent(ActiveDetailActivity.this, WebViewActivity.class);
                        if (URI.contains("?")) {
                            URI += "&app=miaohi";
                        } else {
                            URI += "?app=miaohi";
                        }
                        intent.putExtra("uri", URI);
                        intent.putExtra("title", vo.getActivity_banner_h5_uri_note() == null ? "" : vo.getActivity_banner_h5_uri_note());
                        startActivity(intent);
                    }
                }
            });
        } else {
            ivActBg.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(vo.getActivity_sponsor_gender())) {
            iv_gender.setVisibility(View.VISIBLE);
            if (vo.getActivity_sponsor_gender().equals("1")) {
                iv_gender.setBackgroundResource(R.drawable.gender_man);
            } else {
                iv_gender.setBackgroundResource(R.drawable.gender_women);
            }

        } else {
            iv_gender.setVisibility(View.GONE);

        }


        rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonalHomeActivity.class);
                intent.putExtra("userId", vo.getActivity_sponsor_id());
                startActivity(intent);

            }
        });

        tvContent.setText(vo.getActivity_detail_note());
        tv_person_count.setText("参加专题人数：" + vo.getActivity_player_count());
        tv_video_count.setText("作品：" + vo.getActivity_video_count());

        gridView.addHeaderView(v);
    }

    /**
     * 显示新手引导气泡
     */
    public void showGuideBubble() {
        if (!TextUtils.equals(SysMethod.getVersionCode(context), SpUtils.getString(ACTIVEDETAIL_BUBBLE))) {
            tv_activedetailbubble.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.popupwindowbubble);
            tv_activedetailbubble.setAnimation(anim);
            SpUtils.put(ACTIVEDETAIL_BUBBLE, SysMethod.getVersionCode(context));
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tv_activedetailbubble.setVisibility(View.INVISIBLE);
//                    tv_activedetailbubble.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            tv_activedetailbubble.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("needRefresh", false)) {
            if (null != ptsg_activedetail)
                ptsg_activedetail.doPullRefreshing(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shareDialog != null && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }
    }

    @Override
    public void deleteVideoSuccess() {

    }

    @Override
    public void deleteVideoFail() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isFromActivity(ActivityEvent event) {
        final CommonDialog commonDialog = new CommonDialog(context);
        commonDialog.setCancelable(true);
        commonDialog.setLeftButtonMsg("确定");
        commonDialog.hideTitle();
        commonDialog.setContentMsg("视频正在上传中，" + "\n" + "请到关注页面查看进度");
        commonDialog.hideRightButton();
        commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
            @Override
            public void onLeftButtonOnClick() {
                if (!isFinishing()) {
                    commonDialog.dismiss();
                }

            }
        });
        commonDialog.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        hiddenLoadingView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

}

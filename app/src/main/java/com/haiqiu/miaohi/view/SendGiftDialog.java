package com.haiqiu.miaohi.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.RechargingActivity1;
import com.haiqiu.miaohi.adapter.GiftAdapter;
import com.haiqiu.miaohi.adapter.MyPageAdapter;
import com.haiqiu.miaohi.bean.GiftResultBean;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.RefreshBalanceEvent;
import com.haiqiu.miaohi.receiver.RefreshGift;
import com.haiqiu.miaohi.response.ConfirmRequestResponse;
import com.haiqiu.miaohi.response.GiftResponse;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.NoDoubleClickUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.haiqiu.miaohi.widget.GiftGrideViewLayout;
import com.haiqiu.miaohi.widget.viewpagerindicator.CirclePageIndicator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tendcloud.tenddata.TCAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送礼物dialog
 * Created by ningl on 16/10/10.
 */
public class SendGiftDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private CirclePageIndicator mIndicator;
    private ViewPager mViewpager;
    private TextView tv_gift_selector_residuals_num;
    private TextView tv_recharge;
    private int index = 0;
    List<GiftResultBean> mList = new ArrayList<>();
    int gift_count = 0;
    private ArrayList<RecyclerView> listViews = null;
    MyPageAdapter mAdapter;
    RecyclerView mRecyclerView;
    GiftAdapter giftAdapter;
    Button gift_selector_use;
    public static int selectPos = 0;
    private GiftGrideViewLayout ggl_giftgride;
    private RelativeLayout rl_showgift;
    private ImageView iv_sun;
    private ImageView iv_gift;
    private ProgressBar progress_bar;
    private ObjectAnimator rotateAnim;
    private int currentCount;
    private String videoId;

    private Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(iv_gift, "scaleX", 1, 0);
            ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(iv_gift, "scaleY", 1, 0);
            ObjectAnimator scaleAnimX1 = ObjectAnimator.ofFloat(iv_sun, "scaleX", 1, 0);
            ObjectAnimator scaleAnimY1 = ObjectAnimator.ofFloat(iv_sun, "scaleY", 1, 0);
            AnimatorSet set = new AnimatorSet();
            set.play(scaleAnimX).with(scaleAnimX1).with(scaleAnimY).with(scaleAnimY1);
            set.start();
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    iv_sun.setVisibility(View.GONE);
                    iv_gift.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    };

    public SendGiftDialog(Context context, int currentCount, String videoId) {
        super(context, R.style.MiaoHiDialog);
        this.context = context;
        this.currentCount = currentCount;
        this.videoId = videoId;
        setContentView(R.layout.dialog_bottom_gift);
        registEventBus();
        mViewpager = (ViewPager) findViewById(R.id.gift_selector_viewpager);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        gift_selector_use = (Button) findViewById(R.id.gift_selector_use);
        tv_gift_selector_residuals_num = (TextView) findViewById(R.id.tv_gift_selector_residuals_num);
        tv_recharge = (TextView) findViewById(R.id.tv_recharge);
        ggl_giftgride = (GiftGrideViewLayout) findViewById(R.id.ggl_giftgride);
        rl_showgift = (RelativeLayout) findViewById(R.id.rl_showgift);
        iv_sun = (ImageView) findViewById(R.id.iv_sun);
        iv_gift = (ImageView) findViewById(R.id.iv_gift);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        iv_sun.setVisibility(View.GONE);
        iv_gift.setVisibility(View.GONE);
        rl_showgift.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
//        Typeface textFont = TypefaceHelper.get(context, "fonts/DINCond_Bold.otf");
//        tv_gift_selector_residuals_num.setTypeface(textFont);
        tv_gift_selector_residuals_num.getPaint().setFakeBoldText(true);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(context).x;
        lp.height = ScreenUtils.getScreenSize(context).y;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.BottomInAnim);
        initGiftData();
        show();
        ggl_giftgride.setOndispatchTouchEventListener(new GiftGrideViewLayout.OnDispatchTouchEvent() {
            @Override
            public void OnDispatchTouchEvent() {
                if (rl_showgift.getVisibility() == View.VISIBLE) {
                    iv_sun.setVisibility(View.GONE);
                    iv_gift.setVisibility(View.GONE);
                    handler.removeMessages(0);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.rl_showgift://显示礼物
                if (iv_gift.getVisibility() != View.VISIBLE) {
                    iv_sun.setVisibility(View.GONE);
                    iv_gift.setVisibility(View.GONE);
                    dismiss();
                }
                break;

            case R.id.tv_recharge://礼物充值
                TCAgent.onEvent(context, "视频详情-礼物充值" + ConstantsValue.android);
                Intent intent = new Intent();
                intent.setClass(context, RechargingActivity1.class);
                intent.putExtra("not_wallet", "not_wallet");
                context.startActivity(intent);
                break;
        }
    }

    /**
     * 获取礼物
     */
    private void initGiftData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_index", 0 + "");
        requestParams.addParams("page_size", "50");
        requestParams.addParams("video_id", videoId);

        MHHttpClient.getInstance().post(GiftResponse.class, ConstantsValue.Url.GIFT_SPECIFICATION, requestParams, new MHHttpHandler<GiftResponse>() {

            @Override
            public void onSuccess(GiftResponse response) {
                tv_gift_selector_residuals_num.setText(CommonUtil.formatPrice4Point(response.getData().getUser_balance()));

                mList = response.getData().getGift_result();
                for (int i = 0; i < mList.size(); i++) {
                    if(!mList.get(i).isGift_has_sent()){
                        mList.get(i).setSelect(1); //默认第一个选中
                        selectPos = i;
                        break;
                    }
                }
                gift_count = response.getData().getGift_count();
                GenerateViewPager(gift_count);
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
     * 生成viewpager
     * @param gift_count 礼物数
     */
    private void GenerateViewPager(int gift_count) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gift, null, false);

        if (listViews == null) {
            listViews = new ArrayList<RecyclerView>();
        }

        final int PageCount = (int) Math.ceil(mList.size() / 8.0f);

        MHLogUtil.e(PageCount + "**********");

        if (PageCount == 1) {
            mIndicator.setVisibility(View.INVISIBLE);
        } else {
            mIndicator.setVisibility(View.VISIBLE);
        }

//        int emptyItemCount = 8 - mList.size() % 8;
//        for (int i = 0; i < emptyItemCount; i++) {
//            GiftResultBean bean = new GiftResultBean(-1, "", "", "", "", "敬请期待", "", "");
//            mList.add(bean);
//        }


        for (int i = 0; i < PageCount; i++) {
            mRecyclerView = new RecyclerView(context);
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            mRecyclerView.setAdapter(new GiftAdapter(mList, context, i));

            listViews.add(mRecyclerView);
        }


        final float density = context.getResources().getDisplayMetrics().density;
        mIndicator.setBackgroundColor(Color.argb(0, 255, 255, 255));
        mIndicator.setSpacing(DensityUtil.dip2px(context, 5));
        mIndicator.setRadius(3 * density);
        mIndicator.setPageColor(ContextCompat.getColor(context, R.color.color_ccc));
        mIndicator.setStrokeWidth(0);
        mIndicator.setFillColor(0xff00a0e9);
        mIndicator.setSnap(true);

        mAdapter = new MyPageAdapter(listViews);
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOffscreenPageLimit(3);


        mIndicator.setViewPager(mViewpager);

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        gift_selector_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCAgent.onEvent(context, "视频详情-赠送点击数" + ConstantsValue.android);
                if (mList.get(SendGiftDialog.selectPos).getHi_coin() == 0) {
                    if (!NoDoubleClickUtils.isDoubleClick()) {
                        sendGift();
                    }
                } else {
                    confirmPayResultRequest();
                }
            }
        });
    }

    /**
     * 确定支付结果
     */
    private void confirmPayResultRequest() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("gift_id", mList.get(SendGiftDialog.selectPos).getGift_id());
//        requestParams.addParams("type", ConstantsValue.Other.CONFIRM_PAYSECONDRE_SENDGIFT);

        MHHttpClient.getInstance().post(ConfirmRequestResponse.class, context, ConstantsValue.Url.CONFIRMPAYSECONDREQUEST, requestParams, new MHHttpHandler<ConfirmRequestResponse>() {
            @Override
            public void onSuccess(ConfirmRequestResponse response) {

                if (response.getData().getState() == 10) {
                    sendGift();

                } else {
                    final CommonDialog commonDialog = new CommonDialog(context);
                    commonDialog.setCancelable(true);
                    commonDialog.setLeftButtonMsg("取消");
                    commonDialog.setRightButtonMsg("去充值");
                    commonDialog.setTitleMsg(response.getData().getTitle());
                    if (!TextUtils.isEmpty(response.getData().getContent()) && response.getData().getContent().contains("，")) {
                        String content = response.getData().getContent().replace("，", "，\n");
                        commonDialog.setContentMsg(content);
                    }
                    commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
                        @Override
                        public void onLeftButtonOnClick() {
                            if (!((Activity) context).isFinishing()) {
                                commonDialog.dismiss();
                            }

                        }
                    });
                    commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                        @Override
                        public void onRightButtonOnClick() {
                            Intent intent = new Intent();
                            intent.setClass(context, RechargingActivity1.class);
                            intent.putExtra("not_wallet", "not_wallet");

                            context.startActivity(intent);
                            if (!((Activity) context).isFinishing()) {
                                commonDialog.dismiss();
                            }

                        }
                    });
                    commonDialog.show();

                }


            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    /**
     * 发送礼物
     */
    private void sendGift() {
//        VideoDetailBean videoDetailBean = VideoDetailActivity.videoDetailActivity.videoDetailBean;
//        if(videoDetailBean==null
//                ||videoDetailBean.getVideo_id()==null
//                ||videoDetailBean.getUpload_user_id()==null) return;
//        progress_bar.setVisibility(View.VISIBLE);
//        gift_selector_use.setText("");
//        gift_selector_use.setEnabled(false);
//        MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("gift_id", mList.get(SendGiftDialog.selectPos).getGift_id());
//        requestParams.addParams("video_id", videoId);
//        requestParams.addParams("user_id", VideoDetailActivity.videoDetailActivity.videoDetailBean.getUpload_user_id());
//        MHHttpClient.getInstance().post(VideoSendGiftResponse.class, context, ConstantsValue.Url.SENDGIFT, requestParams, new MHHttpHandler<VideoSendGiftResponse>() {
//            @Override
//            public void onSuccess(VideoSendGiftResponse response) {
//                progress_bar.setVisibility(View.GONE);
//                gift_selector_use.setText("赠送");
//                gift_selector_use.setEnabled(true);
//
//                long user_balance = response.getData().getUser_balance();
//                tv_gift_selector_residuals_num.setText(CommonUtil.formatPrice4Point(user_balance));
//                setGiftImg(mList.get(SendGiftDialog.selectPos).getIcon_uri());
//                currentCount++;
//                if (mList.get(selectPos).getHi_coin() == 0) {//免费
//                    mList.get(selectPos).setGift_has_sent(true);
//                    for (int i = 0; i < listViews.size(); i++) {
//                        if((selectPos+1)>=i*8&&(selectPos+1)<=i*8+8){
//                            selectPos = -1;
//                            listViews.get(i).getAdapter().notifyDataSetChanged();
//                            gift_selector_use.setBackgroundResource(R.drawable.shape_sendgift_gray);
//                            gift_selector_use.setEnabled(false);
//                        }
//                    }
//
//                }
//                EventBus.getDefault().post(new RefreshGiftCountEvent(currentCount, VideoDetailActivity.videoDetailActivity.videoDetailBean.getVideo_id()));
//            }
//
//            @Override
//            public void onFailure(String content) {
//                progress_bar.setVisibility(View.GONE);
//                gift_selector_use.setText("赠送");
//                gift_selector_use.setEnabled(true);
//            }
//
//            @Override
//            public void onStatusIsError(String message) {
//                super.onStatusIsError(message);
//                progress_bar.setVisibility(View.GONE);
//                gift_selector_use.setText("赠送");
//                gift_selector_use.setEnabled(true);
//            }
//        });
    }

    /**
     * 设置礼物图片
     */
    public void setGiftImg(String url) {
        ImageLoader.getInstance().displayImage(url, iv_gift, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                iv_gift.setVisibility(View.VISIBLE);
                iv_sun.setVisibility(View.VISIBLE);
                startAnim();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    /**
     * 开始动画
     */
    private void startAnim() {
        iv_sun.clearAnimation();
        if (null != rotateAnim) rotateAnim.end();
        ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(iv_gift, "scaleX", 0.3f, 1f, 2f, 1);
        ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(iv_gift, "scaleY", 0.3f, 1f, 2f, 1);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(iv_gift, "alpha", 1f, 1f, 0.5f, 1f);
        ObjectAnimator scaleSunX = ObjectAnimator.ofFloat(iv_sun, "scaleX", 0f, 0f, 0f, 1f);
        ObjectAnimator scaleSunY = ObjectAnimator.ofFloat(iv_sun, "scaleY", 0f, 0f, 0f, 1f);
        ObjectAnimator alphaSunAnim = ObjectAnimator.ofFloat(iv_sun, "alpha", 0f, 0f, 0.1f, 0.5f);
        rotateAnim = ObjectAnimator.ofFloat(iv_sun, "rotation", 0f, 360f);
        rotateAnim.setDuration(4500);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnim.setRepeatMode(ValueAnimator.RESTART);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(600);
        set.play(scaleAnimX)
                .with(scaleAnimY)
                .with(alphaAnim)
                .with(scaleSunX)
                .with(scaleSunY)
                .with(alphaSunAnim);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                rotateAnim.start();
                handler.sendEmptyMessageDelayed(0, 3000);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    /**
     * 从消息队列中移除消息
     * @param what
     */
    public void removeMessage(int what){
        if (null != handler) handler.removeMessages(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ClickEvent(RefreshGift event) {
        //更新多个listview
        for (int i = 0; i < listViews.size(); i++) {
            listViews.get(i).getAdapter().notifyDataSetChanged();
        }
        gift_selector_use.setBackgroundResource(R.drawable.bg_gift_corner_6_selector);
        gift_selector_use.setEnabled(true);
    }

    /**
     * 刷新余额
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshEvent(RefreshBalanceEvent event) {
        tv_gift_selector_residuals_num.setText(CommonUtil.formatPrice4Point(event.getPrice()));
    }

    /**
     * 注册eventbus
     */
    private void registEventBus(){
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 反注册eventbus
     */
    public void unRegistEventBus(){
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}

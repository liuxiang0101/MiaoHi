package com.haiqiu.miaohi.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.VideoAndImgDetailAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.UserWork;
import com.haiqiu.miaohi.bean.UserWorkItem;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.fragment.VideoAndImgDetailFragment;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.DeleteVideoAndImgAsyncEvent;
import com.haiqiu.miaohi.receiver.StopVideoEvent;
import com.haiqiu.miaohi.response.UserWorksResponse;
import com.haiqiu.miaohi.utils.AnimateFirstDisplayListener;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.view.picturezoom.OnShowPicture;
import com.haiqiu.miaohi.view.picturezoom.PhotoView;
import com.haiqiu.miaohi.view.picturezoom.PhotoViewAttacher;
import com.haiqiu.miaohi.widget.mediaplayer.BaseVideoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 视频和图片详情
 * Created by ningl on 16/12/13.
 */
public class VideoAndImgActivity extends BaseActivity implements VideoAndImgDetailFragment.DeleteVideoOrImg
        , OnShowPicture
        , VideoAndImgDetailFragment.OnLastCommentId{

    private int currentIndx;
    private ViewPager vp_videoandimg;
    private List<VideoAndImg> data;
    private VideoAndImgDetailAdapter adapter;
    private FrameLayout fl_videoandimgcontent;
    private String command;
    private int pageIndex;
    private String user_id;
    private boolean hasMore = true;//是否还有更多
    private boolean isFromCommentList;
    private boolean isNeedBack;
    private final int VIDEO = 1;
    private final int IMG = 2;
    private FrameLayout fl_videoandimg;
    private PhotoView pv_videoandimg;
    private String lastCommentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoandimg);
        registEvenBus();
        initView();
        if (getIntent() == null) return;
        data = getIntent().getParcelableArrayListExtra("data");
        if (data.size() > 1)
            TCAgent.onEvent(context, "视频滑块" + ConstantsValue.android);
        currentIndx = getIntent().getIntExtra("currentIndex", 0);
        command = getIntent().getStringExtra("command");
        user_id = getIntent().getStringExtra("userId");
        pageIndex = getIntent().getIntExtra("pageIndex", pageIndex);
        isFromCommentList = getIntent().getBooleanExtra("isFromCommentList", false);
        isNeedBack = getIntent().getBooleanExtra("isNeedBack", false);

        adapter = new VideoAndImgDetailAdapter(getSupportFragmentManager(), context, data, isFromCommentList,isNeedBack);
        vp_videoandimg.setOffscreenPageLimit(2);
        vp_videoandimg.setAdapter(adapter);
        vp_videoandimg.setCurrentItem(currentIndx);
        fl_videoandimgcontent.setVisibility(View.GONE);
        vp_videoandimg.setVisibility(View.VISIBLE);
        vp_videoandimg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (command != null) {//需要分页
                    if (data.size() % 20 != 0 && position == data.size()) {//还有更多
                        getData();
                    }
                }
                EventBus.getDefault().post(new StopVideoEvent());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                lastCommentId = "";
            }
        });


    }

    /**
     * 获取更多数据
     */
    public void getData() {
        MHRequestParams params = new MHRequestParams();
        params.addParams("user_id", user_id);
        params.addParams("page_index", String.valueOf(pageIndex));
        params.addParams("page_size", ConstantsValue.Other.PAGESIZE);
        MHHttpClient.getInstance().post(UserWorksResponse.class, command, new MHHttpHandler<UserWorksResponse>() {
            @Override
            public void onSuccess(UserWorksResponse response) {
                pageIndex++;
                List<UserWork> userWorks = null;
                if (TextUtils.equals(ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS, command)) {
                    UserWorkItem userWorkItem = response.getData();
                    userWorks = userWorkItem.getPage_result();
                    if (userWorks.size() < 20) {
                        hasMore = false;
                    }
                    data.addAll(userWorks);
                } else if (TextUtils.equals(ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS, command)) {

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    private void initView() {
        vp_videoandimg = (ViewPager) findViewById(R.id.vp_videoandimg);
        fl_videoandimgcontent = (FrameLayout) findViewById(R.id.fl_videoandimgcontent);
        fl_videoandimg = (FrameLayout) findViewById(R.id.fl_videoandimg);
        pv_videoandimg = (PhotoView) findViewById(R.id.pv_videoandimg);
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
    }

    @Override
    public void onDelete() {

    }

    @Override
    public int getPosition() {
        return 0;
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
     * 接受删除消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateEvent(DeleteVideoAndImgAsyncEvent event) {
//        if(event.getFromType() != ConstantsValue.Other.DELETEVIDEOANDIMG_FROMVIDEOANDIMGDETAIL){
//            for (int i = 0; i < data.size(); i++) {
//                VideoAndImg videoAndImg = data.get(i);
//                if (event.getContentType() == videoAndImg.getContent_type()) {
//                    if (event.getContentType() == VIDEO) {
//                        if (TextUtils.equals(videoAndImg.getVideo_id(), event.getTargetId())) {
//                            videoAndImg.setDelete(true);
//                            int currentPosition = vp_videoandimg.getCurrentItem();
//                            vp_videoandimg.setAdapter(adapter);
//                            vp_videoandimg.setCurrentItem(currentPosition);
//                            return;
//                        }
//                    } else if (event.getContentType() == IMG) {
//                        if (TextUtils.equals(videoAndImg.getPhoto_id(), event.getTargetId())) {
//                            videoAndImg.setDelete(true);
//                            int currentPosition = vp_videoandimg.getCurrentItem();
//                            vp_videoandimg.setAdapter(adapter);
//                            vp_videoandimg.setCurrentItem(currentPosition);
//                            return;
//                        }
//                    }
//                }
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        UMShareAPI.get(this).release();
    }


    @Override
    public void onShowPicture(String url) {
        fl_videoandimg.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(fl_videoandimg, "alpha", 0, 1f)
                .setDuration(400)
                .start();
        ImageLoader.getInstance().displayImage(url, pv_videoandimg, new AnimateFirstDisplayListener(pv_videoandimg));
        pv_videoandimg.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                fl_videoandimg.setVisibility(View.GONE);
            }

            @Override
            public void onOutsidePhotoTap() {
                fl_videoandimg.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hiddenLoadingView();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean backState = false;
            if (null != BaseVideoView.backDownListener) {
                backState = BaseVideoView.backDownListener.OnBackDown();
            }
            //如果是展示大图状态则需要先隐藏大图
            if (fl_videoandimg.getVisibility() == View.VISIBLE) {
                fl_videoandimg.setVisibility(View.GONE);
                backState = true;
            }
            if (backState) return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public String getLastCommentId() {
        return lastCommentId;
    }

    @Override
    public void setLastCommentId(String commentId) {
        this.lastCommentId = commentId;
    }
}

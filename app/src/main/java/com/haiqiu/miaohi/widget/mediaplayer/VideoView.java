package com.haiqiu.miaohi.widget.mediaplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.WebViewActivity;
import com.haiqiu.miaohi.bean.VideoExtraInfo;
import com.haiqiu.miaohi.bean.VideoInfo;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.PraisedUtil;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONObject;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by zhandalin on 2017-01-12 15:56.
 * 说明:我们工程常用的VideoView
 */
public class VideoView extends BottomProgressVideoView {
    private final static String TAG = "VideoView_TAG";
    private ImageView iv_praise;
    private RelativeLayout rl_video_praise;
    private View vertical_line;
    private LinearLayout ll_extra_control;
    private TextView tv_subject;
    private ImageView iv_is_yd;
    private TextView tv_recommend;
    private LinearLayout ll_top_video_info;
    private TextView tv_look_num;
    private TextView tv_video_time_info;
    private LinearLayout ll_bottom_video_info;
    private VideoExtraInfo videoExtraInfo;
    private OnPraiseListener onPraiseListener;
    private boolean isBigVideo = true;
    private boolean playButtonEnableVisible;
    private View iv_prise_animation;
    private BroadcastReceiver broadcastReceiver;
    private RelativeLayout video_extra_info_rootView;


    public VideoView(Context context) {
        this(context, null);
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        video_extra_info_rootView = (RelativeLayout) View.inflate(context, R.layout.video_extra_info_layout, null);
        iv_praise = (ImageView) video_extra_info_rootView.findViewById(R.id.iv_praise);
        rl_video_praise = (RelativeLayout) video_extra_info_rootView.findViewById(R.id.rl_video_praise);
        vertical_line = video_extra_info_rootView.findViewById(R.id.vertical_line);
        RelativeLayout rl_video_replay = (RelativeLayout) video_extra_info_rootView.findViewById(R.id.rl_video_replay);
        ll_extra_control = (LinearLayout) video_extra_info_rootView.findViewById(R.id.ll_extra_control);

        tv_subject = (TextView) video_extra_info_rootView.findViewById(R.id.tv_subject);
        iv_is_yd = (ImageView) video_extra_info_rootView.findViewById(R.id.iv_is_yd);
        tv_recommend = (TextView) video_extra_info_rootView.findViewById(R.id.tv_recommend);
        ll_top_video_info = (LinearLayout) video_extra_info_rootView.findViewById(R.id.ll_top_video_info);

        tv_look_num = (TextView) video_extra_info_rootView.findViewById(R.id.tv_look_num);
        tv_video_time_info = (TextView) video_extra_info_rootView.findViewById(R.id.tv_video_time_info);
        ll_bottom_video_info = (LinearLayout) video_extra_info_rootView.findViewById(R.id.ll_bottom_video_info);

        iv_prise_animation = video_extra_info_rootView.findViewById(R.id.iv_prise_animation);
        iv_prise_animation.setSelected(true);

        rl_video_praise.setOnClickListener(this);
        rl_video_replay.setOnClickListener(this);
        tv_subject.setOnClickListener(this);

        addView(video_extra_info_rootView);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerReceiver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterReceiver();
    }

    /**
     * 注册耳机拔插监听
     */
    private void registerReceiver() {
        if (null != mediaPlayer && mediaPlayer.isPlaying()) {
            return;
        }
        if (null != broadcastReceiver) {
            context.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null != intent && Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
                    if (intent.hasExtra("state")) {
                        if (intent.getIntExtra("state", -1) == 0) {
                            MHLogUtil.d(TAG, "拔出耳机");
                            if (null != mediaPlayer && mediaPlayer.isPlaying())
                                mediaPlayer.pause();
                        } else if (intent.getIntExtra("state", 0) == 1) {
                            MHLogUtil.d(TAG, "插入耳机");
                            if (null != mediaPlayer && CURRENT_MEDIA_STATE == MEDIA_STATE_PAUSE)
                                mediaPlayer.start();
                        }
                    }
                }
            }
        };
        context.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));

    }

    /**
     * 取消耳机拔插监听
     */
    private void unregisterReceiver() {
        if (null != mediaPlayer && mediaPlayer.isPlaying()) {
            return;
        }
        if (null != broadcastReceiver) {
            context.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    @Override
    public void startPlay(final MyMediaPlayer mediaPlayer, VideoInfo videoInfo) {
        super.startPlay(mediaPlayer, videoInfo);
        if (!isNetWorkError) {
            hideTopVideoInfo();
            hideBottomVideoInfo();
        }
    }

    @Override
    protected void start() {
        MHLogUtil.d(TAG, "start--" + this);
        super.start();
        if (isBigVideo)
            iv_close_audio.setVisibility(VISIBLE);
        else
            iv_close_audio.setVisibility(GONE);

        ((ViewGroup) iv_close_audio.getParent()).removeView(iv_close_audio);
        controlView.addView(iv_close_audio);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        MHLogUtil.d(TAG, "onCompletion--" + this);
        //小窗口是连续播放,不做处理
        if (!isBigVideo) {
            if (null != videoControlListener)
                videoControlListener.videoViewState(VideoViewState.ON_VIDEO_COMPLETE, 0);
            return;
        }
        super.onCompletion(iMediaPlayer);

        ll_bottom_control.clearAnimation();
        ll_bottom_control.setVisibility(GONE);

        if (null != videoExtraInfo && videoExtraInfo.getVideoType() != VideoExtraInfo.VideoType.VIDEO_TYPE_YD && !isNetWorkError && isBigVideo) {
            iv_play.clearAnimation();
            iv_play.setVisibility(GONE);
            showExtraControlView();
        } else {
            showBottomVideoInfo();
        }
        if (isNetWorkError) {
            ll_top_video_info.setVisibility(GONE);
            ll_top_video_info.clearAnimation();
            ll_bottom_video_info.setVisibility(GONE);
            ll_bottom_video_info.clearAnimation();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLoading(false);
                }
            }, 300);
        } else {
            showTopVideoInfo();
        }
    }

    @Override
    protected void showLoading(boolean isLoading) {
        if (!isBigVideo) return;
        super.showLoading(isLoading);
    }


    protected void hidePlayView() {
        if (!isBigVideo) return;
        super.hidePlayView();
    }

    public void showPlayView() {
        if (!isBigVideo) return;
        super.showPlayView();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        MHLogUtil.d(TAG, "onSurfaceTextureAvailable--" + this);
        surface = new Surface(surfaceTexture);
        if (null != mediaPlayer)
            mediaPlayer.setSurface(surface);

        if (CURRENT_MEDIA_STATE != MEDIA_STATE_PLAYING && playButtonEnableVisible) {
            showPlayView();
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        MHLogUtil.d(TAG, "onSurfaceTextureDestroyed--surface=" + surface);
        if (CURRENT_MEDIA_STATE != MEDIA_STATE_PLAYING)
            previewImageView.setVisibility(VISIBLE);
        if (playButtonEnableVisible)
            showPlayView();
        return true;
    }


    private void hideBottomVideoInfo() {
        if (ll_bottom_video_info.getVisibility() == GONE) return;

        MHLogUtil.d(TAG, "hideBottomVideoInfo--" + this);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f);

        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_bottom_video_info.setVisibility(GONE);
                ll_bottom_video_info.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_bottom_video_info.startAnimation(translateAnimation);
    }

    private void showBottomVideoInfo() {
        if (ll_bottom_video_info.getVisibility() == VISIBLE) return;
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0);

        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ll_bottom_video_info.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_bottom_video_info.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_bottom_video_info.startAnimation(translateAnimation);
    }

    private void hideTopVideoInfo() {
        if (ll_top_video_info.getVisibility() == GONE) return;
        MHLogUtil.d(TAG,"hideTopVideoInfo");

        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1f);

        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_top_video_info.setVisibility(GONE);
                ll_top_video_info.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_top_video_info.startAnimation(translateAnimation);
    }

    private void showTopVideoInfo() {
        if (ll_top_video_info.getVisibility() == VISIBLE) return;
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0);

        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ll_top_video_info.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_top_video_info.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_top_video_info.startAnimation(translateAnimation);
    }

    private void hideExtraControlView() {
        if (ll_extra_control.getVisibility() == GONE) return;
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_extra_control.setVisibility(GONE);
                ll_extra_control.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_extra_control.startAnimation(alphaAnimation);
    }

    private void showExtraControlView() {
        if (ll_extra_control.getVisibility() == VISIBLE) return;

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (iv_praise.isSelected()) {
                    rl_video_praise.setVisibility(GONE);
                    vertical_line.setVisibility(GONE);
                } else {
                    vertical_line.setVisibility(VISIBLE);
                    rl_video_praise.setVisibility(VISIBLE);
                }
                ll_extra_control.setVisibility(VISIBLE);
                ((ViewGroup) iv_close_audio.getParent()).removeView(iv_close_audio);
                video_extra_info_rootView.addView(iv_close_audio);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_extra_control.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_extra_control.startAnimation(alphaAnimation);
    }

    @Override
    public void reset(int position) {
        MHLogUtil.d(TAG, "reset--position=" + position);
        handler.removeMessages(MSG_HID_CONTROL);
        handler.removeMessages(MSG_UPDATE_PROGRESS);

        if (-1 == position) {//表示正在上传,隐藏所有View
            super.reset(position);
            iv_play.setVisibility(GONE);
            ll_top_video_info.setVisibility(GONE);
            ll_bottom_video_info.setVisibility(GONE);
            ll_extra_control.setVisibility(GONE);
            iv_close_audio.setVisibility(GONE);
            return;
        }
//        if (lastPosition == position) {
//            if (MHApplication.audioIsSilence) {
//                if (null != mediaPlayer)
//                    mediaPlayer.setVolume(0, 0);
//            } else {
//                if (null != mediaPlayer)
//                    mediaPlayer.setVolume(1, 1);
//            }
//            iv_close_audio.setSelected(MHApplication.audioIsSilence);
//            return;
//        }
        super.reset(position);
        ll_bottom_control.setVisibility(GONE);
        ll_extra_control.setVisibility(GONE);

        if (CURRENT_MEDIA_STATE != MEDIA_STATE_PLAYING && !isNetWorkError) {
            ll_top_video_info.setVisibility(VISIBLE);
            ll_bottom_video_info.setVisibility(VISIBLE);
        }
    }

    @Override
    public void pause() {
        MHLogUtil.d(TAG, "pause--" + this);
        super.pause();
        if (null == videoInfo) return;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "playtime_duration");
            jsonObject.put("description", "播放时间长度");
            jsonObject.put("duration", lastPlayDuration + "");
            jsonObject.put("video_id", videoInfo.getVideo_id());
            BehaviourStatistic.uploadBehaviourInfo(jsonObject);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    @Override
    public void mediaPlayerPause() {
        MHLogUtil.d(TAG, "mediaPlayerPause--" + this);
        shouldReset = true;
        lastPlayDuration = mediaPlayer.getCurrentPosition();
        handler.removeMessages(MSG_HID_CONTROL);
        handler.removeMessages(MSG_UPDATE_PROGRESS);
        iv_play.setSelected(false);
        CURRENT_MEDIA_STATE = MEDIA_STATE_PAUSE;

        if (ll_extra_control.getVisibility() != VISIBLE) {
            showPlayView();
        }
        setKeepScreenOn(false);
        MHLogUtil.d(TAG, "mediaPlayerPause");
        if (null != videoControlListener)
            videoControlListener.videoViewState(VideoViewState.ON_VIDEO_PAUSE, 0);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "playtime_duration");
            jsonObject.put("description", "播放时间长度");
            jsonObject.put("duration", lastPlayDuration + "");
            if (null != videoInfo)
                jsonObject.put("video_id", videoInfo.getVideo_id());
            BehaviourStatistic.uploadBehaviourInfo(jsonObject);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_video_praise:
                TCAgent.onEvent(context, "视频播完点赞" + ConstantsValue.android);
                iv_praise.setSelected(!iv_praise.isSelected());
                if (iv_praise.isSelected())
                    PraisedUtil.showPop(iv_praise, context, true, iv_praise.getWidth() * 2);

                if (null != onPraiseListener)
                    onPraiseListener.onPraiseClick(iv_praise, iv_praise.isSelected());
                break;
            case R.id.rl_video_replay:
                TCAgent.onEvent(context, "视频重播" + ConstantsValue.android);
                ll_extra_control.setVisibility(GONE);
                reStart();
                break;
            case R.id.tv_subject:
                if (null != videoExtraInfo && !MHStringUtils.isEmpty(videoExtraInfo.subjectUri)) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("uri", videoExtraInfo.subjectUri);
                    intent.putExtra("title", videoExtraInfo.subjectName);
                    context.startActivity(intent);
                }
                TCAgent.onEvent(context, "专题点击" + videoExtraInfo.subjectName + ConstantsValue.android);
                break;
            case R.id.iv_close_audio:
                if (MHApplication.audioIsSilence) {
                    TCAgent.onEvent(context, "静音关" + ConstantsValue.android);
                } else {
                    TCAgent.onEvent(context, "静音开" + ConstantsValue.android);
                }
                break;
        }
    }

    public void setPraiseState(boolean isPraise) {
        iv_praise.setSelected(isPraise);
    }

    /**
     * @param videoExtraInfo 视频额外信息
     */
    public void setVideoExtraInfo(VideoExtraInfo videoExtraInfo) {
        //和上一次信息相同,不再设置,防止状态回去了
        if (null != this.videoExtraInfo
                && this.videoExtraInfo.position == videoExtraInfo.position
                && ll_extra_control.getVisibility() == VISIBLE) {
            return;
        }
        this.videoExtraInfo = videoExtraInfo;
        if (null == videoExtraInfo) {
            tv_subject.setVisibility(GONE);
            iv_is_yd.setVisibility(GONE);

            tv_look_num.setVisibility(GONE);
            tv_video_time_info.setVisibility(GONE);
            tv_recommend.setVisibility(GONE);
            return;
        }
        if (!MHStringUtils.isEmpty(videoExtraInfo.subjectName) && !MHStringUtils.isEmpty(videoExtraInfo.subjectUri)) {
            tv_subject.setText("# " + videoExtraInfo.subjectName + " #");
            ll_top_video_info.setAlpha(1);
            tv_subject.setVisibility(VISIBLE);
            tv_subject.setOnClickListener(this);
        } else {
            tv_subject.setText(null);
            tv_subject.setVisibility(GONE);
        }
        if (MHStringUtils.isEmpty(videoExtraInfo.recommendStr)) {
            tv_recommend.setVisibility(GONE);
            tv_recommend.setText(null);
        } else {
            tv_recommend.setText(videoExtraInfo.recommendStr);
            ll_top_video_info.setAlpha(1);
            tv_recommend.setVisibility(VISIBLE);
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv_video_time_info.getLayoutParams();
        if (videoExtraInfo.playNum <= 0) {
            tv_look_num.setText(null);
            tv_look_num.setVisibility(GONE);
            layoutParams.leftMargin = DensityUtil.dip2px(context, 10);
        } else {
            layoutParams.leftMargin = DensityUtil.dip2px(context, 5);
            ll_bottom_video_info.setAlpha(1);
            tv_look_num.setVisibility(VISIBLE);
            tv_look_num.setText(CommonUtil.formatCount(videoExtraInfo.playNum));
        }
        tv_video_time_info.setLayoutParams(layoutParams);

        if (videoExtraInfo.videoDuration <= 0) {
            tv_video_time_info.setText(null);
            tv_video_time_info.setVisibility(GONE);
        } else {
            ll_bottom_video_info.setAlpha(1);
            tv_video_time_info.setVisibility(VISIBLE);
            tv_video_time_info.setText(CommonUtil.formatTime2(videoExtraInfo.videoDuration));
        }
        if (VideoExtraInfo.VideoType.VIDEO_TYPE_COMMON == videoExtraInfo.getVideoType()) {
            iv_is_yd.setVisibility(GONE);
            if (playButtonEnableVisible)
                iv_play.setVisibility(VISIBLE);
        } else {
            iv_play.setVisibility(GONE);
            iv_is_yd.setVisibility(VISIBLE);
        }
    }

    public int getCurrentMediaState() {
        return CURRENT_MEDIA_STATE;
    }

    public long getLastPlayPosition() {
        return lastPlayDuration;
    }


    public void switchSmallVideo() {
        MHLogUtil.d(TAG, "switchSmallVideo--" + this);
        isBigVideo = false;
        ll_bottom_control.setVisibility(GONE);
        bottom_progressBar.setVisibility(VISIBLE);
        ll_extra_control.setVisibility(GONE);
        iv_close_audio.setVisibility(GONE);
        iv_play.setVisibility(GONE);
    }

    public void switchBigVideo() {
        MHLogUtil.d(TAG, "switchBigVideo--" + this);
        isBigVideo = true;
        iv_close_audio.setVisibility(VISIBLE);
    }

    /**
     * 设置播放按钮尺寸
     *
     * @param with   宽度(dp)
     * @param height 高度(dp)
     */
    public void setPlayBtnSize(int with, int height) {
        ViewGroup.LayoutParams lp = iv_play.getLayoutParams();
        lp.width = DensityUtil.dip2px(context, with);
        lp.height = DensityUtil.dip2px(context, height);
        iv_play.setLayoutParams(lp);
    }

    @Override
    protected void onControlDoubleClick() {
        MHLogUtil.d(TAG, "onControlDoubleClick--" + this);
        //映答不显示动画
        if (null != videoExtraInfo && videoExtraInfo.getVideoType() != VideoExtraInfo.VideoType.VIDEO_TYPE_YD) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.red_hart_disappear);
            iv_prise_animation.setVisibility(VISIBLE);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    iv_prise_animation.setVisibility(GONE);
                    iv_prise_animation.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            iv_prise_animation.startAnimation(animation);
        }

        if (null != videoControlListener) {
            videoControlListener.videoViewState(VideoViewState.ON_DOUBLE_CLICK, 0);
        }
    }

    public void setPlayButtonEnableVisible(boolean playButtonVisible) {
        this.playButtonEnableVisible = playButtonVisible;
        iv_play.clearAnimation();
        iv_play.setVisibility(playButtonVisible ? VISIBLE : GONE);
    }

    public void setPraiseEnable(boolean praiseEnable) {
        rl_video_praise.setEnabled(praiseEnable);
    }


    public void setOnPraiseListener(OnPraiseListener onPraiseListener) {
        this.onPraiseListener = onPraiseListener;
    }

    public interface OnPraiseListener {
        void onPraiseClick(View view, boolean isPraise);
    }

}

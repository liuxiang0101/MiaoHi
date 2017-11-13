package com.haiqiu.miaohi.widget.mediaplayer;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.VideoInfo;
import com.haiqiu.miaohi.utils.BitmapUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.VideoStatisticUtil;
import com.haiqiu.miaohi.widget.OnBackDownListener;

import jp.wasabeef.blurry.Blurry;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by zhandalin on 2017-01-11 21:32.
 * 说明:只是提供基本的播放控制, 这个类不允许更改,要想改功能,请继承这个类,然后扩展
 * <p>
 * 把视频播放器与视频渲染器分离开来，这样方便在列表中呈现视频
 * 要想切换全屏显示,需要在清单文件做如下配置
 * android:configChanges="keyboardHidden|orientation|screenSize"
 * android:launchMode="singleTop"
 * android:windowSoftInputMode="adjustResize"
 * <p/>
 * 并且需要重写onKeyDown实现全屏返回事件拦截,如下
 * <p/>
 *
 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
 * if (keyCode == KeyEvent.KEYCODE_BACK) {
 * if (null != BaseVideoView.backDownListener) {
 * return BaseVideoView.backDownListener.OnBackDown();
 * }
 * }
 * return super.onKeyDown(keyCode, event);
 * }
 */
public class BaseVideoView extends FrameLayout implements TextureView.SurfaceTextureListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnBufferingUpdateListener, MyMediaPlayer.MyPlayerEventListener, View.OnClickListener {
    private final static String TAG = "BaseVideoView_TAG";
    private final static String CLOSE_AUDIO_ACTION = "close_audio_action";

    public static final int MEDIA_STATE_PLAYING = 10;
    public static final int MEDIA_STATE_PAUSE = 12;
    public static final int MEDIA_STATE_IDLE = 13;
    int CURRENT_MEDIA_STATE = MEDIA_STATE_IDLE;

    public static final int MSG_UPDATE_PROGRESS = 1;
    public static final int MSG_HID_CONTROL = 2;

    /**
     * 隐藏控制器的延时时间
     */
    private static final long HIDE_CONTROL_DELAYED_TIME = 4000;
    public static OnBackDownListener backDownListener;

    final Context context;
    TextureView textureView;
    ImageView previewImageView;
    RelativeLayout controlView;
    ProgressBar progress_bar;
    ImageView iv_play;
    TextView tv_current_time;
    SeekBar seek_bar;
    TextView tv_duration_time;
    ImageView iv_transfer_screen;
    LinearLayout ll_bottom_control;
    ImageView iv_close_audio;
    VideoControlListener videoControlListener;
    MyMediaPlayer mediaPlayer;
    long videoDuration;
    boolean isLoading;
    Surface surface;

    /**
     * 上一次播放的时长
     */
    long lastPlayDuration;


    protected Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HID_CONTROL:
                    handler.removeMessages(MSG_HID_CONTROL);
                    hidePlayView();
                    hideControlView();
                    break;
                case MSG_UPDATE_PROGRESS:
                    handler.removeMessages(MSG_UPDATE_PROGRESS);
                    if (null == mediaPlayer) break;
                    try {
                        setPosition(mediaPlayer.getCurrentPosition());
                    } catch (Exception e) {
                        MHLogUtil.e(TAG, "获取进度出错--");
                        handler.removeMessages(MSG_HID_CONTROL);
                        handler.removeMessages(MSG_UPDATE_PROGRESS);
                        break;
                    }
                    handler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, 30);
                    break;
            }
            return false;
        }
    });
    boolean isFullScreen;
    int videoRotation;
    int videoWidth;
    int videoHeight;
    int currentOrientation;
    int screenHeight;
    int screenWidth;
    private int lastStartY;
    private int originHeight;
    private ViewGroup videoViewParent;
    boolean shouldReset = true;
    boolean isNetWorkError;
    private int llBottomControlHeight;
    private Bitmap lastPreviewBitmap;
    VideoInfo videoInfo;
    private BroadcastReceiver broadcastReceiver;
    private LayoutParams textureViewLayoutParams;

    public BaseVideoView(Context context) {
        this(context, null);
    }

    public BaseVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        setForegroundGravity(Gravity.CENTER);
        setBackgroundColor(Color.BLACK);

        //视频渲染界面
        textureView = new TextureView(context);
        textureView.setBackgroundColor(Color.BLACK);
        textureView.setSurfaceTextureListener(this);
        textureViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        textureViewLayoutParams.gravity = Gravity.CENTER;
        addView(textureView, textureViewLayoutParams);

        //视频预览界面
        previewImageView = new ImageView(context);
        previewImageView.setId(R.id.iv_imageview_video);
        previewImageView.setImageResource(R.color.color_f1);
        previewImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(previewImageView);

        //视频控制界面
        controlView = (RelativeLayout) View.inflate(context, R.layout.base_player_control_layout, null);
        progress_bar = (ProgressBar) controlView.findViewById(R.id.progress_bar);
        iv_play = (ImageView) controlView.findViewById(R.id.iv_play);
        tv_current_time = (TextView) controlView.findViewById(R.id.tv_current_time);
        seek_bar = (SeekBar) controlView.findViewById(R.id.seek_bar);
        seek_bar.setMax(1000);
        tv_duration_time = (TextView) controlView.findViewById(R.id.tv_duration_time);
        iv_transfer_screen = (ImageView) controlView.findViewById(R.id.iv_transfer_screen);
        ll_bottom_control = (LinearLayout) controlView.findViewById(R.id.ll_bottom_control);
        llBottomControlHeight = context.getResources().getDimensionPixelOffset(R.dimen.ll_bottom_control_height);

        iv_close_audio = (ImageView) controlView.findViewById(R.id.iv_close_audio);
        iv_close_audio.setSelected(MHApplication.audioIsSilence);

        addView(controlView);

        iv_play.setOnClickListener(this);
        iv_transfer_screen.setOnClickListener(this);
        iv_close_audio.setOnClickListener(this);


        screenHeight = DensityUtil.getScreenHeight(context);
        screenWidth = DensityUtil.getScreenWidth(context);


        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(MSG_UPDATE_PROGRESS);
                handler.removeMessages(MSG_HID_CONTROL);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo((int) (seekBar.getProgress() * 0.001 * mediaPlayer.getDuration()));
                }
//                handler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
            }
        });
        findViewById(R.id.rl_seek_bar_wrap).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handler.removeMessages(MSG_UPDATE_PROGRESS);
                handler.removeMessages(MSG_HID_CONTROL);
                seek_bar.onTouchEvent(event);
                return true;
            }
        });


        final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                onControlDoubleClick();
                MHLogUtil.d(TAG, "onDoubleTap");
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                MHLogUtil.d(TAG, "onSingleTapConfirmed");
                handler.removeMessages(MSG_HID_CONTROL);
                if (CURRENT_MEDIA_STATE != MEDIA_STATE_IDLE && !isLoading) {
                    handler.sendEmptyMessageDelayed(MSG_HID_CONTROL, HIDE_CONTROL_DELAYED_TIME);
                    if (iv_play.getVisibility() == VISIBLE) { //以一个为准,防止交错
                        hidePlayView();
                        hideControlView();
                    } else {
                        showControlView();
                        showPlayView();
                    }
                }
                if (null != videoControlListener)
                    videoControlListener.videoViewState(VideoViewState.ON_SINGLE_CLICK, 0);
                return true;
            }
        });

        controlView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (null != broadcastReceiver) {
            context.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null != intent && CLOSE_AUDIO_ACTION.equals(intent.getAction())) {
                    if (null != iv_close_audio)
                        iv_close_audio.setSelected(MHApplication.audioIsSilence);
                    if (null != mediaPlayer) {
                        if (MHApplication.audioIsSilence) {
                            mediaPlayer.setVolume(0, 0);
                        } else {
                            mediaPlayer.setVolume(1, 1);
                        }
                    }
                }
            }
        };
        context.registerReceiver(broadcastReceiver, new IntentFilter(CLOSE_AUDIO_ACTION));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != broadcastReceiver) {
            context.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    protected void onControlDoubleClick() {
        if (null != videoControlListener) {
            videoControlListener.videoViewState(VideoViewState.ON_DOUBLE_CLICK, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                if (null != mediaPlayer) {
                    switch (CURRENT_MEDIA_STATE) {
                        case MEDIA_STATE_PLAYING:
                            pause();
                            break;
                        case MEDIA_STATE_PAUSE:
                            hidePreviewImage();
                            start();
                            break;
                    }
                }
                if (CURRENT_MEDIA_STATE == MEDIA_STATE_IDLE && null != videoControlListener)
                    videoControlListener.videoViewState(VideoViewState.ON_START_PLAY_CLICK, 0);
                break;
            case R.id.iv_transfer_screen:
                if (isFullScreen) {
                    quitFullScreen(false);
                } else {
                    setFullScreen();
                }
                break;
            case R.id.iv_close_audio:
                iv_close_audio.setSelected(!iv_close_audio.isSelected());
                MHApplication.audioIsSilence = iv_close_audio.isSelected();
                if (MHApplication.audioIsSilence) {
                    if (null != mediaPlayer)
                        mediaPlayer.setVolume(0, 0);
                } else {
                    if (null != mediaPlayer)
                        mediaPlayer.setVolume(1, 1);
                }
                if (null != videoControlListener)
                    videoControlListener.videoViewState(VideoViewState.ON_CLOSE_AUDIO_CLICK, MHApplication.audioIsSilence ? 1 : 0);
                context.sendBroadcast(new Intent(CLOSE_AUDIO_ACTION));
                break;
        }
    }

    public void pause() {
        MHLogUtil.d(TAG, "pause");
        handler.removeMessages(MSG_HID_CONTROL);
        handler.removeMessages(MSG_UPDATE_PROGRESS);
        if (null != mediaPlayer) {
            try {
                lastPlayDuration = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                showPlayView();
            } catch (Exception e) {
                MHLogUtil.e(TAG, "暂停出错--" + e.getMessage());
            }
            CURRENT_MEDIA_STATE = MEDIA_STATE_PAUSE;
            iv_play.setSelected(false);
        }
        if (null != videoControlListener)
            videoControlListener.videoViewState(VideoViewState.ON_VIDEO_PAUSE, 0);
    }

    public void startPlay(MyMediaPlayer mediaPlayer, VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
        if (null == videoInfo) {
            if (context instanceof BaseActivity) {
                BaseActivity baseActivity = (BaseActivity) context;
                baseActivity.showToastAtCenter("未知错误");
            }
            return;
        }
        MHLogUtil.d(TAG, "video_hls_uri=" + videoInfo.getHls_uri() + "\nvideoUrl=" + videoInfo.getVideo_uri());

        int video_state = videoInfo.getVideo_state();
        if (video_state < 10) {//表示视频状态异常,不支持播放
            if (context instanceof BaseActivity) {
                BaseActivity baseActivity = (BaseActivity) context;
                switch (video_state) {
                    case ConstantsValue.VideoState.STATE_TYPE_UNDEFINE:
                        baseActivity.showToastAtCenter("未知错误");
                        break;
                    case ConstantsValue.VideoState.STATE_TYPE_FREEZE:
                        baseActivity.showToastAtCenter("该视频已冻结");
                        break;
                    case ConstantsValue.VideoState.STATE_TYPE_HIDE:
                        baseActivity.showToastAtCenter("该视频已冻结");
                        break;
                    case ConstantsValue.VideoState.STATE_TYPE_DELETE:
                        baseActivity.showToastAtCenter("该视频已删除");
                        break;
                    case ConstantsValue.VideoState.STATE_TYPE_FAIL:
                        baseActivity.showToastAtCenter("未知错误");
                        break;
                    case ConstantsValue.VideoState.STATE_TYPE_OUTDATE:
                        baseActivity.showToastAtCenter("该视频已过期");
                        break;
                    case ConstantsValue.VideoState.STATE_TYPE_REPORT:
                        baseActivity.showToastAtCenter("该视频被举报,不能播放");
                        break;
                    case ConstantsValue.VideoState.STATE_TYPE_VEST:
                        baseActivity.showToastAtCenter("未知错误");
                        break;
                    case ConstantsValue.VideoState.STATE_TYPE_READ:
                        baseActivity.showToastAtCenter("未知错误");
                        break;
                    default:
                        baseActivity.showToastAtCenter("未知错误");
                        break;
                }
            }
            return;
        }
        if (MHStringUtils.isEmpty(videoInfo.getHls_uri()) || videoInfo.getHls_uri_state() < 10) {
            videoInfo.setHls_uri(videoInfo.getVideo_uri());
        }
        if (null == videoInfo.getHls_uri()) {
            if (context instanceof BaseActivity) {
                BaseActivity baseActivity = (BaseActivity) context;
                baseActivity.showToastAtCenter("未知错误");
            }
            return;
        }
        VideoStatisticUtil.startPlay(videoInfo);
        tv_duration_time.setText("00:00");

        MHLogUtil.d(TAG, "最终播放uri=" + videoInfo.getHls_uri());
        this.mediaPlayer = mediaPlayer;
        CURRENT_MEDIA_STATE = MEDIA_STATE_IDLE;
        setPosition(0);
        showLoading(true);
        try {
            mediaPlayer.reset();
            if (MHApplication.audioIsSilence) {
                mediaPlayer.setVolume(0, 0);
            } else {
                mediaPlayer.setVolume(1, 1);
            }
            mediaPlayer.setSurface(surface);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setPlayerEventListener(this);
            mediaPlayer.setDataSource(videoInfo.getHls_uri());
            mediaPlayer.prepareAsync();

            //高斯模糊, 这里去掉防止重播,一直高斯模糊
//            long currentTimeMillis = System.currentTimeMillis();
//            Blurry.with(context).capture(previewImageView).into(previewImageView);
//            MHLogUtil.d(TAG, "耗时=" + (System.currentTimeMillis() - currentTimeMillis));
        } catch (Exception e) {
            MHLogUtil.e(TAG, e.getMessage());
        }
    }

    /**
     * @param isLoading 是否正在加载
     */
    protected void showLoading(boolean isLoading) {
        this.isLoading = isLoading;
        iv_play.setVisibility(isLoading ? GONE : VISIBLE);
        progress_bar.setVisibility(isLoading ? VISIBLE : INVISIBLE);
        if (null != videoControlListener)
            videoControlListener.videoViewState(VideoViewState.ON_LOADING, isLoading ? 1 : 0);
    }


    protected void hideControlView() {
        if (ll_bottom_control.getVisibility() != VISIBLE) return;

        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f);

        translateAnimation.setDuration(300);
//        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_bottom_control.setVisibility(GONE);
                ll_bottom_control.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_bottom_control.startAnimation(translateAnimation);
    }

    protected void showControlView() {
        if (ll_bottom_control.getVisibility() == VISIBLE) return;
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(300);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ll_bottom_control.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_bottom_control.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_bottom_control.startAnimation(translateAnimation);
    }


    protected void hidePlayView() {
        if (iv_play.getVisibility() == GONE) return;
        MHLogUtil.d(TAG, "hidePlayView--" + this);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_play.setVisibility(GONE);
                iv_play.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_play.startAnimation(alphaAnimation);
    }

    public void showPlayView() {
        if (iv_play.getVisibility() == VISIBLE) return;
        MHLogUtil.d(TAG, "showPlayView--" + this);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iv_play.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_play.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_play.startAnimation(alphaAnimation);
    }

    public void showPreviewImage() {
        if (previewImageView.getVisibility() == VISIBLE) return;
        MHLogUtil.d(TAG, "showPreviewImage");
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                previewImageView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                previewImageView.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        previewImageView.startAnimation(alphaAnimation);
    }

    private void hidePreviewImage() {
        if (previewImageView.getVisibility() == GONE || previewImageView.isSelected()) return;
        previewImageView.setSelected(true);
        MHLogUtil.d(TAG, "hidePreviewImage");
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(600);
        alphaAnimation.setFillAfter(true);

        //有时候添加Animation监听会不执行,原因不明
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                previewImageView.setVisibility(GONE);
                previewImageView.clearAnimation();
                previewImageView.setSelected(false);
            }
        }, 600);
        previewImageView.startAnimation(alphaAnimation);
    }

    public ImageView getPreviewImageView() {
        return previewImageView;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surface = new Surface(surfaceTexture);
        MHLogUtil.d(TAG, "onSurfaceTextureAvailable");
        if (null != mediaPlayer)
            mediaPlayer.setSurface(surface);

        if (CURRENT_MEDIA_STATE != MEDIA_STATE_PLAYING) {
            showPlayView();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        MHLogUtil.d(TAG, "onSurfaceTextureSizeChanged--surface=" + surface + "--width=" + width + "---height=" + height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        MHLogUtil.d(TAG, "onSurfaceTextureDestroyed--surface=" + surface);
        if (CURRENT_MEDIA_STATE != MEDIA_STATE_PLAYING)
            previewImageView.setVisibility(VISIBLE);
        showPlayView();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    /**
     * @param position 设置当前进度,单位毫秒
     */
    protected void setPosition(float position) {
        int progress = (int) (position / videoDuration * 1000 + 0.5);
        seek_bar.setProgress(progress);
        if (0 == position) {
            seek_bar.setSecondaryProgress(0);
        }
        tv_current_time.setText(formatTime((long) (position / 1000f)));
    }

    /**
     * @param second 单位为秒
     * @return 返回格式为 23:21
     */
    public String formatTime(long second) {
        if (second <= 0) {
            return "00:00";
        }
        long hh = (long) (second / 3600 + 0.5);
        long mm = (long) (second % 3600 / 60 + 0.5);
        long ss = second % 60;
        String strTemp;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        return strTemp;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        MHLogUtil.d(TAG, "onPrepared--" + iMediaPlayer);
        if (null != mediaPlayer) {
            mediaPlayer.setSurface(surface);
            if (videoInfo.getLastPlayDuration() > 0 && videoDuration > videoInfo.getLastPlayDuration() + 1000 && lastPlayDuration != 0)//处理误差
                mediaPlayer.seekTo(videoInfo.getLastPlayDuration());
            start();
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        lastPlayDuration = videoDuration;
        MHLogUtil.d(TAG, "onCompletion--" + this);
        handler.removeMessages(MSG_HID_CONTROL);
        handler.removeMessages(MSG_UPDATE_PROGRESS);
        progress_bar.setVisibility(INVISIBLE);
        iv_play.setSelected(false);

        setPosition(0);
        CURRENT_MEDIA_STATE = MEDIA_STATE_IDLE;
        setKeepScreenOn(false);
        if (isFullScreen) {
            previewImageView.setVisibility(VISIBLE);
            quitFullScreen(true);
        }
//        iv_close_audio.setVisibility(GONE);
        showPlayView();
        showControlView();
        if (null != videoControlListener)
            videoControlListener.videoViewState(VideoViewState.ON_VIDEO_COMPLETE, 0);
    }

    public void quitFullScreen(final boolean isCompletion) {
        if (!isFullScreen) return;
        MHLogUtil.d(TAG, "quitFullScreen--" + this);
        shouldReset = false;

        isFullScreen = false;
        textureView.setRotation(videoRotation);
        iv_transfer_screen.setImageResource(R.drawable.icon_fullscreen);

        int finalWidth = videoWidth;
        int finalHeight = videoHeight;

        if (videoRotation == 90 || videoRotation == 270) {
            finalWidth = videoHeight;
            finalHeight = videoWidth;
        }
        removeOnBackDownListener();
        currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        final Activity activity = (Activity) this.context;

        if (finalHeight >= finalWidth) {
            setBackgroundColor(Color.TRANSPARENT);

            final int statusBarHeight = ScreenUtils.getStatusBarHeight(context);
            final int moveLength = (screenHeight - originHeight) / 2;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();

                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.height = (int) (screenHeight - moveLength * animatedValue * 2);
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    setLayoutParams(layoutParams);
                    setY((lastStartY - statusBarHeight) * animatedValue);
                }
            });

            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setY(0);
                    if (!isCompletion)
                        hidePreviewImage();
                    WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
                    attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    Window window = activity.getWindow();
                    window.setAttributes(attrs);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    ViewGroup decorView = (ViewGroup) window.getDecorView();
                    decorView.removeView(BaseVideoView.this);
                    videoViewParent.addView(BaseVideoView.this);

                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    setLayoutParams(layoutParams);
                    setBackgroundColor(Color.BLACK);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            valueAnimator.start();
        } else {
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Window window = activity.getWindow();
            window.setAttributes(attrs);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView.removeView(BaseVideoView.this);
            videoViewParent.addView(BaseVideoView.this);

            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            setLayoutParams(layoutParams);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            changeSize();
        }
    }

    /**
     * 设置全屏
     */
    public void setFullScreen() {
        setFullScreen(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置全屏
     **/
    public void setFullScreen(int requestedOrientation) {
        MHLogUtil.d(TAG, "setFullScreen--" + this);
        if (isFullScreen) return;
        shouldReset = false;
        isFullScreen = true;

        initOnBackDownListener();
        iv_transfer_screen.setImageResource(R.drawable.icon_nonfullscreen);

        int[] location = new int[2];
        getLocationInWindow(location);
        lastStartY = location[1];
        originHeight = getHeight();

        Activity activity = (Activity) this.context;
        videoViewParent = (ViewGroup) this.getParent();

        Window window = activity.getWindow();

        int finalWidth = videoWidth;
        int finalHeight = videoHeight;

        if (videoRotation == 90 || videoRotation == 270) {
            finalWidth = videoHeight;
            finalHeight = videoWidth;
        }

        if (finalHeight >= finalWidth) {
            if (!((currentOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE && requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                    || (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE && currentOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE))) {
                videoViewParent.removeView(this);

                ViewGroup decorView = (ViewGroup) window.getDecorView();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = textureView.getHeight();
                layoutParams.width = LayoutParams.MATCH_PARENT;
                setLayoutParams(layoutParams);

                decorView.addView(this);
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

            final int moveLength = (screenHeight - textureView.getHeight()) / 2;

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    int height = (int) (textureView.getHeight() + 2 * moveLength * animatedValue);
                    if (height > screenHeight) height = screenHeight;
                    layoutParams.height = height;
                    layoutParams.width = LayoutParams.MATCH_PARENT;
                    setLayoutParams(layoutParams);
                }
            });
            valueAnimator.start();
        } else {
            if (!((currentOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE && requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                    || (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE && currentOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE))) {
                videoViewParent.removeView(this);
                ViewGroup decorView = (ViewGroup) window.getDecorView();
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.height = LayoutParams.MATCH_PARENT;
                layoutParams.width = LayoutParams.MATCH_PARENT;
                setLayoutParams(layoutParams);
                decorView.addView(this);
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
            activity.setRequestedOrientation(requestedOrientation);
            changeSize();
        }
        currentOrientation = requestedOrientation;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_UNKNOWN:
                MHLogUtil.d(TAG, "--media_info_unknown");
                break;
            case IMediaPlayer.MEDIA_INFO_STARTED_AS_NEXT:
                MHLogUtil.d(TAG, "--media_info_started_as_next");
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                MHLogUtil.d(TAG, "--media_info_video_rendering_start");
                hidePreviewImage();
                showLoading(false);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                MHLogUtil.d(TAG, "--media_info_video_track_lagging");
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                MHLogUtil.d(TAG, "--media_info_buffering_start");
                showLoading(true);
                //高斯模糊
                //防止视频还没有加载出来,这样就会出现黑屏
                long currentTimeMillis = System.currentTimeMillis();
                if (null != mediaPlayer)
                    MHLogUtil.d(TAG, "getCurrentPosition=" + mediaPlayer.getCurrentPosition());

                if (null != mediaPlayer && mediaPlayer.getCurrentPosition() > 1000) {
                    Bitmap drawingCache = textureView.getBitmap();
                    if (null != drawingCache && !drawingCache.isRecycled()
                            && drawingCache.getHeight() > 0 && drawingCache.getWidth() > 0) {

                        if (!isBlack(drawingCache)) {
                            if (videoRotation == 90 || videoRotation == 270) {
                                drawingCache = BitmapUtil.rotateBitmap(drawingCache, 90);
                            }
                            previewImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            Blurry.with(context).radius(25).from(drawingCache).into(previewImageView);
                        } else {
                            MHLogUtil.e(TAG, "drawingCache is black");
                        }
                    }
                    MHLogUtil.d(TAG, "from---耗时=" + (System.currentTimeMillis() - currentTimeMillis));
                }
                showPreviewImage();
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                MHLogUtil.d(TAG, "--media_info_buffering_end");
                if (previewImageView.getVisibility() == VISIBLE) {
                    hidePreviewImage();
                }
                if (CURRENT_MEDIA_STATE == MEDIA_STATE_PAUSE) {
                    try {
                        mediaPlayer.start();
                    } catch (Exception e) {
                        MHLogUtil.e(TAG, e);
                        MHLogUtil.e(TAG, e.getMessage());
                    }
                }
                isLoading = false;
                progress_bar.setVisibility(INVISIBLE);
                handler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
                handler.sendEmptyMessageDelayed(MSG_HID_CONTROL, HIDE_CONTROL_DELAYED_TIME);
                break;
            case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                MHLogUtil.d(TAG, "--media_info_network_bandwidth");
                break;
            case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                MHLogUtil.d(TAG, "--media_info_bad_interleaving");
                break;
            case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                MHLogUtil.d(TAG, "--media_info_not_seekable");
                break;
            case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                MHLogUtil.d(TAG, "--media_info_metadata_update");
                break;
            case IMediaPlayer.MEDIA_INFO_TIMED_TEXT_ERROR:
                MHLogUtil.d(TAG, "--media_info_timed_text_error");
                break;
            case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                MHLogUtil.d(TAG, "--media_info_unsupported_subtitle");
                break;
            case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                MHLogUtil.d(TAG, "--media_info_subtitle_timed_out");
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                MHLogUtil.d(TAG, "--media_info_video_rotation_changed---extra=" + extra);
                videoRotation = extra;
                textureView.setRotation(extra);
                break;
            case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                MHLogUtil.d(TAG, "--media_info_audio_rendering_start");
                break;

        }
        return false;
    }


    /**
     * @return 图片是否是黑色的
     */
    private boolean isBlack(Bitmap bitmap) {
        if (null == bitmap || bitmap.isRecycled() || bitmap.getHeight() < 0 || bitmap.getWidth() < 0)
            return false;
        //取5点判断
        int blackCount = 0;
        int targetHeight = bitmap.getHeight() - 1;
        int targetWidth = bitmap.getWidth() - 1;

        int pixel = bitmap.getPixel(0, 0);
        if (isPixelBlack(pixel)) {
            blackCount++;
        }
        pixel = bitmap.getPixel(targetWidth, 0);
        if (isPixelBlack(pixel)) {
            blackCount++;
        }
        pixel = bitmap.getPixel(targetWidth, targetHeight);
        if (isPixelBlack(pixel)) {
            blackCount++;
        }
        pixel = bitmap.getPixel(0, targetHeight);
        if (isPixelBlack(pixel)) {
            blackCount++;
        }
        pixel = bitmap.getPixel(targetWidth, targetHeight / 2);
        if (isPixelBlack(pixel)) {
            blackCount++;
        }
        return blackCount >= 5;
    }

    /**
     * @return 像素是否是黑色
     */
    private boolean isPixelBlack(int pixel) {
        if (Color.red(pixel) == Color.blue(pixel)
                && Color.red(pixel) == Color.green(pixel)
                && Color.green(pixel) == Color.blue(pixel)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        setKeepScreenOn(false);
        CURRENT_MEDIA_STATE = MEDIA_STATE_IDLE;
        handler.removeMessages(MSG_HID_CONTROL);
        handler.removeMessages(MSG_UPDATE_PROGRESS);
        String errorMessage = "未知错误,请稍后再试";
        switch (what) {
            case IMediaPlayer.MEDIA_ERROR_UNKNOWN:
                MHLogUtil.d(TAG, "--media_error_unknown");
                break;
            case IMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                MHLogUtil.d(TAG, "--media_error_server_died");
                break;
            case IMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                MHLogUtil.d(TAG, "--media_error_not_valid_for_progressive_playback");
                break;
            case IMediaPlayer.MEDIA_ERROR_IO:
                MHLogUtil.d(TAG, "--media_error_io");
                break;
            case IMediaPlayer.MEDIA_ERROR_MALFORMED:
                MHLogUtil.d(TAG, "--media_error_malformed");
                break;
            case IMediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                MHLogUtil.d(TAG, "--media_error_unsupported");
                errorMessage = "对不起,暂时不支持该格式的视频";
                break;
            case IMediaPlayer.MEDIA_ERROR_TIMED_OUT:
                MHLogUtil.d(TAG, "--media_error_timed_out");
                errorMessage = ConstantsValue.Other.NETWORK_ERROR_TIP_MSG;
                break;
            case -10000://网络错误
                MHLogUtil.d(TAG, "--media_error_net");
                isNetWorkError = true;
                errorMessage = ConstantsValue.Other.NETWORK_ERROR_TIP_MSG;
                break;

        }
        if (null != videoControlListener)
            videoControlListener.videoViewState(VideoViewState.ON_VIDEO_ERROR, what);
        if (context instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) this.context;
            baseActivity.showToastAtCenter(errorMessage);
        }
        if (null != mediaPlayer) mediaPlayer.reset();
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        MHLogUtil.d(TAG, "onVideoSizeChanged---mp=" + mp + "--width=" + width + "--height=" + height);
        if (width == 0 || height == 0) return;
        //保证视频不变形
        videoWidth = width;
        videoHeight = height;

        changeSize();
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
        seek_bar.setSecondaryProgress(percent);
    }

    @Override
    public void mediaPlayerPause() {
        shouldReset = true;
        lastPlayDuration = mediaPlayer.getCurrentPosition();
        handler.removeMessages(MSG_HID_CONTROL);
        handler.removeMessages(MSG_UPDATE_PROGRESS);
        iv_play.setSelected(false);
        CURRENT_MEDIA_STATE = MEDIA_STATE_PAUSE;

        showPlayView();
        setKeepScreenOn(false);
        MHLogUtil.d(TAG, "mediaPlayerPause");
        if (null != videoControlListener)
            videoControlListener.videoViewState(VideoViewState.ON_VIDEO_PAUSE, 0);
    }

    @Override
    public void mediaPlayerStart() {
//        hidePreviewImage();
        handler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
        CURRENT_MEDIA_STATE = MEDIA_STATE_PLAYING;
        iv_play.setSelected(true);
//        handler.sendEmptyMessageDelayed(MSG_HID_CONTROL, HIDE_CONTROL_DELAYED_TIME);
        setKeepScreenOn(true);
        MHLogUtil.d(TAG, "mediaPlayerStart");
    }

    @Override
    public void mediaPlayerReset() {
        handler.removeMessages(MSG_HID_CONTROL);
        handler.removeMessages(MSG_UPDATE_PROGRESS);
        CURRENT_MEDIA_STATE = MEDIA_STATE_IDLE;
        iv_play.setSelected(false);

        setKeepScreenOn(false);
        MHLogUtil.d(TAG, "mediaPlayerReset");
    }

    @Override
    public void mediaPlayerRelease() {
        MHLogUtil.d(TAG, "mediaPlayerRelease");
        VideoStatisticUtil.uploadCount();
    }

    private void removeOnBackDownListener() {
        backDownListener = null;
    }


    protected void start() {
        isNetWorkError = false;
        if (null != mediaPlayer) {
            handler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
            mediaPlayer.start();
            if (mediaPlayer.getDuration() >= 0) {
                this.videoDuration = mediaPlayer.getDuration();
                tv_duration_time.setText(formatTime((long) (videoDuration / 1000f)));
            }
            CURRENT_MEDIA_STATE = MEDIA_STATE_PLAYING;
            iv_play.setSelected(true);
            handler.sendEmptyMessageDelayed(MSG_HID_CONTROL, 2000);
        }
        iv_close_audio.setVisibility(VISIBLE);
        if (null != videoControlListener)
            videoControlListener.videoViewState(VideoViewState.ON_VIDEO_START, 0);
        MHLogUtil.d(TAG, "start");
    }

    /**
     * 重新开始
     */
    public void reStart() {
        isNetWorkError = false;
        lastPlayDuration = 0;
        if (null != videoControlListener)
            videoControlListener.videoViewState(VideoViewState.ON_START_PLAY_CLICK, 0);
    }

    public void reset(int position) {
        if (!shouldReset) {
            shouldReset = true;
            return;
        }

        if (MHApplication.audioIsSilence) {
            if (null != mediaPlayer)
                mediaPlayer.setVolume(0, 0);
        } else {
            if (null != mediaPlayer)
                mediaPlayer.setVolume(1, 1);
        }
        iv_close_audio.setSelected(MHApplication.audioIsSilence);

        if (null != mediaPlayer) {
            lastPlayDuration = mediaPlayer.getCurrentPosition();
            mediaPlayer.setSurface(null);
            mediaPlayer.setOnPreparedListener(null);
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnInfoListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.setOnVideoSizeChangedListener(null);
            mediaPlayer.setOnBufferingUpdateListener(null);
            mediaPlayer.setPlayerEventListener(null);
            mediaPlayer = null;
        }
        handler.removeMessages(MSG_HID_CONTROL);
        handler.removeMessages(MSG_UPDATE_PROGRESS);

        tv_duration_time.setText("00:00");
        setPosition(0);
        CURRENT_MEDIA_STATE = MEDIA_STATE_IDLE;
        ll_bottom_control.setVisibility(INVISIBLE);
        progress_bar.setVisibility(INVISIBLE);
        iv_play.clearAnimation();
        iv_play.setSelected(false);
        iv_play.setVisibility(VISIBLE);


        previewImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        previewImageView.setVisibility(VISIBLE);

        iv_close_audio.setVisibility(VISIBLE);
        iv_close_audio.setSelected(MHApplication.audioIsSilence);
        if (null != videoControlListener)
            videoControlListener.videoViewState(VideoViewState.ON_VIDEO_RESET, 0);

        //防止有上一次的缓存图像
        removeView(textureView);
        addView(textureView, 0, textureViewLayoutParams);
        MHLogUtil.d(TAG, "---reset---" + this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_bottom_control.getLayoutParams();
        layoutParams.topMargin = getHeight() - llBottomControlHeight;
        layoutParams.height = llBottomControlHeight;
        ll_bottom_control.setLayoutParams(layoutParams);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_bottom_control.getLayoutParams();
        layoutParams.topMargin = h - llBottomControlHeight;
        layoutParams.height = llBottomControlHeight;
        ll_bottom_control.setLayoutParams(layoutParams);
        ll_bottom_control.setVisibility(INVISIBLE);
        changeSize();
    }

    public void changeSize() {//如果View没有一边填充满屏幕,可能会导致视频变形
        if (videoWidth == 0 || videoHeight == 0) return;
        MHLogUtil.d(TAG, "changeSize");
        FrameLayout.LayoutParams textureViewLayoutParams = (LayoutParams) textureView.getLayoutParams();

        float ratio = 1.0f * videoWidth / videoHeight;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);

        if (videoRotation == 90 || videoRotation == 270) {//倒置宽高
            if (isFullScreen) {
                if (ratio >= 1) {//以宽为准
                    //已测
                    textureViewLayoutParams.width = dm.heightPixels;
                    textureViewLayoutParams.height = (int) (dm.heightPixels / ratio);
                } else {//以高为准
                    textureView.setRotation(0);
                    textureViewLayoutParams.width = (int) (getHeight() * ratio);
                    textureViewLayoutParams.height = getHeight();
                }
            } else {
                if (ratio >= 1) {
                    //已测
                    textureViewLayoutParams.width = getHeight();
                    textureViewLayoutParams.height = (int) (getHeight() / ratio);
                } else {
                    textureViewLayoutParams.width = getHeight();
                    textureViewLayoutParams.height = (int) (getHeight() * ratio);
                }
            }
        } else {
            if (isFullScreen) {
                textureView.setRotation(0);
                if (ratio >= 1) {//以宽为准
                    //已测
                    textureViewLayoutParams.width = dm.widthPixels;
                    textureViewLayoutParams.height = (int) (dm.widthPixels / ratio);
                } else {//以高为准
                    //已测
                    textureViewLayoutParams.width = (int) (dm.heightPixels * ratio);
                    textureViewLayoutParams.height = dm.heightPixels;
                }
            } else {
                if (ratio >= 1) {//以宽为准
                    //已测
                    textureViewLayoutParams.width = getWidth();
                    textureViewLayoutParams.height = (int) (getWidth() / ratio);
                } else {//以高为准
                    //已测
                    textureViewLayoutParams.width = (int) (getHeight() * ratio);
                    textureViewLayoutParams.height = getHeight();
                }
            }
        }
        textureViewLayoutParams.gravity = Gravity.CENTER;
        textureViewLayoutParams.topMargin = 0;
        textureViewLayoutParams.leftMargin = 0;
        textureView.setLayoutParams(textureViewLayoutParams);
    }

    private void initOnBackDownListener() {
        if (null == backDownListener)
            backDownListener = new OnBackDownListener() {
                @Override
                public boolean OnBackDown() {
                    if (isFullScreen) {
                        quitFullScreen(false);
                        return true;
                    }
                    return false;
                }
            };
    }


    public void setVideoControlListener(VideoControlListener videoControlListener) {
        this.videoControlListener = videoControlListener;
    }

    /**
     * @return 视频实际展示的高
     */
    public int getVideoHeight() {
        if (videoHeight <= 0 || videoWidth <= 0) return 1;

        if (videoRotation == 90 || videoRotation == 270) {
            return videoWidth;
        } else {
            return videoHeight;
        }
    }

    /**
     * @return 视频实际展示的宽
     */
    public int getVideoWidth() {
        if (videoHeight <= 0 || videoWidth <= 0) return 1;
        if (videoRotation == 90 || videoRotation == 270) {
            return videoHeight;
        } else {
            return videoWidth;
        }
    }


    /**
     * @return 视频实际渲染的高
     */
    public int getVideoRenderHeight() {
        if (null != textureView) {
            if (videoRotation == 90 || videoRotation == 270)
                return textureView.getLayoutParams().width;
            else
                return textureView.getLayoutParams().height;
        }
        return 1;
    }

    /**
     * @return 视频实际渲染的宽
     */
    public int getVideoRenderWidth() {
        if (null != textureView) {
            if (videoRotation == 90 || videoRotation == 270)
                return textureView.getLayoutParams().height;
            else
                return textureView.getLayoutParams().width;
        }
        return 1;
    }


    public int getVideoRotate() {
        return videoRotation;
    }

    public enum VideoViewState {
        ON_START_PLAY_CLICK, ON_SINGLE_CLICK, ON_DOUBLE_CLICK,
        ON_LOADING, ON_VIDEO_START, ON_VIDEO_PAUSE,
        ON_VIDEO_RESET, ON_VIDEO_ERROR, ON_VIDEO_COMPLETE,
        ON_CLOSE_AUDIO_CLICK
    }

    public interface VideoControlListener {
        /**
         * @param videoViewState 各种状态
         * @param extra          额外信息
         */
        void videoViewState(VideoViewState videoViewState, int extra);
    }
}

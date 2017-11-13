package com.haiqiu.miaohi.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.VideoRecorderObject;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.utils.BitmapUtil;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.widget.ProgressView;
import com.haiqiu.miaohi.widget.RatioView;
import com.haiqiu.miaohi.widget.VerticalSeekBar;
import com.haiqiu.miaohi.widget.tablayout.CommonTabLayout;
import com.haiqiu.miaohi.widget.tablayout.CustomTabEntity;
import com.haiqiu.miaohi.widget.tablayout.OnTabSelectListener;
import com.haiqiu.miaohi.widget.tablayout.TabEntity;
import com.tendcloud.tenddata.TCAgent;
import com.yixia.camera.MediaRecorderBase;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.view.BZGLSurfaceView;
import org.wysaid.view.CameraGLSurfaceView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;


/**
 * Created by zhandalin on 2016-11-28 11:27.
 * 说明:视频录制
 */
public class VideoRecorderActivity extends BaseActivity implements View.OnClickListener, OnTabSelectListener, MediaRecorderBase.OnErrorListener, MediaRecorderBase.OnEncodeListener {
    private static final int MSG_WHAT_UPDATE_TIME = 20;
    private static final int MSG_WHAT_HIDE_RECORD_FOCUS = 21;
    private static final int MSG_WHAT_HIDE_TIP_VIEW = 22;
    private static final int MSG_WHAT_HIDE_EXPOSURE_VIEW = 23;

    private static final int HIDE_TIP_VIEW_DELAYED_TIME = 3000;

    private int RECORDER_MIN_TIME = 3000;
    private int RECORDER_MAX_TIME = 3 * 60 * 1000;

    private static final int DEFAULT_RECORD_WIDTH = 480;

    private static final String FACE_BEAUTY_CONFIG_BASE = "@beautify face 0.6 ";

    private static final String TIP_MSG_RECORD_MIN_ASK_AND_ANSWER = "回答映答至少拍摄30秒";
    private static final String TIP_MSG_RECORD_MIN_COMMON = "至少录制3秒";
    private static final String TIP_MSG_LAST_15_SECOND = "还能拍摄";


    private ImageView iv_close;
    private ImageView iv_beautiful_face;
    private ImageView iv_flash_light;
    private ImageView iv_face_camera;
    private BZGLSurfaceView bz_glSurfaceView;
    private ProgressView progress_view;
    private TextView tv_time;
    private ImageView iv_delete;
    private Button bt_recorder;
    private ImageView iv_confirm;
    private CommonTabLayout tabLayout;
    private boolean isRecording;
    private String videoRootPath;
    private long recorderTime;
    private MediaRecorderBase mMediaRecorder;
    private int dp_1;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_UPDATE_TIME:
                    handler.removeMessages(MSG_WHAT_UPDATE_TIME);
                    if (null != mMediaObject) {
                        recorderTime = mMediaObject.getDuration();
                    }
//                    MHLogUtil.e(TAG, "msg_what_update_time");
                    if (null != tv_time) {
                        tv_time.setText(CommonUtil.formatTime4S(recorderTime));
                    }
                    if (null != progress_view) {
                        progress_view.setCurrentDuration((int) recorderTime);
                    }
                    checkButtonState();
                    handler.sendEmptyMessageDelayed(MSG_WHAT_UPDATE_TIME, 100);
                    break;
                case MSG_WHAT_HIDE_RECORD_FOCUS:
                    record_focusing.setVisibility(View.GONE);
                    break;
                case MSG_WHAT_HIDE_TIP_VIEW:
                    hideTipView();
                    break;
                case MSG_WHAT_HIDE_EXPOSURE_VIEW:
                    hideExposureView();
                    break;
            }
            return true;
        }
    });
    private int screenWidth;
    private int screenHeight;
    private int mFocusWidth;
    private ImageView record_focusing;
    private Animation mFocusAnimation;
    private boolean isBeautifulFace;
    private VerticalSeekBar seek_bar;
    private View tv_exposure;
    private RatioView ratio_view;
    private LinearLayout ll_ratio_container;
    private boolean ratioAnimationIsShowing;
    private boolean ratioIsShow;//是否正在展示比例控件
    private VideoUploadInfo videoUploadInfo;
    private int currentIndex = 1;
    private Point lastVideoRatio = new Point(1, 1);
    private Point lastPictureRatio = new Point(1, 1);
    private View line;
    private View iv_record_animation;
    private RotateAnimation rotateAnimation;
    private MediaObject mMediaObject;
    private boolean enablePreview;
    private int video_bottom_control_height;
    private AnimationSet animationSet;
    private LinearLayout ll_tip_layout;
    private TextView tv_tip;

    private boolean isHideTipViewing;
    private boolean doConfirmActioning;
    private View cover_top;
    private View cover_bottom;
    private int videoLayoutHeight;
    private ImageView iv_preview;
    private volatile boolean isTakePicture;
    private int navigation_height;
    private View fl_video_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recorder);
        TCAgent.onEvent(context, "拍摄页面" + ConstantsValue.android);
        requestAudioPermission();
        videoUploadInfo = getIntent().getParcelableExtra("videoUploadInfo");
        if (null == videoUploadInfo) videoUploadInfo = new VideoUploadInfo();
        if (videoUploadInfo.getFromInfo() == VideoUploadInfo.FROM_ASK_AND_ANSWER) {
            RECORDER_MIN_TIME = 30 * 1000;
            RECORDER_MAX_TIME = 5 * 60 * 1000;
        } else {
            RECORDER_MIN_TIME = 3 * 1000;
            RECORDER_MAX_TIME = 3 * 60 * 1000;
        }

        initView();
        initListener();
    }

    private void initView() {
        navigation_height = getResources().getDimensionPixelSize(R.dimen.navigation_height);
        dp_1 = DensityUtil.dip2px(context, 1);
        Point screenSize = ScreenUtils.getScreenSize(this);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;

        mFocusWidth = 64 * dp_1;
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_beautiful_face = (ImageView) findViewById(R.id.iv_beautiful_face);
        iv_flash_light = (ImageView) findViewById(R.id.iv_flash_light);
        iv_face_camera = (ImageView) findViewById(R.id.iv_face_camera);

        fl_video_layout = findViewById(R.id.fl_video_layout);
        ViewGroup.LayoutParams layoutParams = fl_video_layout.getLayoutParams();
        videoLayoutHeight = screenWidth * 4 / 3;
        layoutParams.width = screenWidth;
        layoutParams.height = videoLayoutHeight;
        fl_video_layout.setLayoutParams(layoutParams);

        bz_glSurfaceView = (BZGLSurfaceView) findViewById(R.id.bz_glSurfaceView);
//        bz_glSurfaceView.setClearColor(0.113f, 0.113f, 0.113f, 1);
        bz_glSurfaceView.setRecordRatio(new Point(3, 4));

        cover_top = findViewById(R.id.cover_top);
        cover_bottom = findViewById(R.id.cover_bottom);

        //默认是1:1
        int space = (videoLayoutHeight - screenWidth) / 2;
        cover_top.setY(-videoLayoutHeight + space);
        cover_bottom.setY(videoLayoutHeight - space);


        record_focusing = (ImageView) findViewById(R.id.record_focusing);
        record_focusing.setImageResource(R.drawable.shape_video_focus);

        if (!CameraGLSurfaceView.isSupportFrontCamera()) {
            iv_face_camera.setVisibility(View.GONE);
        }
        if (!DeviceUtils.isSupportCameraLedFlash(getPackageManager())) {
            iv_flash_light.setVisibility(View.GONE);
        }
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(-1);

        iv_record_animation = findViewById(R.id.iv_record_animation);
        progress_view = (ProgressView) findViewById(R.id.progress_view);
        progress_view.setMaxDuration(RECORDER_MAX_TIME);
        progress_view.setMiniDuration(RECORDER_MIN_TIME);
        progress_view.setEnabled(false);
        progress_view.setAlpha(0f);

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setAlpha(0);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        bt_recorder = (Button) findViewById(R.id.bt_recorder);
        iv_confirm = (ImageView) findViewById(R.id.iv_confirm);
        tabLayout = (CommonTabLayout) findViewById(R.id.tabLayout);
        iv_delete.setEnabled(false);
        iv_delete.setAlpha(0f);
        iv_confirm.setEnabled(false);
        iv_confirm.setAlpha(0f);

        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        mTabEntities.add(new TabEntity("图库", 0, 0));
        mTabEntities.add(new TabEntity("视频", 0, 0));
        if (VideoUploadInfo.FROM_ASK_AND_ANSWER != videoUploadInfo.getFromInfo())
            mTabEntities.add(new TabEntity("拍照", 0, 0));
        tabLayout.setTabData(mTabEntities);
        tabLayout.setCurrentTab(1);
        tabLayout.setOnTabSelectListener(this);
        RelativeLayout rl_bottom_control_layout = (RelativeLayout) findViewById(R.id.rl_bottom_control_layout);
        //适配底部布局
        video_bottom_control_height = getResources().getDimensionPixelSize(R.dimen.video_bottom_control_height);
        int targetHeight = screenHeight - videoLayoutHeight;
        if (targetHeight > video_bottom_control_height) {
            MHLogUtil.d(TAG, "需要拉伸到--=" + targetHeight);
            ViewGroup.LayoutParams params = rl_bottom_control_layout.getLayoutParams();
            params.height = targetHeight;
            video_bottom_control_height = targetHeight;
            rl_bottom_control_layout.setLayoutParams(params);
        } else {//这种是长度不够,底部覆盖了视频预览区域
            View rl_ratio_container = findViewById(R.id.rl_ratio_container);
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) rl_ratio_container.getLayoutParams();
            layoutParams1.bottomMargin = getResources().getDimensionPixelSize(R.dimen.ratio_container_margin_bottom) + (video_bottom_control_height - targetHeight);
            rl_ratio_container.setLayoutParams(layoutParams1);
        }

        seek_bar = (VerticalSeekBar) findViewById(R.id.seek_bar);
        seek_bar.setMax(99);
        seek_bar.setProgress(50);

        tv_exposure = findViewById(R.id.tv_exposure);
        if (SpUtils.getBoolean("is_first_record_video", true)) {
            tv_exposure.setVisibility(View.VISIBLE);
            seek_bar.setVisibility(View.VISIBLE);
        } else {
            seek_bar.setVisibility(View.INVISIBLE);
            tv_exposure.setVisibility(View.GONE);
        }

        line = findViewById(R.id.line);
        line.setAlpha(0);
        ratio_view = (RatioView) findViewById(R.id.ratio_view);
        ll_ratio_container = (LinearLayout) findViewById(R.id.ll_ratio_container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = DensityUtil.dip2px(context, 15);
        params.leftMargin = margin;
        params.rightMargin = margin;

        RatioView ratio_view4_3 = new RatioView(context);
        ratio_view4_3.setId(R.id.ratio_view4_3);
        ratio_view4_3.setOnClickListener(this);
        ratio_view4_3.setRatio(new Point(4, 3));

        RatioView ratio_view1_1 = new RatioView(context);
        ratio_view1_1.setId(R.id.ratio_view1_1);
        ratio_view1_1.setSelected(true);
        ratio_view1_1.setOnClickListener(this);
        ratio_view1_1.setRatio(new Point(1, 1));

        RatioView ratio_view3_4 = new RatioView(context);
        ratio_view3_4.setId(R.id.ratio_view3_4);
        ratio_view3_4.setOnClickListener(this);
        ratio_view3_4.setRatio(new Point(3, 4));

        RatioView ratio_view16_9 = new RatioView(context);
        ratio_view16_9.setId(R.id.ratio_view16_9);
        ratio_view16_9.setOnClickListener(this);
        ratio_view16_9.setRatio(new Point(16, 9));

        ll_ratio_container.addView(ratio_view1_1, params);
        ll_ratio_container.addView(ratio_view3_4, params);
        ll_ratio_container.addView(ratio_view4_3, params);
        ll_ratio_container.addView(ratio_view16_9, params);
        ll_ratio_container.setX(screenWidth);

        iv_preview = (ImageView) findViewById(R.id.iv_preview);
    }

    private void initListener() {
        iv_delete.setEnabled(false);
        iv_delete.setOnClickListener(this);

        iv_confirm.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        iv_beautiful_face.setOnClickListener(this);
        iv_flash_light.setOnClickListener(this);
        iv_face_camera.setOnClickListener(this);
        ratio_view.setOnClickListener(this);


        if (DeviceUtils.hasICS())
            bz_glSurfaceView.setOnTouchListener(mOnSurfaveViewTouchListener);

        bz_glSurfaceView.setOnSurfaceCreatedListener(new BZGLSurfaceView.OnSurfaceCreatedListener() {
            @Override
            public void onOnSurfaceCreatedListener(final SurfaceTexture surfaceTexture) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null == mMediaRecorder)
                            initMediaRecorder();
                        mMediaRecorder.prepare(surfaceTexture);
                    }
                });
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                MHLogUtil.d(TAG, "onLongPress");
                if (currentIndex == 1)
                    startRecord();
            }

        });
        bt_recorder.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                if (MotionEvent.ACTION_DOWN == event.getAction() && currentIndex == 2) {
                    takePicture();
                }
                if (MotionEvent.ACTION_UP == event.getAction() || MotionEvent.ACTION_CANCEL == event.getAction()) {
                    MHLogUtil.d(TAG, "action_up||action_cancel");
                    if (currentIndex == 1) {
                        if (isRecording) {
                            endRecord();
                        } else if (!doConfirmActioning) {
                            showToastAtCenter("长按拍摄视频");
                        }
                    }
                }
                return true;
            }
        });
        seek_bar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                if (null != mMediaRecorder)
                    mMediaRecorder.setExposureCompensation(99 - progress);
            }
        });
    }

    private void showTipView(String messageContent) {
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        if (null != ll_tip_layout) {
            decorView.removeView(ll_tip_layout);
            ll_tip_layout = null;
        }
        ll_tip_layout = new LinearLayout(context);
        ll_tip_layout.setGravity(Gravity.CENTER_HORIZONTAL);

        tv_tip = new TextView(context);
        tv_tip.setGravity(Gravity.CENTER);
        tv_tip.setTextColor(Color.WHITE);
        tv_tip.setBackgroundResource(R.drawable.paishe_tip_bg);
        tv_tip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tv_tip.setText(messageContent);
        int padding = DensityUtil.dip2px(context, 15);
        int paddingBottom = DensityUtil.dip2px(context, 6);
        tv_tip.setPadding(padding, 0, padding, paddingBottom);

        int height = DensityUtil.dip2px(context, 40);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        params.topMargin = screenHeight - video_bottom_control_height - height + ScreenUtils.getStatusBarHeight(context) - DensityUtil.dip2px(context, 6);
        ll_tip_layout.addView(tv_tip, params);

        decorView.addView(ll_tip_layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (null == animationSet) {
            animationSet = new AnimationSet(true);
            AlphaAnimation showTipAnimation = new AlphaAnimation(0, 1);
            showTipAnimation.setDuration(400);
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -0.2f);
            translateAnimation.setRepeatMode(Animation.REVERSE);
            translateAnimation.setRepeatCount(-1);
            translateAnimation.setDuration(500);
            animationSet.addAnimation(showTipAnimation);
            animationSet.addAnimation(translateAnimation);
        }
        tv_tip.startAnimation(animationSet);
    }


    private void hideTipView() {
        if (isHideTipViewing) return;

        isHideTipViewing = true;
        if (null != ll_tip_layout) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ll_tip_layout.setAlpha((float) animation.getAnimatedValue());
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    MHLogUtil.d(TAG, "hideTipView--onAnimationEnd");
                    ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
                    decorView.removeView(ll_tip_layout);
                    ll_tip_layout = null;
                    isHideTipViewing = false;
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
            isHideTipViewing = false;
        }
    }

    /**
     * 初始化拍摄SDK
     */
    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorderNative();
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnEncodeListener(this);
        mMediaRecorder.setOnPreparedListener(new MediaRecorderBase.OnPreparedListener() {
            @Override
            public void onPrepared() {
                if (SpUtils.getBoolean("is_first_record_video", true)) {
                    SpUtils.put("is_first_record_video", false);
                    startExposureAnimation();
                }
            }
        });

        String key = String.valueOf(System.currentTimeMillis());
        if (null == videoRootPath) {
            videoRootPath = ConstantsValue.Video.VIDEO_TEMP_PATH + "/temp_" + key;
            videoUploadInfo.setFilesParent(videoRootPath);
        }
        File fileRootPath = new File(videoRootPath);
        if (!fileRootPath.exists()) fileRootPath.mkdirs();

        mMediaObject = mMediaRecorder.setOutputDirectory(key, videoRootPath);
        if (null == mMediaObject) {
            showToastAtCenter("启动录制失败");
        } else {
            progress_view.setData(mMediaObject.getMedaParts());
        }
    }

    /**
     * 拍照
     */
    private void takePicture() {
        if (null == mMediaRecorder) {
            showToastAtCenter("拍照启动失败");
            return;
        }
        if (isTakePicture) return;

        MHLogUtil.d(TAG, "takePicture");
        isTakePicture = true;
        mMediaRecorder.takePicture(new MediaRecorderBase.TakePictureCallback() {
            @Override
            public void takePictureOK(Bitmap bitmap) {
                if (null == bitmap) return;


                int targetWidth = bitmap.getWidth();
                int targetHeight = (int) (targetWidth / ((float) lastPictureRatio.x / lastPictureRatio.y));

                int startX = 0;
                int startY = (bitmap.getHeight() - targetHeight) / 2;

                if (targetHeight > bitmap.getHeight()) {
                    MHLogUtil.d(TAG, "targetHeight>bitmap.getHeight()--targetHeight=" + targetHeight);
                    targetHeight = bitmap.getHeight();
                    targetWidth = (int) (targetHeight * ((float) lastPictureRatio.x / lastPictureRatio.y));
                    startY = 0;
                    startX = (bitmap.getWidth() - targetWidth) / 2;
                }
                MHLogUtil.d(TAG, "scale Width=" + targetWidth + "---Height=" + targetHeight + "--startY=" + startY + "--startX=" + startX);
                bitmap = Bitmap.createBitmap(bitmap, startX, startY, targetWidth, targetHeight);

                if (iv_beautiful_face.isSelected()) {
                    CGENativeLibrary.filterImage_MultipleEffectsWriteBack(bitmap, FACE_BEAUTY_CONFIG_BASE + bitmap.getWidth() + " " + bitmap.getHeight(), 1);
                }

                String picPath = videoRootPath + "/pic_" + System.currentTimeMillis() + ".jpg";
                BitmapUtil.saveBitmapToSDcard(bitmap, picPath);
                videoUploadInfo.setPicturePath(picPath);
                videoUploadInfo.setPictureSrcPath(picPath);
                videoUploadInfo.setPictureWidth(bitmap.getWidth());
                videoUploadInfo.setPictureHeight(bitmap.getHeight());
                videoUploadInfo.setMediaType(VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE);
                Intent intent = new Intent(context, PicturePreviewActivity.class);
                intent.putExtra("videoUploadInfo", videoUploadInfo);
                startActivity(intent);

                bitmap.recycle();
                isTakePicture = false;
            }
        }, null, true);
    }

    private void startRecord() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        long totalSpace = file.getFreeSpace();
        if (totalSpace / (1024.0f * 1024) < 200) {
            showToastAtCenter("存储空间不足啦");
            return;
        }

        if (isRecording || doConfirmActioning || recorderTime >= RECORDER_MAX_TIME) return;

        enablePreview = true;
        hideRatioLayout();
        bt_recorder.setBackgroundResource(R.drawable.paishezhongjian);
        iv_record_animation.startAnimation(rotateAnimation);

        resetDeleteState();

        iv_delete.setSelected(false);
        if (mMediaRecorder != null) {
            isRecording = true;
            MediaObject.MediaPart part = mMediaRecorder.startRecord();
            if (part == null) {
                showToastAtCenter("启动录制失败");
                return;
            }
        }
        isRecording = true;
        handler.sendEmptyMessage(MSG_WHAT_UPDATE_TIME);

        if (iv_close.isEnabled()) {
            List<View> viewListHid = new ArrayList<>();
            viewListHid.add(iv_close);
            viewListHid.add(iv_beautiful_face);
            viewListHid.add(iv_flash_light);
            viewListHid.add(iv_face_camera);
//            viewListHid.add(seek_bar);
            if (recorderTime > 0) {
                viewListHid.add(iv_delete);
                viewListHid.add(iv_confirm);
            }
            hideView(viewListHid, 0);
        }
    }

    /**
     * 重置删除状态
     */
    private void resetDeleteState() {
        if (null != mMediaObject && null != mMediaObject.getMedaParts() && mMediaObject.getMedaParts().size() > 0) {
            for (MediaObject.MediaPart mediaPart : mMediaObject.getMedaParts()) {
                mediaPart.remove = false;
            }
        }
    }


    private void endRecord() {
        if (!isRecording) return;
        bt_recorder.setBackgroundResource(0);
        iv_record_animation.clearAnimation();

        MHLogUtil.d(TAG, "endRecord");
        isRecording = false;

        if (mMediaRecorder != null && null != mMediaObject) {
            isRecording = false;
            mMediaRecorder.stopRecord();
        }

        if (!iv_close.isEnabled()) {
            List<View> viewList = new ArrayList<>();
            viewList.add(iv_close);
            viewList.add(iv_beautiful_face);
            viewList.add(iv_flash_light);
            viewList.add(iv_face_camera);
//            viewList.add(seek_bar);
            viewList.add(iv_delete);
            viewList.add(iv_confirm);
            showView(viewList);
        }
    }

    private void showView(final List<View> views) {
        if (views.get(0).isEnabled()) return;

        MHLogUtil.d(TAG, "showView");

        for (View view : views) {
            view.setEnabled(true);
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                for (View view : views) {
                    view.setAlpha(animatedValue);
                }
            }
        });
        valueAnimator.start();
    }

    private void hideView(final List<View> views, long duration) {
        if (!views.get(0).isEnabled()) return;
        MHLogUtil.d(TAG, "hideView");
        for (View view : views) {
            view.setEnabled(false);
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);

        if (0 != duration)
            valueAnimator.setDuration(duration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                for (View view : views) {
                    view.setAlpha(animatedValue);
                }
            }
        });
        valueAnimator.start();
    }

    private void requestAudioPermission() {
        AudioRecord record = null;
        try {
//            boolean hasRequestPermission = (boolean) SpUtils.get( ConstantsValue.Sp.AUDIO_PERMISSION_KEY, false);
//            if (hasRequestPermission) return;
            final int mMinBufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            record = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, mMinBufferSize);
            short[] buffer = new short[mMinBufferSize];
            record.startRecording();
            int bufferReadResult = record.read(buffer, 0, buffer.length);
            if (bufferReadResult <= 0) {
                showToastAtCenter("未获得录音权限\n请到系统设置修改");
                finish();
                overridePendingTransition(0, R.anim.slide_top_in);
            }
            record.stop();
//            SpUtils.put( ConstantsValue.Sp.AUDIO_PERMISSION_KEY, true);
        } catch (Exception e) {
            MHLogUtil.e(TAG, e.toString());
        } finally {
            if (null != record) record.release();
        }
    }


    @Override
    public void onClick(View v) {
        if (isRecording) return;

        Point ratioPoint = null;
        switch (v.getId()) {
            case R.id.iv_delete:
                if (null != mMediaObject && null != mMediaObject.getMedaParts() && mMediaObject.getMedaParts().size() > 0) {
                    MediaObject.MediaPart mediaPart = mMediaObject.getCurrentPart();
                    if (mediaPart.remove) {
                        mMediaObject.removePart(mediaPart, true);
                        iv_delete.setSelected(false);
                        enablePreview = true;
                    } else {
                        iv_delete.setSelected(true);
                        mediaPart.remove = true;
                    }
                    if (recorderTime <= 0) {
                        ratio_view.setEnabled(true);
                        ratio_view.setVisibility(View.VISIBLE);
                        ratio_view.setAlpha(1);
                    }
                }
                MHLogUtil.d(TAG, "iv_delete onClick");
                break;
            case R.id.iv_confirm:
                MHLogUtil.d(TAG, "iv_confirm onClick");
                enablePreview = true;
                doConfirmAction();
                break;
            case R.id.iv_close:
                finish();
                overridePendingTransition(0, R.anim.slide_top_in);
                break;
            case R.id.iv_beautiful_face:
                TCAgent.onEvent(context, "美颜" + ConstantsValue.android);
                if (isBeautifulFace) {
                    bz_glSurfaceView.setFilterWithConfig("");
                    iv_beautiful_face.setSelected(false);
                } else {
                    iv_beautiful_face.setSelected(true);
                    bz_glSurfaceView.setFilterIntensity(1);
                    bz_glSurfaceView.setFilterWithConfig(FACE_BEAUTY_CONFIG_BASE + "480 640");
                }
                isBeautifulFace = !isBeautifulFace;
                break;
            case R.id.iv_flash_light:
                if (null != mMediaRecorder) {
                    mMediaRecorder.toggleFlashMode();
                    iv_flash_light.setSelected(!iv_flash_light.isSelected());
                }
                break;
            case R.id.iv_face_camera:
                if (null != mMediaRecorder) {
                    mMediaRecorder.switchCamera();
                    iv_face_camera.setSelected(!iv_face_camera.isSelected());

                    if (mMediaRecorder.isFrontCamera()) {
                        if (mMediaRecorder.isNexusPhone())
                            bz_glSurfaceView.setSrcRotation((float) (Math.PI * 3 / 2));
                        iv_flash_light.setVisibility(View.GONE);
                    } else {
                        bz_glSurfaceView.setSrcRotation((float) (Math.PI / 2));
                        iv_flash_light.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.ratio_view4_3:
                ratioPoint = new Point(4, 3);
                TCAgent.onEvent(context, "选拍摄尺寸4比3" + ConstantsValue.android);
                break;
            case R.id.ratio_view1_1:
                ratioPoint = new Point(1, 1);
                TCAgent.onEvent(context, "选拍摄尺寸1比1" + ConstantsValue.android);
                break;
            case R.id.ratio_view3_4:
                ratioPoint = new Point(3, 4);
                TCAgent.onEvent(context, "选拍摄尺寸3比4" + ConstantsValue.android);
                break;
            case R.id.ratio_view16_9:
                ratioPoint = new Point(16, 9);
                TCAgent.onEvent(context, "选拍摄尺寸16比9" + ConstantsValue.android);
                break;
            case R.id.ratio_view:
                if (ratioIsShow)
                    hideRatioLayout();
                else {
                    showRatioLayout();
                    TCAgent.onEvent(context, "选拍摄尺寸" + ConstantsValue.android);
                }
                break;
        }

        if (null != ratioPoint) {
            if (currentIndex == 1)
                lastVideoRatio = ratioPoint;
            else
                lastPictureRatio = ratioPoint;
            hideRatioLayout();
            setSelectRatioView(ratioPoint);
            changeCoverView();
        }
    }

    private void doConfirmAction() {
        if (doConfirmActioning) return;

        doConfirmActioning = true;
        if (recorderTime < RECORDER_MIN_TIME) {
            if (VideoUploadInfo.FROM_ASK_AND_ANSWER == videoUploadInfo.getFromInfo()) {
                showTipView(TIP_MSG_RECORD_MIN_ASK_AND_ANSWER);
            } else {
                showTipView(TIP_MSG_RECORD_MIN_COMMON);
            }
            handler.sendEmptyMessageDelayed(MSG_WHAT_HIDE_TIP_VIEW, HIDE_TIP_VIEW_DELAYED_TIME);
            doConfirmActioning = false;
            return;
        }
        resetDeleteState();
        if (isRecording) {
            endRecord();
        }
        if (null != mMediaRecorder)
            mMediaRecorder.startEncoding();
    }


    @Override
    public boolean onTabSelect(int position) {
        if (isRecording) return true;

        if (position != 0) {
            bz_glSurfaceView.takeShot(new BZGLSurfaceView.TakePictureCallback() {
                @Override
                public void takePictureOK(final Bitmap bitmap) {
                    iv_preview.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Blurry.with(context).radius(25).from(bitmap).into(iv_preview);
                                iv_preview.setAlpha(1.0f);
                                iv_preview.setEnabled(true);
                                List<View> viewList = new ArrayList<>();
                                viewList.add(iv_preview);
                                hideView(viewList, 1000);
                            } catch (Exception e) {
                                MHLogUtil.e(TAG, e);
                                MHLogUtil.d(TAG, e.getMessage());
                            }
                        }
                    });
                }
            });
        }

        hideTipView();
        switch (position) {
            case 0:
                TCAgent.onEvent(context, "切图库" + ConstantsValue.android);
                Intent intent = new Intent(context, VideoChooseActivity.class);
                intent.putExtra("videoUploadInfo", videoUploadInfo);
                startActivity(intent);
                break;
            case 1:
                iv_confirm.setVisibility(View.VISIBLE);
                iv_delete.setVisibility(View.VISIBLE);
                progress_view.setVisibility(View.VISIBLE);
                if (recorderTime <= 0) {
                    ratio_view.setEnabled(true);
                    ratio_view.setAlpha(1);
                    ratio_view.setVisibility(View.VISIBLE);
                } else {
                    hideRatioLayout();
                    ratio_view.setEnabled(false);
                    ratio_view.setAlpha(0);
                    ratio_view.setVisibility(View.GONE);
                }
                setSelectRatioView(lastVideoRatio);
                break;
            case 2:
                TCAgent.onEvent(context, "切拍照" + ConstantsValue.android);
                setSelectRatioView(lastPictureRatio);

                ratio_view.setEnabled(true);
                ratio_view.setVisibility(View.VISIBLE);
                ratio_view.setAlpha(1);
                iv_confirm.setVisibility(View.GONE);
                iv_delete.setVisibility(View.GONE);
                progress_view.setVisibility(View.GONE);
                break;
        }
        currentIndex = position;
        changeCoverView();
        return false;
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        hiddenLoadingView();
        UtilityAdapter.freeFilterParser();
        UtilityAdapter.initFilterParser();

        if (mMediaRecorder == null) {
            initMediaRecorder();
        }
        progress_view.onResume();
        bz_glSurfaceView.onResume();
        if (currentIndex == 0)
            tabLayout.setCurrentTab(1);
        handler.sendEmptyMessage(MSG_WHAT_UPDATE_TIME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        endRecord();
        UtilityAdapter.freeFilterParser();
        if (mMediaRecorder != null) {
            MHLogUtil.d(TAG, "stopRecord");
            isRecording = false;
            mMediaRecorder.stopPreview();
        }
        bz_glSurfaceView.release(null);
        bz_glSurfaceView.onPause();
        progress_view.onPause();
        handler.removeMessages(MSG_WHAT_UPDATE_TIME);
    }

    private boolean hasMove;
    private float startY;
    private View.OnTouchListener mOnSurfaveViewTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeMessages(MSG_WHAT_HIDE_EXPOSURE_VIEW);
                    showExposureView();
                    startY = event.getY();
                    hasMove = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!hasMove && Math.abs(event.getY() - startY) > 3 * dp_1) {
                        hasMove = true;
                        startY = event.getY();
                    }
                    if (hasMove) {
                        float temp = event.getY() - startY;
                        startY = event.getY();
                        seek_bar.modProgress((int) (temp * 0.2));
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    handler.sendEmptyMessageDelayed(MSG_WHAT_HIDE_EXPOSURE_VIEW, 2000);
                    //手动对焦
                    if (!hasMove)
                        touch2Focus((int) event.getX(), (int) event.getY());
                    break;
            }
            return true;
        }

    };

    private void touch2Focus(int x, int y) {
        MHLogUtil.d(TAG, "touch2Focus");
        if (null == mMediaRecorder || null == mMediaRecorder.camera) return;

        Camera camera = mMediaRecorder.camera;

        Rect focusRect = getFocusArea(x, y, bz_glSurfaceView.getWidth(),
                bz_glSurfaceView.getHeight(), 300);
        List<Camera.Area> areas = new ArrayList<>();
        areas.add(new Camera.Area(focusRect, 1000));

        Camera.Parameters cameraParams = camera.getParameters();
        if (cameraParams.getMaxNumFocusAreas() > 0) {
            cameraParams.setFocusAreas(areas);// 设置对焦区域
        }
        if (cameraParams.getMaxNumMeteringAreas() > 0) {
            cameraParams.setMeteringAreas(areas);// 设置测光区域
        }
        camera.cancelAutoFocus();
        // if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
        cameraParams.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(cameraParams);
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                record_focusing.setVisibility(View.GONE);
            }
        });

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) record_focusing.getLayoutParams();
        int left = x - (mFocusWidth / 2);// (int) x -
        // (focusingImage.getWidth()
        // / 2);
        int top = y - (mFocusWidth / 2);// (int) y -
        // (focusingImage.getHeight()
        // / 2);
        if (left < 0)
            left = 0;
        else if (left + mFocusWidth > screenWidth)
            left = screenWidth - mFocusWidth;
        if (top + mFocusWidth > bz_glSurfaceView.getHeight())
            top = bz_glSurfaceView.getHeight() - mFocusWidth;

        lp.leftMargin = left;
        lp.topMargin = top;
        record_focusing.setLayoutParams(lp);
        record_focusing.setVisibility(View.VISIBLE);

        if (mFocusAnimation == null)
            mFocusAnimation = AnimationUtils.loadAnimation(this,
                    R.anim.record_focus);

        record_focusing.startAnimation(mFocusAnimation);

        handler.sendEmptyMessageDelayed(MSG_WHAT_HIDE_RECORD_FOCUS, 3500);// 最多3.5秒也要消失
    }

    public Rect getFocusArea(int x, int y, int w, int h, int areaSize) {
        int centerX = x / w * 2000 - 1000;
        int centerY = y / h * 2000 - 1000;
        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int right = clamp(left + areaSize, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        int bottom = clamp(top + areaSize, -1000, 1000);
        return new Rect(left, top, right, bottom);
    }

    /**
     * 限定x取值范围为[min,max]
     *
     * @param x
     * @param min
     * @param max
     * @return
     */
    public int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    private void showRatioLayout() {
        if (ratioAnimationIsShowing) return;

        if (ratioIsShow) return;
        ratioIsShow = true;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                ll_ratio_container.setX(ratio_view.getLeft() - ll_ratio_container.getMeasuredWidth() * 3 / 4 - ll_ratio_container.getMeasuredWidth() / 4 * animatedValue);
                ll_ratio_container.setAlpha(animatedValue);
                line.setAlpha(animatedValue);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ratioAnimationIsShowing = true;
                ratio_view.setBackgroundResource(R.drawable.corners_ratio_bg_2);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ratioAnimationIsShowing = false;

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

    private void setSelectRatioView(Point point) {
        if (null == point) return;
        if (null != mMediaRecorder)
            mMediaRecorder.setRecordRatio(point);
        ratio_view.setRatio(point);
//        bz_glSurfaceView.setRecordRatio(point);

        for (int i = 0; i < ll_ratio_container.getChildCount(); i++) {
            RatioView childAt = (RatioView) ll_ratio_container.getChildAt(i);
            childAt.setSelected(false);
            if (null != childAt.getRatioPoint() && point.x == childAt.getRatioPoint().x
                    && point.y == childAt.getRatioPoint().y) {
                childAt.setSelected(true);
            }
        }
    }

    private void hideRatioLayout() {
        if (ratioAnimationIsShowing) return;

        if (!ratioIsShow) return;
        ratioIsShow = false;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                ll_ratio_container.setX(ratio_view.getLeft() - ll_ratio_container.getMeasuredWidth() + ll_ratio_container.getMeasuredWidth() / 4 * animatedValue);
                ll_ratio_container.setAlpha(1 - animatedValue);
                line.setAlpha(1 - animatedValue);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ratioAnimationIsShowing = true;
                ratio_view.setBackgroundResource(R.drawable.corners_ratio_bg);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ratioAnimationIsShowing = false;
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


    private void checkButtonState() {
        if (recorderTime > 0) {
            if (!isRecording)
                iv_delete.setEnabled(true);
            if (!progress_view.isEnabled()) {
                List<View> viewList = new ArrayList<>();
                viewList.add(progress_view);
                viewList.add(tv_time);
                showView(viewList);
            }
            if (tabLayout.isEnabled()) {
                List<View> viewListHid = new ArrayList<>();
                viewListHid.add(tabLayout);
                viewListHid.add(ratio_view);
                hideView(viewListHid, 0);
            }
        } else {
            if (!tabLayout.isEnabled()) {
                List<View> viewList = new ArrayList<>();
                viewList.add(tabLayout);
                viewList.add(ratio_view);
                showView(viewList);
            }
            if (progress_view.isEnabled()) {
                List<View> viewListHid = new ArrayList<>();
                viewListHid.add(progress_view);
                viewListHid.add(iv_delete);
                viewListHid.add(iv_confirm);
                viewListHid.add(tv_time);
                hideView(viewListHid, 0);
            }
        }
        if (recorderTime >= RECORDER_MIN_TIME) {
            iv_confirm.setSelected(true);
        } else {
            iv_confirm.setSelected(false);
        }
        if (recorderTime >= RECORDER_MAX_TIME && enablePreview) {
            doConfirmAction();
        }

        //注意下面的区间
        if (recorderTime >= RECORDER_MAX_TIME - 15 * 1000) {
            String msg = TIP_MSG_LAST_15_SECOND + CommonUtil.formatTime4Second(RECORDER_MAX_TIME - recorderTime);
            if (null != tv_tip) {
                tv_tip.setText(msg);
            } else {
                showTipView(msg);
            }
        } else if (recorderTime > RECORDER_MAX_TIME - 30 * 1000 && recorderTime < RECORDER_MAX_TIME - 15 * 1000) {
            hideTipView();
        }
        if (recorderTime > RECORDER_MIN_TIME && recorderTime < RECORDER_MIN_TIME + 10 * 1000) {
            hideTipView();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, R.anim.slide_top_in);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MHLogUtil.d(TAG, "onDestroy");
        UtilityAdapter.freeFilterParser();
        if (mMediaRecorder != null)
            mMediaRecorder.release();

        if (null != videoUploadInfo)
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + videoUploadInfo.getVideoSrcPath())));
    }

    @Override
    public void onVideoError(final int what, int extra) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MediaRecorderBase.MEDIA_ERROR_CAMERA_PREVIEW == what) {
                    showToastAtCenter("未获取到摄像头权限\n请到系统设置更改权限");
                    finish();
                    overridePendingTransition(0, R.anim.slide_top_in);
                }
            }
        });
    }

    @Override
    public void onAudioError(int what, String message) {
        MHLogUtil.d(TAG, "onAudioError");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToastAtCenter("未获得录音权限\n请到系统设置修改");
                finish();
                overridePendingTransition(0, R.anim.slide_top_in);
            }
        });
    }

    @Override
    public void onEncodeStart() {
        hideTipView();
        doConfirmActioning = true;
        MHLogUtil.d(TAG, "onEncodeStart");
        showLoading("处理中", false, false);
    }

    @Override
    public void onEncodeProgress(int progress) {
        MHLogUtil.d(TAG, "onEncodeProgress--progress=" + progress);
        showLoading("处理中 " + progress + "%", false, false);
    }

    @Override
    public void onEncodeComplete() {
        doConfirmActioning = false;
        iv_confirm.setEnabled(true);
        hiddenLoadingView();
        MHLogUtil.d(TAG, "onEncodeComplete-path=" + mMediaObject.getOutputTempVideoPath());
        startPreview(mMediaObject.getOutputTempVideoPath());
    }

    @Override
    public void onEncodeError() {
        doConfirmActioning = false;
        iv_confirm.setEnabled(true);
        hiddenLoadingView();
        showToastAtCenter("处理出错啦");
    }

    /**
     * 跳转到预览页面
     *
     * @param videoPath 视频本地路径
     */
    private void startPreview(String videoPath) {
        if (!enablePreview) return;
        if (null == mMediaObject) {
            showToastAtCenter("处理出错了");
            return;
        }
        enablePreview = false;

        if (null == videoUploadInfo) {
            videoUploadInfo = new VideoUploadInfo();
            videoUploadInfo.setFromInfo(VideoUploadInfo.FROM_COMMON);
        }
        ArrayList<VideoRecorderObject> videoList = videoUploadInfo.getVideoList();
        videoList.clear();
        if (null != mMediaObject.getMedaParts() && mMediaObject.getMedaParts().size() > 0) {
            for (MediaObject.MediaPart mediaPart : mMediaObject.getMedaParts()) {
                VideoRecorderObject recorderObject = new VideoRecorderObject();
                recorderObject.setVideoTsPath(mediaPart.mediaPath);
                recorderObject.setDuration(mediaPart.duration);
                videoList.add(recorderObject);
            }
        }
        if (iv_beautiful_face.isSelected()) {
            videoUploadInfo.setExtraFilterParam(FACE_BEAUTY_CONFIG_BASE + "480 640 ");
        }
        videoUploadInfo.setVideoPath(videoPath);
        videoUploadInfo.setVideoSrcPath(videoPath);

        float result = 1.0f * lastVideoRatio.x / lastVideoRatio.y;
        int mRecordWidth = DEFAULT_RECORD_WIDTH;
        int mRecordHeight = DEFAULT_RECORD_WIDTH;
        if (result == 1 / 1.0f) {//1:1
            mRecordWidth = mRecordHeight = DEFAULT_RECORD_WIDTH;
        } else if (result == 4 / 3.0f) {//4:3
            mRecordWidth = DEFAULT_RECORD_WIDTH;
            mRecordHeight = DEFAULT_RECORD_WIDTH * 3 / 4;
        } else if (result == 3 / 4.0f) {//3:4
            mRecordWidth = DEFAULT_RECORD_WIDTH;
            mRecordHeight = DEFAULT_RECORD_WIDTH * 4 / 3;
        } else if (result == 16 / 9.0f) {//16:9
            mRecordWidth = DEFAULT_RECORD_WIDTH;
            mRecordHeight = 9 * DEFAULT_RECORD_WIDTH / 16;
        }

        videoUploadInfo.setVideoSrcWidth(mRecordWidth);
        videoUploadInfo.setVideoWidth(mRecordWidth);
        videoUploadInfo.setVideoHeight(mRecordHeight);
        videoUploadInfo.setMediaType(VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO);
        if (null != mMediaObject)
            videoUploadInfo.setVideoDuration(mMediaObject.getDuration());
        Intent intent = new Intent(context, VideoEditActivity.class);
        intent.putExtra("videoUploadInfo", videoUploadInfo);
        startActivity(intent);
    }

    private void changeCoverView() {
        float result;
        if (currentIndex == 1) {//这是视频
            result = 1.0f * lastVideoRatio.x / lastVideoRatio.y;
        } else {//这是拍照
            result = 1.0f * lastPictureRatio.x / lastPictureRatio.y;
        }
        float topY = 0;
        float bottomY = 0;
        if (result == 1 / 1.0f) {//1:1
            int space = (videoLayoutHeight - screenWidth) / 2;
            topY = -videoLayoutHeight + space;
            bottomY = videoLayoutHeight - space;
        } else if (result == 4 / 3.0f) {//4:3
            int space = (videoLayoutHeight - screenWidth * 3 / 4) / 2;
            topY = -videoLayoutHeight + space;
            bottomY = videoLayoutHeight - space;
        } else if (result == 3 / 4.0f) {//3:4
            topY = -videoLayoutHeight;
            bottomY = videoLayoutHeight;
        } else if (result == 16 / 9.0f) {//16:9
            int space = (videoLayoutHeight - screenWidth * 9 / 16) / 2;
            topY = -videoLayoutHeight + space;
            bottomY = videoLayoutHeight - space;
        }

        final float offsetTopY = topY - cover_top.getY();
        final float offsetBottomY = bottomY - cover_bottom.getY();

        final float startTopY = cover_top.getY();
        final float startBottomY = cover_bottom.getY();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                cover_top.setY(startTopY + offsetTopY * animatedValue);
                cover_bottom.setY(startBottomY + offsetBottomY * animatedValue);
            }
        });
        valueAnimator.start();
    }


    /**
     * hide 曝光度View
     */
    private void hideExposureView() {
        ObjectAnimator.ofFloat(seek_bar, "Alpha", seek_bar.getAlpha(), 0).start();
    }

    /**
     * show 曝光度View
     */
    private void showExposureView() {
        ObjectAnimator.ofFloat(seek_bar, "Alpha", seek_bar.getAlpha(), 1).start();
    }

    private void startExposureAnimation() {
        handler.sendEmptyMessageDelayed(MSG_WHAT_HIDE_EXPOSURE_VIEW, 3000 + 50);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setStartOffset(3000);
        alphaAnimation.setDuration(300);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_exposure.clearAnimation();
                tv_exposure.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tv_exposure.startAnimation(alphaAnimation);
    }

}

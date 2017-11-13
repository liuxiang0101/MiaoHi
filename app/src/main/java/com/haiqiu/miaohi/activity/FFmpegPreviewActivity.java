package com.haiqiu.miaohi.activity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.VideoInfo;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.ffmpeg.FFmpegUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.mediaplayer.BaseVideoView;
import com.haiqiu.miaohi.widget.mediaplayer.MyMediaPlayer;
import com.haiqiu.miaohi.widget.mediaplayer.VideoViewWrap;
import com.tendcloud.tenddata.TCAgent;

import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;

/**
 * Created by zhandalin on 2016-05-27 20:27.
 * 说明:视频预览
 */
public class FFmpegPreviewActivity extends BaseActivity implements FFmpegUtil.MediaInfoListener {
    private static final int MESSAGE_WHAT_COMPRESS_PROGRESS = 35;

    private BaseVideoView videoView;
    private MyMediaPlayer mediaPlayer;
    private VideoUploadInfo videoUploadInfo;
    private int videoSrcWidth;
    private Thread compressThread;
    private float videoDuration;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WHAT_COMPRESS_PROGRESS:
                    showPrivateLoading((String) msg.obj);
                    break;
            }
            return false;
        }
    });
    private FrameLayout windowDecorView;
    private LinearLayout privateLoadingView;
    private TextView tv_msg;
    private boolean isCompress;
    private int video_rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_preview);
        doIntent(getIntent());
        MHLogUtil.d(TAG, "onCreate");

        videoSrcWidth = videoUploadInfo.getVideoSrcWidth();
        CommonNavigation navigation = (CommonNavigation) findViewById(R.id.navigation);
        assert navigation != null;
        navigation.getRightTextView().setTextColor(getResources().getColor(R.color.common_blue2));
        navigation.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                if (isCompress) {
                    showToastAtBottom("正在压缩中...");
                    return;
                }
                if (null == videoUploadInfo.getVideoSrcPath()) {
                    showToastAtCenter("视频文件不存在");
                    return;
                }
                if (null != mediaPlayer && mediaPlayer.getDuration() / 1000 < 3) {
                    showToastAtCenter("您选择视频不足3秒，不支持上传");
                    return;
                }
                File file = new File(videoUploadInfo.getVideoSrcPath());
                if (file.exists()) {
                    long length = file.length();
                    length = (long) (length / 1024 / 1024 + 0.5);
                    MHLogUtil.d(TAG, "file.length()=" + length);
                    if (length > 1024) {
                        showToastAtCenter("您选择视频太大，暂不支持上传");
                        return;
                    }
                } else {
                    showToastAtCenter("视频文件不存在");
                    return;
                }

                if (!isLogin(false)) return;
                if (null != mediaPlayer && mediaPlayer.getVideoWidth() > 0) {
                    videoSrcWidth = mediaPlayer.getVideoWidth();
                    videoUploadInfo.setVideoSrcWidth(videoSrcWidth);
                    MHLogUtil.d(TAG, "mediaPlayer.getVideoWidth()=" + videoSrcWidth);
                } else if (videoSrcWidth <= 0) {
                    showToastAtCenter("该视频暂时不支持上传");
                    return;
                }
                TCAgent.onEvent(context, "第二步预览-下一步" + ConstantsValue.android);
                if (videoSrcWidth > 960) {
                    showToastAtBottom("该视频太大需要压缩");
                    try {
                        mediaPlayer.pause();
                        mediaPlayer.release();
                    } catch (Exception e) {

                        MHLogUtil.e(TAG, e);
                    }
                    compressThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            isCompress = true;
                            long startTime = System.currentTimeMillis();
                            FFmpegUtil.setMediaInfoListener(FFmpegPreviewActivity.this);

                            String videoParentPath = ConstantsValue.Video.VIDEO_TEMP_PATH + "/temp_" + System.currentTimeMillis();
                            File file = new File(videoParentPath);
                            if (!file.exists()) file.mkdirs();
                            videoUploadInfo.setFilesParent(videoParentPath);
                            String videoOutputPath = videoParentPath + "/compress_" + System.currentTimeMillis() + ".mp4";

                            boolean result = FFmpegUtil.startFilter(videoOutputPath, videoUploadInfo.getVideoSrcPath(), null, 0, null, CGENativeLibrary.TextureBlendMode.CGE_BLEND_ADDREV.ordinal(), 1.0f, false);
                            MHLogUtil.d(TAG, "压缩耗时=" + (System.currentTimeMillis() - startTime));
                            if (isDestroyed) return;

                            if (result) {
                                videoUploadInfo.setVideoSrcPath(videoOutputPath);
                                videoUploadInfo.setVideoPath(videoOutputPath);
                                Intent intent = new Intent(context, VideoEditActivity.class);
//                                Intent intent = new Intent(context, GpuVideoPreviewActivity.class);
                                intent.putExtra("videoUploadInfo", videoUploadInfo);
                                startActivity(intent);
                                finish();
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToastAtCenter("压缩失败, 请更换视频尝试");
                                        finish();
                                    }
                                });
                            }
                            isCompress = false;
                        }
                    });
                    compressThread.setPriority(Thread.MAX_PRIORITY);
                    compressThread.start();
                    return;
                }
                Intent intent = new Intent(context, VideoEditActivity.class);
                intent.putExtra("videoUploadInfo", videoUploadInfo);
                startActivity(intent);
                finish();
            }
        });

        VideoViewWrap base_video_view_wrap = (VideoViewWrap) findViewById(R.id.base_video_view_wrap);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) base_video_view_wrap.getLayoutParams();

        Point screenSize = ScreenUtils.getScreenSize(context);
        layoutParams.height = screenSize.x;
        base_video_view_wrap.setLayoutParams(layoutParams);

        videoView = base_video_view_wrap.getVideoView();

        mediaPlayer = new MyMediaPlayer();
        final VideoInfo videoInfo = new VideoInfo();
        if (null != videoUploadInfo)
            videoInfo.setVideo_uri(videoUploadInfo.getVideoPath());

        videoView.setVideoControlListener(new BaseVideoView.VideoControlListener() {
            @Override
            public void videoViewState(BaseVideoView.VideoViewState videoViewState, int extra) {
                switch (videoViewState) {
                    case ON_START_PLAY_CLICK:
                        videoView.startPlay(mediaPlayer, videoInfo);
                        break;
                    case ON_VIDEO_START:
                        if (null != mediaPlayer)
                            videoUploadInfo.setVideoDuration(mediaPlayer.getDuration());

                        if (null == mediaPlayer || mediaPlayer.getVideoWidth() <= 0) {
                            if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                            }
                            showToastAtCenter("该视频暂时不支持上传");
                            finish();
                        }
                        break;
                }
            }
        });
        videoView.startPlay(mediaPlayer, videoInfo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        if (handler != null) {
            handler.removeMessages(MESSAGE_WHAT_COMPRESS_PROGRESS);
        }

        hidePrivateLoading();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MHLogUtil.d(TAG, "onDestroy");
        if (handler != null) {
            handler.removeMessages(MESSAGE_WHAT_COMPRESS_PROGRESS);
        }
        try {
            if (null != compressThread && compressThread.isAlive() && !compressThread.isInterrupted()) {
                FFmpegUtil.endFilter();
                FFmpegUtil.setMediaInfoListener(null);
                compressThread.isInterrupted();
                compressThread = null;
            }
        } catch (Exception e) {

            MHLogUtil.e(TAG, e);
            MHLogUtil.d(TAG, e.getMessage());
        }

        if (null != mediaPlayer) {
            try {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception e) {

                MHLogUtil.e(TAG, e);
                MHLogUtil.d(TAG, e.getMessage());
            }
        }
        hidePrivateLoading();
    }

    @Override
    public void sendMediaInfo(int what, int extra) {
        switch (what) {
            case FFmpegUtil.MEDIA_INFO_WHAT_VIDEO_DURATION:
                videoDuration = extra;
                break;
            case FFmpegUtil.MEDIA_INFO_WHAT_FILTER_PROGRESS:
                if (null != compressThread) {
                    if (extra + 100 > videoDuration) {
                        extra = (int) (videoDuration - 100);
                    }
                    //从C掉过来的要处理一下
                    Message message = new Message();
                    message.what = MESSAGE_WHAT_COMPRESS_PROGRESS;
                    message.obj = String.format("压缩中%.1f", extra / videoDuration * 100) + "%";
                    handler.sendMessage(message);
                }
//                MHLogUtil.d(TAG, "进度=" + extra);
                break;
            case FFmpegUtil.MEDIA_INFO_WHAT_VIDEO_ROTATE:
                MHLogUtil.d(TAG, "video_rotate=" + extra);
                video_rotate = extra;
                break;
            case FFmpegUtil.MEDIA_INFO_WHAT_VIDEO_WIDTH:
                if (video_rotate == 90 || video_rotate == 270) {
                    videoUploadInfo.setVideoHeight(extra);
                } else {
                    videoUploadInfo.setVideoWidth(extra);
                }
                MHLogUtil.d(TAG, "VideoWidth=" + extra);
                break;
            case FFmpegUtil.MEDIA_INFO_WHAT_VIDEO_HEIGHT:
                if (video_rotate == 90 || video_rotate == 270) {
                    videoUploadInfo.setVideoWidth(extra);
                } else {
                    videoUploadInfo.setVideoHeight(extra);
                }
                MHLogUtil.d(TAG, "VideoHeight=" + extra);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        doIntent(intent);
        if (null != mediaPlayer && null != videoView) {
            final VideoInfo videoInfo = new VideoInfo();
            if (null != videoUploadInfo)
                videoInfo.setVideo_uri(videoUploadInfo.getVideoPath());
            videoView.startPlay(mediaPlayer, videoInfo);
        }
    }


    private void doIntent(Intent intent) {
        videoUploadInfo = intent.getParcelableExtra("videoUploadInfo");
        if (null == videoUploadInfo) videoUploadInfo = new VideoUploadInfo();

        if (Intent.ACTION_VIEW.equals(intent.getAction()) || Intent.ACTION_SEND.equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null) {
                String dataPath = data.getPath();
                MHLogUtil.d(TAG, "dataPath=" + dataPath);
                videoUploadInfo.setVideoPath(dataPath);
                videoUploadInfo.setVideoSrcPath(dataPath);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clipData = intent.getClipData();
                    if (null != clipData && clipData.getItemCount() > 0) {
                        ClipData.Item itemAt = clipData.getItemAt(0);
                        if (null != itemAt) {
                            Uri uri = itemAt.getUri();
                            if (null != uri) {
                                MHLogUtil.d(TAG, "ClipData Path=" + uri.getPath());
                                videoUploadInfo.setVideoPath(uri.getPath());
                                videoUploadInfo.setVideoSrcPath(uri.getPath());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hidePrivateLoading();
    }

    private void showPrivateLoading(String content) {
        if (isDestroyed) return;
        if (null == windowDecorView)
            windowDecorView = (FrameLayout) getWindow().getDecorView();
        if (null == privateLoadingView) {
            privateLoadingView = new LinearLayout(context);
            privateLoadingView.setOrientation(LinearLayout.VERTICAL);
            privateLoadingView.setGravity(Gravity.CENTER_HORIZONTAL);
            privateLoadingView.setClickable(true);

            int navigation_height = context.getResources().getDimensionPixelSize(R.dimen.navigation_height);
            View view = View.inflate(context, R.layout.loading_layout, null);
            tv_msg = (TextView) view.findViewById(R.id.tv_msg);
            int size = DensityUtil.dip2px(context, 90);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.topMargin = (ScreenUtils.getScreenHeight(context) - size) / 2 - 2 * navigation_height;
            privateLoadingView.addView(view, params);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.topMargin = navigation_height + ScreenUtils.getStatusBarHeight(context);
            windowDecorView.addView(privateLoadingView, layoutParams);
        }
        if (null != tv_msg) {
            tv_msg.setText(content);
        }
    }

    private void hidePrivateLoading() {
        if (null != windowDecorView && null != privateLoadingView && privateLoadingView.getParent() != null) {
            windowDecorView.removeView(privateLoadingView);
            windowDecorView = null;
            privateLoadingView = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != BaseVideoView.backDownListener) {
                return BaseVideoView.backDownListener.OnBackDown();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

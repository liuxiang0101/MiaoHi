package com.haiqiu.miaohi.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.StoryboardAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.DecalInfo;
import com.haiqiu.miaohi.bean.FilterInfo;
import com.haiqiu.miaohi.bean.MusicInfo;
import com.haiqiu.miaohi.bean.VideoRecorderObject;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.ffmpeg.FFmpegUtil;
import com.haiqiu.miaohi.fragment.DecalFragment;
import com.haiqiu.miaohi.fragment.FilterFragment;
import com.haiqiu.miaohi.fragment.MusicFragment;
import com.haiqiu.miaohi.utils.BitmapUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.BZSeekBar;
import com.haiqiu.miaohi.widget.mediaplayer.GLVideoView;
import com.haiqiu.miaohi.widget.OnItemClickListener;
import com.haiqiu.miaohi.widget.SquareProgressView;
import com.haiqiu.miaohi.widget.decal.DecalView;
import com.haiqiu.miaohi.widget.decal.OnDecalUpdateListener;
import com.haiqiu.miaohi.widget.tablayout.CommonTabLayout;
import com.haiqiu.miaohi.widget.tablayout.CustomTabEntity;
import com.haiqiu.miaohi.widget.tablayout.OnTabSelectListener;
import com.haiqiu.miaohi.widget.tablayout.TabEntity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tendcloud.tenddata.TCAgent;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.texUtils.TextureRenderer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by zhandalin on 2016-12-05 15:45.
 * 说明:视频编辑页面
 */
public class VideoEditActivity extends BaseActivity implements FFmpegUtil.MediaInfoListener,
        IMediaPlayer.OnInfoListener, FilterFragment.OnFilterSelectListener, View.OnClickListener, DecalFragment.OnDecalSelectedListener,
        MusicFragment.OnMusicSelectedListener, OnItemClickListener, OnTabSelectListener,
        MediaPlayer.OnPreparedListener {
    private static final int MESSAGE_WHAT_FILTER_PROGRESS = 31;

    private String decalParentPath;
    private String musicParentPath;
    private CommonNavigation navigation;
    private GLVideoView gl_video_view;
    private ImageView iv_video_cover;
    private ProgressBar video_progress_bar;
    private CommonTabLayout tab_layout;

    private VideoUploadInfo videoUploadInfo;
    private ArrayList<VideoRecorderObject> videoList;
    private IMediaPlayer myMediaPlayer;
    private boolean isFiltering;
    private int video_rotation;
    private double videoDuration;
    private String videoParentPath;
    private boolean isOtherVideo;
    private RecyclerView rv_storyboard;
    private TextView tv_tip_0;
    private TextView tv_tip_1;
    private TextView tv_tip_2;
    private int currentPosition;
    private View ll_audio_control;
    private BZSeekBar seek_bar;
    private DecalView decal_container;
    private Thread filterThread;
    private FragmentManager fragmentManager;
    private ArrayList<BaseFragment> fragments;
    private View iv_close_audio;
    private MediaPlayer audioMediaPlayer;
    private static final float DEFAULT_SRC_AUDIO_VALUE = 0.6f;//原视频的默认音量值
    private static final float DEFAULT_BG_AUDIO_VALUE = 0.3f;//背景音乐最大的值

    private float srcAudioValue = DEFAULT_SRC_AUDIO_VALUE;
    private float bgAudioValue = DEFAULT_BG_AUDIO_VALUE;

    private MusicInfo lastMusicInfo;
    private FilterInfo lastFilterInfo;
    private int currentVideoPosition;
    private TextureRenderer.Viewport viewport;
    private float progressStep = 0.02f;//进度的步长,这样只能用5次(用来分配总时长的 1-transcodeRatio)
    private float transcodeRatio = 0.9f;//转码所占用的总体比例

    private Dialog progressDialog;
    private TextView tv_handle_progress;
    private SquareProgressView square_progress_view;
    private boolean hasWitchStoryboard;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WHAT_FILTER_PROGRESS:
                    if (isFiltering) {
                        if (null == progressDialog || null == tv_handle_progress) {
                            showHandleProgressDialog();
                        } else {
                            float progress = (float) (totalProgress / fixVideoDuration);
                            square_progress_view.setProgress(progress);
                            tv_handle_progress.setText(String.format("%.1f", progress * 100) + "%");
                        }
                    }
                    break;
            }
            return false;
        }
    });
    //用来处理从本地上传视频,关闭页面的逻辑,这个时候主页可能还没有初始化
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);
        TCAgent.onEvent(context, "编辑页面" + ConstantsValue.android);
        videoUploadInfo = getIntent().getParcelableExtra("videoUploadInfo");
        if (null == videoUploadInfo) videoUploadInfo = new VideoUploadInfo();
        videoDuration = videoUploadInfo.getVideoDuration();

        videoList = videoUploadInfo.getVideoList();
        if (null == videoList) videoList = new ArrayList<>();
        if (videoList.size() == 0) {
            if (MHStringUtils.isEmpty(videoUploadInfo.getVideoPath()) || videoDuration == 0) {
                showToastAtCenter("处理出错啦");
                finish();
                return;
            } else {
                VideoRecorderObject videoRecorderObject = new VideoRecorderObject();
                videoRecorderObject.setVideoTsPath(videoUploadInfo.getVideoPath());
                videoRecorderObject.setDuration(videoUploadInfo.getVideoDuration());
                videoList.add(videoRecorderObject);
            }
        }
        CGENativeLibrary.setLoadImageCallback(loadImageCallback, null);
        decalParentPath = context.getFilesDir() + "/" + ConstantsValue.VideoEdit.PASTER_DIR_NAME + "/";
        musicParentPath = context.getFilesDir() + "/" + ConstantsValue.VideoEdit.MUSIC_DIR_NAME + "/";

        initView();
        initData();
    }

    private void initView() {
        navigation = (CommonNavigation) findViewById(R.id.navigation);

        gl_video_view = (GLVideoView) findViewById(R.id.gl_video_view);
        iv_video_cover = (ImageView) findViewById(R.id.iv_video_cover);
        video_progress_bar = (ProgressBar) findViewById(R.id.video_progress_bar);
        View fl_video_layout = findViewById(R.id.fl_video_layout);
        ViewGroup.LayoutParams layoutParams = fl_video_layout.getLayoutParams();
        layoutParams.height = layoutParams.width = ScreenUtils.getScreenWidth(context);
        fl_video_layout.setLayoutParams(layoutParams);
        decal_container = (DecalView) findViewById(R.id.decal_container);
//        decal_container.setOnScaleViewLocationChangeListener(this);
        tab_layout = (CommonTabLayout) findViewById(R.id.tab_layout);

        rv_storyboard = (RecyclerView) findViewById(R.id.rv_storyboard);
        rv_storyboard.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        StoryboardAdapter storyboardAdapter = new StoryboardAdapter(context, videoList.size());
        rv_storyboard.setAdapter(storyboardAdapter);
        storyboardAdapter.setOnItemClickListener(this);
        tv_tip_0 = (TextView) findViewById(R.id.tv_tip_0);
        tv_tip_1 = (TextView) findViewById(R.id.tv_tip_1);
        tv_tip_2 = (TextView) findViewById(R.id.tv_tip_2);

        ll_audio_control = findViewById(R.id.ll_audio_control);
        iv_close_audio = findViewById(R.id.iv_close_audio);
        iv_close_audio.setOnClickListener(this);
        seek_bar = (BZSeekBar) findViewById(R.id.seek_bar);
        seek_bar.setCurrentProgress(srcAudioValue);
    }

    private void initData() {
        FFmpegUtil.setMediaInfoListener(this);
        gl_video_view.setOnPreparedListener(new GLVideoView.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer myMediaPlayer) {
                iv_video_cover.setVisibility(View.GONE);
                MHLogUtil.d(TAG, "IMediaPlayer-onPrepared");
                VideoEditActivity.this.myMediaPlayer = myMediaPlayer;

                if (currentPosition != 2) {
                    FilterInfo filterInfo = videoList.get(currentVideoPosition).getFilterInfo();
                    if (null != filterInfo) {
                        gl_video_view.setFilterWithConfig(videoUploadInfo.getExtraFilterParam() + filterInfo.getFilter_param());
                        gl_video_view.setFilterIntensity(filterInfo.getIntensity());
                    } else {
                        gl_video_view.setFilterWithConfig(videoUploadInfo.getExtraFilterParam());
                        gl_video_view.setFilterIntensity(1);
                    }
                }
                myMediaPlayer.setVolume(srcAudioValue, srcAudioValue);
            }
        });
        gl_video_view.setOnVideoSizeChanged(new IMediaPlayer.OnVideoSizeChangedListener() {

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                if (video_rotation == 90 || video_rotation == 270) {
                    videoUploadInfo.setVideoHeight(width);
                    videoUploadInfo.setVideoWidth(height);
                } else {
                    videoUploadInfo.setVideoHeight(height);
                    videoUploadInfo.setVideoWidth(width);
                }
            }
        });

        gl_video_view.setOnViewportCalcCompleteListener(new GLVideoView.OnViewportCalcCompleteListener() {
            @Override
            public void onViewportCalcComplete(final TextureRenderer.Viewport viewport) {
                MHLogUtil.d(TAG, "onViewportCalcComplete");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        VideoEditActivity.this.viewport = viewport;
                        if (video_rotation == 90 || video_rotation == 270) {
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) decal_container.getLayoutParams();
                            layoutParams.topMargin = (gl_video_view.getHeight() - viewport.width) / 2;
                            layoutParams.leftMargin = (gl_video_view.getWidth() - viewport.height) / 2;
                            layoutParams.height = viewport.width;
                            layoutParams.width = viewport.height;
                            decal_container.setLayoutParams(layoutParams);
                        } else {
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) decal_container.getLayoutParams();
                            layoutParams.topMargin = viewport.y;
                            layoutParams.leftMargin = viewport.x;
                            layoutParams.height = viewport.height;
                            layoutParams.width = viewport.width;
                            decal_container.setLayoutParams(layoutParams);
                        }
                    }
                });
            }
        });

        navigation.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                if (!isLogin(false)) return;

                if (isFiltering) return;
                TCAgent.onEvent(context, "第二步预览-下一步" + ConstantsValue.android);
                startFilterAndDecal();
            }
        });

        gl_video_view.setVideoUri(videoList.get(0).getVideoTsPath());
        gl_video_view.setOnInfoListener(this);

        fragments = new ArrayList<>();
        FilterFragment filterFragment = new FilterFragment();
        filterFragment.setOnFilterSelectListener(VideoEditActivity.this);
        filterFragment.setVideoPath(videoUploadInfo.getVideoPath());
        fragments.add(filterFragment);

        final DecalFragment decalFragment = new DecalFragment();
        decalFragment.setOnDecalSelectedListener(VideoEditActivity.this);
        fragments.add(decalFragment);

        MusicFragment musicFragment = new MusicFragment();
        musicFragment.setOnMusicSelectedListener(VideoEditActivity.this);
        fragments.add(musicFragment);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fragment_container, filterFragment);
        ft.add(R.id.fragment_container, decalFragment);
        ft.add(R.id.fragment_container, musicFragment);
        ft.hide(decalFragment);
        ft.hide(musicFragment);
        ft.commit();


        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        mTabEntities.add(new TabEntity("滤镜", 0, 0));
        mTabEntities.add(new TabEntity("贴纸", 0, 0));
        mTabEntities.add(new TabEntity("配乐", 0, 0));
        tab_layout.setTabData(mTabEntities);
        tab_layout.setOnTabSelectListener(this);

        seek_bar.setOnSeekBarChangeListener(new BZSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(float progress) {
                bgAudioValue = (1 - progress) * DEFAULT_BG_AUDIO_VALUE;
                if (bgAudioValue > DEFAULT_BG_AUDIO_VALUE) bgAudioValue = DEFAULT_BG_AUDIO_VALUE;
                if (bgAudioValue < 0 || progress == 1) bgAudioValue = 0;

                srcAudioValue = progress;
                if (null != myMediaPlayer && !iv_close_audio.isSelected()) {
                    myMediaPlayer.setVolume(progress, progress);
                }
                if (null != audioMediaPlayer && audioMediaPlayer.isPlaying()) {
                    audioMediaPlayer.setVolume(bgAudioValue, bgAudioValue);
                }
                MHLogUtil.d(TAG, "progress--=" + progress + "---bgAudioValue=" + bgAudioValue + "---srcAudioValue=" + srcAudioValue);
            }
        });

        decal_container.setOnDecalUpdateListener(new OnDecalUpdateListener() {
            @Override
            public void onDecalUpdate() {
                //分别记录到当前分镜
                VideoRecorderObject recorderObject = videoList.get(currentVideoPosition);
                recorderObject.setDecalContentView(decal_container.getCurrentDecalView());
                recorderObject.setDecalBitmap(decal_container.getResultBitmap());
            }
        });
        decal_container.setOnDecalDeleteIconClickListener(new DecalView.OnDecalDeleteIconClickListener() {
            @Override
            public void onDeleteIconClick(DecalView singleFingerView) {
                VideoRecorderObject recorderObject = videoList.get(currentVideoPosition);
                recorderObject.setDecalContentView(null);
                recorderObject.setDecalBitmap(null);
                recorderObject.setDecalInfo(null);
                decalFragment.setCurrentSelected(null);
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null != intent && ConstantsValue.IntentFilterAction.MEDIA_UPLOAD_ACTION.equals(intent.getAction())) {
                    if (!isDestroyed)
                        finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(ConstantsValue.IntentFilterAction.MEDIA_UPLOAD_ACTION));
    }

    private void startFilterAndDecal() {
        isFiltering = true;
        gl_video_view.takeShot(new GLVideoView.TakeShotCallback() {
            @Override
            public void takeShotOK(final Bitmap bmp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_video_cover.setVisibility(View.VISIBLE);
                        iv_video_cover.setImageBitmap(bmp);
                        try {
                            gl_video_view.onPause();
                            gl_video_view.release();
                        } catch (Exception e) {
                            MHLogUtil.e(TAG,e);
                        }
                        try {
                            audioMediaPlayer.pause();
                        } catch (Exception e) {
                            MHLogUtil.e(TAG,e);
                        }
                        filterThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handleFilterAndDecal();
                            }
                        });
                        filterThread.setPriority(Thread.MAX_PRIORITY);
                        filterThread.start();
                    }
                });
            }
        });
    }

    /**
     * 处理滤镜与贴纸逻辑
     */
    private void handleFilterAndDecal() {
        lastProgress = 0;
        totalProgress = 0;
        //注意这里的进度纠正
        if (null != lastMusicInfo && lastMusicInfo.getMusic_duration() > 0) {
            fixVideoDuration = videoDuration + videoDuration * (1 - transcodeRatio);
        } else {
            fixVideoDuration = videoDuration;
        }

        long startTime = System.currentTimeMillis();
        int result;
        boolean needMergeTs = false;
        for (int i = 0; i < videoList.size(); i++) {
            MHLogUtil.e(TAG, i + "-------华丽的分割线--------");
            VideoRecorderObject recorderObject = videoList.get(i);
            FilterInfo filterInfo = recorderObject.getFilterInfo();
            //处理滤镜参数
            String param = videoUploadInfo.getExtraFilterParam();
            float intensity = 1;
            if (null != filterInfo && !MHStringUtils.isEmpty(filterInfo.getFilter_param())) {
                param += filterInfo.getFilter_param();
                intensity = filterInfo.getIntensity();
                MHLogUtil.d(TAG, i + "---need filter--param=" + param);
            }
            //处理贴纸参数
            Bitmap decalBitmap = recorderObject.getDecalBitmap();

            MHLogUtil.d(TAG, "param=" + param + "----decalBitmap=" + decalBitmap);
            //有滤镜或者贴纸逻辑
            if (!MHStringUtils.isEmpty(param) || null != decalBitmap) {
                needMergeTs = true;
                //ts--mp4
                long startTime_1 = System.currentTimeMillis();
                String mp4Path = videoParentPath + "/ts_mp4_" + System.currentTimeMillis() + ".mp4";
                String ts_mp4_cmd = String.format("ffmpeg -y -i %s -vcodec copy -acodec copy -absf aac_adtstoasc -f mp4 -movflags faststart %s", recorderObject.getVideoTsPath(), mp4Path);
                MHLogUtil.d(TAG, i + "---ts_mp4_cmd=" + ts_mp4_cmd);
                result = UtilityAdapter.FFmpegRun("", ts_mp4_cmd);
                MHLogUtil.d(TAG, i + "---ts--mp4 耗时=" + (System.currentTimeMillis() - startTime_1));
                if (result != 0) {
                    MHLogUtil.e(TAG, i + "---ts--mp4-失败");
                    gotoFail();
                    break;
                }

                if (!isFiltering) return;
                //添加贴纸
                if (null != decalBitmap) {
                    decalBitmap = BitmapUtil.scaleBitmap(decalBitmap, videoUploadInfo.getVideoWidth(), videoUploadInfo.getVideoHeight());
                    if (video_rotation == 90 || video_rotation == 270)
                        decalBitmap = BitmapUtil.rotateBitmap(decalBitmap, -video_rotation);
                }
                startTime_1 = System.currentTimeMillis();
                String filter_and_decal = videoParentPath + "/filter_and_decal_" + System.currentTimeMillis() + ".mp4";
                boolean resultFilter = FFmpegUtil.startFilter(filter_and_decal, mp4Path, param, intensity, decalBitmap, CGENativeLibrary.TextureBlendMode.CGE_BLEND_ADDREV.ordinal(), 1.0f, false);
                if (!isFiltering) return;
                if (!resultFilter) {
                    MHLogUtil.e(TAG, i + "---添加贴纸-失败");
                    gotoFail();
                    break;
                }
                if (!isFiltering) return;

                MHLogUtil.d(TAG, i + "---添加贴纸与滤镜 video path=" + filter_and_decal);
                MHLogUtil.d(TAG, i + "---添加贴纸与滤镜 耗时=" + (System.currentTimeMillis() - startTime_1));

                //mp4-ts 最终的ts处理结果-final_ts_path
                startTime_1 = System.currentTimeMillis();
                String final_ts_path = videoParentPath + "/final_ts_path_" + System.currentTimeMillis() + ".ts";
                String cmd = String.format("ffmpeg -y -i %s -vcodec copy -acodec copy -vbsf h264_mp4toannexb %s", filter_and_decal, final_ts_path);
                result = UtilityAdapter.FFmpegRun("", cmd);
                MHLogUtil.d(TAG, i + "---mp4-ts=" + cmd);
                if (result != 0) {
                    MHLogUtil.e(TAG, i + "---mp4-ts失败");
                    gotoFail();
                    break;
                }
                recorderObject.setFinalTsPath(final_ts_path);
                MHLogUtil.d(TAG, i + "---mp4-ts 最终的ts处理耗时=" + (System.currentTimeMillis() - startTime_1));
            }
        }
        if (!needMergeTs) {//没有贴纸与滤镜,那么总进度要改变
            fixVideoDuration = videoDuration * (1 - transcodeRatio);
        }
        totalProgress += videoDuration * progressStep;
        showProgress();

        if (!isFiltering) return;

        String final_video_path = videoUploadInfo.getVideoPath();
        if (needMergeTs) {
            String concat_content = "concat:";
            for (int i = 0; i < videoList.size(); i++) {
                VideoRecorderObject recorderObject = videoList.get(i);
                String tsPath = recorderObject.getFinalTsPath();
                if (null == tsPath) tsPath = recorderObject.getVideoTsPath();

                if (i != videoList.size() - 1) {
                    concat_content += tsPath + "|";
                } else {
                    concat_content += tsPath;
                }
            }
            //合并所有Ts文件为MP4
            long startTime_1 = System.currentTimeMillis();
            final_video_path = videoParentPath + "/final_video_path_" + System.currentTimeMillis() + ".mp4";
            String mergeMp4Cmd = String.format("ffmpeg -y -i %s -vcodec copy -acodec copy -absf aac_adtstoasc -f mp4 -movflags faststart %s", concat_content, final_video_path);
            result = UtilityAdapter.FFmpegRun("", mergeMp4Cmd);
            MHLogUtil.d(TAG, "mergeMp4Cmd=" + mergeMp4Cmd);
            if (result != 0) {
                MHLogUtil.e(TAG, "合并所有Ts文件为MP4--失败");
                gotoFail();
                return;
            }
            totalProgress += videoDuration * progressStep;
            showProgress();
            MHLogUtil.d(TAG, "合并所有Ts文件为MP4 耗时=" + (System.currentTimeMillis() - startTime_1));
        }
        if (!isFiltering) return;

        //添加配乐
        long startTime_1 = System.currentTimeMillis();

        String temp = addBackgroundMusic(lastMusicInfo, final_video_path);
        if (null == temp) {
            temp = final_video_path;
            MHLogUtil.e(TAG, "不需要添加配乐--或者添加失败");
        }
        MHLogUtil.d(TAG, "添加配乐 耗时=" + (System.currentTimeMillis() - startTime_1));
        MHLogUtil.d(TAG, "final video path=" + temp);
        totalProgress = (int) fixVideoDuration;
        showProgress();
        if (video_rotation != 0) {
            String rotationPath = videoParentPath + "/final_add_rotation_" + System.currentTimeMillis() + ".mp4";
            String cmd = String.format("ffmpeg -y -i %s -vcodec copy -acodec copy -metadata:s:v:0 rotate=%d %s", temp, video_rotation, rotationPath);
            result = UtilityAdapter.FFmpegRun("", cmd);
            if (result != 0) {
                MHLogUtil.e(TAG, "添加视频旋转信息--失败");
                gotoFail();
                return;
            }
            temp = rotationPath;
            MHLogUtil.d(TAG, "添加视频旋转信息");
        }

        //开始发布
        if (!isDestroyed && isFiltering) {
            Intent intent = new Intent(context, VideoPublishActivity.class);
            videoUploadInfo.setVideoPath(temp);
            intent.putExtra("videoUploadInfo", videoUploadInfo);
            startActivity(intent);
        }
        isFiltering = false;

        MHLogUtil.d(TAG, "处理成功--总体 耗时=" + (System.currentTimeMillis() - startTime));
    }

    private void showProgress() {
        if (totalProgress + 100 > fixVideoDuration) {
            totalProgress = (int) (fixVideoDuration - 100);
        }
        handler.sendEmptyMessage(MESSAGE_WHAT_FILTER_PROGRESS);
    }

    /**
     * 视频处理失败的处理逻辑
     */
    private void gotoFail() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToastAtCenter("视频处理失败\n将不采用特效");
                if (!isDestroyed) {
                    Intent intent = new Intent(context, VideoPublishActivity.class);
                    intent.putExtra("videoUploadInfo", videoUploadInfo);
                    startActivity(intent);
                }
                isFiltering = false;
            }
        });
    }

    int lastProgress;
    int totalProgress;
    double fixVideoDuration;

    @Override
    public void sendMediaInfo(int what, int extra) {
        switch (what) {
            case FFmpegUtil.MEDIA_INFO_WHAT_FILTER_PROGRESS:
                if (extra != 0)
                    totalProgress += extra - lastProgress;
                lastProgress = extra;

//                MHLogUtil.d(TAG, "单项进度=" + extra + "---totalProgress=" + totalProgress);
                //从C掉过来的要处理一下
                handler.sendEmptyMessage(MESSAGE_WHAT_FILTER_PROGRESS);
                break;
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                MHLogUtil.d(TAG, "media_info_video_rendering_start--what=" + what + "---extra=" + extra);
                video_progress_bar.setVisibility(View.INVISIBLE);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                MHLogUtil.d(TAG, "media_info_buffering_start--what=" + what + "---extra=" + extra);
                //会影响贴纸的焦点,导致乱动,问题不明
//                video_progress_bar.setVisibility(View.VISIBLE);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                MHLogUtil.d(TAG, "media_info_buffering_end--what=" + what + "---extra=" + extra);
                video_progress_bar.setVisibility(View.INVISIBLE);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                MHLogUtil.d(TAG, "media_info_video_rotation_changed--what=" + what + "---extra=" + extra);
                video_rotation = extra;
                iv_video_cover.setRotation(extra);

                //贴纸限定的区域要倒置
                if (video_rotation == 90 || video_rotation == 270) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) decal_container.getLayoutParams();
                    layoutParams.topMargin = (gl_video_view.getHeight() - viewport.width) / 2;
                    layoutParams.leftMargin = (gl_video_view.getWidth() - viewport.height) / 2;
                    layoutParams.height = viewport.width;
                    layoutParams.width = viewport.height;
                    decal_container.setLayoutParams(layoutParams);
                }
                break;
        }
        return false;
    }

    @Override
    public void onFilterSelect(FilterInfo filterInfo) {
        if (null == filterInfo) return;
        if (null == lastFilterInfo || !hasWitchStoryboard) {//只要没有切换过分镜,就默认全都采用这种滤镜
            for (VideoRecorderObject recorderObject : videoList) {
                recorderObject.setFilterInfo(filterInfo);
            }
        }
        lastFilterInfo = filterInfo;
        if (null != gl_video_view) {
            gl_video_view.setFilterWithConfig(videoUploadInfo.getExtraFilterParam() + filterInfo.getFilter_param());
            gl_video_view.setFilterIntensity(filterInfo.getIntensity());
        }
        //分别记录到当前分镜
        VideoRecorderObject recorderObject = videoList.get(currentVideoPosition);
        recorderObject.setFilterInfo(filterInfo);
    }

    @Override
    public void onDecalSelectedListener(final DecalInfo decalInfo) {
        VideoRecorderObject recorderObject = videoList.get(currentVideoPosition);
        if (recorderObject.getDecalInfo() == decalInfo) return;
        recorderObject.setDecalInfo(decalInfo);

        if (!MHStringUtils.isEmpty(decalInfo.getSticker_uri())) {
            if (decalInfo.getType() == DecalInfo.FROM_LOCAL) {
                Bitmap bitmap = BitmapFactory.decodeFile(decalParentPath + decalInfo.getSticker_uri());
                if (null != bitmap) {
                    decal_container.addDecal(bitmap);
                }
            } else {
                ImageLoader.getInstance().displayImage(decalInfo.getSticker_uri(), decal_container.mImageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        decal_container.addDecal(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }
        } else {
            decal_container.removeAllViews();
            recorderObject.setDecalBitmap(null);
            recorderObject.setDecalContentView(null);
        }

    }

    @Override
    public void onMusicSelectedListener(MusicInfo musicInfo) {
        lastMusicInfo = musicInfo;
        if (null == audioMediaPlayer) {
            audioMediaPlayer = new MediaPlayer();
            audioMediaPlayer.setOnPreparedListener(this);
        }

        try {
            if (audioMediaPlayer.isPlaying()) audioMediaPlayer.stop();
            audioMediaPlayer.reset();
            audioMediaPlayer.setLooping(true);
            String musicPath = musicParentPath + musicInfo.getMusic_uri();
            if (musicInfo.getType() == MusicInfo.FROM_NETWORK) {
                musicPath = musicInfo.getDownloadMusicPath();
            }
            audioMediaPlayer.setDataSource(musicPath);
            audioMediaPlayer.prepareAsync();
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    private String addBackgroundMusic(MusicInfo musicInfo, final String videoPath) {
        if (null == musicInfo || musicInfo.getMusic_duration() <= 0) return null;
        MHLogUtil.e(TAG, "addBackgroundMusic--videoDuration=" + videoDuration + "---getMusic_duration--=" + musicInfo.getMusic_duration());

        String musicPath = musicParentPath + musicInfo.getMusic_uri();

        if (musicInfo.getType() == MusicInfo.FROM_NETWORK && null != musicInfo.getDownloadMusicPath()) {
            musicPath = musicInfo.getDownloadMusicPath();
        }

        //先对齐
        int result = 0;
        long startTime = System.currentTimeMillis();
        String alignmentOutPath = videoParentPath + "/alignment_" + System.currentTimeMillis() + ".mp3";
        if (musicInfo.getMusic_duration() > videoDuration) {//需要裁切配乐
            String alignmentCmd = "ffmpeg -y -i " + musicPath + " -ss 0 -t " + String.format("%.3f", videoDuration / 1000) + " -acodec copy " + alignmentOutPath;
            result = UtilityAdapter.FFmpegRun("", alignmentCmd);
            totalProgress += videoDuration * progressStep;
            showProgress();
        } else if (musicInfo.getMusic_duration() < videoDuration) {//需要合并配乐到视频长度
            int contactNum = (int) (videoDuration / musicInfo.getMusic_duration() + 1);
            MHLogUtil.d(TAG, "contactNum=" + contactNum);
            String concatCmd = "ffmpeg -y -i concat:";
            for (int i = 0; i < contactNum; i++) {
                if (i == contactNum - 1)
                    concatCmd += musicPath;
                else
                    concatCmd += musicPath + "|";
            }

            String contactOutPath = videoParentPath + "/contact_" + System.currentTimeMillis() + ".mp3";
            concatCmd += " -acodec copy " + contactOutPath;
            MHLogUtil.d(TAG, "contactCmd=" + concatCmd);
            result = UtilityAdapter.FFmpegRun("", concatCmd);
            totalProgress += videoDuration * progressStep;
            showProgress();
            if (result != 0) {
                return null;
            }

            String alignmentCmd = "ffmpeg -y -i " + contactOutPath + " -ss 0 -t " + String.format("%.3f", videoDuration / 1000) + " -acodec copy " + alignmentOutPath;
            result = UtilityAdapter.FFmpegRun("", alignmentCmd);
            totalProgress += videoDuration * progressStep;
            showProgress();
        } else {
            alignmentOutPath = musicPath;
        }
        if (result != 0) {
            return null;
        }
        long endTime = System.currentTimeMillis();
        //裁切到一样长
        MHLogUtil.d(TAG, "------对齐耗时=" + (endTime - startTime));
        startTime = endTime;
        //和视频合在一起
        float audioValue = srcAudioValue;
        if (iv_close_audio.isSelected())
            audioValue = 0;

        String finalPath = videoParentPath + "/final_bg_music_" + System.currentTimeMillis() + ".mp4";
        String mergeCmd = "ffmpeg -y -i %s -i %s -c:v copy -filter_complex \"[0:a]aformat=fltp:44100:stereo,volume=%.2f,apad[0a];[1]aformat=fltp:44100:stereo,volume=%.2f[1a];[0a][1a]amerge[a]\" -map 0:v -map \"[a]\" -ac 2 %s";
        mergeCmd = String.format(mergeCmd, videoPath, alignmentOutPath, audioValue, bgAudioValue, finalPath);
        result = UtilityAdapter.FFmpegRun("", mergeCmd);
        MHLogUtil.d(TAG, "------添加配乐耗时=" + (System.currentTimeMillis() - startTime));
        totalProgress += videoDuration * progressStep;
        showProgress();

        if (result != 0) {
            return null;
        }
        return finalPath;
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            //处理视频存放目录问题
            videoParentPath = videoUploadInfo.getFilesParent();
            //表示视频不是我们录制的视频,或者视频地址不存在
            if (MHStringUtils.isEmpty(videoParentPath) || !videoParentPath.contains(ConstantsValue.Video.VIDEO_TEMP_PATH)) {
                videoParentPath = ConstantsValue.Video.VIDEO_TEMP_PATH + "/temp_" + System.currentTimeMillis();
                File parentFile = new File(videoParentPath);
                if (!parentFile.exists()) parentFile.mkdirs();
                isOtherVideo = true;

                videoUploadInfo.setFilesParent(videoParentPath);
                MHLogUtil.d(TAG, "不是我们平台录制的视频");
            } else {
                isOtherVideo = false;
            }
            if (!isFiltering) {
                hideProgressDialog();
                gl_video_view.onResume();
                iv_video_cover.setVisibility(View.GONE);
                try {
                    if (null != audioMediaPlayer)
                        audioMediaPlayer.start();
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                }
            } else {
                iv_video_cover.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }


    @Override
    protected void onPause() {
        try {
            if (null != myMediaPlayer && myMediaPlayer.isPlaying())
                myMediaPlayer.pause();
            if (null != audioMediaPlayer && audioMediaPlayer.isPlaying()) {
                audioMediaPlayer.pause();
            }
            gl_video_view.onPause();
            gl_video_view.release();

            handler.removeMessages(MESSAGE_WHAT_FILTER_PROGRESS);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_audio:
                if (iv_close_audio.isSelected()) {
                    if (null != myMediaPlayer) {
                        myMediaPlayer.setVolume(srcAudioValue, srcAudioValue);
                    }
                } else {
                    if (null != myMediaPlayer) {
                        myMediaPlayer.setVolume(0, 0);
                    }
                }
                iv_close_audio.setSelected(!iv_close_audio.isSelected());
                break;
        }
    }


    @Override
    public void OnItemClick(View view, int position) {
        hasWitchStoryboard = true;

        currentVideoPosition = position;
        //处理当前分镜逻辑
        VideoRecorderObject videoRecorderObject = videoList.get(position);
        DecalInfo decalInfo = videoRecorderObject.getDecalInfo();
        decal_container.restoreDecalView(videoRecorderObject.getDecalContentView());

        DecalFragment fragment = (DecalFragment) fragments.get(1);
        fragment.setCurrentSelected(decalInfo);

        gl_video_view.setVideoUri(videoRecorderObject.getVideoTsPath());

        FilterInfo filterInfo = videoRecorderObject.getFilterInfo();
        if (null != filterInfo) {
            gl_video_view.setFilterWithConfig(filterInfo.getFilter_param());
            gl_video_view.setFilterIntensity(filterInfo.getIntensity());
        }
        FilterFragment filterFragment = (FilterFragment) fragments.get(0);
        filterFragment.setCurrentSelected(filterInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
            isFiltering = false;
            progressDialog = null;
            tv_handle_progress = null;
        }
        FFmpegUtil.endFilter();
        isFiltering = false;
        FFmpegUtil.setMediaInfoListener(null);
        handler.removeMessages(MESSAGE_WHAT_FILTER_PROGRESS);
        MHLogUtil.d(TAG, "onDestroy");
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onTabSelect(int position) {
        if (null == fragmentManager)
            fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(fragments.get(position)).hide(fragments.get(currentPosition)).commit();

        VideoRecorderObject recorderObject = videoList.get(currentVideoPosition);
        FilterInfo filterInfo = recorderObject.getFilterInfo();
        String videoPath = recorderObject.getVideoTsPath();

        currentPosition = position;
        switch (currentPosition) {
            case 0:
                if (null != recorderObject.getDecalContentView()) {
                    decal_container.restoreDecalView(recorderObject.getDecalContentView());
                }
                if (null != filterInfo) {
                    gl_video_view.setFilterWithConfig(videoUploadInfo.getExtraFilterParam() + filterInfo.getFilter_param());
                    gl_video_view.setFilterIntensity(filterInfo.getIntensity());
                }
                rv_storyboard.setVisibility(View.VISIBLE);
                ll_audio_control.setVisibility(View.GONE);
                tv_tip_0.setText("分镜");
                tv_tip_1.setText("滤镜");
                tv_tip_2.setText("(每个分镜可以添加不同滤镜)");
                tv_tip_2.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (null != recorderObject.getDecalContentView()) {
                    decal_container.restoreDecalView(recorderObject.getDecalContentView());
                }
                if (null != filterInfo) {
                    gl_video_view.setFilterWithConfig(videoUploadInfo.getExtraFilterParam() + filterInfo.getFilter_param());
                    gl_video_view.setFilterIntensity(filterInfo.getIntensity());
                }
                rv_storyboard.setVisibility(View.VISIBLE);
                ll_audio_control.setVisibility(View.GONE);
                tv_tip_0.setText("分镜");
                tv_tip_1.setText("贴纸");
                tv_tip_2.setText("(每个分镜可以添加不同贴纸)");
                tv_tip_2.setVisibility(View.VISIBLE);
                break;
            case 2:
                decal_container.removeAllViews();
                gl_video_view.setFilterWithConfig("");
                gl_video_view.setFilterIntensity(1);

                ll_audio_control.setVisibility(View.VISIBLE);
                rv_storyboard.setVisibility(View.GONE);
                tv_tip_0.setText("原声");
                tv_tip_1.setText("配乐");
                tv_tip_2.setVisibility(View.GONE);
//                tv_tip_2.setText("(每个分镜可以添加不同配乐)");
                videoPath = videoUploadInfo.getVideoPath();
                break;
        }
        gl_video_view.setVideoUri(videoPath);
        return false;
    }

    private CGENativeLibrary.LoadImageCallback loadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        //注意， 这里回传的name不包含任何路径名， 仅为具体的图片文件名如 1.jpg
        @Override
        public Bitmap loadImage(String name, Object arg) {

            MHLogUtil.i(TAG, "正在加载图片: " + name);
            AssetManager am = getAssets();
            InputStream is;
            try {
                is = am.open(name);
            } catch (IOException e) {
                MHLogUtil.e(TAG, "Can not open file " + name);
                return null;
            }
            return BitmapFactory.decodeStream(is);
        }

        @Override
        public void loadImageOK(Bitmap bmp, Object arg) {
            MHLogUtil.i(TAG, "加载图片完毕， 可以自行选择 recycle or cache");

            //loadImage结束之后可以马上recycle
            //唯一不需要马上recycle的应用场景为 多个不同的滤镜都使用到相同的bitmap
            //那么可以选择缓存起来。
//            bmp.recycle();
        }
    };

    @Override
    public void onTabReselect(int position) {

    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setVolume(bgAudioValue, bgAudioValue);
        mp.start();
    }


    public void showHandleProgressDialog() {
        MHLogUtil.e(TAG, "showHandleProgressDialog");
        progressDialog = new Dialog(context, R.style.Dialog_loading);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        View view = View.inflate(context, R.layout.dialog_handle_progress_layout, null);
        tv_handle_progress = (TextView) view.findViewById(R.id.tv_handle_progress);
        square_progress_view = (SquareProgressView) view.findViewById(R.id.square_progress_view);

        FilterFragment baseFragment = (FilterFragment) fragments.get(0);
        Bitmap firstFrameImage = baseFragment.getFirstFrameImage();
        if (null != firstFrameImage) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) square_progress_view.getLayoutParams();
            Point screenSize = ScreenUtils.getScreenSize(context);
            layoutParams.height = (screenSize.x - layoutParams.rightMargin - layoutParams.leftMargin) * firstFrameImage.getHeight() / firstFrameImage.getWidth();
            //要保证下面的最小高度
            int height = screenSize.y - DensityUtil.dip2px(context, 160);
            if (layoutParams.height > height) {
                layoutParams.height = height;
                layoutParams.width = height * firstFrameImage.getWidth() / firstFrameImage.getHeight();
            }
            square_progress_view.setImageBitmap(firstFrameImage);
            square_progress_view.setLayoutParams(layoutParams);
        }
        square_progress_view.setProgressColor(Color.WHITE);

        TextView tv_handle_info = (TextView) view.findViewById(R.id.tv_handle_info);
        tv_handle_info.setText("正在加工视频...");
        TCAgent.onEvent(context, "处理页" + ConstantsValue.android);
        view.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFiltering = false;
                hideProgressDialog();
                TCAgent.onEvent(context, "处理退回上一页" + ConstantsValue.android);
            }
        });

        progressDialog.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(context), ViewGroup.LayoutParams.MATCH_PARENT));
        progressDialog.show();

    }

    private void hideProgressDialog() {
        FFmpegUtil.endFilter();
        if (null != progressDialog && progressDialog.isShowing()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    isFiltering = false;
                    progressDialog = null;
                    tv_handle_progress = null;
                    if (!isDestroyed) {
                        iv_video_cover.setVisibility(View.GONE);
                        try {
                            gl_video_view.onResume();
                        } catch (Exception e) {
                            MHLogUtil.e(TAG,e);
                        }
                        try {
                            audioMediaPlayer.start();
                        } catch (Exception e) {
                            MHLogUtil.e(TAG,e);
                        }
                    }
                }
            });
        }
    }


}

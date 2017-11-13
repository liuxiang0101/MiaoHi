package com.haiqiu.miaohi.widget.mediaplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Surface;

import com.haiqiu.miaohi.utils.MHLogUtil;

import org.wysaid.common.Common;
import org.wysaid.nativePort.CGEFrameRenderer;
import org.wysaid.texUtils.TextureRenderer;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by zhandalin on 2016/10/21.
 */
public class GLVideoView extends GLTextureView implements SurfaceTexture.OnFrameAvailableListener {

    private static final java.lang.String TAG = "GLVideoView_TAG";
    private MyMediaPlayer mPlayer;

    private SurfaceTexture mSurfaceTexture;
    private int mVideoTextureID;
    private CGEFrameRenderer mFrameRenderer;

    private TextureRenderer.Viewport mRenderViewport = new TextureRenderer.Viewport();
    private float[] mTransformMatrix = new float[16];
    private boolean mIsUsingMask = false;
    private Context context;
    private String uri;
    private OnPreparedListener onPreparedListener;
    private boolean isSurfaceCreated;
    private IMediaPlayer.OnInfoListener onInfoListener;
    private Surface surface;
    private String filterConfig;
    private float intensity;
    private IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener;
    private OnViewportCalcCompleteListener onViewportCalcCompleteListener;
    private int video_rotation;

    public boolean isUsingMask() {
        return mIsUsingMask;
    }

    private float mMaskAspectRatio = 1.0f;

    private int mViewWidth = 1000;
    private int mViewHeight = 1000;

    public int getViewWidth() {
        return mViewWidth;
    }

    public int getViewheight() {
        return mViewHeight;
    }

    private int mVideoWidth = 1000;
    private int mVideoHeight = 1000;

    private boolean mFitFullView = false;

    public GLVideoView(Context context) {
        this(context, null);
    }

    public GLVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    private void initVideoView() {
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 0, 0);

        setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                MHLogUtil.d(TAG, "onSurfaceCreated");
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                GLES20.glDisable(GLES20.GL_STENCIL_TEST);
                if (uri != null && (mSurfaceTexture == null || mVideoTextureID == 0)) {
                    mVideoTextureID = Common.genSurfaceTextureID();
                    mSurfaceTexture = new SurfaceTexture(mVideoTextureID);
                    mSurfaceTexture.setOnFrameAvailableListener(GLVideoView.this);
                    _useUri();
                }
                isSurfaceCreated = true;
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                MHLogUtil.d(TAG, "onSurfaceChanged");

                GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

                mViewWidth = width;
                mViewHeight = height;
                calcViewport();
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                if (mSurfaceTexture == null || mFrameRenderer == null) {
                    return;
                }

                mSurfaceTexture.updateTexImage();

                if (!mPlayer.isPlaying()) {
                    return;
                }

                mSurfaceTexture.getTransformMatrix(mTransformMatrix);
                mFrameRenderer.update(mVideoTextureID, mTransformMatrix);

                mFrameRenderer.runProc();

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

                GLES20.glEnable(GLES20.GL_BLEND);
                mFrameRenderer.render(mRenderViewport.x, mRenderViewport.y, mRenderViewport.width, mRenderViewport.height);
                GLES20.glDisable(GLES20.GL_BLEND);
            }
        });
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public synchronized void setFilterWithConfig(final String filterConfig) {
        this.filterConfig = filterConfig;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mFrameRenderer != null) {
                    mFrameRenderer.setFilterWidthConfig(filterConfig);
                } else {
                    MHLogUtil.e(TAG, "setFilterWithConfig after release!!");
                }
            }
        });
    }

    public void setFilterIntensity(final float intensity) {
        this.intensity = intensity;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mFrameRenderer != null) {
                    mFrameRenderer.setFilterIntensity(intensity);
                } else {
                    MHLogUtil.e(TAG, "setFilterIntensity after release!!");
                }
            }
        });
    }

    private long mTimeCount2 = 0;
    private long mFramesCount2 = 0;
    private long mLastTimestamp2 = 0;

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        requestRender();
        if (mLastTimestamp2 == 0)
            mLastTimestamp2 = System.currentTimeMillis();

        long currentTimestamp = System.currentTimeMillis();

        ++mFramesCount2;
        mTimeCount2 += currentTimestamp - mLastTimestamp2;
        mLastTimestamp2 = currentTimestamp;
        if (mTimeCount2 >= 1e3) {
//            MHLogUtil.i(TAG, String.format("播放帧率: %d", mFramesCount2));
            mTimeCount2 -= 1e3;
            mFramesCount2 = 0;
        }
    }


    private void _useUri() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.reset();
        } else {
            mPlayer = new MyMediaPlayer();
        }
        mPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final IMediaPlayer mp) {
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        if (mFrameRenderer == null) {
                            mFrameRenderer = new CGEFrameRenderer();
                        }
                        if (mFrameRenderer.init(mVideoWidth, mVideoHeight, mVideoWidth, mVideoHeight)) {
                            //Keep right orientation for source texture blendings
                            mFrameRenderer.setSrcFlipScale(1.0f, -1.0f);
                            mFrameRenderer.setRenderFlipScale(1.0f, -1.0f);
                        } else {
                            MHLogUtil.e(TAG, "Frame Recorder init failed!");
                        }
                        if (null != filterConfig && intensity != 0) {
                            mFrameRenderer.setFilterWidthConfig(filterConfig);
                            mFrameRenderer.setFilterIntensity(intensity);
                        }
                        calcViewport();
                        post(new Runnable() {
                            @Override
                            public void run() {
                                if (null != onPreparedListener) onPreparedListener.onPrepared(mp);
                            }
                        });
                    }
                });
                mp.start();
                MHLogUtil.d(TAG, String.format("Video resolution 1: %d x %d", mVideoWidth, mVideoHeight));
            }
        });
        mPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
                if (null != onInfoListener) onInfoListener.onInfo(iMediaPlayer, what, extra);
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                        MHLogUtil.d(TAG, "--media_info_video_rotation_changed---extra=" + extra);
                        video_rotation = extra;
                        setRotation(extra);
                        calcViewport();
                        break;
                }
                return false;
            }
        });
        mPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                if (video_rotation == 90 || video_rotation == 270) {
                    mVideoWidth = height;
                    mVideoHeight = width;
                } else {
                    mVideoWidth = width;
                    mVideoHeight = height;
                }
                if (null != onVideoSizeChangedListener)
                    onVideoSizeChangedListener.onVideoSizeChanged(mp, width, height, sar_num, sar_den);
                calcViewport();
            }
        });

        try {
            mPlayer.setDataSource(getContext(), Uri.parse(uri));
            if (null == surface) surface = new Surface(mSurfaceTexture);
            mPlayer.setSurface(surface);
            mPlayer.setLooping(true);
            mPlayer.prepareAsync();
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    public MyMediaPlayer getmPlayer() {
        return mPlayer;
    }


    public synchronized void setVideoUri(String uri) {
        this.uri = uri;
        if (!isSurfaceCreated) return;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                MHLogUtil.i(TAG, "setVideoUri...");
                if (mSurfaceTexture == null || mVideoTextureID == 0) {
                    mVideoTextureID = Common.genSurfaceTextureID();
                    mSurfaceTexture = new SurfaceTexture(mVideoTextureID);
                    mSurfaceTexture.setOnFrameAvailableListener(GLVideoView.this);
                }
                _useUri();
            }
        });
    }

    private void calcViewport() {
        float scaling = 1.0f * mVideoWidth / mVideoHeight;

        //控件高度是可变的,宽是屏幕宽度,所以每次都要检查高度是否满足
        //长视频
        if (mVideoWidth >= mVideoHeight) {
            mRenderViewport.width = mViewWidth;
            mRenderViewport.height = (int) (mViewWidth / scaling);

            if (mRenderViewport.height > mViewHeight) {
                mRenderViewport.width = (int) (mViewHeight * scaling);
                mRenderViewport.height = mViewHeight;
            }
        } else {//高视频
            if (video_rotation == 90 || video_rotation == 270) {
                mRenderViewport.width = mViewHeight;
                mRenderViewport.height = (int) (mViewHeight * scaling);
            } else {
                mRenderViewport.width = (int) (mViewHeight * scaling);
                mRenderViewport.height = mViewHeight;
            }
        }

        mRenderViewport.x = (mViewWidth - mRenderViewport.width) / 2;
        mRenderViewport.y = (mViewHeight - mRenderViewport.height) / 2;
        if (null != onViewportCalcCompleteListener)
            onViewportCalcCompleteListener.onViewportCalcComplete(mRenderViewport);
        MHLogUtil.i(TAG, String.format("View port: %d, %d, %d, %d", mRenderViewport.x, mRenderViewport.y, mRenderViewport.width, mRenderViewport.height));
    }


    //must be in the OpenGL thread!
    public void release() {
        surface = null;
        MHLogUtil.i(TAG, "Video player view release...");
        if (mPlayer != null) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    MHLogUtil.i(TAG, "Video player view release run...");
                    if (mPlayer != null) {
                        mPlayer.setSurface(null);
                        if (mPlayer.isPlaying())
                            mPlayer.stop();
                        mPlayer.release();
                        mPlayer = null;
                    }
                    if (mFrameRenderer != null) {
                        mFrameRenderer.release();
                        mFrameRenderer = null;
                    }
                    if (mSurfaceTexture != null) {
                        mSurfaceTexture.release();
                        mSurfaceTexture = null;
                    }
                    if (mVideoTextureID != 0) {
                        GLES20.glDeleteTextures(1, new int[]{mVideoTextureID}, 0);
                        mVideoTextureID = 0;
                    }
                    mIsUsingMask = false;
                    MHLogUtil.i(TAG, "Video player view release OK");
                }
            });
        }
    }

    public interface TakeShotCallback {
        //传入的bmp可以由接收者recycle
        void takeShotOK(Bitmap bmp);
    }


    public synchronized void takeShot(final TakeShotCallback callback) {
        assert callback != null : "callback must not be null!";

        if (mFrameRenderer == null) {
            MHLogUtil.e(TAG, "Drawer not initialized!");
            callback.takeShotOK(null);
            return;
        }

        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mRenderViewport.width <= 0 || mRenderViewport.height <= 0) {
                    callback.takeShotOK(null);
                    return;
                }
                IntBuffer buffer = IntBuffer.allocate(mRenderViewport.width * mRenderViewport.height);

                GLES20.glReadPixels(mRenderViewport.x, mRenderViewport.y, mRenderViewport.width, mRenderViewport.height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
                Bitmap bmp = Bitmap.createBitmap(mRenderViewport.width, mRenderViewport.height, Bitmap.Config.ARGB_8888);
                bmp.copyPixelsFromBuffer(buffer);

                Bitmap bmp2 = Bitmap.createBitmap(mRenderViewport.width, mRenderViewport.height, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bmp2);
                Matrix mat = new Matrix();
                mat.setTranslate(0.0f, -mRenderViewport.height / 2.0f);
                mat.postScale(1.0f, -1.0f);
                mat.postTranslate(0.0f, mRenderViewport.height / 2.0f);

                canvas.drawBitmap(bmp, mat, null);
                bmp.recycle();

                callback.takeShotOK(bmp2);
            }
        });

    }

    public TextureRenderer.Viewport getRenderViewport() {
        return mRenderViewport;
    }

    public interface OnPreparedListener {
        void onPrepared(IMediaPlayer myMediaPlayer);
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    public void setOnInfoListener(IMediaPlayer.OnInfoListener onInfoListener) {
        this.onInfoListener = onInfoListener;
    }

    public void setOnVideoSizeChanged(IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.onVideoSizeChangedListener = onVideoSizeChangedListener;
    }


    public interface OnViewportCalcCompleteListener {
        void onViewportCalcComplete(TextureRenderer.Viewport viewport);
    }

    public void setOnViewportCalcCompleteListener(OnViewportCalcCompleteListener onViewportCalcCompleteListener) {
        this.onViewportCalcCompleteListener = onViewportCalcCompleteListener;
    }


}

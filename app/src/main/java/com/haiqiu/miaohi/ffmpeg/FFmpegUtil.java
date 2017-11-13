package com.haiqiu.miaohi.ffmpeg;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.wysaid.nativePort.NativeLibraryLoader;

import java.lang.ref.WeakReference;

/**
 * Created by luoye on 16/9/18.
 */
public class FFmpegUtil {
    public static final int MEDIA_INFO_WHAT_VIDEO_DURATION = 20;
    public static final int MEDIA_INFO_WHAT_FILTER_PROGRESS = 21;
    public static final int MEDIA_INFO_WHAT_VIDEO_WIDTH = 23;
    public static final int MEDIA_INFO_WHAT_VIDEO_HEIGHT = 24;
    public static final int MEDIA_INFO_WHAT_VIDEO_ROTATE = 25;


    static {
        NativeLibraryLoader.load();
    }

    private static WeakReference<MediaInfoListener> mediaInfoListener;

    public static native int init(Context context);


    /**
     * @param dstData 目标数据
     * @param srcData 源数据
     * @param width   yuv的宽
     * @param height  yuv的高
     * @param degrees 度数
     */
    public static native int rotateYUV(byte[] dstData, byte[] srcData, int width, int height, int degrees);


    public static native boolean startFilter(String outputFilename, String inputFilename, String filterConfig, float filterIntensity, Bitmap blendImage, int blendMode, float blendIntensity, boolean mute);

    public static native void endFilter();


    public static void sendMediaInfo(int what, int extra) {
        if (null != mediaInfoListener && mediaInfoListener.get() != null) {
            mediaInfoListener.get().sendMediaInfo(what, extra);
        }
    }

    public interface MediaInfoListener {
        void sendMediaInfo(int what, int extra);
    }

    public static void setMediaInfoListener(MediaInfoListener mediaListener) {
        mediaInfoListener = new WeakReference<>(mediaListener);
    }
}

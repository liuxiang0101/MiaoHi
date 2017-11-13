package com.haiqiu.miaohi.widget.mediaplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.haiqiu.miaohi.R;

/**
 * Created by zhandalin on 2016-06-06 10:11.
 * 说明:套一层方便横竖屏切换
 */
public class VideoViewWrap extends RelativeLayout {

    private BaseVideoView videoView;

    public VideoViewWrap(Context context) {
        this(context, null);
    }

    public VideoViewWrap(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoViewWrap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VideoViewWrap);
        int videoType = typedArray.getInt(R.styleable.VideoViewWrap_video_type, 0);
        switch (videoType) {
            case 1:
                videoView = new BottomProgressVideoView(context);
                break;
            case 2:
                videoView = new BaseVideoView(context);
                break;
            default:
                videoView = new VideoView(context);
        }
        setBackgroundColor(Color.BLACK);
        addView(videoView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        typedArray.recycle();
    }

    public BaseVideoView getVideoView() {
        return videoView;
    }

}

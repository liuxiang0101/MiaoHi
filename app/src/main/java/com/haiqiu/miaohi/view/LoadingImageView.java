package com.haiqiu.miaohi.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.haiqiu.miaohi.R;

/**
 * Created by zhandalin on 2016-07-27 19:54.
 * 说明:加载的View,飞小鲸鱼
 */
public class LoadingImageView extends ImageView {

    private AnimationDrawable animationDrawable;

    public LoadingImageView(Context context) {
        this(context, null);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setImageResource(R.drawable.anim_loading);
        animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (null == animationDrawable) return;
        if (View.VISIBLE != visibility) {
            animationDrawable.stop();
        } else {
            animationDrawable.start();
        }
    }
}

package com.haiqiu.miaohi.widget.mediaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.DensityUtil;

/**
 * Created by zhandalin on 2017-01-12 15:17.
 * 说明:下边带一个进度条的videoView
 */
public class BottomProgressVideoView extends BaseVideoView {

    ProgressBar bottom_progressBar;

    public BottomProgressVideoView(Context context) {
        this(context, null);
    }

    public BottomProgressVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomProgressVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bottom_progressBar = (ProgressBar) View.inflate(context, R.layout.player_progress_bar, null);
        bottom_progressBar.setMax(1000);
        bottom_progressBar.setVisibility(INVISIBLE);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 3));
        layoutParams.gravity = Gravity.BOTTOM;
        addView(bottom_progressBar, layoutParams);
    }

    @Override
    protected void setPosition(float position) {
        super.setPosition(position);
        bottom_progressBar.setProgress((int) (position / videoDuration * 1000 + 0.5));
    }

    @Override
    protected void start() {
        super.start();
        if (ll_bottom_control.getVisibility() == VISIBLE) {
            hideControlView();
        } else {
            bottom_progressBar.setVisibility(VISIBLE);
        }
    }

    @Override
    public void setFullScreen(int requestedOrientation) {
        super.setFullScreen(requestedOrientation);
        bottom_progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void quitFullScreen(boolean isCompletion) {
        super.quitFullScreen(isCompletion);
        bottom_progressBar.setVisibility(VISIBLE);
    }

    @Override
    protected void showControlView() {
        if (ll_bottom_control.getVisibility() == VISIBLE) return;
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0);
            translateAnimation.setDuration(300);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ll_bottom_control.setVisibility(VISIBLE);
                    bottom_progressBar.setVisibility(INVISIBLE);
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

    @Override
    protected void hideControlView() {
        if (ll_bottom_control.getVisibility() != VISIBLE) return;
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f);

        translateAnimation.setDuration(300);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_bottom_control.setVisibility(GONE);
                ll_bottom_control.clearAnimation();
                bottom_progressBar.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll_bottom_control.startAnimation(translateAnimation);
    }
}

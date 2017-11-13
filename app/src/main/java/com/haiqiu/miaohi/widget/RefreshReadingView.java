package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhandalin on 2016-12-27 20:41.
 * 说明:加载前的动画View
 */
public class RefreshReadingView extends View {

    private Paint mPaint;
    private RefreshReadingIndicator mIndicatorController;
    private boolean mHasAnimation;

    public RefreshReadingView(Context context) {
        this(context, null);
    }

    public RefreshReadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshReadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!mHasAnimation) {
            mHasAnimation = true;
            applyAnimation();
        }
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#1b1b1b"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        applyIndicator();
    }

    private void applyIndicator() {
        mIndicatorController = new RefreshReadingIndicator();
        mIndicatorController.setTarget(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mIndicatorController.draw(canvas, mPaint);
    }

    void applyAnimation() {
        mIndicatorController.initAnimation();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (null != mIndicatorController) {
            if (VISIBLE == visibility) {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
            } else {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mIndicatorController)
            mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (null != mIndicatorController)
            mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
    }

}

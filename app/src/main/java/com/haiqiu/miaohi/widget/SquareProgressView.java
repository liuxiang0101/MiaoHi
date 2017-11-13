package com.haiqiu.miaohi.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.haiqiu.miaohi.utils.MHLogUtil;

/**
 * Created by zhandalin on 2016-12-5 10:44.
 * 说明:图片矩形进度条
 */
public class SquareProgressView extends ImageView {
    private static final String TAG = "SquareProgressView";

    private static final int DEFAULT_FRAME_WIDTH_DP = 3;
    private int mFrameWidth;
    private Paint mProgressPaint;
    private float mProgress;
    private boolean shadeEnable = true;
    private int mProgressColor;
    private Paint mAlphaPaint;
    private float lastProgress;
    private ValueAnimator valueAnimator;


    public SquareProgressView(Context context) {
        this(context, null);
    }

    public SquareProgressView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SquareProgressView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    private void init() {
        mProgressPaint = new Paint(1);
        mProgressPaint.setStyle(Paint.Style.FILL);

        mAlphaPaint = new Paint(2);
        mAlphaPaint.setStyle(Paint.Style.FILL);
        mAlphaPaint.setAlpha(0);
        mFrameWidth = (int) (getContext().getResources().getDisplayMetrics().density * DEFAULT_FRAME_WIDTH_DP);
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (shadeEnable) {
//            mAlphaPaint.setColor(Color.WHITE);
            setAlpha(0.3f + mProgress * 0.7f);
        }

        int width = getWidth();
        int height = getHeight();
        canvas.drawRect(0.0F, 0.0F, (float) width, (float) height, mAlphaPaint);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setAlpha(255);
        int length = (int) ((float) ((width + height) * 2) * mProgress);

        canvas.drawRect(0.0F, 0.0F, (float) Math.min(length, width), (float) mFrameWidth, mProgressPaint);
        length -= width;
        if (length >= 0) {
            canvas.drawRect((float) (width - mFrameWidth), mFrameWidth, (float) width, (float) Math.min(height, length), mProgressPaint);
            length -= height;
            if (length >= 0) {
                canvas.drawRect((float) Math.max(0, width - length), (float) (height - mFrameWidth), (float) width, (float) height, mProgressPaint);
                width = length - width;
                if (width >= 0) {
                    canvas.drawRect(0.0F, (float) Math.max(0, height - width), (float) mFrameWidth, (float) height, mProgressPaint);
                }
            }
        }
    }

    /**
     * @param mFrameWidth 进度宽度
     */
    public void setFrameWidth(int mFrameWidth) {
        this.mFrameWidth = mFrameWidth;
    }

    /**
     * @param shadeEnable 是否允许渐变
     */
    public void setShadeEnable(boolean shadeEnable) {
        this.shadeEnable = shadeEnable;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
    }

    /**
     * @param mProgress 0-1
     */
    public void setProgress(float mProgress) {
        MHLogUtil.d(TAG, "mProgress=" + mProgress);
        updateProgress(mProgress);
    }

    public float getProgress() {
        return mProgress;
    }

    public void updateProgress(final float progress) {
        if (null != valueAnimator)
            valueAnimator.cancel();
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mProgress = lastProgress + (progress - lastProgress) * animatedValue;
                invalidate();
//                MHLogUtil.d(TAG, "onAnimationUpdate---=" + animatedValue + "--mProgress=" + mProgress + "---lastProgress=" + lastProgress);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                MHLogUtil.d(TAG, "onAnimationEnd");
                lastProgress = progress;
                mProgress = progress;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
//                MHLogUtil.d(TAG, "onAnimationCancel");
                lastProgress = progress;
                mProgress = progress;
                invalidate();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }
}

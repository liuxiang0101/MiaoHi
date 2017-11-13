package com.haiqiu.miaohi.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.haiqiu.miaohi.R;


public class VerticalSeekBar extends View {

    private int mThumbColor = Color.YELLOW;
    private int mBgColor = Color.WHITE;
    private int mProgressColor = Color.WHITE;
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private int mLeftMargin;
    private int mTopMargin;
    private int mRightMargin;
    private int mBottomMargin;
    private Path mPath;
    private int mTotalHeight;
    private int mTotalWidth;
    private int mBgWidth;
    private RectF mRectF;
    private float mCurrentY;
    private int mMax = 99;
    private int mProgress = 0;
    private int space = 30;
    private float progressY;
    private Bitmap bitmap;
    private Context context;
    private OnSeekBarChangeListener onSeekBarChangeListener;

    public VerticalSeekBar(Context context) {
        this(context, null);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mRectF = new RectF();
        space = dip2px(context, space);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.paishetiaoguang);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isEnabled()) return;
        prosessOutSide();
        mProgress = (int) ((mCurrentY - mWidth / 2) / (mHeight - mWidth) * mMax);
        if (null != onSeekBarChangeListener) {
            onSeekBarChangeListener.onProgressChanged(mProgress);
        }
        drawBg(canvas);

        drawProgress(canvas);

        drawThumb(canvas);
    }

    private void drawProgress(Canvas canvas) {
        mRectF.top = mCurrentY + space;
        mPaint.setColor(mProgressColor);
        canvas.drawRect(mRectF, mPaint);

//        canvas.drawCircle(mRectF.left + mBgWidth / 2, mRectF.bottom, mBgWidth / 2, mPaint);
    }

    private void drawThumb(Canvas canvas) {
        mPaint.setColor(mThumbColor);
        prosessOutSide();
        canvas.drawBitmap(bitmap, 0, mCurrentY - bitmap.getHeight() / 2, mPaint);
//        canvas.drawCircle(mWidth / 2, mCurrentY, mWidth / 2, mPaint);
    }

    private void prosessOutSide() {
        if (mCurrentY <= 1.0f * mWidth / 2) {
            mCurrentY = 1.0f * mWidth / 2;
        }
        if (mCurrentY >= mHeight - 1.0f * mWidth / 2) {
            mCurrentY = mHeight - 1.0f * mWidth / 2;
        }
    }

    private void drawBg(Canvas canvas) {
        mPaint.setColor(mBgColor);
        mRectF.left = mLeftMargin + (mWidth - mBgWidth) / 2;
        mRectF.top = mTopMargin + mBgWidth / 2;
        mRectF.right = mRectF.left + mBgWidth;
        mRectF.bottom = mCurrentY - space;
        canvas.drawRect(mRectF, mPaint);

        mRectF.top = mCurrentY + space;
        mRectF.bottom = mHeight - mBgWidth / 2;
        canvas.drawRect(mRectF, mPaint);

//        canvas.drawCircle(mRectF.left + mBgWidth / 2, mRectF.top, mBgWidth / 2, mPaint);
//        canvas.drawCircle(mRectF.left + mBgWidth / 2, mRectF.bottom, mBgWidth / 2, mPaint);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_CANCEL) {
            return false;
        }
        if (!isEnabled()) return false;
        mCurrentY = event.getY();
        invalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = getLayoutParams();
        if (null != params) {
            params.width = bitmap.getWidth();
            setLayoutParams(params);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalHeight = h;
        mTotalWidth = w;

        if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
            mLeftMargin = lp.leftMargin;
            mTopMargin = lp.topMargin;
            mRightMargin = lp.rightMargin;
            mBottomMargin = lp.bottomMargin;
        }
        if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
            mLeftMargin = lp.leftMargin;
            mTopMargin = lp.topMargin;
            mRightMargin = lp.rightMargin;
            mBottomMargin = lp.bottomMargin;
        }
        if (getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
            mLeftMargin = lp.leftMargin;
            mTopMargin = lp.topMargin;
            mRightMargin = lp.rightMargin;
            mBottomMargin = lp.bottomMargin;
        }

        mHeight = mTotalHeight - mTopMargin - mBottomMargin;
        mWidth = mTotalWidth - mLeftMargin - mRightMargin;
        mBgWidth = dip2px(context, 1);

        progressY = getProgressY();
        mCurrentY = progressY;

    }

    /**
     * 设置最大值
     *
     * @param max
     */
    public void setMax(int max) {
        this.mMax = max;
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        mCurrentY = getProgressY();
        invalidate();
    }

    public void modProgress(int progress) {
        this.mProgress += progress;
        if (mProgress > mMax) mProgress = mMax;
        if (mProgress < 0) mProgress = 0;

        mCurrentY = getProgressY();
        invalidate();
    }

    /**
     * 获取进度
     *
     * @return
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 将进度转对应的Y
     *
     * @return
     */
    private float getProgressY() {
        float everyProgressPx = 1.0f * (mHeight - mWidth) / mMax;
        return 1.0f * mWidth / 2 + mProgress * everyProgressPx;
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(int progress);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

}

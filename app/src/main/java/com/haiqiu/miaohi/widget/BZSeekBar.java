package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.haiqiu.miaohi.R;


/**
 * Created by zhandalin on 2016-12-10 20:15.
 * 说明:默认是横向的
 */
public class BZSeekBar extends View {
    private float backgroundLineHeight = 1;//dp
    private float progressLineHeight = 3;//dp
    private float padding = 3;//dp 防止出现残留像素

    private float progressPix;
    private Drawable thumb;
    private OnSeekBarChangeListener onSeekBarChangeListener;
    private Paint progressBackgroundPaint;
    private Paint progressPaint;
    private Context context;
    private Paint clearPaint;
    private float currentProgress;

    public BZSeekBar(Context context) {
        this(context, null);
    }

    public BZSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BZSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        padding = dip2px(context, padding);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BZSeekBar,
                0, 0);

        thumb = typedArray.getDrawable(R.styleable.BZSeekBar_thumb);
        if (null == thumb)
            thumb = getResources().getDrawable(R.drawable.shape_progress_thumb);
        thumb.setBounds(0, 0, thumb.getIntrinsicWidth(), thumb.getIntrinsicHeight());

        backgroundLineHeight = dip2px(context, backgroundLineHeight);
        backgroundLineHeight = typedArray.getDimensionPixelSize(R.styleable.BZSeekBar_backgroundLineHeight, (int) backgroundLineHeight);

        progressLineHeight = dip2px(context, progressLineHeight);
        progressLineHeight = typedArray.getDimensionPixelSize(R.styleable.BZSeekBar_progressLineHeight, (int) progressLineHeight);

        //背景画笔
        int colorPB = typedArray.getColor(R.styleable.BZSeekBar_progressBackgroundColor, Color.GRAY);
        progressBackgroundPaint = new Paint();
        progressBackgroundPaint.setStrokeWidth(backgroundLineHeight);
        progressBackgroundPaint.setAntiAlias(true);
        progressBackgroundPaint.setFilterBitmap(true);
        progressBackgroundPaint.setDither(true);
        progressBackgroundPaint.setColor(colorPB);

        //进度画笔
        int colorP = typedArray.getColor(R.styleable.BZSeekBar_progressColor, Color.BLUE);
        progressPaint = new Paint();
        progressPaint.setStrokeWidth(progressLineHeight);
        progressPaint.setAntiAlias(true);
        progressPaint.setFilterBitmap(true);
        progressPaint.setDither(true);
        progressPaint.setColor(colorP);

        int colorB = typedArray.getColor(R.styleable.BZSeekBar_backgroundColor, Color.GRAY);
        clearPaint = new Paint();
        clearPaint.setColor(colorB);
        clearPaint.setAntiAlias(true);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        progressPix = (w - 2 * padding - thumb.getIntrinsicWidth() / 2) * currentProgress;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return onInterceptTouchEvent(event);
            case MotionEvent.ACTION_MOVE:
                progressPix = event.getX();
                invalidate();
                break;
        }

        return super.onTouchEvent(event);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (null != thumb) {
            //只是触摸thumb才触发进度
            if (event.getX() > progressPix - padding && event.getX() < progressPix + thumb.getIntrinsicWidth() + padding) {
                return true;
            }
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        adjustValue();

        //清除画布
        canvas.drawRect(0, 0, getWidth(), getHeight(), clearPaint);
        //背景
        canvas.drawLine(padding, getHeight() / 2, getWidth() - padding, getHeight() / 2, progressBackgroundPaint);
        //进度
        canvas.drawLine(padding, getHeight() / 2, progressPix, getHeight() / 2, progressPaint);


        if (null != thumb) {
            if (null != onSeekBarChangeListener) {
                currentProgress = (progressPix - padding) / (getWidth() - thumb.getIntrinsicWidth() - 2 * padding);
                onSeekBarChangeListener.onProgressChanged(currentProgress);
            }
            canvas.translate(progressPix, (getHeight() - thumb.getIntrinsicHeight()) / 2);
            thumb.draw(canvas);
        }

        canvas.restore();
    }

    private void adjustValue() {
        if (progressPix < padding) {
            progressPix = padding;
        }
        if (progressPix > getWidth() - thumb.getIntrinsicWidth() - padding) {
            progressPix = getWidth() - thumb.getIntrinsicWidth() - padding;
        }
    }

    public void setThumb(Drawable thumb) {
        if (null == thumb) return;
        thumb.setBounds(0, 0, thumb.getIntrinsicWidth(), thumb.getIntrinsicHeight());
        this.thumb = thumb;
    }

    public void setBackgroundLineHeight(float backgroundLineHeight) {
        this.backgroundLineHeight = backgroundLineHeight;
        progressBackgroundPaint.setStrokeWidth(dip2px(context, backgroundLineHeight));
        invalidate();
    }

    public void setProgressLineHeight(float progressLineHeight) {
        this.progressLineHeight = progressLineHeight;
        progressPaint.setStrokeWidth(dip2px(context, progressLineHeight));
        invalidate();
    }

    public void setProgressBackgroundColor(int color) {
        progressBackgroundPaint.setColor(color);
        invalidate();
    }

    public void setProgressColor(int color) {
        progressPaint.setColor(color);
        invalidate();
    }

    public void setCurrentProgress(float currentProgress) {
        progressPix = (getWidth() - 2 * padding - thumb.getIntrinsicWidth() / 2) * currentProgress;
        this.currentProgress = currentProgress;
        invalidate();
    }

    private float dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale;
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(float progress);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }
}

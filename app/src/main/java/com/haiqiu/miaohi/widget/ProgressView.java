package com.haiqiu.miaohi.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.haiqiu.miaohi.R;
import com.yixia.camera.model.MediaObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-12-30 11:06.
 * 说明:视频录制的进度条
 */
public class ProgressView extends View {

    /**
     * 背景paint
     */
    private Paint progressBgPaint;

    /**
     * 最大时长
     */
    private int mMaxDuration = 180 * 1000;

    /**
     * 最小时长
     */
    private int mMiniDuration = 3 * 1000;

    /**
     * 当前时间
     */
    private long currentDuration;

    /**
     * 数据源,必须设置才有效
     */
    private List<MediaObject.MediaPart> mMediaList = new ArrayList<>();

    /**
     * 进度渐变开始的颜色
     */
    private int progressStartColor;

    /**
     * 进度渐变结束的颜色
     */
    private int progressEndColor;

    /**
     * 画分割线的Paint
     */
    private Paint divideLinePaint;

    /**
     * 画回删的Paint
     */
    private Paint deletePaint;

    /**
     * 画呼吸灯的Paint
     */
    private Paint huxidengPaint;


    /**
     * 分割线的宽度
     */
    private float divideLineSize;

    /**
     * 分割线的高度
     */
    private float divideLineHeight;


    /**
     * 上下padding的大小
     */
    private float paddingSize;

    private float alphaAnimatedValue;
    private ValueAnimator alphaAnimator;
    private Bitmap huxideng_bitmap;
    private float dp_1;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp_1 = dip2px(1);
        progressBgPaint = new Paint();
        progressBgPaint.setStyle(Paint.Style.FILL);
        progressStartColor = Color.parseColor("#00a3fe");
        progressEndColor = Color.parseColor("#00ddac");

        //分割线
        divideLinePaint = new Paint();
        divideLinePaint.setColor(Color.WHITE);
        divideLinePaint.setStyle(Paint.Style.FILL);


        divideLineSize = dp_1;
        paddingSize = dp_1 * 6;

        //deletePaint
        deletePaint = new Paint();
        deletePaint.setColor(Color.RED);
        deletePaint.setStyle(Paint.Style.FILL);


        //外面渐变的呼吸灯
        huxidengPaint = new Paint();
        huxidengPaint.setColor(progressEndColor);
        huxidengPaint.setStyle(Paint.Style.FILL);

        alphaAnimator = ValueAnimator.ofFloat(0, 1);
        alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimator.setRepeatCount(-1);
        alphaAnimator.setDuration(800);

        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alphaAnimatedValue = (float) animation.getAnimatedValue();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Drawable drawable = getResources().getDrawable(R.drawable.huxideng);
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        huxideng_bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), config);
        Canvas canvas = new Canvas(huxideng_bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        huxideng_bitmap = scaleBitmap(huxideng_bitmap, h, h);

        //比进度条宽2dp
//        divideLineHeight = h - 2 * paddingSize + 2 * dp_1;
        divideLineHeight = h - 2 * paddingSize;
    }

    int width, height;
    float right = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        right = 0;

        //画整体背景
        right += 1.0f * currentDuration / mMaxDuration * width;
        if (right > width - height / 2) right = width - height / 2;


        LinearGradient progressShader = new LinearGradient(0, paddingSize, right, height - paddingSize, new int[]{progressStartColor, progressEndColor}, null, Shader.TileMode.REPEAT);
        progressBgPaint.setShader(progressShader);
        canvas.drawRect(0, paddingSize, right, height - paddingSize, progressBgPaint);


        //画分割线
        for (int i = 0; i < mMediaList.size() - 1; i++) {
            if (i == 0) right = 0;

            MediaObject.MediaPart mediaPart = mMediaList.get(i);
            right += 1.0f * mediaPart.getDuration() / mMaxDuration * width;
            canvas.drawRect(right - divideLineSize, (height - divideLineHeight) / 2, right, height - (height - divideLineHeight) / 2, divideLinePaint);

            if (i == mMediaList.size() - 2) {//表示是最后一个了,修正right值
                right += 1.0f * mMediaList.get(mMediaList.size() - 1).getDuration() / mMaxDuration * width;
            }
        }

        //画回删
        boolean isRemove = false;
        if (mMediaList.size() > 0) {
            MediaObject.MediaPart mediaPart = mMediaList.get(mMediaList.size() - 1);
            float temp = 1.0f * mediaPart.getDuration() / mMaxDuration * width;
            if (mediaPart.remove) {
                isRemove = true;
                canvas.drawRect(right - temp, paddingSize, right, height - paddingSize, deletePaint);
                right += temp;
            }
        }
        //画呼吸灯
        if (!isRemove) {
            float targetRight = right - height / 2;
            if (targetRight > width - height) targetRight = width - height;

            huxidengPaint.setAlpha((int) (180 * alphaAnimatedValue));
            canvas.drawBitmap(huxideng_bitmap, targetRight, 0, huxidengPaint);

            canvas.drawBitmap(huxideng_bitmap, targetRight, 0, null);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        alphaAnimator.cancel();
        alphaAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        alphaAnimator.cancel();
    }

    public void onResume() {
        alphaAnimator.cancel();
        alphaAnimator.start();
    }

    public void onPause() {
        alphaAnimator.cancel();
    }

    public void setData(List<MediaObject.MediaPart> mMediaList) {
        this.mMediaList = mMediaList;
    }

    public void setCurrentDuration(long currentDuration) {
        this.currentDuration = currentDuration;
        invalidate();
    }

    public void setMaxDuration(int duration) {
        this.mMaxDuration = duration;
        invalidate();
    }

    public void setMiniDuration(int mMiniDuration) {
        this.mMiniDuration = mMiniDuration;
        invalidate();
    }

    private float dip2px(float dipValue) {
        return dipValue * getResources().getDisplayMetrics().density;
    }

    private Bitmap scaleBitmap(Bitmap srcBitmap, float targetWidth, float targetHeight) {
        float scaleX = targetWidth / srcBitmap.getWidth();
        float scaleY = targetHeight / srcBitmap.getHeight();
        float targetScale = scaleY > scaleX ? scaleY : scaleX;

        Matrix matrix = new Matrix();
        matrix.postScale(targetScale, targetScale);
        return Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
    }
}

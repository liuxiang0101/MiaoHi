package com.haiqiu.miaohi.widget.decal;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


/**
 * Created by zhandalin on 2016-12-29 17:13.
 * 说明:用来拿取onDraw回调
 */
public class MyImageView extends ImageView {
    private final static String TAG = "MyImageView";

    private OnDecalUpdateListener onDecalUpdateListener;
    private boolean hasCallBack;

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        MHLogUtil.d(TAG, this + "onDraw");
        //只是第一次的时候回调
        if (!hasCallBack) {
            hasCallBack = true;
            if (null != onDecalUpdateListener)
                onDecalUpdateListener.onDecalUpdate();
        }
    }

    public void setOnDecalUpdateListener(OnDecalUpdateListener onDecalUpdateListener) {
        this.onDecalUpdateListener = onDecalUpdateListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
//            MHLogUtil.d(TAG, this + "ACTION_UP");
            if (null != onDecalUpdateListener)
                onDecalUpdateListener.onDecalUpdate();
        }
        return super.onTouchEvent(event);
    }
}

package com.haiqiu.miaohi.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;

/**
 * Created by zhandalin on 2017-01-13 14:23.
 * 说明:用来处理键盘弹出,布局上滑的逻辑
 */
public class MyScrollView extends ScrollView {
    private final static String TAG = "MyScrollView";
    private int lastHeight;
    private int offset;
    private boolean isShowKeyboard;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        offset = DensityUtil.dip2px(getContext(), 149);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //这个数字,TMD改布局了,就自己去算
                int height = getHeight();
                if (lastHeight == height) return;
                int keyboardHeight = Math.abs(lastHeight - height);
                MHLogUtil.d(TAG, "keyboardHeight=" + keyboardHeight);
                offset = keyboardHeight - DensityUtil.dip2px(getContext(), 99);
                //99是根据布局算出来的
                smoothScrollTo(0, offset);
                if (lastHeight != 0 && lastHeight != getHeight()) {
                    if (lastHeight > getHeight()) {//键盘弹起
                        isShowKeyboard = true;
                    } else {//键盘收起
                        isShowKeyboard = false;
                    }
                }
                lastHeight = getHeight();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isShowKeyboard) {
            ev.offsetLocation(0, offset);
        }
        getChildAt(0).dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isShowKeyboard) {
            ev.offsetLocation(0, offset);
        }
        getChildAt(0).onTouchEvent(ev);
        return true;
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        getChildAt(0).dispatchDragEvent(event);
        return true;
    }

}

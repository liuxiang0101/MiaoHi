package com.haiqiu.miaohi.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by LiuXiang on 2017/1/2.
 */
public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = false;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    int lastXIntercept;
    int lastYIntercept;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (noScroll) {
            switch (ev.getAction()) {
                /*如果拦截了Down事件,则子类不会拿到这个事件序列*/
                case MotionEvent.ACTION_DOWN:
                    lastXIntercept = x;
                    lastYIntercept = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    final int deltaX = x - lastXIntercept;
                    final int deltaY = y - lastYIntercept;
                    /*根据条件判断是否拦截该事件*/
                    if (Math.abs(deltaX) > 20 || Math.abs(deltaY) > 20) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            lastXIntercept = x;
            lastYIntercept = y;
            return false;
        } else
            return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}

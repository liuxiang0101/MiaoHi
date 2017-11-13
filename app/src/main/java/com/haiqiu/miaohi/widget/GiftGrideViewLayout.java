package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 可送礼物外层 礼物内view点击
 * Created by ningl on 16/9/5.
 */
public class GiftGrideViewLayout extends RelativeLayout {

    private OnDispatchTouchEvent onDispatchTouchEvent;

    public interface OnDispatchTouchEvent{
        void OnDispatchTouchEvent();
    }

    public void setOndispatchTouchEventListener(OnDispatchTouchEvent onDispatchTouchEvent){
        this.onDispatchTouchEvent = onDispatchTouchEvent;
    }

    public GiftGrideViewLayout(Context context) {
        super(context);
    }

    public GiftGrideViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftGrideViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){
            if(null != onDispatchTouchEvent)onDispatchTouchEvent.OnDispatchTouchEvent();
        }
        return super.dispatchTouchEvent(ev);
    }

}

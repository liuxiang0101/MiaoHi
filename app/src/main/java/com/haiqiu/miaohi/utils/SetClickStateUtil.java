package com.haiqiu.miaohi.utils;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by LiuXiang on 2017/2/6.
 */

public class SetClickStateUtil {
    static SetClickStateUtil setClickStateUtil;
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    public static SetClickStateUtil getInstance() {
        if (setClickStateUtil == null) {
            setClickStateUtil = new SetClickStateUtil();
        }
        return setClickStateUtil;
    }

    public void setStateListener(View targetView) {
        targetView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setAlpha(1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = motionEvent.getX();
                    y1 = motionEvent.getY();
                    view.setAlpha(0.5f);
                }else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    x2 = motionEvent.getX();
                    y2 = motionEvent.getY();
                    if(Math.abs(x2-x1)>50||Math.abs(y2-y1)>50){
                        view.setAlpha(1);
                    }
                }
                return false;
            }
        });
    }
}

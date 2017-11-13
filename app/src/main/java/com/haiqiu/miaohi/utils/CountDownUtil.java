package com.haiqiu.miaohi.utils;

import android.os.Handler;

import java.util.Vector;

public class CountDownUtil {

    private static volatile CountDownUtil countDownUtil;
    private static Vector<OnCountDown> OnCountDowns;
    private Handler handler;
    private boolean isCountDown;
    private OnCountDown onCountDown;

    private CountDownUtil() {
    }

    public static CountDownUtil getInstance() {
        if (countDownUtil == null) {
            synchronized (CountDownUtil.class) {
                if (countDownUtil == null) {
                    countDownUtil = new CountDownUtil();
                    OnCountDowns = new Vector<>();
                }
            }
        }
        return countDownUtil;
    }

    /**
     * 注册需要倒计时的对象
     */
    public void registe(OnCountDown onCountDown){
        if(OnCountDowns.contains(onCountDown)){
            return;
        }
        if(!isCountDown){
            OnCountDowns.add(onCountDown);
            isCountDown = true;
            countDown();
        } else {
            OnCountDowns.add(onCountDown);
        }
    }

    /**
     * 移除倒计时对象
     */
    public void unRegiste(OnCountDown onCountDown){
        if(OnCountDowns.isEmpty()){
            isCountDown = false;
            return;
        } else {
            OnCountDowns.remove(onCountDown);
            if(OnCountDowns.isEmpty()){
                isCountDown = false;
            }
        }
    }

    /**
     * 处理逻辑
     */
    public void push(){
        for (OnCountDown onCountDown : OnCountDowns) {
            onCountDown.onTick();
        }
    }

    /**
     * 开始倒计时
     */
    private void countDown(){
        if(handler!=null) return;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isCountDown) return;
                push();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    /**
     * 是否已经注册过
     * @param onCountDown
     * @return
     */
    public boolean isContain(OnCountDown onCountDown){
        if(null == OnCountDowns) return false;
        return OnCountDowns.contains(onCountDown);
    }

    public interface OnCountDown{
        void onTick();
    }

    public void setOnCountDownListener(OnCountDown onCountDown){
        this.onCountDown = onCountDown;
    }

}
package com.haiqiu.miaohi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by LiuXiang on 2016/11/28.
 * 用于显示倒计时的textview
 */
public class TimeTextView extends TextView {
    private long Time;
    private boolean run = true;                                 // 是否启动了
    private SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
    private OnTimeMonitorListener onTimeMonitorListener;        //倒计时结束监听

    public TimeTextView(Context context) {
        super(context);
    }


    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 在控件被销毁时移除消息
        handler.removeMessages(0);
    }

    @SuppressLint("NewApi")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (run) {
                        long mTime = Time;
                        if (mTime >= 0) {
                            String day = "";
//                            TimeTextView.this.setText("还剩" + format.format(new Date(mTime)));
                            TimeTextView.this.setText("还剩" + timeParse(mTime));
                            Time = Time - 1000;
                            handler.sendEmptyMessageDelayed(0, 1000);
                        } else {
                            stop();
                        }
                    } else {
//                        TimeTextView.this.setVisibility(View.GONE);
                    }
                    break;
            }

        }
    };

    private String timeParse(long duration) {
        String time = "";
        long hour = duration / 3600000;
        long minute = duration / 60000;
        long seconds = duration % 60000;

        long second = Math.round((float) seconds / 1000);
        //小时
        if (hour < 10) {
            time += "0";
        }
        //分钟
        time += hour + ":";
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        //秒
        if (second < 10) {
            time += "0";
        }
        time += second;

        return time;
    }

    @SuppressLint("NewApi")
    public void setTimes(long mT) {
        // 标示已经启动
//        Date date = new Date();
//        long t2 = date.getSaveTime();
//        Time = mT - t2;
//        date = null;
        //限制时间一小时
        int limitTime = 1 * 60 * 60 * 1000;
        if (mT > limitTime) return;
        Time = mT;
        if (Time > 0) {
            handler.removeMessages(0);
            handler.sendEmptyMessage(0);
        } else {
            TimeTextView.this.setVisibility(View.GONE);
        }
    }

    public void stop() {
        run = false;
        onTimeMonitorListener.isTimeUp(true);
        TimeTextView.this.setVisibility(View.GONE);
    }

    public void setOnTimeMonitorListener(OnTimeMonitorListener onTimeMonitorListener) {
        this.onTimeMonitorListener = onTimeMonitorListener;
    }

    public interface OnTimeMonitorListener {
        void isTimeUp(boolean b);
    }
}
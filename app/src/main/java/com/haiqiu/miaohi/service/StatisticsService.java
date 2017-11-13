package com.haiqiu.miaohi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.haiqiu.miaohi.utils.MHLogUtil;

/**
 * Created by LiuXiang on 2016/11/16.
 * 开启后台服务，用来统计数据
 */
public class StatisticsService extends Service {
    private static final String TAG = "StatisticsService";

    @Override
    public void onCreate() {
        MHLogUtil.i(TAG, "--onCreate");
//        handler.post(task);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MHLogUtil.i(TAG, "--onStartCommand");
        //在主进程方法里通过intent传值，在这里将数据存入数据库
//        SqlHelper msh = new SqlHelper(this, "statictis.db", null, 1);
//        SQLiteDatabase db = msh.getWritableDatabase();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        MHLogUtil.i(TAG, "--onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        MHLogUtil.i(TAG, "--onBind");
        return null;
    }

    /**
     * 开启子线程用于定时任务
     */
//    private Handler handler = new Handler();

    /**
     * 线程任务--执行定时任务的载体
     */
//    private Runnable task = new Runnable() {
//        public void run() {
//            // 定时任务
//            handler.postDelayed(this, 4 * 60 * 1000);//设置延迟时间，此处是4min
//            // 需要执行的代码
//            MHLogUtil.e(TAG, "定时向服务端发送日志信息");
//        }
//    };

    /**
     * 此处将统计的数据存入数据库
     * @param object
     */
    public void saveDb(Object... object) {
        MHLogUtil.e(object.toString());
    }

}
